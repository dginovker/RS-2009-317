package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.EmoteData;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the emote tab interface
 *
 * @author Emperor
 */
public final class EmoteTabInterface extends ComponentPlugin {

    private static final Logger log = LoggerFactory.getLogger(EmoteTabInterface.class);

    /**
     * Represents the skillcape info.
     */
    private static final int[][] SKILLCAPE_INFO = {
        { 9747, 9748, 823, 4959 }, { 9750, 9751, 828, 4981 }, { 9753, 9754, 824, 4961 },
        { 9756, 9757, 832, 4973 }, { 9759, 9760, 829, 4979 }, { 9762, 9763, 813, 4939 },
        { 9765, 9766, 817, 4947 }, { 9768, 9769, 833, 4971 }, { 9771, 9772, 830, 4977 },
        { 9774, 9775, 835, 4969 }, { 9777, 9778, 826, 4965 }, { 9780, 9781, 818, 4949 },
        { 9783, 9784, 812, 4937 }, { 9786, 9787, 827, 4967 }, { 9789, 9790, 820, 4953 },
        { 9792, 9793, 814, 4941 }, { 9795, 9796, 815, 4943 }, { 9798, 9799, 819, 4951 },
        { 9801, 9802, 821, 4955 }, { 9804, 9805, 831, 4975 }, { 9807, 9808, 822, 4957 },
        { 9810, 9811, 825, 4963 }, { 12169, 12170, 1515, 8525 }, { 9948, 9949, 907, 5158 },
        { 9813, -1, 816, 4945 }, { Item.ACHIEVEMENT_DIARY_CAPE_T, -1, 2043, 18856 },
    };

    @Override
    public Plugin<Object> newInstance(Object arg) {
        ComponentDefinition.put(147, this);
        return this;
    }

    /**
     * Represents an emote animation.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public enum Emote {

        YES(1, 168, Animation.create(855), null),
        NO(2, 169, Animation.create(856), null),
        BOW(3, 164, Animation.create(858), null) {
            @Override
            public boolean play(Player player) {
                Item legs = player.getEquipment().get(Equipment.SLOT_LEGS);
                if (legs != null) {
                    if (legs.getId() == 10396) {
                        player.getActionSender().sendAnimation(5312);
                        return true;
                    }
                }
                player.getActionSender().sendAnimation(858);
                return true;
            }
        },
        ANGRY(4, 167, Animation.create(859), null),
        THINK(5, 162, Animation.create(857), null),
        WAVE(6, 163, Animation.create(863), null),
        SHRUG(7, 13370, Animation.create(2113), null),
        CHEER(8, 171, Animation.create(862), null),
        BECKON(9, 165, Animation.create(864), null) {
            @Override
            public boolean play(Player player) {
                Item hat = player.getEquipment().get(Equipment.SLOT_HAT);
                if (hat != null) {
                    if (hat.getId() == 10392) {
                        player.getActionSender().sendAnimation(5315);
                        return true;
                    }
                }
                player.getActionSender().sendAnimation(859);
                return true;
            }
        },
        LAUGH(10, 170, Animation.create(861), null),
        JUMP_FOR_JOY(11, 13366, Animation.create(2109), null),
        YAWN(12, 13368, Animation.create(2111), null) {
            public boolean play(Player player) {
                Item hat = player.getEquipment().get(Equipment.SLOT_HAT);
                if (hat != null) {
                    if (hat.getId() == 10398) {
                        player.getActionSender().sendAnimation(5313);
                        return true;
                    }
                }
                player.getActionSender().sendAnimation(2111);
                return true;
            }
        },
        DANCE(13, 166, Animation.create(866), null) {
            @Override
            public boolean play(Player player) {
                Item legs = player.getEquipment().get(Equipment.SLOT_LEGS);
                player.setAttribute("emote_end", (long) (System.currentTimeMillis() + 4000));
                if (legs != null) {
                    if (legs.getId() == 10394) {
                        player.getActionSender().sendAnimation(5316);
                        player.setAttribute("emote_end", (long) (System.currentTimeMillis() + 6000));
                        return true;
                    }
                }
                player.getActionSender().sendAnimation(866);
                return true;
            }
        },
        JIG(14, 13363, Animation.create(2106), null),
        SPIN(15, 13364, Animation.create(2107), null),
        HEADBANG(16, 13365, Animation.create(2108), null),
        CRY(17, 161, Animation.create(860), null),
        BLOW_KISS(18, 11100, Animation.create(0x55E), Graphics.create(/*1702*/ 574)),
        PANIC(19, 13362, Animation.create(2105), null),
        RASPBERRY(20, 13367, Animation.create(2110), null),
        CLAP(21, 172, Animation.create(865), null),
        SALUTE(22, 13369, Animation.create(2112), null),
        GOBLIN_BOW(23, 13383, Animation.create(2127), null, 745, "This emote can be unlocked during the Lost Tribe quest."),
        GOBLIN_SALUTE(24, 13384, Animation.create(2128), null, 746, "This emote can be unlocked during the Lost Tribe quest."),
        GLASS_BOX(25, 667, Animation.create(1131), null, 747, "This emote can be unlocked during the mime random event."),
        CLIMB_ROPE(26, 6503, Animation.create(1130), null, 748, "This emote can be unlocked during the mime random event."),
        LEAN(27, 6506, Animation.create(1129), null, 749, "This emote can be unlocked during the mime random event."),
        GLASS_WALL(28, 666, Animation.create(1128), null, 750, "This emote can be unlocked during the mime random event."),
        SLAP_HEAD(29, 25591, Animation.create(4275), null, 754, "You can't use that emote yet - visit the Stronghold of Security<br>to unlock it."),
        STOMP(30, 22589, Animation.create(4278), null, 752, "You can't use that emote yet - visit the Stronghold of Security<br>to unlock it."),
        FLAP(31, 22590, Animation.create(4280), null, 753, "You can't use that emote yet - visit the Stronghold of Security<br>to unlock it.") {
            @Override
            public boolean play(Player player) {
                Item head = player.getEquipment().get(Equipment.SLOT_HAT);
                Item wings = player.getEquipment().get(Equipment.SLOT_CHEST);
                Item legs = player.getEquipment().get(Equipment.SLOT_LEGS);
                Item feet = player.getEquipment().get(Equipment.SLOT_FEET);
                if (head != null && wings != null && legs != null && feet != null) {
                    if (head.getId() == 11021 && wings.getId() == 11020 && legs.getId() == 11022 && feet.getId() == 11019) {
                        player.getActionSender().sendAnimation(3859);
                        return true;
                    }
                }
                player.getActionSender().sendAnimation(4280);
                return true;
            }
        },
        IDEA(32, 22588, Animation.create(4276), Graphics.create(712), 751, "You can't use that emote yet - visit the Stronghold of Security<br>to unlock it."),
        ZOMBIE_WALK(33, 18464, Animation.create(3544), null, 755, "This emote can be unlocked during the gravedigger random event."),
        ZOMBIE_DANCE(34, 18465, Animation.create(3543), null, 756, "This emote can be unlocked during the gravedigger random event."),
        ZOMBIE_HAND(35, 22593, Animation.create(7272), Graphics.create(1244), 757, "This emote can be unlocked by doing a Halloween seasonal event."),
        SCARED(36, 15166, Animation.create(2836), null, 758, "This emote can be unlocked by doing a Halloween seasonal event."),
        // TODO 317 Rabbit Hop
        BUNNY_HOP(37, 18686, Animation.create(6111), null, 759, "This emote can be unlocked by doing an Easter seasonal event."),
        SKILLCAPE(38, 19052, null, null, 760, "You need to be wearing a skillcape to preform that emote."),
        CHICKEN_DANCE(39, -1, new Animation(1835), null, -1, null);
        /**
         * The id.
         */
        private final int id;
        /**
         * The id of the button.
         */
        private final int button;
        /**
         * The animation for this emote.
         */
        private final Animation animation;
        /**
         * The graphic for this emote.
         */
        private final Graphics graphic;
        /**
         * The unlocked configuration id.
         */
        private int configId;
        /**
         * The locked message.
         */
        private String lockedMessage;

        /**
         * Plays this emote for the player.
         *
         * @param player The player.
         */
        public boolean play(Player player) {
            if (this == SKILLCAPE) {
                handleSkillCape(player);
                return true;
            }
            player.animate(getAnimation());
            if (getGraphic() != null) {
                player.graphics(getGraphic());
            }
            return true;
        }

        /**
         * Constructs a new {@link plugin.interaction.inter.EmoteTabInterface.Emote}.
         *
         * @param button    The button id.
         * @param animation The animation to play.
         * @param graphic   The graphics to play; if any.
         */
        private Emote(int id, int button, Animation animation, Graphics graphic) {
            this.id = id;
            this.button = button;
            this.animation = animation;
            this.graphic = graphic;
        }

        /**
         * Constructs a new {@link plugin.interaction.inter.EmoteTabInterface.Emote}.
         *
         * @param button        The button id.
         * @param animation     The animation to play.
         * @param graphic       The graphics to play; if any.
         * @param configId      The configuration id to unlock.
         * @param lockedMessage The message to send when this emote is locked.
         */
        private Emote(int id, int button, Animation animation, Graphics graphic, int configId, String lockedMessage) {
            this.id = id;
            this.button = button;
            this.animation = animation;
            this.graphic = graphic;
            this.configId = configId;
            this.lockedMessage = lockedMessage;
        }

        /**
         * Gets the id.
         *
         * @return The id.
         */
        public int getId() {
            return id;
        }

        /**
         * @return the animation
         */
        public Animation getAnimation() {
            return animation;
        }

        /**
         * @return the button
         */
        public int getButton() {
            return button;
        }

        /**
         * @return the graphic
         */
        public Graphics getGraphic() {
            return graphic;
        }

        /**
         * Gets the configuration id.
         *
         * @return The configuration id.
         */
        public int getConfigId() {
            return configId;
        }

        /**
         * Gets the locked message.
         *
         * @return The locked message.
         */
        public String getLockedMessage() {
            return lockedMessage;
        }

        /**
         * Gets an {@link plugin.interaction.inter.EmoteTabInterface.Emote} by the id.
         *
         * @param id The id.
         * @return The emote.
         */
        public static Emote forId(int id) {
            for (Emote emote : Emote.values()) {
                if (emote.getId() == id) {
                    return emote;
                }
            }
            return null;
        }

        /**
         * Gets an {@link plugin.interaction.inter.EmoteTabInterface.Emote} by the button id.
         *
         * @param button The button id.
         * @return The emote.
         */
        public static Emote forButtonId(int button) {
            for (Emote emote : Emote.values()) {
                if (emote.getButton() == button) {
                    return emote;
                }
            }
            return null;
        }

        /**
         * Gets an {@link plugin.interaction.inter.EmoteTabInterface.Emote} by the enum name.
         *
         * @param name The name.
         * @return The emote.
         */
        public static Emote forName(String name) {
            name = name.toUpperCase();
            try {
                return valueOf(name);
            } catch (IllegalArgumentException ex) {
                log.error("No such emote: [{}].", name, ex);
            }
            return null;
        }

    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (button == -1) {
            return false;
        }
        Emote emote = Emote.forButtonId(button);
        if (emote == null) {
            return false;
        }
        player.getInterfaceState().close();
        if (System.currentTimeMillis() < player.getAttribute("emote_end", 0L)) {
            player.getActionSender().sendMessage("You're already doing an emote!");
            return true;
        }
        if (player.getProperties().getCombatPulse().isAttacking() || player.inCombat()) {
            player.getActionSender().sendMessage("You can't perform an emote while being in combat.");
            return true;
        }
        player.getPulseManager().clear();
        EmoteData emoteData = player.getEmotes();
        if (emoteData != null) {
            if (emote.getConfigId() > 0) {
                if (emote == Emote.SKILLCAPE) {
                    if (player.getSkills().getMasteredSkills() == 0) {
                        player.getActionSender().sendMessage(Emote.SKILLCAPE.getLockedMessage());
                        return true;
                    }
                } else if (!emoteData.getUnlockedEmotes().contains(emote)) {
                    String message = emote.getLockedMessage();
                    if (message == null) {
                        message = "You can't use this emote yet.";
                    }
                    player.getDialogueInterpreter().sendPlaneMessage(message);
                    return true;
                }
            }
        }
        player.setAttribute("emote_end", System.currentTimeMillis() + 2000L);
        Item item = player.getEquipment().get(Equipment.SLOT_WEAPON);
        if (item != null) {
            switch (item.getId()) {
                case 4084:
                    player.getActionSender().sendAnimation(1483);
                    return true;
            }
        }

        if (emote.play(player)) {
            return true;
        }
        player.removeAttribute("emote_end");//Rigging this for now...
        return false;
    }

    /**
     * Method used to handle a skillcape.
     *
     * @param player the player.
     */
    private static void handleSkillCape(Player player) {
        Item cape = player.getEquipment().get(Equipment.SLOT_CAPE);
        if (cape == null) {
            player.getActionSender().sendMessage("You need to be wearing a skillcape in order to perform this emote.");
            return;
        }
        if (cape.getName().toLowerCase().contains("max cape")) {
            player.getActionSender().sendGraphic(2042);
            player.getActionSender().sendAnimation(23268);
            return;
        }
        int capeId = cape.getId();
        for (int[] skillcape : SKILLCAPE_INFO) {
            if (capeId == skillcape[0] || capeId == skillcape[1]) {
                player.getActionSender().sendGraphic(skillcape[2]);
                player.getActionSender().sendAnimation(skillcape[3]);
                return;
            }
        }
        player.getActionSender().sendMessage("You need to be wearing a skillcape in order to perform this emote.");
    }

}
