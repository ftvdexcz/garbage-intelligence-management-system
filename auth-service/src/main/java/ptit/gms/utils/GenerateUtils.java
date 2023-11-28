package ptit.gms.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class GenerateUtils {
    public static String generateRandomRoleCode(){
        return "role-" + RandomStringUtils.random(7, true, true);
    }

    public static String generateRandomUUID(){
        UUID uuid = UUID.randomUUID();

        return uuid.toString();
    }

    public static String generateRandomString(int length){
        return RandomStringUtils.random(length, true, true);
    }
}
