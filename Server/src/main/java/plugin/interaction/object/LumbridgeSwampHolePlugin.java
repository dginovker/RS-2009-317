package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.global.action.ClimbActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to handle the swamp hole.
 * @author 'Vexia
 * @version 1.0
 */
public final class LumbridgeSwampHolePlugin extends OptionHandler {

    /**
     * Represents the top location.
     */
    private static final Location TOP = Location.create(3169, 3173, 0);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(10375).getConfigurations().put("option:take", this);
        ObjectDefinition.forId(5947).getConfigurations().put("option:climb-down", this);
        ObjectDefinition.forId(5946).getConfigurations().put("option:climb", this);
        ObjectDefinition.forId(15566).getConfigurations().put("option:read", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "climb-down":
                if (!player.getSavedData().getGlobalData().hasTiedLumbridgeRope()) {
                    player.getDialogueInterpreter().open(70099, "There is a sheer drop below the hole. You will need a rope.");
                    return true;
                } else {
                    ClimbActionHandler.climb(player, new Animation(827), Location.create(3168, 9572, 0));
                }
            case "climb":
                player.getProperties().setTeleportLocation(TOP);
                break;
            case "take":
                if (player.getInventory().freeSlots() < 2) {
                    player.getActionSender().sendMessage("You do not have enough inventory space.");
                    return true;
                }
                if (player.getInventory().add(new Item(5341), new Item(952))) {
                    ObjectBuilder.replace(((GameObject) node), ((GameObject) node).transform(10373), 300);
                }
                break;
            case "read":
                player.getActionSender().sendString498("<col=8A0808>~-~-~ WARNING ~-~-~", 220, 5);
                player.getActionSender().sendString498("<col=8A0808>Noxious gases vent into this cave.", 220, 7);
                player.getActionSender().sendString498("<col=8A0808>Naked flames may cause an explosion!", 220, 8);
                player.getActionSender().sendString498("<col=8A0808>Beware of vicious head-grabbing beasts!", 220, 10);
                player.getActionSender().sendString498("<col=8A0808>Contact a Slayer master for protective headgear.", 220, 11);
                player.getInterfaceState().open(new Component(220));
                break;
        }
        return true;
    }

}
