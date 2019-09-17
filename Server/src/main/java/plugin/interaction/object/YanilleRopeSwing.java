package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class YanilleRopeSwing extends OptionHandler {

    /**
     * The rope delay.
     */
    private static int ropeDelay;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2322).getConfigurations().put("option:swing-on", this);
        return null;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (ropeDelay > World.getTicks()) {
            player.getActionSender().sendMessage("The rope is being used.");
            return true;
        }
        ropeDelay = World.getTicks() + 2;
        AgilityHandler.forceWalk(player, 0, player.getLocation(), Location.create(2505, 3087, 0), Animation.create(751), 17, 4, "You skillfully swing across.", 1);
        World.submit(new Pulse(0, player) {

            @Override
            public boolean pulse() {
                player.getActionSender().sendObjectAnimation((GameObject) node, Animation.create(497), true);
                return true;
            }
        });
        return true;
    }

}
