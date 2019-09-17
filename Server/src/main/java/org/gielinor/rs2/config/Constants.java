package org.gielinor.rs2.config;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.impl.YellCommand;

/**
 * Represents server constants.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class Constants {

    /**
     * The minimum play time for actions (30 minutes).
     */
    public static final long MINIMUM_PLAY_TIME = TimeUnit.MINUTES.toSeconds(30);
    public static final boolean CONVERT_IDS = true;
    /**
     * Titles.
     */
    public static final boolean PLAYER_TITLES = true;
    /**
     * The revision of the game server.
     */
    public static final int REVISION = 47;
    /**
     * NPC bits for the client to read.
     */
    public static final int NPC_BITS = Integer.parseInt(ServerVar.fetch("npc_bits"));
    /**
     * The coins drop modifier for NPC drops.
     */
    public static final int COIN_DROP_MULTIPLIER = 2;
    /**
     * The max combat level.
     */
    public static final int MAX_COMBAT_LEVEL = 126;
    /**
     * Temporary variable of trial Gielinor Moderators.
     */
    private static final String[] TEMP_FMODS = new String[]{ "" };
    /**
     * The rights that will not be saved into highscores.
     */
    public static final Rights[] HIGHSCORE_RIGHTS_DISALLOW = new Rights[]{ Rights.GIELINOR_MODERATOR };

    /**
     * Global Yell timer - set duration to 0 or -1 to disable
     */
    public static final YellCommand.YellTimer YELL_TIMER = new YellCommand.YellTimer(TimeUnit.SECONDS);

    /**
     * Phrases which are not allowed to be sent in yell
     */
    public static final List<String> DISALLOWED_PHRASES = Arrays.asList(":tradereq:", ":chalreq:", ":duelfriend:",
        ":duelreq:", ":assistreq:", ":clanreq:", ":link:", "<img=", "<a href", "<u>", "<b>", "<col");

    /**
     * Phrases which are not allowed to be used in yell tags
     */
    public static final List<String> DISALLOWED_TAG_PHRASES = Arrays.asList("owner", "mod", "admin", "developer", "<b", "<col", "<a href", "<img");

    /**
     * The max char length of yell tags
     */
    public static final int YELL_TAG_MAX_LENGTH = 16;

    public static final String YELL_MESSAGE_COLOUR = "151580";

    /**
     * The paths of the beans configurations for Spring.
     */
    public static final String[] BEANS_CONFIGURATIONS = new String[]{ "beans-config.xml" };
    /**
     * The directory of the cache.
     */
    public static final String CACHE_DIRECTORY = "data/cache";
    /**
     * The directory of class files.
     */
    public static final String CLASS_DIRECTORY = "target/classes/";
    /**
     * The path in which on demand json data files are stored.
     */
    public static final String ONDEMAND_PATH = "data/ondemand";

    public static final String
        HIGHSCORE_VOTE_CONFIGURATION_PATH = "database.yml",
        DATA_CONFGIURATION_PATH = "data/configuration.cfg";

    /**
     * The location binary player accounts are saved and loaded.
     */
    public static final String BINARY_PLAYER_SAVE_DIRECTORY = "data/player/";

    /**
     * The default clan name.
     */
    public static final String DEFAULT_CLAN_NAME = "Help";
    /**
     * The incoming packet sizes.
     */
    public static final int[] PACKET_SIZES = new int[257];

    static {
        PACKET_SIZES[0] = 0;
        PACKET_SIZES[1] = 0;
        PACKET_SIZES[2] = 0;
        PACKET_SIZES[3] = 1;
        PACKET_SIZES[4] = -1;
        PACKET_SIZES[5] = 0;
        PACKET_SIZES[6] = 0;
        PACKET_SIZES[7] = 0;
        PACKET_SIZES[8] = 0;
        PACKET_SIZES[9] = 0;
        PACKET_SIZES[10] = 0;
        PACKET_SIZES[11] = 0;
        PACKET_SIZES[12] = 0;
        PACKET_SIZES[13] = 0;
        PACKET_SIZES[14] = 8;
        PACKET_SIZES[15] = 0;
        PACKET_SIZES[16] = 6;
        PACKET_SIZES[17] = 2;
        PACKET_SIZES[18] = 2;
        PACKET_SIZES[19] = 0;
        PACKET_SIZES[20] = 0;
        PACKET_SIZES[21] = 2;
        PACKET_SIZES[22] = 0;
        PACKET_SIZES[23] = 6;
        PACKET_SIZES[24] = 0;
        PACKET_SIZES[25] = 12;
        PACKET_SIZES[26] = 0;
        PACKET_SIZES[27] = 0;
        PACKET_SIZES[28] = 0;
        PACKET_SIZES[29] = 0;
        PACKET_SIZES[30] = 0;
        PACKET_SIZES[31] = 0;
        PACKET_SIZES[32] = 0;
        PACKET_SIZES[33] = 0;
        PACKET_SIZES[34] = 0;
        PACKET_SIZES[35] = 8;
        PACKET_SIZES[36] = 4;
        PACKET_SIZES[37] = 0;
        PACKET_SIZES[38] = 0;
        PACKET_SIZES[39] = 2;
        PACKET_SIZES[40] = 2;
        PACKET_SIZES[41] = 6;
        PACKET_SIZES[42] = 0;
        PACKET_SIZES[43] = 6;
        PACKET_SIZES[44] = 0;
        PACKET_SIZES[45] = -1;
        PACKET_SIZES[46] = 0;
        PACKET_SIZES[47] = 0;
        PACKET_SIZES[48] = 0;
        PACKET_SIZES[49] = 0;
        PACKET_SIZES[50] = 0;
        PACKET_SIZES[51] = 0;
        PACKET_SIZES[52] = 0;
        PACKET_SIZES[53] = 12;
        PACKET_SIZES[54] = 0;
        PACKET_SIZES[55] = 0;
        PACKET_SIZES[56] = 0;
        PACKET_SIZES[57] = 8;
        PACKET_SIZES[58] = 8;
        PACKET_SIZES[59] = 1;
        PACKET_SIZES[60] = -1;
        PACKET_SIZES[61] = 8;
        PACKET_SIZES[62] = 0;
        PACKET_SIZES[63] = 0;
        PACKET_SIZES[64] = 0;
        PACKET_SIZES[65] = 0;
        PACKET_SIZES[66] = 0;
        PACKET_SIZES[67] = 0;
        PACKET_SIZES[68] = 0;
        PACKET_SIZES[69] = 0;
        PACKET_SIZES[70] = 6;
        PACKET_SIZES[71] = 0;
        PACKET_SIZES[72] = 2;
        PACKET_SIZES[73] = 2;
        PACKET_SIZES[74] = 8;
        PACKET_SIZES[75] = 6;
        PACKET_SIZES[76] = 0;
        PACKET_SIZES[77] = -1;
        PACKET_SIZES[78] = 0;
        PACKET_SIZES[79] = 6;
        PACKET_SIZES[80] = 0;
        PACKET_SIZES[81] = 0;
        PACKET_SIZES[82] = 0;
        PACKET_SIZES[83] = 0;
        PACKET_SIZES[84] = 0;
        PACKET_SIZES[85] = 1;
        PACKET_SIZES[86] = 4;
        PACKET_SIZES[87] = 6;
        PACKET_SIZES[88] = 0;
        PACKET_SIZES[89] = 0;
        PACKET_SIZES[90] = 0;
        PACKET_SIZES[91] = 0;
        PACKET_SIZES[92] = 0;
        PACKET_SIZES[93] = 2;
        PACKET_SIZES[94] = 0;
        PACKET_SIZES[95] = 3;
        PACKET_SIZES[96] = 0;
        PACKET_SIZES[97] = 0;
        PACKET_SIZES[98] = -1;
        PACKET_SIZES[99] = 0;
        PACKET_SIZES[100] = 0;
        PACKET_SIZES[101] = 20;
        PACKET_SIZES[102] = 0;
        PACKET_SIZES[103] = -1;
        PACKET_SIZES[104] = 0;
        PACKET_SIZES[105] = 0;
        PACKET_SIZES[106] = 0;
        PACKET_SIZES[107] = 0;
        PACKET_SIZES[108] = 0;
        PACKET_SIZES[109] = 0;
        PACKET_SIZES[110] = 0;
        PACKET_SIZES[111] = 0;
        PACKET_SIZES[112] = 0;
        PACKET_SIZES[113] = 0;
        PACKET_SIZES[114] = 0;
        PACKET_SIZES[115] = 0;
        PACKET_SIZES[116] = 0;
        PACKET_SIZES[117] = 6;
        PACKET_SIZES[118] = 0;
        PACKET_SIZES[119] = 0;
        PACKET_SIZES[120] = 1;
        PACKET_SIZES[121] = 0;
        PACKET_SIZES[122] = 6;
        PACKET_SIZES[123] = 0;
        PACKET_SIZES[124] = 0;
        PACKET_SIZES[125] = 0;
        PACKET_SIZES[126] = -1;
        PACKET_SIZES[127] = 0;
        PACKET_SIZES[128] = 2;
        PACKET_SIZES[129] = 6;
        PACKET_SIZES[130] = 2;
        PACKET_SIZES[131] = 4;
        PACKET_SIZES[132] = 6;
        PACKET_SIZES[133] = 8;
        PACKET_SIZES[134] = 0;
        PACKET_SIZES[135] = 6;
        PACKET_SIZES[136] = 0;
        PACKET_SIZES[137] = 0;
        PACKET_SIZES[138] = 0;
        PACKET_SIZES[139] = 2;
        PACKET_SIZES[140] = 0;
        PACKET_SIZES[141] = 6;
        PACKET_SIZES[142] = 0;
        PACKET_SIZES[143] = 0;
        PACKET_SIZES[144] = 0;
        PACKET_SIZES[145] = 6;
        PACKET_SIZES[146] = 0;
        PACKET_SIZES[147] = 0;
        PACKET_SIZES[148] = 0;
        PACKET_SIZES[149] = 0;
        PACKET_SIZES[150] = 0;
        PACKET_SIZES[151] = 0;
        PACKET_SIZES[152] = 1;
        PACKET_SIZES[153] = 2;
        PACKET_SIZES[154] = 0;
        PACKET_SIZES[155] = 2;
        PACKET_SIZES[156] = 6;
        PACKET_SIZES[157] = 0;
        PACKET_SIZES[158] = 0;
        PACKET_SIZES[159] = 0;
        PACKET_SIZES[160] = 0;
        PACKET_SIZES[161] = 0;
        PACKET_SIZES[162] = 0;
        PACKET_SIZES[163] = 0;
        PACKET_SIZES[164] = -1;
        PACKET_SIZES[165] = -1;
        PACKET_SIZES[166] = 0;
        PACKET_SIZES[167] = 0;
        PACKET_SIZES[168] = 0;
        PACKET_SIZES[169] = 2;
        PACKET_SIZES[170] = 0;
        PACKET_SIZES[171] = 0;
        PACKET_SIZES[172] = 0;
        PACKET_SIZES[173] = 0;
        PACKET_SIZES[174] = 0;
        PACKET_SIZES[175] = 0;
        PACKET_SIZES[176] = 0;
        PACKET_SIZES[177] = 0;
        PACKET_SIZES[178] = 0;
        PACKET_SIZES[179] = 0;
        PACKET_SIZES[180] = 0;
        PACKET_SIZES[181] = 8;
        PACKET_SIZES[182] = 6;
        PACKET_SIZES[183] = 3;
        PACKET_SIZES[184] = 0;
        PACKET_SIZES[185] = 2;
        PACKET_SIZES[186] = 0;
        PACKET_SIZES[187] = 0;
        PACKET_SIZES[188] = 8;
        PACKET_SIZES[189] = 1;
        PACKET_SIZES[190] = 0;
        PACKET_SIZES[191] = 0;
        PACKET_SIZES[192] = 12;
        PACKET_SIZES[193] = 0;
        PACKET_SIZES[194] = 0;
        PACKET_SIZES[195] = 0;
        PACKET_SIZES[196] = 0;
        PACKET_SIZES[197] = 0;
        PACKET_SIZES[198] = 0;
        PACKET_SIZES[199] = 0;
        PACKET_SIZES[200] = 2;
        PACKET_SIZES[201] = 0;
        PACKET_SIZES[202] = 0;
        PACKET_SIZES[203] = 0;
        PACKET_SIZES[204] = 3;
        PACKET_SIZES[205] = 0;
        PACKET_SIZES[206] = 0;
        PACKET_SIZES[207] = 0;
        PACKET_SIZES[208] = 4;
        PACKET_SIZES[209] = 0;
        PACKET_SIZES[210] = 4;
        PACKET_SIZES[211] = 2;
        PACKET_SIZES[212] = 0;
        PACKET_SIZES[213] = 2;
        PACKET_SIZES[214] = 7;
        PACKET_SIZES[215] = 8;
        PACKET_SIZES[216] = 0;
        PACKET_SIZES[217] = 0;
        PACKET_SIZES[218] = 10;
        PACKET_SIZES[219] = 0;
        PACKET_SIZES[220] = 0;
        PACKET_SIZES[221] = 2;
        PACKET_SIZES[222] = 0;
        PACKET_SIZES[223] = 0;
        PACKET_SIZES[224] = 0;
        PACKET_SIZES[225] = 0;
        PACKET_SIZES[226] = -1;
        PACKET_SIZES[227] = 0;
        PACKET_SIZES[228] = 6;
        PACKET_SIZES[229] = 0;
        PACKET_SIZES[230] = 1;
        PACKET_SIZES[231] = 0;
        PACKET_SIZES[232] = 0;
        PACKET_SIZES[233] = 0;
        PACKET_SIZES[234] = 6;
        PACKET_SIZES[235] = 2;
        PACKET_SIZES[236] = 6;
        PACKET_SIZES[237] = 8;
        PACKET_SIZES[238] = 1;
        PACKET_SIZES[239] = 0;
        PACKET_SIZES[240] = 0;
        PACKET_SIZES[241] = 4;
        PACKET_SIZES[242] = 9;
        PACKET_SIZES[243] = 0;
        PACKET_SIZES[244] = 0;
        PACKET_SIZES[245] = 0;
        PACKET_SIZES[246] = -1;
        PACKET_SIZES[247] = 0;
        PACKET_SIZES[248] = -1;
        PACKET_SIZES[249] = 4;
        PACKET_SIZES[250] = 0;
        PACKET_SIZES[251] = 0;
        PACKET_SIZES[252] = 6;
        PACKET_SIZES[253] = 6;
        PACKET_SIZES[254] = 0;
        PACKET_SIZES[255] = 0;
        PACKET_SIZES[256] = 0;
    }

    public static boolean KILL_SERVER;

    /**
     * Official Discord server link
     */
    public static final String DISCORD_SERVER = "https://discord.me/gielinor-rsps"; // leave blank if none

    /**
     * Represents the name of the server.
     */
    public static String SERVER_NAME = "Gielinor";

    private static final String WEBSITE_URL = "https://gielinor.org/";

    public static final String FORUMS_URL = WEBSITE_URL + "community/";

    public static final String VOTE_URL = WEBSITE_URL + "vote/";

    /**
     * The id of the game world.
     */
    public static int WORLD_ID = 0;
    /**
     * Represents the post the server is hosted on.
     */
    public static int PORT = 43594;
    /**
     * Whether or not the {@link org.gielinor.utilities.crypto.ISAACCipher} is
     * enabled.
     */
    public static boolean ISAAC_ENABLED = false;
    /**
     * Whether or not bank tabs are enabled.
     */
    public static boolean BANK_TABS = true;
    /**
     * The experience modifier, for combat skills.
     */
    public static double COMBAT_EXP_MODIFIER = 1;
    /**
     * The experience modifier, for non-combat skills.
     */
    public static double NON_COMBAT_EXP_MODIFIER = 1;
    /**
     * The {@link org.gielinor.spring.service.AccountService} to use for player
     * loading and saving.
     */
    public static String ACCOUNT_SERVICE = "mysqlAccountService";
    /**
     * Whether or not highscores are enabled.
     */
    public static boolean HIGHSCORES_ENABLED = true;

    public static boolean isTemp(String username) {
        for (String name : TEMP_FMODS) {
            if (name.equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

}
