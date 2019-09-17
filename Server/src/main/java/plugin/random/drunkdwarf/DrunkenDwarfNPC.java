package plugin.random.drunkdwarf;

import org.gielinor.game.content.anticheat.AntiMacroEvent;
import org.gielinor.game.content.anticheat.AntiMacroNPC;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.world.map.Location;

/**
 * Handles the drunken dwarf npc.
 *
 * @author Vexia
 */
public final class DrunkenDwarfNPC extends AntiMacroNPC {

    /**
     * The quotes that the dwarf will say.
     */
    private static final String[] QUOTES = new String[]{
        "'Ere, matey, 'ave some 'o the good stuff.",
        "Dun ignore your matey!",
        "I hates you @name!",
        "Aww comeon, talk to ikle me @name!"
    };

    /**
     * Constructs a new {@code DrunkenDwarfNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     * @param event
     * @param player
     */
    public DrunkenDwarfNPC(int id, Location location, AntiMacroEvent event, Player player) {
        super(id, location, event, player, QUOTES);
    }

    public DrunkenDwarfNPC() {
        super(0, null, null, null);
    }

    @Override
    public void init() {
        super.init();
        player.getAudioManager().send(new Audio(344));
    }

    @Override
    public void handleTickActions() {
        super.handleTickActions();
        if (timeUp && getDialoguePlayer() == null) {
            if (!getProperties().getCombatPulse().isAttacking()) {
                getProperties().getCombatPulse().attack(player);
            }
        }
    }

    @Override
    public void handleTimeUp() {
    }

    @Override
    public boolean isIgnoreAttackRestrictions(Entity victim) {
        return true;
    }

    @Override
    public boolean isIgnoreMultiBoundaries(Entity victim) {
        return victim == player;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 956 };
    }

}
