package util;

import java.io.File;

public class Constants {

    public static final String VALIDATED_REQUEST = "Request verified";
    public static final String REQUEST_DENIED = "Request denied";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String REQUEST_ERROR = "Error while validating";
    public static final String NO_FILE_ERROR = "File not found";
    public static final int HTTP_PORT = 80;
    public static final String HASH_FUNCTION = "SHA-256";

    public static final String FILE_DIRECTORY = "files" + File.separator;
    public static final String GAMEPACK_PATH = FILE_DIRECTORY + "gamepack.jar";
    public static final String CONFIG_FILE_PATH = FILE_DIRECTORY + "gielinor.yml";
    public static final String LAUNCHER_PATH = FILE_DIRECTORY + "Gielinor.jar";

    public static final String LOG_FILE = "out.txt";

}