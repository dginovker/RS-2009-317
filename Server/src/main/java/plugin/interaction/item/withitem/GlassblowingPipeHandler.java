package plugin.interaction.item.withitem;

import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The {@link org.gielinor.game.interaction.UseWithHandler} for the Glassblow pipe.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GlassblowingPipeHandler extends UseWithHandler {

    /**
     * Constructs a new {@code MakeGlassInterfacePlugin}.
     */
    public GlassblowingPipeHandler() {
        super(1775);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(1785, ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(final NodeUsageEvent event) {
        event.getPlayer().getInterfaceState().open(new Component(11462));
        return true;
    }

}
