package org.code.Helpers;

import java.util.Random;

public class HelperFunctions {

    private static final Random random = new Random();

    public static int randomId() {
        return random.nextInt(10000);
    }
}
