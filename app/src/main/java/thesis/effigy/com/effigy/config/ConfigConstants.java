package thesis.effigy.com.effigy.config;

/**
 * Created by Borys on 1/21/17.
 */

public class ConfigConstants {
    //General config
    public static final String SERVER_ADDRESS = "http://192.168.0.11:8080/";
    //Specific server requests
    public static final String REQUEST_LOGIN = SERVER_ADDRESS + "login";
    public static final String REQUEST_REGISTER = SERVER_ADDRESS + "register";
    public static final String REQUEST_SCORES = SERVER_ADDRESS + "scores";
    public static final String REQUEST_PARENT_IMAGE = SERVER_ADDRESS + "images/parent/random?username=";
    public static final String REQUEST_SIMILAR_IMAGES = SERVER_ADDRESS + "images?parentId=";
    public static final String REQUEST_TOTAL_SCORE = SERVER_ADDRESS + "scores/count?username=";
    public static final String REQUEST_BEST_SCORE = SERVER_ADDRESS + "scores/count/best?username=";
    public static final String REQUEST_FILE = SERVER_ADDRESS + "images/file";
}
