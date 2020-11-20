package com.example.klobold;

import java.util.Random;

class Util {


    static int getRandomVideoIndex() {
        Random random = new Random();
        return random.nextInt(Config.RANDOM_BOUND);
    }
}
