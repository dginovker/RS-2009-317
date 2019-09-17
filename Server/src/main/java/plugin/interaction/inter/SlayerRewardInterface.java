package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.content.skill.member.slayer.Tasks;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.util.extensions.CalendarExtensionsKt;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the {@link org.gielinor.game.component.ComponentPlugin} for the Slayer Rewards interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SlayerRewardInterface extends ComponentPlugin {

    /**
     * The enabled color.
     */
    private static String ENABLED = "<col=D7B011>";
    /**
     * The color red.
     */
    private static String RED = "<col=FF0000>";

    private final static int
        LEARN_TAB = 24569,
        ASSIGNMENT_TAB = 24586;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(24546, this);
        ComponentDefinition.put(24569, this);
        ComponentDefinition.put(24586, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int buttonId, int slot, int itemId) {
        switch (component.getId()) {

            case 24546:// purchase tab
                for (RewardType rewardType : RewardType.values()) {
                    if (Arrays.stream(rewardType.getButtons()).anyMatch(id -> id == buttonId)) {
                        final int availableSlots = player.getInventory().freeSlots();
                        final int requiredSlots = Math.toIntExact(Arrays.stream(rewardType.getRewardItems()).mapToInt(Item::getSlotsUsed).count());

                        if(availableSlots >= requiredSlots){
                            if (hasRequiredPoints(player, rewardType.getPointCost(), rewardType.getType()))
                                Arrays.stream(rewardType.getRewardItems()).forEach(player.getInventory()::add);
                        } else
                            player.getActionSender().sendMessage("You need "+(requiredSlots-availableSlots)+" more free inventory slots.");
                        return true;
                    }
                }

                switch (buttonId) {

                    case 24551:// Open learn tab.
                        player.getInterfaceState().open(new Component(LEARN_TAB));
                        configure(player, 2);
                        return true;

                    case 24552:// Open assignment tab.
                        player.getInterfaceState().open(new Component(ASSIGNMENT_TAB));
                        configure(player, 3);
                        return true;

                    case 24553:
                    case 24558:
                    case 24563:// Buy Slayer XP.
                        if (hasRequiredPoints(player, 400, 1)) {
                            player.getActionSender().sendMessage("You have received 50,000 Slayer experience.");
                            player.getSkills().addExperienceNoMod(Skills.SLAYER, 50000);
                        }
                        return true;
                }
                break;

            case 24569:// Learn tab

                switch (buttonId) {

                    case 24575:// Open buy tab.
                        player.getInterfaceState().open(new Component(24546));
                        configure(player, 1);
                        return true;

                    case 24576://  Open assignment tab.
                        player.getInterfaceState().open(new Component(24586));
                        configure(player, 3);
                        return true;

                    case 24577:
                    case 24580:
                    case 24583:// Learn how to fletch broad bolts / arrows.
                        if (player.getSavedData().getActivityData().hasLearnedSlayerOption(0)) {
                            player.getActionSender().sendMessage("You have already unlocked this ability.");
                            return true;
                        }
                        if (hasRequiredPoints(player, 300, 2)) {
                            player.getActionSender().sendMessage("The secret is yours. You can now fletch and make broad bolts and arrows.");
                            player.getSavedData().getActivityData().setLearnedSlayerOption(0);
                        }
                        return true;

                    case 24578:
                    case 24581:
                    case 24584:// Learn how to craft rings of slaying.
                        if (player.getSavedData().getActivityData().hasLearnedSlayerOption(1)) {
                            player.getActionSender().sendMessage("You have already unlocked this ability.");
                            return true;
                        }
                        if (hasRequiredPoints(player, 300, 2)) {
                            player.getActionSender().sendMessage("You have learned how to combine an enchanted gem, a gold bar,");
                            player.getActionSender().sendMessage("and a ring mould into a ring of slaying.");
                            player.getSavedData().getActivityData().setLearnedSlayerOption(1);
                        }
                        return true;
                    case 24579:
                    case 24582:
                    case 24585:// Learn how to craft Slayer helmets.
                        if (player.getSavedData().getActivityData().hasLearnedSlayerOption(2)) {
                            player.getActionSender().sendMessage("You have already unlocked this ability.");
                            return true;
                        }
                        if (hasRequiredPoints(player, 400, 2)) {
                            player.getActionSender().sendMessage("The secret is yours. You can now combine a black mask, face mask, spiny helm,");
                            player.getActionSender().sendMessage("nosepeg and earmuffs into one useful item.");
                            player.getSavedData().getActivityData().setLearnedSlayerOption(2);
                        }
                        return true;
                }
                break;
            case 24586:// Assignment tab.
                switch (buttonId) {

                    case 24592:// Open buy tab.
                        player.getInterfaceState().open(new Component(24546));
                        configure(player, 1);
                        return true;

                    case 24593:// Open learn tab.
                        player.getInterfaceState().open(new Component(24569));
                        configure(player, 2);
                        return true;

                    case 24594:
                    case 24596:// Cancel current task.
                        if (!player.getSlayer().hasTask()) {
                            player.getActionSender().sendMessage("You currently do not have a slayer task!");
                            return true;
                        }
                        if (hasRequiredPoints(player, 20, 3)) {
                            player.getActionSender().sendMessage("You have reset your slayer task.");
                            player.getSlayer().clear();
                            configure(player, 3);
                        }
                        return true;

                    case 24603:
                    case 24604:
                    case 24605:
                    case 24606:
                        final int cancel_removal_index = buttonId - 24603;
                        final String disabledTask = player.getSavedData().getActivityData().getDisabledTasks()[cancel_removal_index];

                        if(!disabledTask.equals("nothing")){
                            player.getActionSender().sendMessage("Removed "+disabledTask+" from canceled tasks.");
                            player.getSavedData().getActivityData().enableSlayerTask(cancel_removal_index);
                            configure(player, 3);
                        }else
                            player.getActionSender().sendMessage("No task is set here.");

                        return true;

                    case 24595:
                    case 24597:// cancel task.
                        if (!player.getSlayer().hasTask() || player.getSlayer().getTasks() == null) {
                            player.getActionSender().sendMessage("You currently do not have a slayer task!");
                            return true;
                        }
                        int index = removeSlayerTask(player);
                        if (index == -1) {
                            player.getActionSender().sendMessage("You cannot remove any more tasks.");
                            return true;
                        }
                        if (index == -2) {
                            player.getActionSender().sendMessage("You already have that task removed!");
                            return true;
                        }
                        if (hasRequiredPoints(player, 70, 3)
                            || player.getDonorManager().getDonorStatus().getCanBlockTask() && CalendarExtensionsKt.compareHours(System.currentTimeMillis(), player.getSavedData().getActivityData().getLastTaskBlock()) >= 24) {
                            player.getActionSender().sendMessage("You will no longer receive " + player.getSlayer().getTaskName() + "s as a task.");
                            player.getSavedData().getActivityData().disableSlayerTask(index, player.getSlayer().getTasks());
                            player.getSlayer().clear();
                            player.getSavedData().getActivityData().setLastTaskBlock(System.currentTimeMillis());
                            configure(player, 3);
                        }
                        return true;
                }
                break;
        }
        return false;
    }

    /**
     * Checks if the player's Slayer points is more than or equal to the amount given.
     *
     * @param points The points.
     * @return <code>True</code> if so.
     */
    public static boolean is(int playerPoints, int points) {
        return playerPoints >= points;
    }

    /**
     * Checks if a task slot is disabled.
     *
     * @param disabled The disabled tasks.
     * @param index    The index.
     * @return <code>True</code> if so.
     */
    public static boolean isDisabled(String[] disabled, int index) {
        return disabled[index] == null || disabled[index].isEmpty() || disabled[index].equalsIgnoreCase("nothing");
    }

    /**
     * Configures the rewards interface.
     *
     * @param player The player.
     */
    public static void configure(Player player, int type) {
        int slayerPoints = player.getSavedData().getActivityData().getSlayerPoints();
        boolean zero = slayerPoints <= 0;
        player.getActionSender().sendString(24568, "Current points = <col=" + (zero ? "FF0000" : "D7B011") + ">" + slayerPoints);
        Object[][] data = null;
        switch (type) {
            /**
             * The buy tab.
             */
            case 1:
                data = new Object[][]{
                    { "Slayer XP", 400, 24558, 24563 },
                    { "ring of slaying", 75, 24559, 24564 },
                    { "runes for Slayer Dart", 35, 24560, 24565 },
                    { "broad bolts", 35, 24561, 24566 },
                    { "broad arrows", 35, 24562, 24567 },
                };
                for (Object[] info : data) {
                    player.getActionSender().sendString("<col=" + (!is(slayerPoints, (int) info[1]) ? "FF0000" : "D7B011") + ">Buy "
                        + info[0], (int) info[2]);
                    player.getActionSender().sendString("<col=" + (!is(slayerPoints, (int) info[1]) ? "FF0000" : "D7B011") + ">" +
                        info[1] + " points", (int) info[3]);

                }
                break;
            /**
             * The learn tab.
             */
            case 2:
                data = new Object[][]{
                    { "fletch broad arrows/bolts", 300, 24580, 24583 },
                    { "rings of slaying", 300, 24581, 24584 },
                    { "Slayer helmets", 400, 24582, 24585 },
                };
                for (Object[] info : data) {
                    player.getActionSender().sendString("<col=" + (!is(slayerPoints, (int) info[1]) ? "FF0000" : "D7B011") + ">Learn how to "
                        + info[0], (int) info[2]);
                    player.getActionSender().sendString("<col=" + (!is(slayerPoints, (int) info[1]) ? "FF0000" : "D7B011") + ">" +
                        info[1] + " points", (int) info[3]);

                }
                break;
            /**
             * The assignment tab.
             */
            case 3:
                String[] disabled = player.getSavedData().getActivityData().getDisabledTasks();
                if (player.getSlayer().hasTask()) {
                    player.getActionSender().sendString("Cancel task of " + player.getSlayer().getTaskName() + "s.", 24594);
                    for (int i = 0; i < 4; i++) {
                        if (!Objects.equals(player.getSlayer().getTaskName().toLowerCase(), disabled[i].toLowerCase())) {
                            player.getActionSender().sendString("Never assign " + player.getSlayer().getTaskName() + "s again.", 24595);
                        }
                    }
                } else {
                    player.getActionSender().sendString("Reassign current mission", 24594);
                    player.getActionSender().sendString("Permanently remove current", 24595);
                }
                // Currently disabled
                player.getActionSender().sendString(isDisabled(disabled, 0) ? "nothing " : Tasks.forName(disabled[0]).getName() + "s", 24599);
                player.getActionSender().sendString(isDisabled(disabled, 1) ? "nothing " : Tasks.forName(disabled[1]).getName() + "s", 24600);
                player.getActionSender().sendString(isDisabled(disabled, 2) ? "nothing " : Tasks.forName(disabled[2]).getName() + "s", 24601);
                player.getActionSender().sendString(isDisabled(disabled, 3) ? "nothing " : Tasks.forName(disabled[3]).getName() + "s", 24602);
                break;
        }
    }

    /**
     * Disables a task.
     *
     * @param player The player.
     */
    private int removeSlayerTask(Player player) {
        int index = -1;
        String[] disabled = player.getSavedData().getActivityData().getDisabledTasks();
        for (String task : disabled) {
            if (task == null || task.isEmpty() || task.equals("nothing")) {
                continue;
            }
            if (task.equalsIgnoreCase(player.getSlayer().getTasks().name())) {
                return -2;
            }
        }
        for (String task : disabled) {
            index++;
            if (task == null || task.isEmpty() || task.equals("nothing")) {
                break;
            }
        }
        return index;
    }

    /**
     * Whether or not the player has the required amount of slayer points
     *
     * @param player         The player.
     * @param requiredPoints The required amount of points.
     * @return <code>True</code> if so.
     */
    private boolean hasRequiredPoints(Player player, int requiredPoints, int type) {
        if (player.getSavedData().getActivityData().getSlayerPoints() >= requiredPoints) {
            player.getSavedData().getActivityData().decreaseSlayerPoints(requiredPoints);
            configure(player, type);
            return true;
        }
        player.getActionSender().sendMessage("You do not have enough Slayer Points to purchase that.");
        return false;
    }
}

enum RewardType {

    RUNES(
        new int[]{24555, 24560, 24565},
        35,
        1,
        new Item[]{
            new Item(Runes.DEATH_RUNE.getId(), 500),
            new Item(Runes.MIND_RUNE.getId(), 2000)}),
    BROAD_BOLTS(
        new int[]{24556, 24561, 24566},
        35,
        1,
        new Item[]{new Item(31875, 250)}),
    BROAD_ARROWS(
        new int[]{24557, 24562, 24567},
        35,
        1,
        new Item[]{new Item(24160, 250)}),
    SLAYER_RING(
        new int[]{24554, 24559, 24564},
        75,
        1,
        new Item[]{new Item(31866, 1)}
    )
    ;

    private int[] buttons;
    private int pointCost;
    private int type;
    private Item[] rewardItems;

    RewardType(int[] buttons, int pointCost, int type, Item[] rewardItems) {
        this.buttons = buttons;
        this.pointCost = pointCost;
        this.type = type;
        this.rewardItems = rewardItems;
    }

    public int[] getButtons() {
        return buttons;
    }

    public int getPointCost() {
        return pointCost;
    }

    public int getType() {
        return type;
    }

    public Item[] getRewardItems() {
        return rewardItems;
    }
}
