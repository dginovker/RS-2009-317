package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.CameraContext;
import org.gielinor.net.packet.context.CameraContext.CameraType;
import org.gielinor.net.packet.out.CameraViewPacket;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the reading of a sign post.
 *
 * @author 'Vexia
 */
public class ReadSignPostPlugin extends OptionHandler {

    /**
     * Represents the areas on the sign.
     *
     * @author 'Vexia
     */
    public enum Signs {
        NEAR_LUMBRIDGE(18493, "North to farms and<br> Varrock.", "The River Lum lies to<br> the south.", "West to<br>Lumbridge.", "East to Al<br>Kharid - toll<br>gate; bring some<br>money."),
        NEAR_VARROCK(24263, "Varrock", "Lumbridge", "Draynor Manor", "Dig Site");

        public static Signs forId(int id) {
            for (Signs sign : Signs.values()) {
                if (sign == null) {
                    continue;
                }
                if (sign.object == id) {
                    return sign;
                }
            }
            return null;
        }

        /**
         * The object id.
         */
        private int object;

        /**
         * The directions.
         */
        private String directions[];

        /**
         * Constructs a new {@code ReadSignPostPlugin.java} {@code Object}.
         *
         * @param object     the object.
         * @param directions the directions.
         */
        Signs(int object, String... directions) {
            this.object = object;
            this.directions = directions;
        }
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (player.getLocation().getDistance(node.getLocation()) > 5) {
            player.faceLocation(node.getLocation());
            player.getActionSender().sendMessage("You're too far away to make out the sign. Get closer.");
            return true;
        }
        player.getInterfaceState().open(new Component(46950));
        player.faceLocation(player.getLocation().getNorth());
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.RESET, 0, 0, 0, 0, 0));
        final GameObject object = (GameObject) node;
        Signs sign = Signs.forId(object.getId());
        if (sign == null) {
            return false;
        }
        player.getActionSender().sendString(46957, sign.directions[0]);
        player.getActionSender().sendString(46958, sign.directions[1]);
        player.getActionSender().sendString(46960, sign.directions[2]);
        player.getActionSender().sendString(46959, sign.directions[3]);
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2366).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(2367).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(2368).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(2369).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(2370).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(2371).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(4132).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(4133).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(4134).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(4135).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(5164).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(10090).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(13873).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(15522).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(18493).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(24263).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(25397).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(30039).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(30040).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(31296).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(31297).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(31298).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(31299).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(31300).getConfigurations().put("option:read", this);
        ObjectDefinition.forId(31301).getConfigurations().put("option:read", this);
        return this;
    }

    @Override
    public boolean isWalk() {
        return false;
    }
}
