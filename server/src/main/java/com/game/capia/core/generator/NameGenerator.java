package com.game.capia.core.generator;

import com.game.capia.model.character.CharacterGender;

import java.util.Random;

public class NameGenerator {
    private static Random random = new Random();
    private static String[] maleFirstNames = {
            "James", "John", "Robert", "Michael", "William",
            "David", "Richard", "Joseph", "Charles", "Thomas",
            "Christopher", "Daniel", "Matthew", "Anthony", "Donald",
            "Mark", "Paul", "Steven", "Andrew", "Kenneth",
            "Joshua", "George", "Kevin", "Brian", "Edward",
            "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan"
    };
    private static String[] femaleFirstNames = {
            "Mary", "Jennifer", "Linda", "Patricia", "Elizabeth",
            "Susan", "Jessica", "Sarah", "Karen", "Nancy",
            "Lisa", "Betty", "Dorothy", "Sandra", "Ashley",
            "Kimberly", "Donna", "Emily", "Michelle", "Carol",
            "Amanda", "Melissa", "Deborah", "Stephanie", "Rebecca",
            "Laura", "Sharon", "Cynthia", "Kathleen", "Amy"
    };
    private static String[] lastNames = {
            "Smith", "Johnson", "Williams", "Brown", "Jones",
            "Miller", "Davis", "Garcia", "Rodriguez", "Wilson",
            "Martinez", "Anderson", "Taylor", "Thomas", "Hernandez",
            "Moore", "Martin", "Jackson", "Thompson", "White",
            "Lopez", "Lee", "Gonzalez", "Harris", "Clark",
            "Lewis", "Robinson", "Walker", "Perez", "Hall",
            "Young", "Allen", "Sanchez", "Wright", "King",
            "Scott", "Green", "Baker", "Adams", "Nelson",
            "Hill", "Ramirez", "Campbell", "Mitchell", "Roberts",
            "Carter", "Phillips", "Evans", "Turner", "Torres",
            "Parker", "Collins", "Edwards", "Stewart", "Flores",
            "Morris", "Nguyen", "Murphy", "Rivera", "Cook"
    };

    public static String generate(CharacterGender gender) {
        String firstName;
        if(gender == CharacterGender.FEMALE) {
            firstName = femaleFirstNames[random.nextInt(femaleFirstNames.length)];
        }
        else{
            firstName = maleFirstNames[random.nextInt(maleFirstNames.length)];
        }
        String lastName = lastNames[random.nextInt(lastNames.length)];
        return firstName + " " + lastName;
    }
}
