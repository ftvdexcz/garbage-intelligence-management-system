package ptit.gms.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.UUID;

public class GenerateUtils {
    public static String generateRandomBinCode(){
        return "bin-" + RandomStringUtils.random(12, true, true);
    }

    public static String generateRandomUUID(){
        UUID uuid = UUID.randomUUID();

        return uuid.toString();
    }

    public static String generateRandomString(int length){
        return RandomStringUtils.random(length, true, true);
    }
}
