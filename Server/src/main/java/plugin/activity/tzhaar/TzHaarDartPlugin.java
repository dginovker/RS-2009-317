package plugin.activity.tzhaar;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;


/**
 * Represents a TzHaar dart to be used against TzHaar fight cave monsters.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class TzHaarDartPlugin extends UseWithHandler {

    /**
     * Represents the ids to use.
     */
    private static final int[] IDS = new int[]{ 2734, 2735, 2736, 2737, 2738, 2739, 2740, 2741, 2742, 2743, 2744, 2745, 2746 };
    /**
     * Represents the TzHaar dart item.
     */
    private static final Item TZHAAR_DART = new Item(11954);

    /**
     * Constructs a new  {@link plugin.activity.tzhaar.TzHaarDartPlugin}.
     */
    public TzHaarDartPlugin() {
        super(TZHAAR_DART.getId());
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int id : IDS) {
            addHandler(id, NPC_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        final NPC npc = (NPC) event.getUsedWith();
        if (npc.getSkills().getLifepoints() < 1) {
            player.getActionSender().sendMessage("They're already dead!");
            return true;
        }
        player.face(npc);
        if (player.getInventory().remove(TZHAAR_DART)) {
            player.playAnimation(new Animation(806));
            npc.getImpactHandler().manualHit(player, npc.getSkills().getLifepoints() + 100, ImpactHandler.HitsplatType.NORMAL);
            player.getActionSender().sendMessage("You throw a TzHaar dart at the monster.");
        }
        return true;
    }
}
