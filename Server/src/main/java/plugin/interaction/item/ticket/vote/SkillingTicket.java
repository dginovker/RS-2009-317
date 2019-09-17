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
public class SkillingTicket extends TicketPlugin{

    private final int[] UN_AFFECTED_SKILLS = new int[]{Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH, Skills.RANGE, Skills.MAGIC, Skills.PRAYER, Skills.HITPOINTS};
    private final int DURATION_IN_MINUTES = 60;
    private final double EXPERIENCE_MODIFIER = 1.5D;

    public SkillingTicket(int itemID) {
        super(itemID);
    }

    @Override
    protected void onClaim(Player player) {
        for (int skillID = 0; skillID < player.getSavedData().getGlobalData().getExperienceModifiersTime().length; skillID++) {
            for(int unAffectedSkillID : UN_AFFECTED_SKILLS){
                if(skillID != unAffectedSkillID){
                    player.getSavedData().getGlobalData().getExperienceModifiersTime()[skillID] = DURATION_IN_MINUTES;
                    player.getSavedData().getGlobalData().getExperienceModifiers()[skillID] = EXPERIENCE_MODIFIER;
                }
            }
        }
        player.getDialogueInterpreter().sendDialogue(
            "You have been granted "+DURATION_IN_MINUTES+" minutes of increased experience.",
            " ("+EXPERIENCE_MODIFIER+"x for all non-combat skills)");
    }

    @Override
    protected boolean canClaim(Player player) {
        if(player.getSavedData().getGlobalData().hasAnyExperienceModifier()){
            if (!player.getSavedData().getGlobalData().hasAnyExperienceModifier(UN_AFFECTED_SKILLS)) {
                player.getActionSender().sendMessage("You already have one or multiple active experience modifier(s)!");
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean deleteAfterClaim(Player player) {
        return true;
    }
}
