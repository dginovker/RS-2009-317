package plugin.interaction.item.ticket.vote;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import plugin.interaction.item.ticket.TicketPlugin;

/**
 * Created by Stan van der Bend on 05/02/2018.
 *
 * TODO: map an ID to this.
 *
 * task: https://www.meistertask.com/app/task/cOWITe9h/vote-tickets
 * project: Gielinor-Server
 * package: plugin.interaction.item.ticket.vote
 */
public class CombatTicket extends TicketPlugin{

    private final int[] AFFECTED_SKILLS = new int[]{Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH, Skills.RANGE, Skills.MAGIC, Skills.PRAYER, Skills.HITPOINTS};
    private final int DURATION_IN_MINUTES = 30;
    private final double EXPERIENCE_MODIFIER = 1.5D;

    public CombatTicket(int itemID) {
        super(itemID);
    }

    @Override
    protected void onClaim(Player player) {
        for (int skillID = 0; skillID < player.getSavedData().getGlobalData().getExperienceModifiersTime().length; skillID++) {
            for(int affectedSkillID : AFFECTED_SKILLS){
                if(skillID == affectedSkillID){
                    player.getSavedData().getGlobalData().getExperienceModifiersTime()[skillID] = DURATION_IN_MINUTES;
                    player.getSavedData().getGlobalData().getExperienceModifiers()[skillID] = EXPERIENCE_MODIFIER;
                }
            }
        }
        player.getDialogueInterpreter().sendDialogue(
            "You have been granted "+DURATION_IN_MINUTES+" minutes of increased experience.",
            " ("+EXPERIENCE_MODIFIER+"x for all combat skills)");
    }

    @Override
    protected boolean canClaim(Player player) {
        if(player.getSavedData().getGlobalData().hasAnyExperienceModifier(AFFECTED_SKILLS)){
            player.getActionSender().sendMessage("You already have one or multiple active experience modifier(s)!");
            return false;
        }
        return true;
    }

    @Override
    protected boolean deleteAfterClaim(Player player) {
        return true;
    }
}
