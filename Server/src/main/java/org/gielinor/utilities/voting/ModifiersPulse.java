package org.gielinor.utilities.voting;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.GlobalData;
import org.gielinor.game.world.World;
import org.gielinor.game.world.callback.CallBack;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.pulse.Pulse;

/**
 * The pulse for updating all players' modifier times.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 * @author Stan van der Bend
 */
public final class ModifiersPulse extends Pulse implements CallBack {

    /**
     * Creates the {@link ModifiersPulse} {@link Pulse}.
     */
    public ModifiersPulse() {
        super(100);
    }

    @Override
    public boolean pulse() {
        for (Player player : Repository.getPlayers()) {

            if (player == null || !player.isActive())
                continue;

            final GlobalData playerData = player.getSavedData().getGlobalData();

            parseExperienceModifiers(player, playerData);
            parseCombatStatModifiers(player, playerData);
            parseDropModifier(player, playerData);

        }
        return false;
    }

    private void parseExperienceModifiers(Player player, GlobalData playerData){

        /*
         * skill specific modifiers
         */

        for(int skillID = 0; skillID < playerData.getExperienceModifierTime(); skillID++){
            int timeLeft = playerData.getExperienceModifiersTime()[skillID];
            if(timeLeft > 0){
                if(timeLeft == 1){
                    playerData.getExperienceModifiersTime()[skillID] = -1;
                    playerData.getExperienceModifiers()[skillID] =-1;
                    player.getActionSender().sendMessage("<col=254F00>Your experience bonus for "+ Skills.SKILL_NAME[skillID]+" has run out.");
                } else if (timeLeft == 5)
                    player.getActionSender().sendMessage("<col=254F00>Your experience bonus for "+Skills.SKILL_NAME[skillID]+" will end in less than 5 minutes.");
                playerData.getExperienceModifiersTime()[skillID]--;
            }
        }

        /*
         * universal modifier
         */

        if (playerData.getExperienceModifierTime() < 1)
            return;

        if (playerData.getExperienceModifierTime() == 1) {
            playerData.setExperienceModifier(-1);
            playerData.setExperienceModifierTime(-1);
            player.getActionSender().sendMessage("<col=254F00>Your universal experience bonus has run out.");
            return;
        }

        if (playerData.getExperienceModifierTime() == 5)
            player.getActionSender().sendMessage("<col=254F00>Your universal experience bonus will end in less than 5 minutes.");

        playerData.decreasesExperienceModifierTime();
    }

    private void parseCombatStatModifiers(Player player, GlobalData playerData){

        if (playerData.getAccuracyModifierTime() < 1
            && playerData.getStrengthModifierTime() < 1
            && playerData.getDefenceModifierTime() < 1)
            return;

        if(playerData.getAccuracyModifierTime() == 1) {
            playerData.setAccuracyModifierTime(-1);
            playerData.setAccuracyModifier(-1);
            player.getActionSender().sendMessage("<col=254F00>Your increased accuracy bonus has run out.");
        } else {
            if (playerData.getAccuracyModifierTime() == 5)
                player.getActionSender().sendMessage("<col=254F00>Your increased accuracy bonus will end in less than 5 minutes.");

            playerData.setAccuracyModifierTime(playerData.getAccuracyModifierTime()-1);
        }
        if(playerData.getStrengthModifierTime() == 1) {
            playerData.setStrengthModifierTime(-1);
            playerData.setStrengthModifier(-1);
            player.getActionSender().sendMessage("<col=254F00>Your increased strength bonus has run out.");
        } else {
            if (playerData.getStrengthModifierTime() == 5)
                player.getActionSender().sendMessage("<col=254F00>Your increased strength bonus will end in less than 5 minutes.");

            playerData.setStrengthModifierTime(playerData.getStrengthModifierTime()-1);
        }
        if(playerData.getDefenceModifierTime() == 1) {
            playerData.setDefenceModifierTime(-1);
            playerData.setDefenceModifier(-1);
            player.getActionSender().sendMessage("<col=254F00>Your increased defence bonus has run out.");
        } else {
            if (playerData.getDefenceModifierTime() == 5)
                player.getActionSender().sendMessage("<col=254F00>Your increased defence bonus will end in less than 5 minutes.");

            playerData.setDefenceModifierTime(playerData.getDefenceModifierTime()-1);
        }
    }
    private void parseDropModifier(Player player, GlobalData playerData){
        if (playerData.getDropModifierTime() < 1)
            return;

        if (playerData.getDropModifierTime() == 1) {
            playerData.setDropModifier(-1);
            playerData.setDropModifierTime(-1);
            player.getActionSender().sendMessage("<col=254F00>Your increased drop-rate bonus has run out.");
            return;
        }

        if (playerData.getDropModifierTime() == 5)
            player.getActionSender().sendMessage("<col=254F00>Your increased drop-rate bonus will end in less than 5 minutes.");

        playerData.setDropModifierTime(playerData.getDropModifierTime()-1);
    }

    @Override
    public boolean call() {
        World.submit(this);
        return true;
    }

}
