package plugin.skill.magic.lunar;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.content.skill.member.slayer.Master;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the npc contact spell.
 *
 * @author 'Vexia
 * @version 1.0
 */
public class NPContactSpell extends MagicSpell {

    /**
     * Represents the animation of this graphics.
     */
    private static final Animation ANIMATION = new Animation(4413);

    /**
     * Represents the graphhcics of the spell.
     */
    private static final Graphics GRAPHIC = new Graphics(730, 130);

    /**
     * Constructs a new {@code CurePlantSpell} {@code Object}.
     */
    public NPContactSpell() {
        super(SpellBook.LUNAR, 67, 63, ANIMATION, GRAPHIC, null, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.ASTRAL_RUNE.getId(), 1), new Item(Runes.AIR_RUNE.getId(), 2) });
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(30040, this);
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        final Player player = ((Player) entity);
        final Component component = new Component(429); // TODO
        if (!meetsRequirements(player, true, true)) {
            return false;
        }
        player.getInterfaceState().open(component);
        component.setPlugin(new ComponentPlugin() {

            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                return this;
            }

            @Override
            public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
                switch (button) {
                    case 110:
                        player.getInterfaceState().close();
                        break;
                    default:
                        NPCContact contact = NPCContact.forId(button);
                        if (contact == NPCContact.RANDOM) {
                            contact = NPCContact.values()[RandomUtil.random(NPCContact.values().length)];
                        }
                        if (contact == null) {
                            return true;
                        }
                        if (player.getDialogueInterpreter().open(contact.getNpc(), contact.getNpc())) {
                            player.animate(ANIMATION);
                            player.graphics(GRAPHIC);
                            player.getAudioManager().send(3618);
                            player.getInterfaceState().close();
                        } else {
                            player.getActionSender().sendMessage("This npc is unable to be contacted at this moment.");
                        }
                        return true;
                }
                return true;
            }
        });
        return true;
    }

    /**
     * Represents the npc's to contact.
     *
     * @author 'Vexia
     */
    public enum NPCContact {
        HONEST_JIMM(86, 1816),
        BERT_THE_SANDMAN(87, 5815),
        ADVISOR_GHRIM(88, 5447),
        TURAEL(89, Master.TURAEL.getNpc()),
        LANTHUS(90, 5721),
        MAZCHNA(91, Master.MAZCHNA.getNpc()),
        DURADEL(92, Master.DURADEL.getNpc()),
        VANNAKA(93, Master.VANNAKA.getNpc()),
        MURPHY(82, 5607),
        CHAELDAR(95, Master.CHAELDAR.getNpc()),
        CYRISUS(96, 3470),
        RANDOM(97, -1);

        /**
         * Constructs a new {@code NPContactSpell} {@code Object}.
         *
         * @param button the button.
         * @param npc    the npc.
         */
        NPCContact(int button, int npc) {
            this.button = button;
            this.npc = npc;
        }

        /**
         * Represents the button of the npc to contact.
         */
        private int button;

        /**
         * Represents the npc to contact.
         */
        private int npc;

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
         * Gets the npc.
         *
         * @return The npc.
         */
        public int getNpc() {
            return npc;
        }

        /**
         * Sets the npc.
         *
         * @param npc The npc to set.
         */
        public void setNpc(int npc) {
            this.npc = npc;
        }

        /**
         * Gets the npc.
         *
         * @param id the id.
         * @return the npc contact.
         */
        public static NPCContact forId(int id) {
            for (NPCContact npc : NPCContact.values()) {
                if (npc.getButton() == id) {
                    return npc;
                }
            }
            return null;
        }

    }
}
