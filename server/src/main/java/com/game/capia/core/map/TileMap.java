package com.game.capia.core.map;

import com.capia.model.map.TileType;

import java.util.ArrayList;
import java.util.List;


//Tile width 128px, height 64px
class Tile
{
    int width;
    int height;

    int x;
    int y;

    TileType type;

    public Tile(int width, int height, int x, int y, TileType type)
    {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.type = type;
    }

}

//서버에서 체크하지 말자
public class TileMap {

    final int TILE_WIDTH = 128;
    final int TILE_HEIGHT = 64;

    int width;
    int height;

    ArrayList<ArrayList<Tile>> arrTile;

    public TileMap(int width, int height, int minsize, List<Block> blockList)
    {
        this.width = width;
        this.height = height;

        int halfwidth = TILE_WIDTH/2;
        int halfheight = TILE_HEIGHT/2;

        arrTile = new ArrayList<ArrayList<Tile>>();
        for(int r = 0; r < width; ++r)
        {
            ArrayList<Tile> arr = new ArrayList<>();
            for(int c = 0; c < height; ++c)
            {
                arr.add(new Tile(r, c,  (halfwidth * c) - (halfwidth * r),  (halfheight * r) + (halfheight * c), TileType.GRASS));
            }
            arrTile.add(arr);
        }

        UpdateRoadTileTexture(width, height, blockList);

    }

    private void UpdateRoadTileTexture(int width, int height, List<Block> leaves) {
        // 도로의 텍스쳐 업데이트
        for (Block leaf : leaves) {
            for (int i = leaf.getX(); i < leaf.getX() + leaf.getHeight(); ++i) {
                for (int j = leaf.getY(); j < leaf.getY() + leaf.getWidth(); ++j) {
                    // 왼쪽 경계
                    if (i == leaf.getX()) {
                        // 왼쪽과 위쪽 경계의 교차점
                        if (j == leaf.getY()) {
                            arrTile.get(i).get(j).type = TileType.ROAD_CROSS;
                        } else {
                            arrTile.get(i).get(j).type = TileType.ROAD_UP;
                        }
                        // 아래쪽 경계
                    } else if (j == leaf.getY()) {
                        arrTile.get(i).get(j).type = TileType.ROAD_DOWN;
                    }
                }
            }
        }

        // 가장 오른쪽 경계에 도로 설정
        for (int cr = 0; cr < width; ++cr) {
            Tile left = arrTile.get(cr).get(height -2);
            if (left.type == TileType.ROAD_UP) {
                arrTile.get(cr).get(height - 1).type = TileType.ROAD_CROSS;
            } else {
                arrTile.get(cr).get(height - 1).type = TileType.ROAD_DOWN;
            }
        }

        // 가장 위쪽 경계에 도로 설정
        for (int j = 0; j < height; ++j) {
            Tile down = arrTile.get(width -2).get(j);
            if (down.type == TileType.ROAD_DOWN) {
                arrTile.get(width - 1).get(j).type = TileType.ROAD_CROSS;
            } else {
                arrTile.get(width - 1).get(j).type = TileType.ROAD_UP;
            }
        }
    }


    public void dispose() {

        // Clear the arrTile Array.
        for (ArrayList<Tile> array : arrTile) {
            array.clear();
        }
        arrTile.clear();
    }

}
