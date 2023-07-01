package com.game.capia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.capia.core.generator.NameGenerator;
import com.game.capia.core.generator.TexturePathGenerator;
import com.game.capia.core.map.Block;
import com.game.capia.core.map.TileMap;
import com.game.capia.core.websocket.GameRequestHandler;
import com.game.capia.core.websocket.SessionManager;
import com.game.capia.dto.RequestMessage;
import com.game.capia.dto.ResponseMessage;
import com.game.capia.dto.map.*;
import com.game.capia.dto.world.*;
import com.game.capia.model.character.*;
import com.game.capia.model.character.Character;
import com.game.capia.model.city.CityBase;
import com.game.capia.model.city.CityRepository;
import com.game.capia.model.corporate.Corporate;
import com.game.capia.model.corporate.CorporateRepository;
import com.game.capia.model.map.*;
import com.game.capia.model.tech.TechRepository;
import com.game.capia.model.user.User;
import com.game.capia.model.user.UserRepository;
import com.game.capia.model.world.World;
import com.game.capia.model.world.WorldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WorldService {

    private final ObjectMapper objectMapper;

    private final RoadDataRepository roadDataRepository;

    private final CityRepository cityRepository;
    private final CityInfoRepository cityInfoRepository;

    private final WorldRepository worldRepository;

    private final UserRepository userRepository;

    private final CharacterRepository characterRepository;

    private final CorporateRepository corporateRepository;

    private final TechRepository techRepository;

    private final SessionManager sessionManager;

    private String worldName;

    private Random random = new Random();

    public void handleRequest(WebSocketSession session, RequestMessage requestMessage) {
        try{
            if ("GENERATE".equals(requestMessage.getSubType())) {

                JsonNode payloadNode = objectMapper.convertValue(requestMessage.getPayload(), JsonNode.class);
                GenerateWorldRequest generateWorldRequest = objectMapper.treeToValue(payloadNode, GenerateWorldRequest.class);
                월드생성(session, generateWorldRequest);
            } else if ("CONNECT".equals(requestMessage.getSubType())) {
                JsonNode payloadNode = objectMapper.convertValue(requestMessage.getPayload(), JsonNode.class);
                ConnectWorldRequest connectWorldRequest = objectMapper.treeToValue( payloadNode, ConnectWorldRequest.class);
                월드접속(session, connectWorldRequest);
            }else if("LIST".equals(requestMessage.getSubType())){
                JsonNode payloadNode = objectMapper.convertValue(requestMessage.getPayload(), JsonNode.class);
                WorldListRequest worldListRequest = objectMapper.treeToValue( payloadNode, WorldListRequest.class);
                월드목록(session, worldListRequest);
            }else if("DELETE".equals(requestMessage.getSubType())){
                JsonNode payloadNode = objectMapper.convertValue(requestMessage.getPayload(), JsonNode.class);
                DeleteWorldRequest deleteWorldRequest = objectMapper.treeToValue( payloadNode, DeleteWorldRequest.class);
                월드삭제(session, deleteWorldRequest);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            Logger.getLogger(GameRequestHandler.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Transactional
    public void 월드생성(WebSocketSession session, GenerateWorldRequest worldRequest) throws IOException {

        String name = worldRequest.getName();
        int width = worldRequest.getWidth();//512, 1024, 2048, 4096
        int height = worldRequest.getHeight();

        //WORLD
        World world = World.builder()
                .name(name)
                .width(width)
                .heigth(height)
                .build();

        World worldPS = null;
        try{
            worldPS = worldRepository.save(world);
        }catch (Exception e){
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setStatus("FAIL");
            responseMessage.setType("WORLD");
            responseMessage.setSubType("LIST");
            responseMessage.setPayload("World with this name already exists.");

            String responseJson = objectMapper.writeValueAsString(responseMessage);
            session.sendMessage(new TextMessage(responseJson));
        }



        int cityIntiRadius = 50;

        int numCircles = estimateNumCircles(height, width, cityIntiRadius, 0);

        //CITY
        List<CityBase> cities = cityRepository.findRandomCities(numCircles);
        List<CityInfo> cityInfoList = new ArrayList<>();



        for(CityBase city : cities)
        {

            int x = -1, y = -1;
            boolean validPosition = false;

            for (int attempt = 0; attempt < 3; attempt++) { // 최대 3번 시도
                // 랜덤한 위치 생성
                x = cityIntiRadius + random.nextInt(width - 2 * cityIntiRadius);
                y = cityIntiRadius + random.nextInt(height - 2 * cityIntiRadius);

                // 생성된 위치가 모든 다른 도시와 충분한 거리를 가지는지 확인
                validPosition = true;
                for (CityInfo otherCity : cityInfoList) {
                    int dx = otherCity.getX() - x;
                    int dy = otherCity.getY() - y;
                    int distanceSquared = dx * dx + dy * dy;

                    if (distanceSquared < 4 * cityIntiRadius * cityIntiRadius) { // 거리가 충분하지 않으면 실패
                        validPosition = false;
                        break;
                    }
                }

                if (validPosition) {
                    break; // 성공적으로 위치를 찾음
                }
            }

            if (validPosition) {
                CityInfo cityInfo = CityInfo.builder()
                        .name(city.getName())
                        .x(x)
                        .y(y)
                        .radius(cityIntiRadius)
                        .population(10000)
                        .spendingLevel(50)
                        .salaryLevel(50)
                        .economicClimate(EconomicClimate.Normal)
                        .world(worldPS)
                        .build();

                cityInfoList.add(cityInfoRepository.save(cityInfo));
            }

        }

        //전체맵은 이미 10x10 로 쪼개져 있는상태이다 (리스트에 10x10을 저장할 필요는 없다 연산할때 10x10을 사용하기만 하면된다.)

        //블록의 크기는 최대 10x10 최소 5x5 이다.

        //도시의 중심을 중심으로 하는 원과 충돌하는 10x10 블록들을 찾는다
        //도시에 속한(원과 충돌하는) 블록은 이분할방식을 두번한다.
        //80%확률로 2분할 한다 분할할때는 가로 세로 길이중에 길이가 긴쪽을 분할한다. 가로세로 길이가 같다면 랜덤하게 선택해서 분할한다.
        //그러면 10x10 에서 5x5까지 다양한 블록이 생성된다
        //블록의 오른쪽변, 위쪽변의 타일속성을 도로로 변경한다. 이때 전체맵의 위쪽변이나 오른쪽변에 맞닿은 부분은 도로로 변경하지 않는다.

        // 그리고 도시가 성장하면 도시외부에 있지만 도시 중심에서 가장가까운 블록 하나(10x10)를 랜덤하게 선택해서 도시에 편입하고
        // 편입된 블록을 이분할을 두번 해준다
        //블록의 오른쪽변, 위쪽변 을 도로로 만들자

        //유저가 맵에 도로를 배치할수 있다. 5x5규격에 맞게만 만들수 있다. 전체맵이 5x5로 나뉘어 있다고 가정하고 그 5x5에 맞게 도로를 만들수있다
        //만약 도로가 어떤 10x10 블록을 분할한다면 그 분할 정보를 저장한다. 도시가 확장할때 도시에 편입은 시키되 분할하지는 않는다.

        int BlockSize = 10;
        int hfBS = BlockSize/2;

        for(CityInfo cityPS : cityInfoList)
        {
            int radius = cityPS.getRadius();

            List<Block> blockList = new ArrayList<>();
            int istart = (cityPS.getX() - cityPS.getRadius())/BlockSize * BlockSize;
            int iend = istart + (cityPS.getRadius()*2);
            int jstart = (cityPS.getY() - cityPS.getRadius())/BlockSize * BlockSize;
            int jend = jstart + (cityPS.getRadius()*2);
            for(int i = istart; i <= iend; i += BlockSize){
                for(int j = jstart; j <= jend; j += BlockSize){
                    // Check if the block is within the city's circle
                    if(Math.sqrt(Math.pow( (i+hfBS) - cityPS.getX(), 2) + Math.pow( (j+hfBS) - cityPS.getY(), 2)) <= cityPS.getRadius()){
                        Block temp = new Block(i, j, BlockSize, BlockSize);
                        temp.divide(0);
                        blockList.addAll(temp.getLeaves());
                    }
                }
            }

            //TileMap tileMap = new TileMap(width, height, 5, blockList);

            // Convert leaves to DTOs suitable for JSON serialization
            List<RoadData> roadDataList = blockList.stream().map(block ->{
                RoadData roadData = RoadData.builder()
                        .x(block.getX())
                        .y(block.getY())
                        .width(block.getWidth())
                        .height(block.getHeight())
                        .cityInfo(cityPS).build();
                return roadData;
            }).collect(Collectors.toList());

            cityPS.setRoadDataList(roadDataList);

            roadDataRepository.saveAll(roadDataList); // Save all maps

        }

        Corporate corporate = Corporate.builder()
                .world(worldPS)
                .name("LocalMarket")
                .logo("logo.png")
                .cash(Long.MAX_VALUE)
                .landAsset(0L)
                .techAsset(0L)
                .stockAsset(0L)
                .loans(0L)
                .commonStock(0L)
                .retainedEarnings(0L)
                .dividendsPaid(0L)
                .netWorth(0L)
                .donation(0L)
                .stockReturn(0L)
                .receivedDividends(0L)
                .loanInterest(0L)
                .otherProfit(0L)
                .netProfit(0L)
                .build();

        corporateRepository.save(corporate);

        List<String> allName = worldRepository.getAllName();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus("OK");
        responseMessage.setType("WORLD");
        responseMessage.setSubType("LIST");
        responseMessage.setPayload(allName);

        String responseJson = objectMapper.writeValueAsString(responseMessage);
        session.sendMessage(new TextMessage(responseJson));

    }

    public int estimateNumCircles(int mapWidth, int mapHeight, int radius, int spacing) {
        double effectiveRadius = radius + spacing; // spacing 원들사이의 최소간격
        double circleArea = Math.PI * Math.pow(effectiveRadius, 2);//원의 넓이
        double effectiveMapWidth = mapWidth - 2 * effectiveRadius; // account for map boundaries
        double effectiveMapHeight = mapHeight - 2 * effectiveRadius; // account for map boundaries
        double mapArea = effectiveMapWidth * effectiveMapHeight;
        return (int) Math.floor(mapArea / circleArea);
    }
    public CityInfoDTO convertCityInfoToCityInfoDTO(CityInfo cityInfo)
    {
        List<RoadData> roadDataList = cityInfo.getRoadDataList();
        List<RoadDataDTO> roadDataDTOList = roadDataList.stream().map(leave ->{
            RoadDataDTO roadDataDTO = RoadDataDTO.builder()
                    .x(leave.getX())
                    .y(leave.getY())
                    .width(leave.getWidth())
                    .height(leave.getHeight())
                    .build();
            return roadDataDTO;
        }).collect(Collectors.toList());

        CityInfoDTO cityInfoDTO = CityInfoDTO.builder()
                .name(cityInfo.getName())
                .x(cityInfo.getX())
                .y(cityInfo.getY())
                .radius(cityInfo.getRadius())
                .population(cityInfo.getPopulation())
                .spendingLevel(cityInfo.getSpendingLevel())
                .salaryLevel(cityInfo.getSalaryLevel())
                .economicClimate(cityInfo.getEconomicClimate())
                .roadDataList(roadDataDTOList)
                .build();

        return cityInfoDTO;
    }

    @Transactional
    public void 월드접속(WebSocketSession session, ConnectWorldRequest worldRequest) {

        boolean isHost = false;
        if(worldRequest.getWorldName().isEmpty()) {//일반유저

            if(worldName.isEmpty())
                throw new RuntimeException("normal user enter but no world");
        }
        else {//host 유저
            worldName = worldRequest.getWorldName();
            isHost = true;
        }
        String username = worldRequest.getUserName();
        String password = worldRequest.getPassword();
        Optional<User> opUser = userRepository.findByUsernamePassword(username, password);
        Optional<World> worldPS = worldRepository.findByName(worldName);

        if(worldPS.isPresent() == false)
            throw new RuntimeException("no world exsist");

        if(opUser.isPresent()){
            Long characterId = opUser.get().getCharacterId();
            Optional<Character> opCharacter = characterRepository.findById(characterId);
            Optional<World> opWorld = worldRepository.findByName(worldRequest.getWorldName());
            opUser.get().setHost(true);

            sessionManager.registerSession(session.getId(), session, opUser.get());
        }
        else//유저가 없다면 새로만들기 캐릭터 아무거나 연결시키기
        {
            Corporate corporate = Corporate.builder()
                    .world(worldPS.get())
                    .name("Random Corp")
                    .logo("logo.png")
                    .cash(10000000L)
                    .landAsset(0L)
                    .techAsset(0L)
                    .stockAsset(0L)
                    .loans(0L)
                    .commonStock(0L)
                    .retainedEarnings(0L)
                    .dividendsPaid(0L)
                    .netWorth(0L)
                    .donation(0L)
                    .stockReturn(0L)
                    .receivedDividends(0L)
                    .loanInterest(0L)
                    .otherProfit(0L)
                    .netProfit(0L)
                    .build();

            Corporate corporatePS = corporateRepository.save(corporate);

            CharacterGender gender = CharacterGender.getRandomGender();

            String fullname = NameGenerator.generate(gender);
            String texture = TexturePathGenerator.generate(gender);

            Character character = Character.builder()
                    .corporate(corporatePS)
                    .fullname(fullname)
                    .gender(gender)
                    .charController(CharController.USER)
                    .jobTitle(JobTitle.CEO)
                    .texture(texture)
                    .annualSalary(1000000L)
                    .businessExpertise(50)
                    .retailing(50)
                    .farming(50)
                    .manufacturing(50)
                    .researchNdevelopment(50)
                    .rawMaterialProduction(50)
                    .marketing(50)
                    .training(50)
                    .electorics(50)
                    .build();

            Character characterPS = characterRepository.save(character);

            User user = User.builder()
                    .username(username)
                    .password(password)
                    .worldId(worldPS.get().getId())
                    .characterId(characterPS.getId())
                    .isHost(isHost)
                    .build();
            User userPS = userRepository.save(user);

            sessionManager.registerSession(session.getId(), session, userPS);
        }


        ConnectWorldResponse connectWorldResponse = new ConnectWorldResponse();
        String name = worldRequest.getWorldName();
        connectWorldResponse.setName(name);
        connectWorldResponse.setHeight(worldPS.get().getHeigth());
        connectWorldResponse.setWidth(worldPS.get().getWidth());

        List<CityInfo> cityInfoList = cityInfoRepository.findAllByWorldId(worldPS.get().getId());

        List<CityInfoDTO> cityInfoDTOList = new ArrayList<>();
        for(CityInfo cityInfo : cityInfoList)
        {
            List<RoadData> roadDataList = roadDataRepository.findByCityInfo(cityInfo);
            cityInfo.setRoadDataList(roadDataList);
            cityInfoDTOList.add(convertCityInfoToCityInfoDTO(cityInfo));
        }
        connectWorldResponse.setCities(cityInfoDTOList);

        // ResponseMessage 구성
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus("OK");
        responseMessage.setType("WORLD");
        responseMessage.setSubType("CONNECT");
        responseMessage.setPayload(connectWorldResponse);

        String responseJson;
        try {
            responseJson = objectMapper.writeValueAsString(responseMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            session.sendMessage(new TextMessage(responseJson));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public void 월드목록(WebSocketSession session, WorldListRequest worldListRequest)
    {
        List<String> allName = worldRepository.getAllName();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus("OK");
        responseMessage.setType("WORLD");
        responseMessage.setSubType("LIST");
        responseMessage.setPayload(allName);

        String responseJson;
        try {
            responseJson = objectMapper.writeValueAsString(responseMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            session.sendMessage(new TextMessage(responseJson));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void 월드삭제(WebSocketSession session, DeleteWorldRequest deleteWorldRequest)
    {
        String worldName = deleteWorldRequest.getName();
        Optional<World> OpWorld = worldRepository.findByName(worldName);
        if(OpWorld.isPresent())
        {
            List<CityInfo> cityInfoList = cityInfoRepository.findAllByWorldId(OpWorld.get().getId());
            for(CityInfo cityInfoPS : cityInfoList)
            {
                roadDataRepository.deleteByMapNameId(cityInfoPS.getId());
            }

            cityInfoRepository.deleteByWorldId(OpWorld.get().getId());

            worldRepository.deleteById(OpWorld.get().getId());
        }

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus("OK");
        responseMessage.setType("WORLD");
        responseMessage.setSubType("DELETE");

        String responseJson;
        try {
            responseJson = objectMapper.writeValueAsString(responseMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            session.sendMessage(new TextMessage(responseJson));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
