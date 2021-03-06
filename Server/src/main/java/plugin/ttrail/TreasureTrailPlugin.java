package plugin.ttrail;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueLevel;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueScrollPlugin;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.item.ItemPlugin;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.parser.npc.NPCConfiguration;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the clue scroll options.
 *
 * @author Vexia
 */
public final class TreasureTrailPlugin extends OptionHandler {

    /**
     * The ids of the clues.
     */
    public static final int[] IDS = new int[]{
        2677, 2678, 2679, 2680, 2681, 2682, 2683, 2684, 2685, 2686, 2687,
        2688, 2689, 2690, 2691, 2692, 2693, 2694, 2695, 2696, 2697, 2698, 2699, 2700, 2701, 2702, 2703, 2704, 2705,
        2706, 2707, 2708, 2709, 2710, 2711, 2712, 2713, 2716, 2719, 2722, 2723, 2725, 2727, 2729, 2731, 2733, 2735,
        2737, 2739, 2741, 2743, 2745, 2747, 2773, 2774, 2776, 2778, 2780, 2782, 2783, 2785, 2786, 2788, 2790, 2792,
        2793, 2794, 2796, 2797, 2799, 2801, 2803, 2805,
        /**
         * Coordinates
        */
        2809
    };

    public static final int[] UNUSED_IDS = new int[]{
        2808, 2811, 2813, 2815, 2817, 2819, 2821,
        2823, 2825, 2827, 2829, 2831, 2833, 2835, 2837, 2839,
        2841, 2843, 2845, 2847, 2848, 2849, 2851, 2853, 2855,
        2856, 2857, 2858, 3490, 3491, 3492, 3493, 3494, 3495,
        3496, 3497, 3498, 3499, 3500, 3501, 3502, 3503, 3504,
        3505, 3506, 3507, 3508, 3509, 3510, 3512, 3513, 3514,
        3515, 3516, 3518, 3520, 3522, 3524, 3525, 3526, 3528,
        3530, 3532, 3534, 3536, 3538, 3540, 3542, 3544, 3546,
        3548, 3550, 3552, 3554, 3556, 3558, 3560, 3562, 3564,
        3566, 3568, 3570, 3572, 3573, 3574, 3575, 3577, 3579,
        3580, 3582, 3584, 3586, 3588, 3590, 3592, 3594, 3596,
        3598, 3599, 3601, 3602, 3604, 3605, 3607, 3609, 3610,
        3611, 3612, 3613, 3614, 3615, 3616, 3617, 3618, 7236,
        7238, 7239, 7241, 7243, 7245, 7247, 7248, 7249, 7250,
        7251, 7252, 7253, 7254, 7255, 7256, 7258, 7260, 7262,
        7264, 7266, 7268, 7270, 7272, 7274, 7276, 7278, 7280,
        7282, 7284, 7286, 7288, 7290, 7292, 7294, 7296, 7298,
        7300, 7301, 7303, 7304, 7305, 7307, 7309, 7311, 7313,
        7315, 7317, 10180, 10182, 10184, 10186, 10188, 10190,
        10192, 10194, 10196, 10198, 10200, 10202, 10204, 10206,
        10208, 10210, 10212, 10214, 10216, 10218, 10220, 10222,
        10224, 10226, 10228, 10230, 10232, 10234, 10236, 10238,
        10240, 10242, 10244, 10246, 10248, 10250, 10252, 10254,
        10256, 10258, 10260, 10262, 10264, 10266, 10268, 10270,
        10272, 10274, 10276, 10278
    };

    @Override
    public Plugin<Object> newInstance(Object arg) {
        for (int id : IDS) {
            ItemDefinition.forId(id).getConfigurations().put("option:read", this);
        }
        for (int id : UNUSED_IDS) {
            ItemDefinition.forId(id).getConfigurations().put("option:read", this);
        }
        for (ClueLevel level : ClueLevel.values()) {
            ItemDefinition.forId(level.getCasket().getId()).getConfigurations().put("option:open", this);
        }
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.lock(1);
        switch (option) {
            case "read":
                final ClueScrollPlugin plugin = ClueScrollPlugin.getClueScrolls().get(node.getId());
                if (plugin == null) {
                    player.getActionSender().sendMessage("Invalid clue scroll! Please report this on forums: " + node.getId());
                    player.getInventory().remove((Item) node);
                    if (node.getName() != null && node.getName().toLowerCase().contains("clue scroll")) {
                        player.getActionSender().sendMessage("You have been given another clue scroll.");
                        // TODO
                        if (node.getId() == 2811 || node.getId() == 2727) {
                            player.getInventory().add(ClueScrollPlugin.getClue(ClueLevel.MEDIUM));
                        } else {
                            player.getInventory().add(ClueScrollPlugin.getClue(ClueLevel.EASY));
                        }
                    }
                    return true;
                }
                plugin.read(player);
                break;
            case "open":
                ClueLevel.forCasket((Item) node).open(player, (Item) node);
                break;
        }
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    /**
     * Handles options related to treasure trails.
     *
     * @author Vexia
     */
    public static final class TTrailOptionHandler extends OptionHandler {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ObjectDefinition.forId(19171).getConfigurations().put("option:squeeze-through", this);
            return this;
        }

        @Override
        public boolean handle(Player player, Node node, String option) {
            switch (node.getId()) {
                case 19171:
                    if(player.getAttribute("reverse_gun_game", false)){
                        player.getActionSender().sendMessage("You cannot enter the lobby during a gun game.");
                        return true;
                    }
                    Location start = player.getLocation().getX() <= 2522 ? node.getLocation() : node.getLocation().transform(1, 0, 0);
                    player.lock(1);
                    AgilityHandler.forceWalk(player, -1, start, start.transform(player.getLocation().getX() <= 2522 ? 1 : -1, 0, 0), Animation.create(2240), 5, 1, null, 0);
                    break;
            }
            return true;
        }
    }


    /**
     * Represents a clue scroll item.
     *
     * @author Vexia
     */
    public final class ClueItemPlugin extends ItemPlugin {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            register(IDS);
            return this;
        }

        @Override
        public boolean canPickUp(Player player, GroundItem item, int type) {
            if (hasClue(player)) {
                player.getActionSender().sendMessage("A magical force prevents you from picking the clue scroll up.");
                return false;
            }
            return true;
        }

        @Override
        public boolean createDrop(Item item, Player player, NPC npc, Location l) {
            if (npc.getDefinition().getDropTables().getMainTable().size() < 3 && RandomUtil.random(200) > 6) {
                return false;
            }
            return !hasClue(player);
        }

        @Override
        public Item getItem(Item item, NPC npc) {
            return new Item(ClueScrollPlugin.getClue(npc.getDefinition().getConfiguration(NPCConfiguration.CLUE_LEVEL, ClueLevel.EASY)).getId());
        }

        /**
         * Checks if the player has a clue.
         *
         * @param player the player.
         * @return {@code True} if so.
         */
        private boolean hasClue(Player player) {
            return player.getTreasureTrailManager().hasClue();
        }

    }
}
