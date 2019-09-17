package plugin.skill.runecrafting;

import org.gielinor.game.content.skill.free.runecrafting.Altar;
import org.gielinor.game.content.skill.free.runecrafting.CombinationRune;
import org.gielinor.game.content.skill.free.runecrafting.Rune;
import org.gielinor.game.content.skill.free.runecrafting.RuneCraftPulse;
import org.gielinor.game.content.skill.free.runecrafting.Talisman;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to combine runes.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CombinationRunePlugin extends UseWithHandler {

    /**
     * Constructs a new {@code CombinationRunePlugin} {@code Object}.
     */
    public CombinationRunePlugin() {
        super(Talisman.AIR.getTalisman().getId(),
            Talisman.WATER.getTalisman().getId(),
            Talisman.EARTH.getTalisman().getId(),
            Talisman.FIRE.getTalisman().getId(),
            Rune.WATER.getRune().getId(),
            Rune.EARTH.getRune().getId(),
            Rune.AIR.getRune().getId(),
            Rune.FIRE.getRune().getId());
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (Altar altar : Altar.values()) {
            addHandler(altar.getObject(), OBJECT_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        final Altar altar = Altar.forObject(((GameObject) event.getUsedWith()));
        final CombinationRune combo = CombinationRune.forAltar(altar, event.getUsedItem());
        if (combo == null) {
            return false;
        }
        player.getPulseManager().run(new RuneCraftPulse(player, event.getUsedItem(), altar, true, combo));
        return true;
    }

}
