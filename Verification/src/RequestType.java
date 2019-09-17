import util.Constants;

import java.io.File;
import java.util.Arrays;

public enum RequestType {
    GAMEPACK(Constants.GAMEPACK_PATH),
    CONFIG(Constants.CONFIG_FILE_PATH),
    LAUNCHER(Constants.LAUNCHER_PATH);

    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public static RequestType forName(String name) {
        return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static boolean allFilesAvailable() {
        for (RequestType file: values()) {
            File toCheck = new File(file.filePath);
            if (!toCheck.exists()) {
                return false;
            }
        }
        return true;
    }

    RequestType(String filePath) {
        this.filePath = filePath;
    }

}
