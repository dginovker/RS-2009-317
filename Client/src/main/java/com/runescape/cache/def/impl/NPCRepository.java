package com.runescape.cache.def.impl;

import com.runescape.cache.def.npc.NPCDefinition;
import com.runescape.cache.def.object.ObjectDefinition;

/**
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class NPCRepository {

    public static NPCDefinition forId(int npcId, NPCDefinition npcDefinition) {
        NPCDefinition npcDefCopy = null;
        if (npcDefinition.hasAction("Use") && npcDefinition.hasAction("Use-quickly")) {
            npcDefinition.actions[0] = "Use-quickly";
            npcDefinition.actions[1] = "Exchange";
            npcDefinition.actions[2] = "History";
            npcDefinition.actions[3] = "Sets";
        }
        switch (npcId) {
            case 8604:
                npcDefinition.name = "Zulrah";
                npcDefinition.actions = new String[]{null, "Attack", null, null, null};
                npcDefinition.modelIds = new int[]{45731};
                npcDefinition.standAnimation = 21217;
                npcDefinition.walkAnimationId = 21217;
                npcDefinition.combatLevel = 725;
                npcDefinition.width = 100;
                npcDefinition.size = 5;
                break;

            case 8605:
                npcDefinition.name = "Zulrah";
                npcDefinition.actions = new String[]{null, "Attack", null, null, null};
                npcDefinition.modelIds = new int[]{45732};
                npcDefinition.standAnimation = 21217;
                npcDefinition.walkAnimationId = 21217;
                npcDefinition.combatLevel = 725;
                npcDefinition.width = 100;
                npcDefinition.size = 5;
                break;

            case 8606:
                npcDefinition.name = "Zulrah";
                npcDefinition.actions = new String[]{null, "Attack", null, null, null};
                npcDefinition.modelIds = new int[]{45733};
                npcDefinition.standAnimation = 21217;
                npcDefinition.walkAnimationId = 21217;
                npcDefinition.combatLevel = 725;
                npcDefinition.width = 100;
                npcDefinition.size = 5;
                break;

            case 5053:
                npcDefinition.actions = new String[]{"Talk-to", null, null, null, null};
                break;

            case 409:
                npcDefinition.actions = new String[]{"Talk-to", null, null, null, null};
                break;
            case 1526:
                npcDefinition.actions = new String[]{"Talk-to", null, "Trade", "Perks", "Shop"};
                break;
            case 70:
            case 1596:
            case 1597:
            case 1598:
            case 1599:
                npcDefinition.actions = new String[]{"Talk-to", null, null, "Trade", "Rewards"};
                break;

            case 1037:
                npcDefinition.modelIds = new int[]{8238, 2909, 2922};
                break;

            case 2323:
                npcDefinition.modelIds = new int[]{210, 252, 292, 326, 170, 176, 496, 274, 7121};
                break;

            case 2324:
                npcDefinition.modelIds = new int[]{378, 471, 4392, 348, 353, 7368, 421, 7366};
                break;

            case 2325:
                npcDefinition.modelIds = new int[]{4843, 235, 253, 315, 173, 176, 496, 274, 7121};
                break;

            case 2326:
                npcDefinition.modelIds = new int[]{385, 470, 483, 348, 353, 7368, 421, 361, 7366};
                break;

            case 2327:
                npcDefinition.modelIds = new int[]{6848, 250, 292, 170, 176, 7368, 265, 7121};
                break;

            case 2330:
                npcDefinition.modelIds = new int[]{193, 215, 247, 292, 3667, 151, 176, 496, 5218};
                break;

            case 2331:
                npcDefinition.modelIds = new int[]{370, 390, 456, 332, 353, 7368, 433, 7366};
                break;

            case 2332:
                npcDefinition.modelIds = new int[]{377, 390, 456, 343, 353, 7368, 428, 361};
                break;

            case 2333:
                npcDefinition.modelIds = new int[]{4843, 217, 248, 292, 151, 176, 496, 254, 4390, 185};
                break;

            case 2334:
                npcDefinition.modelIds = new int[]{217, 248, 292, 8233, 170, 176, 7368, 265, 185};
                break;

            case 2335:
                npcDefinition.modelIds = new int[]{206, 253, 292, 326, 151, 176, 496, 254, 7121, 276};
                break;

            case 2336:
                npcDefinition.modelIds = new int[]{390, 456, 332, 353, 7368, 433, 7366};
                break;

            case 2338:
                npcDefinition.modelIds = new int[]{223, 248, 299, 163, 176, 7368, 260, 7121};
                break;

            case 2339:
                npcDefinition.modelIds = new int[]{206, 250, 292, 170, 176, 496, 260, 7121};
                break;

            case 2340:
                npcDefinition.modelIds = new int[]{390, 456, 480, 332, 353, 7368, 433, 7366};
                break;

            case 2341:
                npcDefinition.modelIds = new int[]{25671, 25686};
                break;

            case 2342:
                npcDefinition.modelIds = new int[]{393, 470, 480, 332, 353, 7368, 433, 7366};
                break;

            case 2343:
                npcDefinition.modelIds = new int[]{8238, 2909, 2922};
                break;

            case 2344:
                npcDefinition.modelIds = new int[]{390, 470, 482, 332, 353, 7368, 428, 7366};
                break;

            case 2860:
                npcDefinition.modelIds = new int[]{9893};
                break;

            case 4560:
                npcDefinition.modelIds = new int[]{8238, 2909, 2922};
                break;

            case 4562:
                npcDefinition.modelIds = new int[]{8238, 2909, 2922};
                break;

            case 4947:
                npcDefinition.modelIds = new int[]{19109};
                break;

            case 3092:
                npcDefinition.name = "Monty <col=337ABC>the Collector</col>";
                npcDefinition.actions = new String[]{"Talk-to", null, "Open", null, null};
                break;

            case 945:
                npcDefinition.name = "Gielinor Guide";
                break;

            case 3050:
                npcDefinition.name = "Supplies salesman";
                npcDefinition.actions = new String[]{null, "Trade", null, null, null};
                break;

            case 5880:
                npcDefinition.name = "Patrick Bateman";
                npcDefinition.actions = new String[]{"Talk-to", "Trade", null, null, null};
                break;

            case 2824:
                npcDefinition.name = "Crafting salesman";
                npcDefinition.actions = new String[]{null, "Trade", "Tan-hides", null, null};
                break;

            case 4362:
                npcDefinition.actions = new String[]{"Talk-to", "Trade-points", null, null, null};
                break;

            case 5494:
            case 4555:
                npcDefinition.actions = new String[]{null, "Trade", null, null, null};
                break;

            case 5197:
                npcDefinition.name = "Magic Mike";
                npcDefinition.actions = new String[]{null, "Trade", null, null, null};
                break;

            case 5517:
                npcDefinition.actions = new String[]{null, "Attack", null, null, null};
                break;

            case 6203:
                npcDefinition.modelIds = new int[]{27768, 27773, 27764, 27765, 27770};
                npcDefinition.name = "K'ril Tsutsaroth";
                npcDefinition.size = 5;
                npcDefinition.standAnimation = 6943;
                npcDefinition.walkAnimationId = 6942;
                npcDefinition.actions = new String[]{null, "Attack", null, null, null};
                npcDefinition.height = npcDefinition.width = 110;
                break;

            case 6140:
                npcDefinition.name = "Squire";
                break;

            case 8591:
                npcDefCopy = NPCDefinition.forId(6260);
                npcDefinition.npcId = npcId;
                npcDefinition.name = "General Graardor Jr.";
                npcDefinition.actions = new String[]{"Pick-up", "Talk-to", null, null, null};
                npcDefinition.modelIds = npcDefCopy.modelIds;
                npcDefinition.headModelIds = new int[]{45511};
                npcDefinition.degreesToTurn = npcDefCopy.degreesToTurn;
                npcDefinition.walkAnimationId = npcDefCopy.walkAnimationId;
                npcDefinition.standAnimation = npcDefCopy.standAnimation;
                npcDefinition.turn180Animation = npcDefCopy.turn180Animation;
                npcDefinition.turn90CCWAnimation = npcDefCopy.turn90CCWAnimation;
                npcDefinition.turn90CWAnimation = npcDefCopy.turn90CWAnimation;
                npcDefinition.width = 40;
                npcDefinition.height = 40;
                npcDefinition.combatLevel = 0;
                npcDefinition.drawMinimapDot = false;
                break;

            case 8592:
                npcDefCopy = NPCDefinition.forId(6203);
                npcDefinition.npcId = npcId;
                npcDefinition.name = "K'ril Tsutsaroth Jr.";
                npcDefinition.actions = new String[]{"Pick-up", "Talk-to", null, null, null};
                npcDefinition.modelIds = npcDefCopy.modelIds;
                npcDefinition.headModelIds = new int[]{45512};
                npcDefinition.degreesToTurn = npcDefCopy.degreesToTurn;
                npcDefinition.walkAnimationId = npcDefCopy.walkAnimationId;
                npcDefinition.standAnimation = npcDefCopy.standAnimation;
                npcDefinition.turn180Animation = npcDefCopy.turn180Animation;
                npcDefinition.turn90CCWAnimation = npcDefCopy.turn90CCWAnimation;
                npcDefinition.turn90CWAnimation = npcDefCopy.turn90CWAnimation;
                npcDefinition.width = 20;
                npcDefinition.height = 20;
                npcDefinition.combatLevel = 0;
                npcDefinition.size = 1;
                npcDefinition.drawMinimapDot = false;
                break;

            case 8593:
                npcDefCopy = NPCDefinition.forId(6222);
                npcDefinition.npcId = npcId;
                npcDefinition.name = "Kree'arra Jr.";
                npcDefinition.actions = new String[]{"Pick-up", "Talk-to", null, null, null};
                npcDefinition.modelIds = npcDefCopy.modelIds;
                npcDefinition.headModelIds = new int[]{45513};
                npcDefinition.degreesToTurn = npcDefCopy.degreesToTurn;
                npcDefinition.walkAnimationId = npcDefCopy.walkAnimationId;
                npcDefinition.standAnimation = npcDefCopy.standAnimation;
                npcDefinition.turn180Animation = npcDefCopy.turn180Animation;
                npcDefinition.turn90CCWAnimation = npcDefCopy.turn90CCWAnimation;
                npcDefinition.turn90CWAnimation = npcDefCopy.turn90CWAnimation;
                npcDefinition.width = 40;
                npcDefinition.height = 40;
                npcDefinition.combatLevel = 0;
                npcDefinition.drawMinimapDot = false;
                break;

            case 8594:
                npcDefCopy = NPCDefinition.forId(6247);
                npcDefinition.npcId = npcId;
                npcDefinition.name = "Zilyana Jr.";
                npcDefinition.actions = new String[]{"Pick-up", "Talk-to", null, null, null};
                npcDefinition.modelIds = npcDefCopy.modelIds;
                npcDefinition.headModelIds = new int[]{45514};
                npcDefinition.degreesToTurn = npcDefCopy.degreesToTurn;
                npcDefinition.walkAnimationId = npcDefCopy.walkAnimationId;
                npcDefinition.standAnimation = npcDefCopy.standAnimation;
                npcDefinition.turn180Animation = npcDefCopy.turn180Animation;
                npcDefinition.turn90CCWAnimation = npcDefCopy.turn90CCWAnimation;
                npcDefinition.turn90CWAnimation = npcDefCopy.turn90CWAnimation;
                npcDefinition.width = 70;
                npcDefinition.height = 70;
                npcDefinition.combatLevel = 0;
                npcDefinition.drawMinimapDot = false;
                break;

            case 8595:
                ObjectDefinition objectDefinition = ObjectDefinition.forId(823);
                npcDefinition.npcId = npcId;
                npcDefinition.name = "<col=00FFFF>Max hit dummy</col>";
                npcDefinition.actions = new String[]{"Hit", null, null, null, null};
                npcDefinition.modelIds = objectDefinition.modelIds;
                npcDefinition.drawMinimapDot = false;
                npcDefinition.standAnimation = -1;
                break;

            case 8596:
                npcDefinition.name = "Adam";
                npcDefinition.actions = new String[]{"Talk-to", "Armour", null, null, null};
                npcDefinition.modelIds = new int[]{235, 246, 45515, 45491, 13307, 45517, 4954};
                npcDefinition.recolourTarget = new short[]{6798, 24, 61, 41, 57, 10394};
                npcDefinition.recolourOriginal = new short[]{6067, 61, 33, 24, 33, 5651};
                npcDefinition.standAnimation = 808;
                npcDefinition.walkAnimationId = 819;
                npcDefinition.combatLevel = 0;
                break;

            case 8597:
                npcDefinition.name = "Paul";
                npcDefinition.actions = new String[]{"Talk-to", "Armour", null, null, null};
                npcDefinition.modelIds = new int[]{215, 246, 45518, 45519, 13307, 45520, 4954};
                npcDefinition.recolourTarget = new short[]{6798, 24, 61, 41, 57, 10394};
                npcDefinition.recolourOriginal = new short[]{7056, 61, 33, 24, 33, 5652};
                npcDefinition.standAnimation = 808;
                npcDefinition.walkAnimationId = 819;
                npcDefinition.combatLevel = 0;
                break;

            case 8599:
                npcDefinition.name = "Mac";
                npcDefinition.modelIds = new int[]{34490, 34494, 34495, 34491, 34496, 176, 34492, 34493};
                npcDefinition.headModelIds = new int[]{45528};
                npcDefinition.degreesToTurn = 32;
                npcDefinition.width = 128;
                npcDefinition.height = 128;
                npcDefinition.turn180Animation = 820;
                npcDefinition.turn90CWAnimation = 821;
                npcDefinition.turn90CCWAnimation = 822;
                npcDefinition.standAnimation = 808;
                npcDefinition.walkAnimationId = 819;
                npcDefinition.priorityRender = true;
                npcDefinition.actions = new String[]{"Talk-to", null, null, null, null};
                npcDefinition.combatLevel = 126;
                break;

            case 8600:
                npcDefinition.name = "TzRek-Jad";
                npcDefinition.modelIds = new int[]{45651};
                npcDefinition.headModelIds = new int[]{45652};
                npcDefinition.degreesToTurn = 32;
                npcDefinition.width = 128;
                npcDefinition.height = 128;
                npcDefinition.standAnimation = 9216;
                npcDefinition.walkAnimationId = 9219;
                npcDefinition.priorityRender = false;
                npcDefinition.actions = new String[]{"Interact", null, null, null, null};
                npcDefinition.combatLevel = 0;
                npcDefinition.drawMinimapDot = false;
                break;

            case 8601:
                npcDefinition.name = "TzRek-Jad";
                npcDefinition.modelIds = new int[]{45651};
                npcDefinition.headModelIds = new int[]{45652};
                npcDefinition.degreesToTurn = 32;
                npcDefinition.width = 128;
                npcDefinition.height = 128;
                npcDefinition.standAnimation = 9216;
                npcDefinition.walkAnimationId = 9219;
                npcDefinition.priorityRender = false;
                npcDefinition.actions = new String[]{"Pick-up", null, "Talk-to", null, null};
                npcDefinition.combatLevel = 0;
                npcDefinition.drawMinimapDot = true;
                break;

            case 8602:
                npcDefinition.name = "Grace";
                npcDefinition.modelIds = new int[]{45679, 45680, 45681, 45682, 45683, 45684, 45685};
                npcDefinition.headModelIds = new int[]{45686, 120};
                npcDefinition.turn180Animation = 820;
                npcDefinition.turn90CWAnimation = 821;
                npcDefinition.turn90CCWAnimation = 822;
                npcDefinition.standAnimation = 808;
                npcDefinition.walkAnimationId = 819;
                npcDefinition.priorityRender = false;
                npcDefinition.actions = new String[]{"Talk-to", null, "Trade", null, null};
                break;

            case 8603:
                npcDefinition.name = "Mark";
                npcDefinition.modelIds = new int[]{7755};
                npcDefinition.standAnimation = 2268;
                npcDefinition.priorityRender = false;
                npcDefinition.actions = new String[]{null, null, null, null, null};
                break;
        }
        return npcDefinition;
    }
}
