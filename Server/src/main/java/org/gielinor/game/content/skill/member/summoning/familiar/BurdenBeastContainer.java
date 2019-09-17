package org.gielinor.game.content.skill.member.summoning.familiar;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.model.container.Container;

/**
 * Represents the beast of burden container.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BurdenBeastContainer extends Container {

    /**
     * The listener.
     */
    private final BurdenContainerListener listener;

    /**
     * Constructs a new <code>BurdenBeastContainer</code>.
     */
    public BurdenBeastContainer(Player player) {
        super(30);
        super.register(listener = new BurdenContainerListener(player));
    }

    /**
     * Closes the price guide component.
     */
    public void close() {
//        player.removeExtension(LogoutTask.class);
//        player.getInventory().addAll(this);
//        player.getInventory().getListeners().remove(listener);
//        player.getInterfaceState().closeSingleTab();
    }

}
