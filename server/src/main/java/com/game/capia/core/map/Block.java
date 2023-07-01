package com.game.capia.core.map;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter

public class Block {
    private int x;//left
    private int y;//down
    private int width;
    private int height;

    private Block leftChild;
    private Block rightChild;

    private static final Random rand = new Random();

    public Block(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public List<Block> getLeaves() {
        List<Block> leaves = new ArrayList<>();
        if(this.leftChild == null && this.rightChild == null) {
            leaves.add(this);
        } else {
            if(this.leftChild != null) {
                leaves.addAll(this.leftChild.getLeaves());
            }
            if(this.rightChild != null) {
                leaves.addAll(this.rightChild.getLeaves());
            }
        }
        return leaves;
    }
    public void divide(int depth) {
        // 50% 확률로 분할
        if(depth < 2 && rand.nextInt(10) < 5) {
            if (this.width < this.height || (this.width == this.height && rand.nextBoolean())) {
                // 가로로 분할
                this.leftChild = new Block(x, y, width, height/2);
                this.rightChild = new Block(x, y + height / 2, width, height/2);
            } else {
                // 세로로 분할
                this.leftChild = new Block(x, y, width/2, height);
                this.rightChild = new Block(x + width / 2, y, width/2, height);
            }

            if(rand.nextBoolean()){
                this.leftChild.divide(depth + 1);
            }
            else{
                this.rightChild.divide(depth + 1);
            }

        }
    }


}
