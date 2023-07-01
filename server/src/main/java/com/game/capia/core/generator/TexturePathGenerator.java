package com.game.capia.core.generator;

import com.game.capia.model.character.CharacterGender;

import java.util.Random;

public class TexturePathGenerator {
    private static Random random = new Random();

    private static int MaxCount = 10;
    public static String generate(CharacterGender gender) {
        int randomNumber = random.nextInt(MaxCount) + 1;
        // Generate the file name based on the gender and random number
        String fileName = "character_" + gender.toString().toLowerCase() + "_" + String.format("%02d", randomNumber) + ".png";
        return fileName;
    }
}
