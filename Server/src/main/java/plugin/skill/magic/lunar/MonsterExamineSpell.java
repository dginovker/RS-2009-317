package plugin.skill.magic.lunar;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.context.InterfaceContext;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the monster examine spell.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class MonsterExamineSpell extends MagicSpell {

    /**
     * Represents the animation of this spell.
     */
    private final static Animation ANIMATION = new Animation(6293);

    /**
     * Repesents the graphics of this spell.
     */
    private static final Graphics GRAPHIC = new Graphics(738, 130);

    /**
     * Represents the graphics of the eye.
     */
    private static final Graphics EYE = new Graphics(1059);

    /**
     * Represents the animation of the spell.
     */
    private static final Component COMPONENT = new Component(522); // TODO

    /**
     * Constructs a new {@code CurePlantSpell} {@code Object}.
     */
    public MonsterExamineSpell() {
        super(SpellBook.LUNAR, 66, 61, ANIMATION, null, null, new Item[]{ new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.ASTRAL_RUNE.getId(), 1), new Item(Runes.MIND_RUNE.getId(), 1) });
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(6, this);
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        final Player player = ((Player) entity);
        if (!(target instanceof NPC)) {
            player.getActionSender().sendMessage("You can only cast this spell on monsters.");
            return false;
        }
        if (!super.meetsRequirements(player, true, true)) {
            return false;
        }
        final NPC npc = ((NPC) target);
        player.animate(ANIMATION);
        player.face(npc);
        player.getAudioManager().send(3621);
        COMPONENT.getDefinition().setContext(new InterfaceContext(player, 548, 126, 522, false));
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
        npc.graphics(GRAPHIC);
        npc.getSkills().updateCombatLevel();
        player.getActionSender().sendString498("Monster name: " + npc.getName(), 522, 0);
        player.getActionSender().sendString498("Combat level: " + npc.getDefinition().getCombatLevel(), 522, 1);
        player.getActionSender().sendString498("Life points: " + npc.getSkills().getMaximumLifepoints(), 522, 2);
        player.getActionSender().sendString498("Creature's max hit: " + npc.getSwingHandler(false).calculateHit(npc, npc, 1.0), 522, 3);
        player.getActionSender().sendString498("", 522, 4);
        player.getInterfaceState().open(COMPONENT);
        return true;
    }
}
