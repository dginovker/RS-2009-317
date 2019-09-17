package org.gielinor.game.system.script.context;

import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.system.script.ScriptContext;

/**
 * Handles the opening of a shop instruction.
 * @author 'Vexia
 *
 */
public class ShopInstruction extends ScriptContext {

    /**
     * The id to open.
     */
    private int id;

    /**
     * Constructs a new {@code ShopInstruction} {@code Object}.
     */
    public ShopInstruction() {
        this(-1);
    }

    /**
     * Constructs a new {@code ShopInstruction} {@code Object}.
     * @param id the id.
     */
    public ShopInstruction(int id) {
        super("openshop");
        this.setInstant(false);
        this.id = id;
    }

    @Override
    public boolean execute(Object... args) {
        Player player = (Player) args[0];
        Shops.forId(id).open(player);
        return true;
    }

    @Override
    public ScriptContext parse(Object... params) {
        int id = -1;
        if (params[0] instanceof Integer) {
            id = (Integer) params[0];
        }
        ShopInstruction context = new ShopInstruction(id);
        context.parameters = params;
        return context;
    }

}
