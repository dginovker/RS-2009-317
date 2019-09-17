package plugin.activity.zulrah;

import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;

/**
 * Represents the {@link org.gielinor.game.node.entity.combat.CombatSwingHandler} for the Zulrah NPC.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public abstract class ZulrahCombatSwingHandler extends CombatSwingHandler {

    /**
     * The {@link plugin.activity.zulrah.ZulrahNPC}.
     */
    private ZulrahNPC zulrahNPC;

    /**
     * Constructs a new <code>ZulrahCombatSwingHandler</code>.
     */
    public ZulrahCombatSwingHandler(CombatStyle combatStyle) {
        super(combatStyle);
    }

    /**
     * Gets the <code>ZulrahNPC</code>.
     *
     * @return The Zulrah npc.
     */
    public ZulrahNPC getZulrahNPC() {
        return zulrahNPC;
    }

    /**
     * Sets the <code>ZulrahNPC</code>.
     *
     * @param zulrahNPC The Zulrah npc.
     */
    public void setZulrahNPC(ZulrahNPC zulrahNPC) {
        this.zulrahNPC = zulrahNPC;
    }
}
