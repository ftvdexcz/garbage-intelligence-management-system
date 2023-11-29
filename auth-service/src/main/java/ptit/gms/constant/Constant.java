package ptit.gms.constant;

public class Constant {
    public static int STATUS_USER_ACTIVE = 1;

    public static int STATUS_USER_INACTIVE = 0;

    public static String X_USER_ID_HEADER = "X-USER-ID";

    public static String X_USER_ROLE_HEADER = "X-USER-ROLE";

    public static String X_API_KEY_HEADER = "X-API-KEY";


    // get from header "X_USER_ID"
    public static String X_USER_ID = "";

    // get from header "X_USER_ROLE"

    public static String X_USER_ROLE = "";

    public static String ADMIN_ROLE_TYPE = "2";

    public static String USER_ROLE_TYPE = "1";

    public static String INTERNAL_ROLE_TYPE = "3";

    public static long REDIS_TTL_USER = 86400000L;
}
