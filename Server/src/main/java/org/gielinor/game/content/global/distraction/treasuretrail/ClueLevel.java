package org.gielinor.game.content.global.distraction.treasuretrail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.gielinor.game.node.entity.npc.drop.DropFrequency;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.ChanceItem;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * A clue scroll level.
 *
 * @author Vexia
 * @author Torchic
 */
public enum ClueLevel {

    EASY(new Item(2714), 1 << 16 | 5,
        new ChanceItem(true, 2633, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2635, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2637, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2587, 1, 1, DropFrequency.UNCOMMON),//black t
        new ChanceItem(true, 2583, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2585, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3472, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2589, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2595, 1, 1, DropFrequency.UNCOMMON),//black g
        new ChanceItem(true, 2591, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2593, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3473, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2597, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2631, 1, 1, DropFrequency.UNCOMMON),//highway man mask
        new ChanceItem(true, 7392, 1, 1, DropFrequency.UNCOMMON),//wizard t
        new ChanceItem(true, 7396, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7388, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7390, 1, 1, DropFrequency.UNCOMMON),//wizard g
        new ChanceItem(true, 7386, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7394, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7362, 1, 1, DropFrequency.UNCOMMON),//studded g
        new ChanceItem(true, 7366, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7364, 1, 1, DropFrequency.UNCOMMON),//studded t
        new ChanceItem(true, 7368, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10404, 1, 1, DropFrequency.UNCOMMON),//red ele shirt
        new ChanceItem(true, 10406, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10424, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10426, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10408, 1, 1, DropFrequency.UNCOMMON),//blue ele shirt
        new ChanceItem(true, 10410, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10428, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10430, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10412, 1, 1, DropFrequency.UNCOMMON),//green ele shirt
        new ChanceItem(true, 10414, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10432, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10434, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10316, 1, 1, DropFrequency.RARE),//bob the cat
        new ChanceItem(true, 10318, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 10320, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 10322, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 10324, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 10392, 1, 1, DropFrequency.RARE),//emote enhancers
        new ChanceItem(true, 10394, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 10396, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 10398, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 10366, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10458, 1, 1, DropFrequency.UNCOMMON),//vestement robes(sara, guthix, zammy)
        new ChanceItem(true, 10464, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10462, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10466, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10460, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10468, 1, 1, DropFrequency.UNCOMMON),
        //new ChanceItem(true, 13095, 1, 1, DropFrequency.UNCOMMON),//black cane
        //new ChanceItem(true, 13105, 1, 1, DropFrequency.UNCOMMON),//spikey helmet
        new ChanceItem(true, 1077, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1089, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1107, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1125, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1151, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1165, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1179, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1195, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1217, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1283, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1297, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1313, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1327, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1341, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1361, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1367, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1426, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 3098, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 4821, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 8779, 4, 38, DropFrequency.COMMON),
        new ChanceItem(true, 850, 1, 4, DropFrequency.COMMON),
        new ChanceItem(true, 334, 4, 19, DropFrequency.COMMON),
        new ChanceItem(true, 1169, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1059, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1061, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1063, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1095, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1129, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1167, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1131, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 858, 1, 4, DropFrequency.COMMON),
        new ChanceItem(true, 330, 3, 23, DropFrequency.COMMON),
        new ChanceItem(true, 1441, 1, 3, DropFrequency.COMMON),
        new ChanceItem(true, 1443, 1, 3, DropFrequency.COMMON),
        new ChanceItem(true, 1270, 1, 3, DropFrequency.COMMON),
        new ChanceItem(true, 1097, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1133, 1, 1, DropFrequency.COMMON)
    ),
    MEDIUM(new Item(2717), 1 << 16 | 6,
        new ChanceItem(true, 2605, 1, 1, DropFrequency.UNCOMMON),//Trimmed addy
        new ChanceItem(true, 2599, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2601, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2603, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3474, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2613, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2607, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2609, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3475, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2611, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2577, 1, 1, DropFrequency.VERY_RARE),//Boots
        new ChanceItem(true, 2579, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2647, 1, 1, DropFrequency.UNCOMMON),    //Headbands
        new ChanceItem(true, 2645, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2649, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2605, 1, 1, DropFrequency.UNCOMMON),//addy trimmed
        new ChanceItem(true, 2599, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2601, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2603, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3474, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2613, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2607, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2609, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3475, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2611, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2577, 1, 1, DropFrequency.VERY_RARE),//Boots
        new ChanceItem(true, 2579, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2647, 1, 1, DropFrequency.UNCOMMON),//Headbands
        new ChanceItem(true, 2645, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2649, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7319, 1, 1, DropFrequency.UNCOMMON),//Boaters
        new ChanceItem(true, 7321, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7323, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7325, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7327, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7372, 1, 1, DropFrequency.UNCOMMON),//Trimmed Dragonhide
        new ChanceItem(true, 7380, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7370, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7378, 1, 1, DropFrequency.UNCOMMON),
        //new ChanceItem(true, 13103, 1, 1, DropFrequency.UNCOMMON),
        //new ChanceItem(true, 13097, 1, 1, DropFrequency.UNCOMMON),
        //new ChanceItem(true, 13109, 1, 1, DropFrequency.UNCOMMON),//animal masks
        //new ChanceItem(true, 13107, 1, 1, DropFrequency.UNCOMMON),
        //new ChanceItem(true, 13111, 1, 1, DropFrequency.UNCOMMON),
        //new ChanceItem(true, 13113, 1, 1, DropFrequency.UNCOMMON),
        //new ChanceItem(true, 13115, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10364, 1, 1, DropFrequency.UNCOMMON),//ammy of strength (t)
        new ChanceItem(true, 10420, 1, 1, DropFrequency.UNCOMMON),//white ele
        new ChanceItem(true, 10422, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10400, 1, 1, DropFrequency.RARE),//black ele
        new ChanceItem(true, 10402, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 10416, 1, 1, DropFrequency.UNCOMMON),//purple ele
        new ChanceItem(true, 10418, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10436, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10438, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10296, 1, 1, DropFrequency.UNCOMMON),//addy heraldic helms
        new ChanceItem(true, 10298, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10300, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10302, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10304, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10666, 1, 1, DropFrequency.UNCOMMON),//addy heraldic shield
        new ChanceItem(true, 10669, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10672, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10675, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10678, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10446, 1, 1, DropFrequency.UNCOMMON),//cloaks
        new ChanceItem(true, 10448, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10450, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 1161, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1123, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1073, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1091, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1199, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1183, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1111, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1211, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1271, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1287, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1301, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1317, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1357, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1371, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1430, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 4823, 10, 40, DropFrequency.COMMON),
        new ChanceItem(true, 9183, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1393, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1099, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1135, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 857, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 8781, 10, 43, DropFrequency.COMMON),
        new ChanceItem(true, 374, 6, 23, DropFrequency.COMMON),
        new ChanceItem(true, 380, 15, 43, DropFrequency.COMMON)
    ),
    HARD(new Item(2720), 1 << 16 | 8,
        //new ChanceItem(true, 13101, 1, 1, DropFrequency.UNCOMMON),
        //new ChanceItem(true, 13099, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10470, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10472, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10474, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10440, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10442, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10444, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10362, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2628, 1, 1, DropFrequency.UNCOMMON),//Trimmed rune
        new ChanceItem(true, 2623, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2625, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2629, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2619, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3477, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2615, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2617, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2628, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2628, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3476, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2621, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2673, 1, 1, DropFrequency.UNCOMMON),// God armour
        new ChanceItem(true, 2669, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2671, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3480, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2675, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2657, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2653, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2655, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2659, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2665, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2661, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3478, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2663, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2667, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3479, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10286, 1, 1, DropFrequency.UNCOMMON),// Heraldic
        new ChanceItem(true, 10667, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10288, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10670, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10290, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10673, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10292, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10676, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10294, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10679, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7376, 1, 1, DropFrequency.UNCOMMON),// Blue dragonhide trimmed
        new ChanceItem(true, 7384, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7374, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7382, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10368, 1, 1, DropFrequency.UNCOMMON),// God dhide
        new ChanceItem(true, 10370, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10372, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10376, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10378, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10380, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10384, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10386, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10388, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2651, 1, 1, DropFrequency.RARE),//Hats
        new ChanceItem(true, 2581, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 2639, 1, 1, DropFrequency.UNCOMMON),//Cavaliers
        new ChanceItem(true, 2641, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 2643, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 1163, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1127, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1079, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1093, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1201, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1185, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1213, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1275, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1289, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1303, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1319, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1359, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1373, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1432, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 4824, 10, 43, DropFrequency.COMMON),
        new ChanceItem(true, 9144, 6, 23, DropFrequency.COMMON),
        new ChanceItem(true, 9185, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 1747, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 2503, 1, 1, DropFrequency.COMMON),
        new ChanceItem(true, 861, 1, 2, DropFrequency.COMMON),
        new ChanceItem(true, 859, 1, 2, DropFrequency.COMMON),
        new ChanceItem(true, 8783, 6, 27, DropFrequency.COMMON),
        new ChanceItem(true, 380, 16, 38, DropFrequency.COMMON),
        new ChanceItem(true, 386, 7, 32, DropFrequency.COMMON)
    ),;

    /**
     * The default rewards.
     */
    private static final ChanceItem[] DEFAULT_REWARDS = new ChanceItem[]{
        new ChanceItem(true, Item.COINS, 500, 7000, DropFrequency.COMMON),
        new ChanceItem(true, 10326, 15, 40, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7329, 15, 40, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7331, 15, 40, DropFrequency.UNCOMMON),
        new ChanceItem(true, 7330, 15, 40, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10327, 15, 40, DropFrequency.UNCOMMON),
        new ChanceItem(true, 10476, 1, 27, DropFrequency.UNCOMMON),
        new ChanceItem(true, 556, 1, 228, DropFrequency.COMMON),
        new ChanceItem(true, 554, 1, 228, DropFrequency.COMMON),
        new ChanceItem(true, 557, 1, 228, DropFrequency.COMMON),
        new ChanceItem(true, 557, 1, 228, DropFrequency.COMMON),
        new ChanceItem(true, 1694, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 1696, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 1698, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 1700, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 1702, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 847, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 855, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 859, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(true, 3827, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 3828, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 3829, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 3830, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 3831, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 3832, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 3833, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 3834, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 3835, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 3836, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 3837, 1, 1, DropFrequency.RARE),
        new ChanceItem(true, 3838, 1, 1, DropFrequency.RARE)
    };

    /**
     * The super rare items.
     */
    public static final ChanceItem[] SUPER_RARE = new ChanceItem[]{
        new ChanceItem(true, 3486, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 3481, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 3483, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 3485, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 3488, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10330, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10332, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10334, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10336, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10338, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10340, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10342, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10344, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10346, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10348, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10350, 1, 1, DropFrequency.VERY_RARE),
        new ChanceItem(true, 10352, 1, 1, DropFrequency.VERY_RARE)
    };

    /**
     * The casket.
     */
    private final Item casket;

    /**
     * The length hash of the level.
     */
    private final int lengthHash;

    /**
     * The reward items.
     */
    private final ChanceItem[] rewards;

    /**
     * Constructs a new {@code ClueLevel} {@code Object}
     *
     * @param casket     the casket.
     * @param lengthHash the hash.
     * @param rewards    the rewards.
     */
    private ClueLevel(Item casket, int lengthHash, ChanceItem... rewards) {
        this.casket = casket;
        this.lengthHash = lengthHash;
//        List<ChanceItem> chanceItems = new ArrayList<>();
//        for (ChanceItem chanceItem : rewards) {
//            if (chanceItem.getDropFrequency() == DropFrequency.COMMON) {
//                continue;
//            }
//            chanceItems.add(chanceItem);
//        }
        this.rewards = rewards;//chanceItems.toArray(new ChanceItem[chanceItems.size()]);
    }

    /**
     * Checks if a reward is super rare.
     *
     * @param item The reward item.
     * @return <code>True</code> if so.
     */
    public static boolean isSuperRare(Item item) {
        for (ChanceItem chanceItem : SUPER_RARE) {
            if (item.getId() == chanceItem.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Opens a cakset for the player.
     *
     * @param player the player.
     * @param casket the casket.
     *               TODO Correct colors
     */
    public void open(Player player, Item casket) {
        if (player.specialDetails() || player.getRights().isAdministrator()) {
            String type = "easy";
            String color = "559040";
            int amount = 1;
            switch (this) {
                case EASY:
                    amount = player.getSavedData().getActivityData().increaseEasyClueScrolls();
                    break;
                case MEDIUM:
                    amount = player.getSavedData().getActivityData().increaseMediumClueScrolls();
                    type = "medium";
                    color = "8F7C38";
                    break;
                case HARD:
                    amount = player.getSavedData().getActivityData().increaseHardClueScrolls();
                    type = "hard";
                    color = "712E1E";
                    break;
            }
            final List<Item> rewards = getLoot(player);
            long rewardValue = getValue(rewards);
            for (Item item : rewards) {
                player.getInventory().add(item, player);
                if (this == HARD && isSuperRare(item)) {
                    player.sendGlobalNewsMessage("ff8c38", " was rewarded " + item.getName() + " from a clue scroll!", 5);
                }
            }
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 6963, rewards.toArray(new Item[rewards.size()]), false));
            player.getInterfaceState().openComponent(6960);
            if (casket != null) {
                player.getInventory().remove(casket);
            }
            player.getTreasureTrailManager().clearTrail();
            player.getActionSender().sendMessage("Well done, you've completed the Treasure Trail!");
            // TODO Correct color
            player.getActionSender().sendMessage("<col=" + color + ">You have completed " + amount + " " + type + " Treasure Trails.");
            String approxAmount = "";
            if (rewardValue >= 1000 && rewardValue < 1000000) {
                approxAmount = (rewardValue / 1000) + "K";
            } else if (rewardValue >= 1000000) {
                approxAmount = (rewardValue / 1000000) + " million.";
            } else {
                approxAmount = String.valueOf(rewardValue);
            }
            player.getActionSender().sendMessage("<col=" + color + ">Your clue is worth approximately " + approxAmount + " gp!");
            return;
        }
        if (!player.getTreasureTrailManager().hasTrail()) {
            player.getInventory().remove(casket);
            return;
        }
        if (player.getTreasureTrailManager().isCompleted()) {
            String type = "easy";
            String color = "559040";
            int amount = 1;
            switch (this) {
                case EASY:
                    amount = player.getSavedData().getActivityData().increaseEasyClueScrolls();
                    break;
                case MEDIUM:
                    amount = player.getSavedData().getActivityData().increaseMediumClueScrolls();
                    type = "medium";
                    color = "8F7C38";
                    break;
                case HARD:
                    amount = player.getSavedData().getActivityData().increaseHardClueScrolls();
                    type = "hard";
                    color = "712E1E";
                    break;
            }
            final List<Item> rewards = getLoot(player);
            long rewardValue = getValue(rewards);
            for (Item item : rewards) {
                player.getInventory().add(item, player);
                if (this == HARD && isSuperRare(item)) {
                    player.sendGlobalNewsMessage("ff8c38", " was rewarded " + item.getName() + " from a clue scroll!", 5);
                }
            }
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 6963, rewards.toArray(new Item[rewards.size()]), false));
            player.getInterfaceState().openComponent(6960);
            if (casket != null) {
                player.getInventory().remove(casket);
            }
            player.getTreasureTrailManager().clearTrail();
            player.getActionSender().sendMessage("Well done, you've completed the Treasure Trail!");
            player.getActionSender().sendMessage("<col=" + color + ">You have completed " + amount + " " + type + " Treasure Trails.");
            String approxAmount = "";
            if (rewardValue >= 1000 && rewardValue < 1000000) {
                approxAmount = (rewardValue / 1000) + "K";
            } else if (rewardValue >= 1000000) {
                approxAmount = (rewardValue / 1000000) + " million.";
            } else {
                approxAmount = String.valueOf(rewardValue);
            }
            player.getActionSender().sendMessage("<col=" + color + ">Your clue is worth approximately " + approxAmount + " gp!");
            return;
        }
        Item clue = ClueScrollPlugin.getClue(this);
        if (clue == null) {
            return;
        }
        if (casket != null) {
            player.getInventory().replace(clue, casket.getSlot());
        } else {
            player.getInventory().add(clue);
        }
        player.getTreasureTrailManager().setClueId(clue.getId());
        player.getDialogueInterpreter().sendDoubleItemMessage(2677, -1, "<br><br>You've found another clue!");
    }

    /**
     * Gets the value of the rewards.
     *
     * @return The value.
     */
    public long getValue(List<Item> rewards) {
        long value = 0;
        for (Item item : rewards) {
            value += item.getValue();
        }
        return value;
    }

    /**
     * Gets the rewards.
     *
     * @return the rewards.
     */
    public List<Item> getLoot(Player player) {
        int chance = (player == null || !player.specialDetails()) ? 4000 : 400;
        List<ChanceItem> items = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        // if (RandomUtil.random(4) < 2) {
        items.addAll(Arrays.asList(rewards));
        // }
        items.addAll(Arrays.asList(DEFAULT_REWARDS));
        List<Item> rewards = new ArrayList<>();
        int size = RandomUtil.random(1, 6);
        if (this == HARD) {
            size = RandomUtil.random(4, 6);
        }
        Item item = null;
        for (int i = 0; i < size; i++) {
            item = getReward(items);
            if (ids.contains(item.getId())) {
                continue;
            }
            ids.add(item.getId());
            rewards.add(item);
        }
        if (this == HARD && RandomUtil.random(chance) == 11) {
            rewards.remove(0);
            rewards.add(RandomUtil.getChanceItem(SUPER_RARE).getRandomItem());
        }
        return rewards;
    }

    /**
     * Gets a reward item.
     *
     * @return the item.
     */
    public Item getReward(List<ChanceItem> items) {
        Collections.shuffle(items);
        return RandomUtil.getChanceItem(items.toArray(new ChanceItem[items.size()])).getRandomItem();
    }

    /**
     * Gets the clue level for the casket.
     *
     * @param node the node.
     * @return the level.
     */
    public static ClueLevel forCasket(Item node) {
        for (ClueLevel level : values()) {
            if (node.getId() == level.getCasket().getId()) {
                return level;
            }
        }
        return null;
    }

    /**
     * Gets the maximum length.
     *
     * @return the length.
     */
    public int getMaximumLength() {
        return lengthHash & 0xFFFF;
    }

    /**
     * Gets the minimum length.
     *
     * @return the length.
     */
    public int getMinimumLength() {
        return lengthHash >> 16 & 0xFFFF;
    }

    /**
     * Gets the brewards.
     *
     * @return the rewards
     */
    public ChanceItem[] getRewards() {
        return rewards;
    }

    /**
     * Gets the bcasket.
     *
     * @return the casket
     */
    public Item getCasket() {
        return casket;
    }

    /**
     * Gets the blengthHash.
     *
     * @return the lengthHash
     */
    public int getLengthHash() {
        return lengthHash;
    }

}
