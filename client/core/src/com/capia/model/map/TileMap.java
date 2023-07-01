package com.capia.model.map;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.capia.game.Capia;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


//Tile width 128px, height 64px
class Tile
{
    int x;
    int y;

    TileType type;

    public Tile(int x, int y,TileType type)
    {
        this.x = x;
        this.y = y;
        this.type = type;
    }

}

public class TileMap {

    final int TILE_WIDTH = 128;
    final int HF_TILE_WIDTH = TILE_WIDTH/2;
    final int TILE_HEIGHT = 64;
    final int HF_TILE_HEIGHT = TILE_HEIGHT/2;

    int height;
    int width;

    BitmapFont font;

    Array<Array<Tile>> arrTile;

    HashMap<TileType, Texture> tileTextures;

    public TileMap(int height, int width)
    {
        this.height = height;
        this.width = width;

        int halfwidth = TILE_WIDTH/2;
        int halfheight = TILE_HEIGHT/2;

        arrTile = new Array<Array<Tile>>();
        for(int y = 0; y < height; ++y)
        {
            Array<Tile> arr = new Array<>();
            for(int x = 0; x < width; ++x)
            {
                //타일의 왼쪽 하단 좌표임
                arr.add(new Tile( (halfwidth * x) - (halfwidth * y),  (halfheight * y) + (halfheight * x), TileType.GRASS));
            }
            arrTile.add(arr);
        }

        tileTextures = new HashMap<>();
        tileTextures.put(TileType.GRASS, new Texture(Gdx.files.internal("civic/ground/grass.png")));
        tileTextures.put(TileType.ROAD_DOWN, new Texture(Gdx.files.internal("civic/road/road.png")));
        tileTextures.put(TileType.ROAD_UP, new Texture(Gdx.files.internal("civic/road/road2.png")));
        tileTextures.put(TileType.ROAD_CROSS, new Texture(Gdx.files.internal("civic/road/road3.png")));

        font = new BitmapFont();
    }

    public void UpdateRoadTileTexture(@NotNull ArrayList<RoadData> leaves) {
        // 도로의 텍스쳐 업데이트
        for (RoadData leaf : leaves) {
            for (int i = leaf.getY(); i < leaf.getY() + leaf.getHeight(); ++i) {
                for (int j = leaf.getX(); j < leaf.getX() + leaf.getWidth(); ++j) {
                    // 해당 Tile 찾기
                    Tile tile = arrTile.get(i).get(j);

                    // 왼쪽 경계
                    if (j == leaf.getX()) {
                        tile.type = TileType.ROAD_UP;
                    }
                    // 아래쪽 경계
                    if (i == leaf.getY() + leaf.getHeight() - 1) {
                        tile.type = TileType.ROAD_DOWN;
                    }
                }
            }
        }
    }

    public Point getTileIndex(int x, int y) {

        //x,y가 어느 타일의 왼쪽하단 좌표이다. x,y를 통해 타일의 행과 열을 구한다
        int col = (2*y + x) / TILE_WIDTH;
        int row = (2*y - x) / TILE_WIDTH;

        return new Point(col, row);
    }
    public void render(Capia game) {

        //int cameraTileX = (int)(game.camera.position.x / (TILE_WIDTH/2));
        //int cameraTileY = (int)(game.camera.position.y / (TILE_HEIGHT/2));

        int leftx = (int)(game.camera.position.x - (game.camera.viewportWidth*game.camera.zoom/2.f));
        int rightx = (int)(game.camera.position.x + (game.camera.viewportWidth*game.camera.zoom/2.f));
        int downy = (int)(game.camera.position.y - (game.camera.viewportHeight*game.camera.zoom/2.f));
        int upy = (int)(game.camera.position.y + (game.camera.viewportHeight*game.camera.zoom/2.f));

        //좌표를 HF_TILE_WIDTH, HF_TILE_HEIGHT 단위로 변경한다. 이는 타일의 좌측하단 좌표를 의미한다
        int left_tilex = leftx - leftx%HF_TILE_WIDTH;
        int right_tilex = rightx - rightx%HF_TILE_WIDTH;
        int down_tiley = downy - downy%HF_TILE_HEIGHT;
        int up_tiley = upy- upy%HF_TILE_HEIGHT;

        //Point leftdownTileIndex = getTileIndex((int) leftx, (int) downy, width, height);

        //System.out.printf("campos %.1f %.1f, leftx %.1f, leftTilex %d\n", game.camera.position.x , game.camera.position.y, leftx, leftdownTileIndex.x);

        for(int y = down_tiley; y <= up_tiley; y+=HF_TILE_HEIGHT) {
            for (int x = left_tilex; x <= right_tilex; x+=HF_TILE_WIDTH) {

                //좌측하단 좌표를 통해 타일의 행과 열을 구한다
                Point tileIndex = getTileIndex(x, y);

                if(tileIndex.x < 0 || tileIndex.x >= width)
                    continue;
                if(tileIndex.y < 0 || tileIndex.y >= height)
                    continue;

                Tile tile = arrTile.get(tileIndex.y).get(tileIndex.x);
                float tileCenterX = tile.x + HF_TILE_WIDTH;
                float tileCenterY = tile.y + HF_TILE_HEIGHT;

                // Check if the tile is within the camera's view before drawing.
                if (game.camera.frustum.pointInFrustum(tileCenterX, tileCenterY, 0)) {
                    Texture texture = tileTextures.get(tile.type);
                    if (texture != null) {
                        game.batch.draw(texture, tile.x, tile.y);
                    }
                }

                font.draw(game.batch, String.format("%d, %d", tileIndex.x, tileIndex.y), tileCenterX, tileCenterY);
            }
        }
    }

    public void dispose() {
        // Dispose all textures in the tileTextures HashMap.
        for (Texture texture : tileTextures.values()) {
            texture.dispose();
        }

        // Clear the HashMap itself.
        tileTextures.clear();

        // Clear the arrTile Array.
        for (Array<Tile> array : arrTile) {
            array.clear();
        }
        arrTile.clear();

        font.dispose();
    }

}
