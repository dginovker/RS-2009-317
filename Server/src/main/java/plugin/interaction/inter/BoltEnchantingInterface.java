package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.magic.MagicStaff;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the component plugin used to handle the bolt enchanting interface.
 *
 * @author Emperor
 * @author SonicForce41
 */
public final class BoltEnchantingInterface extends ComponentPlugin {

    /**
     * Represents the graphics to use.
     */
    private static final Graphics GRAPHICS = new Graphics(759);

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(4462);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(47200, this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
        final Bolts bolts = Bolts.forId(button);
        if (bolts != null) {
            player.getInterfaceState().close();
            if (player.getSkills().getLevel(Skills.MAGIC) < bolts.getLevel()) {
                player.getActionSender().sendMessage("You need a level of " + bolts.getLevel() + " to cast this spell.");
                return true;
            }
            for (Item item : bolts.getRunes()) {
                if (!Runes.hasRunes(player, item) && !usingStaff(player, item.getId())) {
                    player.getActionSender().sendMessage("You do not have enough runes to cast this spell.");
                    return true;
                }
            }
            final int amount = player.getInventory().getCount(new Item(bolts.getBolt(), 1));
            if (amount < 1) {
                player.getActionSender().sendMessage("You don't have any bolts to enchant.");
                return true;
            }
            final Item add = new Item(bolts.getEnchanted());
            if (amount > 10 && !player.getInventory().hasRoomFor(add)) {
                player.getActionSender().sendMessage("Not enough space in your inventory!");
                return true;
            }
            player.graphics(GRAPHICS);
            player.animate(ANIMATION);
            player.getPulseManager().run(new Pulse(1) {

                @Override
                public boolean pulse() {
                    int enchant = amount;
                    if (enchant > 10) {
                        enchant = 10;
                    }
                    add.setCount(enchant);
                    player.getInventory().remove(new Item(bolts.getBolt(), enchant));
                    player.getInventory().add(add);

                    for (Item item : bolts.getRunes()) {
                        boolean staff = usingStaff(player, item.getId());
                        if (!Runes.hasRunes(player, item) && !staff) {
                            player.getActionSender().sendMessage("You do not have enough " + item.getName().toLowerCase() + "s to cast this spell.");
                            return true;
                        }
                        if (!staff) {
                            Runes.removeRunes(player, item);
                        }
                    }
                    player.getSkills().addExperience(Skills.MAGIC, bolts.getExp());
                    player.getActionSender().sendMessage("The magic of the runes coaxes out the true nature of the gem tips.");
                    return true;
                }
            });
            return true;
        }
        return false;
    }

    /**
     * Checks if the player is holding the staff that will be used instead of the rune.
     *
     * @param p    The player.
     * @param rune The rune item id.
     * @return <code>True</code> if the player is wearing the correct staff for this rune.
     */
    public static boolean usingStaff(Player p, int rune) {
        Item weapon = p.getEquipment().get(3);
        if (weapon == null) {
            return false;
        }
        MagicStaff staff = MagicStaff.forId(rune);
        if (staff == null) {
            return false;
        }
        int[] staves = staff.getStaves();
        for (int id : staves) {
            if (weapon.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Represents the enumeration of bolts.
     *
     * @author SonicForce41
     * @version 1
     */
    public enum Bolts {
        OPAL(47228, 879, 4, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.AIR_RUNE.getId(), 2) }, 9, 9236),
        SAPPHIRE(47229, 9337, 7, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.WATER_RUNE.getId(), 1), new Item(Runes.MIND_RUNE.getId(), 1) }, 17, 9240),
        JADE(47230, 9335, 14, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 2) }, 19, 9237),
        PEARL(47231, 880, 24, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.WATER_RUNE.getId(), 2) }, 29, 9238),
        EMERALD(47232, 9338, 27, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.AIR_RUNE.getId(), 3), new Item(Runes.NATURE_RUNE.getId(), 1) }, 37, 9241),
        RED_TOPAZ(47233, 9336, 29, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.FIRE_RUNE.getId(), 2) }, 33, 9239),
        RUBY(47234, 9339, 49, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.FIRE_RUNE.getId(), 5), new Item(Runes.BLOOD_RUNE.getId(), 1) }, 59, 9242),
        DIAMOND(47235, 9340, 57, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 10), new Item(Runes.LAW_RUNE.getId(), 2) }, 67, 9243),
        DRAGONSTONE(47236, 9341, 68, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 15), new Item(Runes.SOUL_RUNE.getId(), 1) }, 78, 9244),
        ONYX(47237, 9342, 87, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.FIRE_RUNE.getId(), 20), new Item(Runes.DEATH_RUNE.getId(), 1) }, 97, 9245);

        /**
         * The button
         */
        private int button;

        /**
         * The actual bolt
         */
        private int bolt;

        /**
         * The level
         */
        private int level;

        /**
         * The runes required
         */
        private Item[] runes;

        /**
         * The exp gained
         */
        private double exp;

        /**
         * The enchanted bolt
         */
        private int enchanted;

        /**
         * Constructs a new {@code BoltEnchantingInterface} {@code Object}.
         *
         * @param button    the button.
         * @param bolt      the bolt.
         * @param level     the level.
         * @param runes     the runes.
         * @param exp       the exp.
         * @param enchanted the enchanted.
         */
        Bolts(int button, int bolt, int level, Item[] runes, double exp, int enchanted) {
            this.button = button;
            this.bolt = bolt;
            this.level = level;
            this.runes = runes;
            this.exp = exp;
            this.enchanted = enchanted;
        }

        /**
         * Gets the button.
         *
         * @return The button.
         */
        public int getButton() {
            return button;
        }

        /**
         * Sets the button.
         *
         * @param button The button to set.
         */
        public void setButton(int button) {
            this.button = button;
        }

        /**
         * Gets the bolt.
         *
         * @return The bolt.
         */
        public int getBolt() {
            return bolt;
        }

        /**
         * Sets the bolt.
         *
         * @param bolt The bolt to set.
         */
        public void setBolt(int bolt) {
            this.bolt = bolt;
        }

        /**
         * Gets the level.
         *
         * @return The level.
         */
        public int getLevel() {
            return level;
        }

        /**
         * Sets the level.
         *
         * @param level The level to set.
         */
        public void setLevel(int level) {
            this.level = level;
        }

        /**
         * Gets the runes.
         *
         * @return The runes.
         */
        public Item[] getRunes() {
            return runes;
        }

        /**
         * Sets the runes.
         *
         * @param runes The runes to set.
         */
        public void setRunes(Item[] runes) {
            this.runes = runes;
        }

        /**
         * Gets the exp.
         *
         * @return The exp.
         */
        public double getExp() {
            return exp;
        }

        /**
         * Sets the exp.
         *
         * @param exp The exp to set.
         */
        public void setExp(double exp) {
            this.exp = exp;
        }

        /**
         * Gets the enchanted.
         *
         * @return The enchanted.
         */
        public int getEnchanted() {
            return enchanted;
        }

        /**
         * Sets the enchanted.
         *
         * @param enchanted The enchanted to set.
         */
        public void setEnchanted(int enchanted) {
            this.enchanted = enchanted;
        }

        /**
         * Gets the bolt by the id.
         *
         * @param button the button.
         * @return the bolt.
         */
        public static Bolts forId(int button) {
            for (Bolts bolts : Bolts.values()) {
                if (bolts.getButton() == button) {
                    return bolts;
                }
            }
            return null;
        }
    }

}
