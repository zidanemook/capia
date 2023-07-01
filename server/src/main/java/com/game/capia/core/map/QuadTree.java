package com.game.capia.core.map;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Random;


@Getter
public class QuadTree {
    int x, y, width, height;
    QuadTree[] children;

    public QuadTree(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.children = new QuadTree[4];
    }

    public void split(int minSize) {
        if (width < 2 * minSize || height < 2 * minSize) {
            return;
        }

        Random rand = new Random();
        int randWidth, randHeight;
        if(width - 2 * minSize > 0) {
            randWidth = rand.nextInt(width - 2 * minSize) + minSize;
        } else {
            randWidth = width / 2;
        }

        if(height - 2 * minSize > 0) {
            randHeight = rand.nextInt(height - 2 * minSize) + minSize;
        } else {
            randHeight = height / 2;
        }

        children[0] = new QuadTree(x, y, randWidth, randHeight);
        children[1] = new QuadTree(x + randWidth, y, width - randWidth, randHeight);
        children[2] = new QuadTree(x, y + randHeight, randWidth, height - randHeight);
        children[3] = new QuadTree(x + randWidth, y + randHeight, width - randWidth, height - randHeight);

        for (QuadTree child : children) {
            child.split(minSize);
        }
    }

    public ArrayList<QuadTree> getLeaves() {
        ArrayList<QuadTree> leaves = new ArrayList<>();
        if (children[0] == null) {
            leaves.add(this);
        } else {
            for (QuadTree child : children) {
                leaves.addAll(child.getLeaves());
            }
        }
        return leaves;
    }

}
