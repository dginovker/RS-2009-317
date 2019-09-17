package plugin.skill.magic.lunar;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The stat spy spell.
 *
 * @author 'Vexia
 */
public final class StatSpySpell extends MagicSpell {

    /**
     * Represents the animation of this spell.
     */
    private final static Animation ANIMATION = new Animation(6293);

    /**
     * Repesents the graphics of this spell.
     */
    private static final Graphics GRAPHIC = new Graphics(734, 120);

    /**
     * Represents the graphics.
     */
    private final Graphics EYE = new Graphics(1059);

    /**
     * Represents the component.
     */
    private final Component COMPONENT = new Component(49000);

    /**
     * Represents the array of skills.
     */
    private static int[][] SKILLS = {
        { Skills.ATTACK, 1, 2 },
        { Skills.HITPOINTS, 5, 6 },
        { Skills.MINING, 9, 10 },
        { Skills.STRENGTH, 13, 14 },
        { Skills.AGILITY, 17, 18 },
        { Skills.SMITHING, 21, 22 },
        { Skills.DEFENCE, 25, 26 },
        { Skills.HERBLORE, 29, 30 },
        { Skills.FISHING, 33, 34 }, { Skills.RANGE, 37, 38 }, { Skills.THIEVING, 41, 42 }, { Skills.COOKING, 45, 46 }, { Skills.PRAYER, 49, 50 }, { Skills.CRAFTING, 53, 54 }, { Skills.FIREMAKING, 57, 58 }, { Skills.MAGIC, 61, 62 }, { Skills.FLETCHING, 65, 66 }, { Skills.WOODCUTTING, 69, 70 }, { Skills.RUNECRAFTING, 73, 74 }, { Skills.SLAYER, 77, 78 }, { Skills.FARMING, 81, 82 }, { Skills.CONSTRUCTION, 85, 86 }, { Skills.HUNTER, 89, 90 }, { Skills.SUMMONING, 93, 94 } };

    /**
     * Constructs a new {@code CureOtherSpell} {@code Object}.
     */
    public StatSpySpell() {
        super(SpellBook.LUNAR, 75, 76, ANIMATION, null, null, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 2), new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.BODY_RUNE.getId(), 5) });
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(30130, this);
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        final Player player = ((Player) entity);
        if (!(target instanceof Player)) {
            player.getActionSender().sendMessage("You can only cast this spell on players.");
            return false;
        }
        if (super.meetsRequirements(player, true, true)) {
            final Player o = ((Player) target);
            player.animate(ANIMATION);
            player.face(o);
            player.getAudioManager().send(3620);
            //COMPONENT.getDefinition().setContext(new InterfaceContext(player, 548, 126, 523, false));
            COMPONENT.setCloseEvent(new CloseEvent() {

                @Override
                public void close(Player player, Component c) {
                    player.getInterfaceState().openDefaultTabs();
                }

                @Override
                public boolean canClose(Player player, Component component) {
                    return true;
                }
            });
            player.graphics(EYE);
            o.graphics(GRAPHIC);
            for (int[] element : SKILLS) {
                // TODO Do we get CURRENT level for hitpoints & prayer?
                player.getActionSender().sendString("" + (element[0] == Skills.HITPOINTS ? o.getSkills().getLifepoints() : o.getSkills().getLevel(element[0])), 49007 + element[1]);
                player.getActionSender().sendString("" + o.getSkills().getStaticLevel(element[0]), 49007 + element[2]);
            }
            player.getActionSender().sendString((o.getUsername()), 49005);
            player.getInterfaceState().openTab(Sidebar.MAGIC_TAB.ordinal(), COMPONENT);
            return true;
        }
        return true;
    }

}
