package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.CameraContext;
import org.gielinor.net.packet.context.CameraContext.CameraType;
import org.gielinor.net.packet.out.CameraViewPacket;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;

/**
 * Handles the barcrawl npc interactions.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BarcrawlPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (BarcrawlType type : BarcrawlType.values()) {
            for (int npc : type.getNpc()) {
                NPCDefinition.forId(npc).getConfigurations().put("option:talk-to", this);
            }
        }
        PluginManager.definePlugin(new BarcrawlDialogue());
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final BarcrawlType type = BarcrawlType.forId(node.getId());
        if (player.getBarcrawlManager().isFinished() || !player.getBarcrawlManager().isStarted() || player.getBarcrawlManager().isCompleted(type.ordinal())) {
            player.getDialogueInterpreter().open(node.getId(), node);
        } else {
            player.getDialogueInterpreter().open("barcrawl dialogue", node.getId(), type);
        }
        return true;
    }

    /**
     * A barcrawl type npc.
     *
     * @author 'Vexia
     */
    public enum BarcrawlType {
        BLUE_MOON(733, 50, "Uncle Humphrey's Gutrot", new String[]{ "Oh no not another of you guys. These barbarian", "barcrawls cause too much damage to my bar." }, new String[]{ "You're going to have to pay me 50 gold for the Uncle", "Humphrey's Gutrot." }) {
            @Override
            public void effect(final Player player) {
                player.sendChat("Blearrgh!");
                player.getImpactHandler().manualHit(player, 1, HitsplatType.NORMAL);
                addBonus(player, 1, Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH, Skills.SMITHING);
            }

            @Override
            public void message(final Player player, boolean start) {
                if (!start) {
                    player.getActionSender().sendMessages("Your insides feel terrible.", "The bartender signs your card.");
                } else {
                    player.getActionSender().sendMessages("You buy some " + getName() + ".", "You drink the " + getName() + ".");
                }
            }
        },
        BLUEBERRY_BAR(848, 10, "Fire Toad Blast", new String[]{ "Ah, you've come to the best stop on your list! I'll give", "you my famous Fire Toad last! It'll cost you 10", "coins." }) {
            @Override
            public void effect(final Player player) {
                player.getImpactHandler().manualHit(player, 1, HitsplatType.NORMAL);
            }

            @Override
            public void message(final Player player, boolean start) {
                if (!start) {
                    player.getActionSender().sendMessage("Blueberry signs your card.");
                } else {
                    super.message(player, start);
                    player.getActionSender().sendMessage("Your mouth and throat burns as you gulp it down.");
                }
            }

        },
        DEADMAN_CHEST(735, 15, "Supergrog", new String[]{ "Haha time to be breaking out the old Supergrog. That'll", "be 15 coins please." }) {
            @Override
            public void effect(final Player player) {
                addBonus(player, 1, Skills.ATTACK, Skills.DEFENCE, Skills.HERBLORE, Skills.CONSTRUCTION, Skills.PRAYER);
            }

            @Override
            public void message(final Player player, boolean start) {
                if (!start) {
                    player.getActionSender().sendMessages("You stagger backwards.", "You think you see 2 bartenders signing 2 barcrawl cards.");
                } else {
                    player.getActionSender().sendMessages("The bartender serves you a glass of strange thick dark liquid.", "You wince and drink it.");
                }
            }

        },
        DRAGON_INN(739, 12, "Fire Brandy", new String[]{ "I suppose you'll be wanting some Fire Brandy. That'll", "cost you 12 coins." }) {
            @Override
            public void effect(final Player player) {
                addBonus(player, 1, Skills.ATTACK, Skills.DEFENCE);
            }

            @Override
            public void message(final Player player, boolean start) {
                if (!start) {
                    player.getActionSender().sendMessages("Your vision blurs and you stagger slightly.", "You can just about make out the bartender signing your barcrawl card.");
                } else {
                    player.getActionSender().sendMessages("The bartender hands you a small glass and sets light to the contents.", "You blow out the flame and drink it.");
                }
            }

        },
        FLYING_HORSE_INN(737, 8, "Heart Stopper", new String[]{ "Fancy a bit of Heart Stopper then do you? It'll only be", "8 coins." }) {
            @Override
            public void effect(final Player player) {
                player.getImpactHandler().manualHit(player, (int) (player.getSkills().getLevel(Skills.HITPOINTS) * 0.15), HitsplatType.NORMAL);
            }

            @Override
            public void message(final Player player, boolean start) {
                if (!start) {
                    player.getActionSender().sendMessages("You clutch your chest.", "Through your tears you see the bartender...", "signing your barcrawl card.");
                } else {
                    player.getActionSender().sendMessages("The bartender hands you a shot of Heart Stopper.", "You grimace and drink it.");
                }
            }
        },
        FORESTERS_ARMS(738, 18, "Liverbane Ale", new String[]{ "Oh you're a barbarian then. Now which of these barrels", "contained the Liverbane Ale? That'll be 18 coins please." }) {
            @Override
            public void effect(final Player player) {
                addBonus(player, Skills.ATTACK, Skills.DEFENCE, Skills.FLETCHING, Skills.FIREMAKING, Skills.WOODCUTTING);
            }

            @Override
            public void message(final Player player, boolean start) {
                if (!start) {
                    player.getActionSender().sendMessages("The room seems to be swaying.", "The bartender scrawls his signature on your card.");
                } else {
                    player.getActionSender().sendMessages("The bartender gives you a glass of Liverbane Ale.", "You gulp it down.");
                }
            }

        },
        JOLLY_BOAR(731, 10, "Olde Suspiciouse", new String[]{ "Ah, there seems to be a fair few doing that one these", "days. My supply of Olde suspiciouse is starting to run", "low, it'll cost you 10 coins." }) {
            @Override
            public void effect(final Player player) {
                addBonus(player, 1, Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH, Skills.MINING, Skills.CRAFTING, Skills.MAGIC);
                player.getImpactHandler().manualHit(player, 1, HitsplatType.NORMAL);
                player.getDialogueInterpreter().sendDialogues(player, null, "Thanksh very mush...");
            }

            @Override
            public void message(final Player player, boolean start) {
                if (!start) {
                    player.getActionSender().sendMessages("Your head is spinning.", "The bartender signs your card.");
                } else {
                    player.getActionSender().sendMessages("You buy a pint of Olde Suspiciouse.", "You gulp it down.");
                }
            }
        },
        KARAMJA_SPIRITS(568, 7, "Ape Bite Liqueur", new String[]{ "Ah, you'll be wanting some Ape Bite Liqueur then. It's", "got a lovely bannana taste, and it'll only cost you 7", "coins." }) {
            @Override
            public void effect(final Player player) {
                addBonus(player, 1, Skills.ATTACK, Skills.DEFENCE);
            }

            @Override
            public void message(final Player player, boolean start) {
                if (!start) {
                    player.getActionSender().sendMessages("Zambo signs your card.");
                    player.getDialogueInterpreter().sendDialogues(player, null, "Mmmmm, dat was luverly...");
                } else {
                    player.getActionSender().sendMessages("You buy some Ape Bite liqueur.", "You swirl it around and swallow it.");
                }
            }

        },
        RISING_SUNN_INN(new int[]{ 3217, 736 }, 70, "Hand of Death Cocktail", new String[]{ "Heehee, this'll be fun!" }, new String[]{ "You'll be after our Hand of Death cocktail, then. Lots", "of expensive parts to the cocktail, though, so it will cost", "you 70 coins." }) {
            @Override
            public void effect(final Player player) {
                addBonus(player, 1, Skills.ATTACK, Skills.DEFENCE, Skills.RANGE, Skills.FIREMAKING);
                player.getImpactHandler().manualHit(player, 1, HitsplatType.NORMAL);
            }

            @Override
            public void message(final Player player, boolean start) {
                if (!start) {
                    player.getActionSender().sendMessages("The barmaid giggles.", "The barmaid signs your card.");
                } else {
                    PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.SHAKE, 4, 4, 1, 4, 4));
                    World.submit(new Pulse(3, player) {

                        @Override
                        public boolean pulse() {
                            PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.RESET, 4, 4, 1, 4, 4));
                            return true;
                        }
                    });
                    player.getActionSender().sendMessages("You buy a Hand of Death cocktail.", "You drink the cocktail.", "You stumble around the room.");
                }
            }
        },
        RUSTY_ANCHOR_INN(734, 8, "Black Skull Ale", new String[]{ "Ok one Black Skull Ale coming up, 8 coins please." }) {
            @Override
            public void effect(final Player player) {
                player.sendChat("Hiccup!");
            }

            @Override
            public void message(final Player player, boolean start) {
                if (!start) {
                    super.message(player, start);
                } else {
                    player.getActionSender().sendMessages("You buy a Black Skull Ale...", "You drink your Black Skull Ale...", "Your vision blurs.");
                }
            }
        },;

        /**
         * The npc id.
         */
        private final int[] npc;

        /**
         * The name.
         */
        private final String name;

        /**
         * The coin required.
         */
        private final Item coins;

        /**
         * The dialogue to use.
         */
        private final String[][] dialogue;

        /**
         * Constructs a new {@code BarcrawlType} {@code Object}.
         *
         * @param npc      the npc.
         * @param name     the name.
         * @param coins    the coins.
         * @param dialogue the dialogue.
         */
        private BarcrawlType(int npc, Item coins, String name, String[][] dialogue) {
            this.npc = new int[]{ npc };
            this.name = name;
            this.coins = coins;
            this.dialogue = dialogue;
        }

        /**
         * Constructs a new {@code BarcrawlType} {@code Object}.
         *
         * @param npc   the npc.
         * @param coins the coins.
         */
        private BarcrawlType(int npc, int coins, final String name, String[] first, String[] second) {
            this.npc = new int[]{ npc };
            this.name = name;
            this.coins = new Item(Item.COINS, coins);
            this.dialogue = new String[][]{ first, second };
        }

        /**
         * Constructs a new {@code BarcrawlType} {@code Object}.
         *
         * @param npc    the npc.
         * @param coins  the coins.
         * @param first  the first dial.
         * @param second the second dial.
         */
        private BarcrawlType(int[] npc, int coins, String name, String[] first, String[] second) {
            this.npc = npc;
            this.name = name;
            this.coins = new Item(Item.COINS, coins);
            this.dialogue = new String[][]{ first, second };
        }

        /**
         * Constructs a new {@code BarcrawlType} {@code Object}.
         *
         * @param npc   the npc.
         * @param coins the coins.
         */
        private BarcrawlType(int npc, int coins, String name, String[] first) {
            this.npc = new int[]{ npc };
            this.name = name;
            this.coins = new Item(Item.COINS, coins);
            this.dialogue = new String[][]{ first };
        }

        /**
         * Method used to effect the player.
         *
         * @param player the player.
         */
        public void effect(final Player player) {

        }

        /**
         * Method used to message the player.
         *
         * @param player the player.
         * @param start  or finish.
         */
        public void message(final Player player, boolean start) {
            if (!start) {
                player.getActionSender().sendMessage("The bartender signs your card.");
            } else {
                player.getActionSender().sendMessage("You buy a " + (TextUtils.isPlusN(getName()) ? "an" : "a") + " " + getName() + ".");
            }
        }

        /**
         * Method used to a skill bonus.
         *
         * @param player the player.
         * @param amount the amount.
         * @param skills the skills.
         */
        public void addBonus(final Player player, int amount, final int... skills) {
            for (int i : skills) {
                player.getSkills().updateLevel(i, -amount, 0);
            }
        }

        /**
         * Gets the bar crawl type.
         *
         * @param id the id.
         * @return the type.
         */
        public static BarcrawlType forId(int id) {
            for (BarcrawlType type : values()) {
                for (int npc : type.getNpc()) {
                    if (npc == id) {
                        return type;
                    }
                }
            }
            return null;
        }

        /**
         * Gets the npc.
         *
         * @return The npc.
         */
        public int[] getNpc() {
            return npc;
        }

        /**
         * Gets the coins.
         *
         * @return The coins.
         */
        public Item getCoins() {
            return coins;
        }

        /**
         * Gets the dialogue.
         *
         * @return The dialogue.
         */
        public String[][] getDialogue() {
            return dialogue;
        }

        /**
         * Gets the name.
         *
         * @return The name.
         */
        public String getName() {
            return name;
        }

    }

    /**
     * The barcrawl diaogue plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class BarcrawlDialogue extends DialoguePlugin {

        /**
         * The npc id being used.
         */
        private BarcrawlType type;

        /**
         * The npc id.
         */
        private int npcId;

        /**
         * Constructs a new {@code BarcrawlDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public BarcrawlDialogue(final Player player) {
            super(player);
        }

        /**
         * Constructs a new {@code BarcrawlDialogue} {@code Object}.
         */
        public BarcrawlDialogue() {
            /**
             * empty.
             */
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new BarcrawlDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npcId = (Integer) args[0];
            type = (BarcrawlType) args[1];
            player("I'm doing Alfred Grimhand's Barcrawl.");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    interpreter.sendDialogues(npcId, null, type.getDialogue()[0]);
                    stage = type.getDialogue().length > 1 ? 1 : 2;
                    break;
                case 1:
                    interpreter.sendDialogues(npcId, null, type.getDialogue()[1]);
                    stage++;
                    break;
                case 2:
                    end();
                    if (!player.getInventory().containsItem(type.getCoins())) {
                        break;
                    }
                    type.message(player, true);
                    player.getInventory().remove(type.getCoins());
                    player.getBarcrawlManager().complete(type.ordinal());
                    player.lock(6);
                    World.submit(new Pulse(6, player) {

                        @Override
                        public boolean pulse() {
                            type.message(player, false);
                            type.effect(player);
                            return true;
                        }
                    });
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("barcrawl dialogue") };
        }

    }
}
