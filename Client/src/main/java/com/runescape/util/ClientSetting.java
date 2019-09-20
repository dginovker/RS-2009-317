package com.runescape.util;

import com.runescape.Constants;
import com.runescape.Game;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.net.ByteBufferUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a client setting.
 *
 * @author
 */
public class ClientSetting {
    /**
     * The {@link java.util.logging.Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(ClientSetting.class.getName());
    /**
     * The username opcode.
     */
    private static final int USERNAME = 1;
    /**
     * The password opcode.
     */
    private static final int PASSWORD = 2;
    /**
     * The remember me opcode.
     */
    private static final int REMEMBER_ME = 3;
    /**
     * The saved config opcode.
     */
    private static final int SAVED_CONFIG = 4;
    /**
     * The settings file location.
     */
    private final File SETTINGS_FILE = new File(Constants.getCachePath(true) + File.separator + "preferences.dat");
    /**
     * The {@link Game} instance.
     */
    private final Game game;
    /**
     * The username to save.
     */
    private String username;
    /**
     * The password to save.
     */
    private String password;
    /**
     * If the user is remembered on next launch.
     */
    private boolean rememberMe;
    /**
     * The saved configuration values.
     */
    private int[][] savedConfigs;

    /**
     * Constructs the <code>ClientSetting</code> class.
     *
     * @param game The {@link Game} instance.
     */
    public ClientSetting(Game game) {
        this.game = game;
    }

    /**
     * Loads the settings.
     */
    public void load() {
        if (Constants.DEBUG_MODE) {
            logger.log(Level.INFO, "Loading settings...");
        }
        final File file = SETTINGS_FILE;
        if (!file.exists()) {
            boolean created = false;
            try {
                created = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Constants.DEBUG_MODE) {
                logger.log(Level.INFO, "No settings file found. {0}", (created ? "Created new settings file." : "Could not create settings file!"));
            }
            return;
        }
        if (file.length() == 0) {
            if (Constants.DEBUG_MODE) {
                logger.log(Level.INFO, "Settings file empty.");
            }
            return;
        }
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r"); FileChannel fileChannel = randomAccessFile.getChannel()) {
            java.nio.ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            int opcode;
            int length;
            int[] opcodeHistory = new int[5];
            while ((opcode = byteBuffer.get() & 0xFF) != 0) {
                switch (opcode) {
                    case USERNAME:
                        String savedUsername = ByteBufferUtils.getRS2String(byteBuffer);
                        if (savedUsername == null || savedUsername.length() < 1) {
                            break;
                        }
                        game.setLoginUsername(username = savedUsername);
                        game.rememberMe = true;
                        game.loginScreenCursorPos = 1;
                        break;
                    case PASSWORD:
                        String savedPassword = ByteBufferUtils.getRS2String(byteBuffer);
                        if (savedPassword == null || savedPassword.length() < 1) {
                            break;
                        }
                        game.setLoginPassword(password = savedPassword);
                        game.rememberMe = true;
                        game.loginScreenCursorPos = 0;
                        break;
                    case REMEMBER_ME:
                        game.rememberMe = true;
                        break;
                    case SAVED_CONFIG:
                        length = byteBuffer.getInt();
                        while (length > 0) {
                            int index = byteBuffer.getInt();
                            int value = byteBuffer.getInt();
                            game.processInterfaceConfiguration(index, value);
                            length--;
                        }
                        break;
                    default:
                        if (Constants.DEBUG_MODE) {
                            logger.log(Level.WARNING, "Failed handling setting opcode: {0}, history: {1}", new Object[]{opcode, Arrays.toString(opcodeHistory)});
                        }
                        break;
                }
                System.arraycopy(opcodeHistory, 0, opcodeHistory, 1, opcodeHistory.length - 2 + 1);
                opcodeHistory[0] = opcode;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves the settings.
     */
    public void save() {
        if (Constants.DEBUG_MODE) {
            logger.log(Level.INFO, "Saving settings...");
        }
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(4096 << 2);
        File file = SETTINGS_FILE;
        if (game.rememberMe) {
            if (game.getLoginUsername() != null && game.getLoginUsername().length() > 0) {
                byteBuffer.put((byte) USERNAME);
                ByteBufferUtils.putRS2String(game.getLoginUsername(), byteBuffer);
            }
            if (game.getLoginUsername() != null && game.getLoginUsername().length() > 0) {
                byteBuffer.put((byte) PASSWORD);
                ByteBufferUtils.putRS2String(game.getLoginPassword(), byteBuffer);
            }
            byteBuffer.put((byte) REMEMBER_ME);
        }
        if (game.getInterfaceConfig() != null) {
            int length = 0;
            for (int savedConfig : Constants.SAVED_CONFIGS) {
                InterfaceConfiguration interfaceConfiguration = InterfaceConfiguration.forId(savedConfig);
                if (interfaceConfiguration == null) {
                    continue;
                }
                if (game.getInterfaceConfig(interfaceConfiguration) > 0) {
                    length++;
                }
            }
            if (length > 0) {
                byteBuffer.put((byte) SAVED_CONFIG);
                byteBuffer.putInt(length);
                for (int savedConfig : Constants.SAVED_CONFIGS) {
                    InterfaceConfiguration interfaceConfiguration = InterfaceConfiguration.forId(savedConfig);
                    if (interfaceConfiguration == null) {
                        continue;
                    }
                    if (game.getInterfaceConfig(interfaceConfiguration) > 0) {
                        byteBuffer.putInt(savedConfig);
                        byteBuffer.putInt(game.getInterfaceConfig(interfaceConfiguration));
                    }
                }
            }
        }
        byteBuffer.put((byte) 0);
        byteBuffer.flip();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw"); FileChannel fileChannel = randomAccessFile.getChannel()) {
            fileChannel.write(byteBuffer);
            randomAccessFile.close();
            fileChannel.close();
            if (Constants.DEBUG_MODE) {
                logger.log(Level.INFO, "Wrote settings file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the username to save.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password to save.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets if the user is remembered on next launch.
     *
     * @return <code>True</code> if so.
     */
    public boolean isRememberMe() {
        return rememberMe;
    }

    /**
     * Gets the saved configuration values.
     *
     * @return The configuration values.
     */
    public int[][] getSavedConfigs() {
        return savedConfigs;
    }
}
