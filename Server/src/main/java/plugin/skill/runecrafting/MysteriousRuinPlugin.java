package plugin.skill.runecrafting;

import org.gielinor.game.content.skill.free.runecrafting.MysteriousRuin;
import org.gielinor.game.content.skill.free.runecrafting.Talisman;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the mysterious ruin plugin handler.
 * @author 'Vexia
 * @version 1.0
 */
public final class MysteriousRuinPlugin extends UseWithHandler {

    /**
     * Represents the animation used.
     */
    private static final Animation ANIMATION = new Animation(827);

    /**
     * Constructs a new {@code RunecraftingOptionPlugin} {@code Object}.
     */
    public MysteriousRuinPlugin() {
        super(1438, 1448, 1444, 1440, 1442, 5516, 1446, 1454, 1452, 1462, 1458, 1456, 1450, 1460);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (MysteriousRuin ruin : MysteriousRuin.values()) {
            for (int i : ruin.getObject()) {
                addHandler(i, OBJECT_TYPE, this);
            }
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        final Talisman talisman = Talisman.forItem(event.getUsedItem());
        final MysteriousRuin ruin = MysteriousRuin.forObject(((GameObject) event.getUsedWith()));
        if (talisman != ruin.getTalisman()) {
            return false;
        }
        player.lock(4);
        player.animate(ANIMATION);
        player.getActionSender().sendMessage("You hold the " + event.getUsedItem().getName() + " towards the mysterious ruins.", 1);
        player.getActionSender().sendMessage("You feel a powerful force take hold of you.", 1);
        World.submit(new Pulse(3, player) {

            @Override
            public boolean pulse() {
                player.getProperties().setTeleportLocation(ruin.getEnd());
                return true;
            }
        });
        return true;
    }

}
