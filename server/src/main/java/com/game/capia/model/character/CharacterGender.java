package com.game.capia.model.character;

import com.nimbusds.openid.connect.sdk.claims.Gender;

import java.util.Random;

public enum CharacterGender {
    MALE,
    FEMALE;

    private static final Random RANDOM = new Random();

    public static CharacterGender getRandomGender() {
        return values()[RANDOM.nextInt(values().length)];
    }
}
