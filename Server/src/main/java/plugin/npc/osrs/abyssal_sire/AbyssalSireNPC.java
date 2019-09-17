package plugin.npc.osrs.abyssal_sire;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.handlers.MultiSwingHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 * Created by Stan van der Bend on 11/01/2018.
 * project: GielinorGS
 * package: plugin.npc.osrs.abyssal_sire
 */
public class AbyssalSireNPC extends NPC{

    private final AbyssalSireCombat abyssalSireCombat = new AbyssalSireCombat();

    public AbyssalSireNPC() {
        super(5886, Location.create(3372, 3891, 0));
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return abyssalSireCombat;
    }

}
class AbyssalSireCombat extends MultiSwingHandler {

    private final static Graphics DRAIN_GRAPHIC = Graphics.create(1176);
    private final static String DRAIN_MESSAGE = "The sire has regained some health!";
    private final static double DRAIN_PERCENTILE = 0.9D;

    private void drainLifePointsOf(Player target){
        target.getSkills().drainLevel(Skills.HITPOINTS, DRAIN_PERCENTILE, DRAIN_PERCENTILE);
        target.getActionSender().sendMessage(DRAIN_MESSAGE);
        target.graphics(DRAIN_GRAPHIC);
    }



}
