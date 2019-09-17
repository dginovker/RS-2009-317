package plugin.interaction.item.withobject;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles using a rotten potato on an object.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class RottenPotatoObjectPlugin extends UseWithHandler {

    /**
     *
     */
    private boolean walks;

    /**
     * Constructs a new {@code RottenPotatoNPCPlugin}.
     */
    public RottenPotatoObjectPlugin() {
        super(5733);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (ObjectDefinition objectDefinition : ObjectDefinition.getDefinitions().values()) {
            addHandler(objectDefinition.getId(), OBJECT_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        if (event.getUsed().getId() != 5733) {
            return false;
        }
        GameObject gameObject = (GameObject) event.getUsedWith();
        Player player = event.getPlayer();
        player.setAttribute("REMOVE_OBJECT", new int[]{ gameObject.getId(), gameObject.getLocation().getX(), gameObject.getLocation().getY() });
        player.getDialogueInterpreter().open("RottenPotato");
        return true;
    }

    @Override
    public boolean isWalks() {
        return true;
    }
}
