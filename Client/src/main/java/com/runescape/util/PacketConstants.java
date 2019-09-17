package com.runescape.util;

/**
 * Class containing packet-related constants.
 *
 * @author 7Winds
 */
public class PacketConstants {
    public static final int GRAND_EXCHANGE_OFFER = 33;

    public static final int PLAYER_UPDATING = 81;

    public static final int DELETE_GROUND_ITEM = 64;

    public static final int REMOVE_ITEM_MODEL = 156;

    public static final int OPEN_WELCOME_SCREEN = 176;

    public static final int SHOW_PLAYER_HEAD_ON_INTERFACE = 185;

    public static final int CLAN_CHAT = 217;

    public static final int RESET_CAMERA = 107;

    public static final int CLEAN_ITEMS_OF_INTERFACE = 72;

    public static final int SHOW_IGNORE_NAMES = 214;

    public static final int SPIN_CAMERA = 166;

    public static final int SEND_SKILL = 134;

    public static final int SEND_SIDE_TAB = 71;

    public static final int PLAY_SONG = 74;

    public static final int NEXT_OR_PREVIOUS_SONG = 121;

    public static final int LOGOUT = 109;

    public static final int MOVE_COMPONENT = 70;

    /**
     * region X = ShortA
     * region Y = Short
     */
    public static final int SEND_MAP_REGION = 73;

    /**
     * region Y - ShortA
     * blank tile - 1 bit
     * x << 14 | y << 3 | h << 24 |
     * rotation << 1 - 26 Bits region X - Short
     */
    public static final int SEND_REGION_MAP_REGION = 241;

    public static final int SYSTEM_UPDATE = 114;

    public static final int PLAY_SOUND_EFFECT = 174;

    public static final int SUMMONING_OPTIONS = 90;

    public static final int SUMMONING_INFORMATION = 112;

    public static final int[] PACKET_SIZES = new int[257];

    static {
        PACKET_SIZES[0] = 0;
        PACKET_SIZES[1] = 0;
        PACKET_SIZES[2] = 0;
        PACKET_SIZES[3] = 0;
        PACKET_SIZES[4] = 6;
        PACKET_SIZES[5] = 0;
        PACKET_SIZES[6] = 0;
        PACKET_SIZES[7] = 0;
        PACKET_SIZES[8] = 4;
        PACKET_SIZES[9] = 0;
        PACKET_SIZES[10] = 0;
        PACKET_SIZES[11] = 0;
        PACKET_SIZES[12] = 0;
        PACKET_SIZES[13] = 0;
        PACKET_SIZES[14] = 0;
        PACKET_SIZES[15] = 0;
        PACKET_SIZES[16] = 0;
        PACKET_SIZES[17] = 0;
        PACKET_SIZES[18] = 0;
        PACKET_SIZES[19] = 0;
        PACKET_SIZES[20] = 0;
        PACKET_SIZES[21] = 0;
        PACKET_SIZES[22] = 0;
        PACKET_SIZES[23] = 0;
        PACKET_SIZES[24] = 1;
        PACKET_SIZES[25] = 0;
        PACKET_SIZES[26] = 0;
        PACKET_SIZES[27] = 0;
        PACKET_SIZES[28] = 0;
        PACKET_SIZES[29] = 0;
        PACKET_SIZES[30] = 0;
        PACKET_SIZES[31] = 0;
        PACKET_SIZES[32] = 0;
        PACKET_SIZES[33] = 20;
        PACKET_SIZES[34] = -2;
        PACKET_SIZES[35] = 4;
        PACKET_SIZES[36] = 3;
        PACKET_SIZES[37] = 6;
        PACKET_SIZES[38] = 0;
        PACKET_SIZES[39] = 0;
        PACKET_SIZES[40] = 0;
        PACKET_SIZES[41] = 0;
        PACKET_SIZES[42] = 0;
        PACKET_SIZES[43] = 0;
        PACKET_SIZES[44] = 5;
        PACKET_SIZES[45] = 0;
        PACKET_SIZES[46] = 0;
        PACKET_SIZES[47] = 6;
        PACKET_SIZES[48] = 0;
        PACKET_SIZES[49] = 0;
        PACKET_SIZES[50] = 10; // was 9
        PACKET_SIZES[51] = 0;
        PACKET_SIZES[52] = 0;
        PACKET_SIZES[53] = -2;
        PACKET_SIZES[54] = 0;
        PACKET_SIZES[55] = 0;
        PACKET_SIZES[56] = 0;
        PACKET_SIZES[57] = 0;
        PACKET_SIZES[58] = -2;
        PACKET_SIZES[59] = 0;
        PACKET_SIZES[60] = -2;
        PACKET_SIZES[61] = 1;
        PACKET_SIZES[62] = 0;
        PACKET_SIZES[63] = 0;
        PACKET_SIZES[64] = 2;
        PACKET_SIZES[65] = -2;
        PACKET_SIZES[66] = 0;
        PACKET_SIZES[67] = 0;
        PACKET_SIZES[68] = 0;
        PACKET_SIZES[69] = 0;
        PACKET_SIZES[70] = 6;
        PACKET_SIZES[71] = 3;
        PACKET_SIZES[72] = 2;
        PACKET_SIZES[73] = 4;
        PACKET_SIZES[74] = 2;
        PACKET_SIZES[75] = 4;
        PACKET_SIZES[76] = 0;
        PACKET_SIZES[77] = 0;
        PACKET_SIZES[78] = 0;
        PACKET_SIZES[79] = 4;
        PACKET_SIZES[80] = 0;
        PACKET_SIZES[81] = -2;
        PACKET_SIZES[82] = 0;
        PACKET_SIZES[83] = 0;
        PACKET_SIZES[84] = 7;
        PACKET_SIZES[85] = 2;
        PACKET_SIZES[86] = 0;
        PACKET_SIZES[87] = 6;
        PACKET_SIZES[88] = 0;
        PACKET_SIZES[89] = 0;
        PACKET_SIZES[SUMMONING_OPTIONS] = -2;
        PACKET_SIZES[91] = 0;
        PACKET_SIZES[92] = 0;
        PACKET_SIZES[93] = 0;
        PACKET_SIZES[94] = 0;
        PACKET_SIZES[95] = 0;
        PACKET_SIZES[96] = 0;
        PACKET_SIZES[97] = 4;
        PACKET_SIZES[98] = 0;
        PACKET_SIZES[99] = 1;
        PACKET_SIZES[100] = 0;
        PACKET_SIZES[101] = 2;
        PACKET_SIZES[102] = 0;
        PACKET_SIZES[103] = 0;
        PACKET_SIZES[104] = -1;
        PACKET_SIZES[105] = 4;
        PACKET_SIZES[106] = 1;
        PACKET_SIZES[107] = 0;
        PACKET_SIZES[108] = 0;
        PACKET_SIZES[109] = 0;
        PACKET_SIZES[110] = 1;
        PACKET_SIZES[111] = 0;
        PACKET_SIZES[112] = -2;
        PACKET_SIZES[113] = 0;
        PACKET_SIZES[114] = 2;
        PACKET_SIZES[115] = 0;
        PACKET_SIZES[116] = 0;
        PACKET_SIZES[117] = 15;
        PACKET_SIZES[118] = 0;
        PACKET_SIZES[119] = 0;
        PACKET_SIZES[120] = 0;
        PACKET_SIZES[121] = 4;
        PACKET_SIZES[122] = 4;
        PACKET_SIZES[123] = 6;
        PACKET_SIZES[124] = 0;
        PACKET_SIZES[125] = 0;
        PACKET_SIZES[126] = -2;
        PACKET_SIZES[127] = 4;
        PACKET_SIZES[128] = 0;
        PACKET_SIZES[129] = 0;
        PACKET_SIZES[130] = 0;
        PACKET_SIZES[131] = 0;
        PACKET_SIZES[132] = 0;
        PACKET_SIZES[133] = 0;
        PACKET_SIZES[134] = 6;
        PACKET_SIZES[135] = -1;
        PACKET_SIZES[136] = 4;
        PACKET_SIZES[137] = 0;
        PACKET_SIZES[138] = 0;
        PACKET_SIZES[139] = 0;
        PACKET_SIZES[140] = 0;
        PACKET_SIZES[141] = 0;
        PACKET_SIZES[142] = 2;
        PACKET_SIZES[143] = 0;
        PACKET_SIZES[144] = 10;
        PACKET_SIZES[145] = 0;
        PACKET_SIZES[146] = 0;
        PACKET_SIZES[147] = 14;
        PACKET_SIZES[148] = 0;
        PACKET_SIZES[149] = 0;
        PACKET_SIZES[150] = 0;
        PACKET_SIZES[151] = 4;
        PACKET_SIZES[152] = 0;
        PACKET_SIZES[153] = 0;
        PACKET_SIZES[154] = 0;
        PACKET_SIZES[155] = 0;
        PACKET_SIZES[156] = 3;
        PACKET_SIZES[157] = 0;
        PACKET_SIZES[158] = 0;
        PACKET_SIZES[159] = 0;
        PACKET_SIZES[160] = 4;
        PACKET_SIZES[161] = 0;
        PACKET_SIZES[162] = 0;
        PACKET_SIZES[163] = 0;
        PACKET_SIZES[164] = 2;
        PACKET_SIZES[165] = 0;
        PACKET_SIZES[166] = 6;
        PACKET_SIZES[167] = 0;
        PACKET_SIZES[168] = 0;
        PACKET_SIZES[169] = 0;
        PACKET_SIZES[170] = 0;
        PACKET_SIZES[171] = 3;
        PACKET_SIZES[172] = 0;
        PACKET_SIZES[173] = 0;
        PACKET_SIZES[174] = 7;
        PACKET_SIZES[175] = 0;
        PACKET_SIZES[176] = 10;
        PACKET_SIZES[177] = 6;
        PACKET_SIZES[178] = 0;
        PACKET_SIZES[179] = 0;
        PACKET_SIZES[180] = 0;
        PACKET_SIZES[181] = 0;
        PACKET_SIZES[182] = 0;
        PACKET_SIZES[183] = 0;
        PACKET_SIZES[184] = 0;
        PACKET_SIZES[185] = 2;
        PACKET_SIZES[186] = 0;
        PACKET_SIZES[187] = 0;
        PACKET_SIZES[188] = 0;
        PACKET_SIZES[189] = 0;
        PACKET_SIZES[190] = 0;
        PACKET_SIZES[191] = 0;
        PACKET_SIZES[192] = -2;
        PACKET_SIZES[193] = 0;
        PACKET_SIZES[194] = 0;
        PACKET_SIZES[195] = 0;
        PACKET_SIZES[196] = -1;
        PACKET_SIZES[197] = 0;
        PACKET_SIZES[198] = 0;
        PACKET_SIZES[199] = 0;
        PACKET_SIZES[200] = 4;
        PACKET_SIZES[201] = 0;
        PACKET_SIZES[202] = 0;
        PACKET_SIZES[203] = 0;
        PACKET_SIZES[204] = 0;
        PACKET_SIZES[205] = 0;
        PACKET_SIZES[206] = 3;
        PACKET_SIZES[207] = 0;
        PACKET_SIZES[208] = 2;
        PACKET_SIZES[209] = 0;
        PACKET_SIZES[210] = 0;
        PACKET_SIZES[211] = 0;
        PACKET_SIZES[212] = 0;
        PACKET_SIZES[213] = 0;
        PACKET_SIZES[214] = -2;
        PACKET_SIZES[215] = 7;
        PACKET_SIZES[216] = 0;
        PACKET_SIZES[217] = -1;
        PACKET_SIZES[218] = 2;
        PACKET_SIZES[219] = 0;
        PACKET_SIZES[220] = 0;
        PACKET_SIZES[221] = 1;
        PACKET_SIZES[222] = 0;
        PACKET_SIZES[223] = 0;
        PACKET_SIZES[224] = -2;
        PACKET_SIZES[225] = 0;
        PACKET_SIZES[226] = 0;
        PACKET_SIZES[227] = 0;
        PACKET_SIZES[228] = 0;
        PACKET_SIZES[229] = 0;
        PACKET_SIZES[230] = 8;
        PACKET_SIZES[231] = 0;
        PACKET_SIZES[232] = 0;
        PACKET_SIZES[233] = 0;
        PACKET_SIZES[234] = 0;
        PACKET_SIZES[235] = 0;
        PACKET_SIZES[236] = 0;
        PACKET_SIZES[237] = -1;
        PACKET_SIZES[238] = 1;
        PACKET_SIZES[239] = 0;
        PACKET_SIZES[240] = 2;
        PACKET_SIZES[241] = -2;
        PACKET_SIZES[242] = 0;
        PACKET_SIZES[243] = 0;
        PACKET_SIZES[244] = 0;
        PACKET_SIZES[245] = 0;
        PACKET_SIZES[246] = 6;
        PACKET_SIZES[247] = 0;
        PACKET_SIZES[248] = 4;
        PACKET_SIZES[249] = 3;
        PACKET_SIZES[250] = 0;
        PACKET_SIZES[251] = 0;
        PACKET_SIZES[252] = 0;
        PACKET_SIZES[253] = -1;
        PACKET_SIZES[254] = 6;
        PACKET_SIZES[255] = 0;
        PACKET_SIZES[256] = 0;
    }
}
