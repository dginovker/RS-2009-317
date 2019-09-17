package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for Explorer's rings.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ExplorersRingPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) {
        ItemDefinition.forId(Item.EXPLORERS_RING_1).getConfigurations().put("option:recharge-energy", this);
        ItemDefinition.forId(Item.EXPLORERS_RING_2).getConfigurations().put("option:recharge-energy", this);
        ItemDefinition.forId(Item.EXPLORERS_RING_3).getConfigurations().put("option:recharge-energy", this);
        ItemDefinition.forId(Item.EXPLORERS_RING_4).getConfigurations().put("option:recharge-energy", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "recharge-energy":
                boolean canRecharge = false;
                int usingCharge = 0;
                long rechargeTime = (24 * 60 * 60_000);
                int charges = node.getId() == Item.EXPLORERS_RING_1 ? 2 : node.getId() == Item.EXPLORERS_RING_2 ? 3 :
                    node.getId() == Item.EXPLORERS_RING_3 ? 4 : node.getId() == Item.EXPLORERS_RING_4 ? 3 : 2;
                double percentage = node.getId() == Item.EXPLORERS_RING_4 ? 100.0 : 50.0;
                for (int charge = 0; charge < charges; charge++) {
                    if ((System.currentTimeMillis() -
                        player.getAttribute("run_" + node.getId() + "_" + charge, 0L))
                        > rechargeTime) {
                        canRecharge = true;
                        usingCharge = charge;
                        break;
                    }
                }
                if (!canRecharge) {
                    player.getActionSender().sendMessage("You cannot recharge your energy anymore today.");
                    return true;
                }
                double recharge = ((100 * percentage) / 100);
                if (player.getSettings().getRunEnergy() >= 100) {
                    player.getActionSender().sendMessage("You are fully rested. You do not need to use the ring's power for the moment.");
                    return true;
                }
                double energy = player.getSettings().getRunEnergy() + recharge;
                player.getSettings().setRunEnergy(energy >= 100 ? 100 : energy);
                player.getActionSender().sendRunEnergy();
                player.saveAttribute("run_" + node.getId() + "_" + usingCharge, System.currentTimeMillis());
                break;
            case "low-Alchemy":
                break;
            case "cabbage-port":
                //   player.getTeleporter().send(new Location(3052, 3289, 0), Teleport.TeleportType.CABBAGE_PORT, -1);
                break;
        }
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}
