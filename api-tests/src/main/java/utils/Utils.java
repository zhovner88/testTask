package utils;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    
    public static int generateNonExistentOrderId() {
        return ThreadLocalRandom.current().nextInt(100000, 999999);
    }
}