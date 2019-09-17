package plugin.skill.crafting;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.crafting.BattleStaves;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the battle stave making plugin used for zaff.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BattlestaveMakePlugin extends UseWithHandler {

    /**
     * Represents the original staff item.
     */
    private static final Item STAFF = new Item(1391, 1);

    /**
     * Constructs a new {@code BattlestaveMakePlugin} {@code Object}.
     */
    public BattlestaveMakePlugin() {
        super(STAFF.getId());
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(573, ITEM_TYPE, this);
        addHandler(571, ITEM_TYPE, this);
        addHandler(575, ITEM_TYPE, this);
        addHandler(569, ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        int id = event.getUsedItem().getId() == STAFF.getId() ? event.getUsedWith().getId() : event.getUsedItem().getId();
        final BattleStaves staff = BattleStaves.forId(id);
        if (staff == null) {
            return true;
        }
        if (player.getSkills().getLevel(Skills.CRAFTING) < staff.getLevel()) {
            player.getActionSender().sendMessage("You need a crafting level of " + staff.getLevel() + " to make this.");
            return true;
        }
        SkillDialogueHandler skillDialogueHandler = new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION,
            new Item(staff.getProduct())) {

            @Override
            public void create(final int amount, final int index) {
                player.getPulseManager().run(new BattleStaffCreatePulse(player, STAFF, staff, amount));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(STAFF);
            }

        };
        if (player.getInventory().getCount(STAFF) == 1) {
            skillDialogueHandler.create(1, 0);
        } else {
            skillDialogueHandler.open();
        }
        return true;
    }

    /**
     * Represents the {@link org.gielinor.game.content.skill.SkillPulse} for creating battlestaves.
     *
     * @author <a href="http://Gielinor.org/">Gielinor</a>
     */
    public static final class BattleStaffCreatePulse extends SkillPulse<Item> {

        /**
         * The {@link org.gielinor.game.content.skill.free.crafting.BattleStaves}.
         */
        private BattleStaves battleStaves;
        /**
         * The initial amount.
         */
        private final int initialAmount;
        /**
         * The amount to make.
         */
        private int amount;

        /**
         *
         */
        public BattleStaffCreatePulse(Player player, Item node, BattleStaves battleStaves, int amount) {
            super(player, node);
            this.battleStaves = battleStaves;
            this.initialAmount = amount;
            this.amount = amount;
        }

        @Override
        public boolean checkRequirements() {
            if (player.getSkills().getLevel(Skills.CRAFTING) < battleStaves.getLevel()) {
                player.getDialogueInterpreter().sendPlaneMessage("You need a Crafting level of " + battleStaves.getLevel() + " to do that.");
            }
            if (!player.getInventory().contains(STAFF)) {
                if (initialAmount != 1) {
                    player.getDialogueInterpreter().sendPlaneMessage("You do not have any battlestaves.");
                }
                return true;
            }
            if (!player.getInventory().contains(battleStaves.getObelisk())) {
                if (initialAmount != 1) {
                    player.getDialogueInterpreter().sendPlaneMessage("You do not have any " +
                        new Item(battleStaves.getObelisk()).getName().toLowerCase() + "s.");
                }
                return true;
            }
            return true;
        }

        @Override
        public void animate() {
        }

        @Override
        public boolean reward() {
            if (player.getInventory().remove(STAFF, new Item(battleStaves.getObelisk()))) {
                player.getSkills().addExperience(Skills.FLETCHING, battleStaves.getExp());
                player.getInventory().add(new Item(battleStaves.getProduct(), 1));
                if (amount == 1 || (amount % 5) == 0) {
                    String name = new Item(battleStaves.getObelisk()).getName();
                    player.getActionSender().sendMessage("You attach a" + (TextUtils.isPlusN(name) ? "n " : " ") + name.toLowerCase() + " to the staff.", 1);
                }
            }
            if (!player.getInventory().contains(STAFF)) {
                if (initialAmount != 1) {
                    player.getDialogueInterpreter().sendPlaneMessage("You have run out of battlestaves.");
                }
                return true;
            }
            if (!player.getInventory().contains(battleStaves.getObelisk())) {
                if (initialAmount != 1) {
                    player.getDialogueInterpreter().sendPlaneMessage("You have run out of " + new Item(battleStaves.getObelisk()).getName().toLowerCase() + "s.");
                }
                return true;
            }
            amount--;
            return amount == 0;
        }

        @Override
        public void message(int type) {
        }
    }


}
