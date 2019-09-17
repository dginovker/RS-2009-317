package plugin.skill.summoning.familiar;


import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.player.FaceLocationFlag;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the class used for handling the npc options of familiars.
 *
 * @author Emperor
 * @author 'Vexia
 * @version 2.0
 */
public final class FamiliarNPCOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.setOptionHandler("pick-up", this);
        NPCDefinition.setOptionHandler("interact-with", this);
        NPCDefinition.setOptionHandler("interact", this);
        NPCDefinition.setOptionHandler("store", this);
        NPCDefinition.setOptionHandler("withdraw", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (!(node instanceof Familiar)) {
            return false;
        }
        Familiar familiar = (Familiar) node;
        if (familiar.getOwner() != player) {
            player.getActionSender().sendMessage("This is not your familiar.");
            return true;
        }
        switch (option) {
            case "pick-up":
                player.faceLocation(FaceLocationFlag.getFaceLocation(player, familiar));
                player.getFamiliarManager().pickup();
                break;
            case "interact-with":
                player.getDialogueInterpreter().open(343823);
                break;
            case "interact":
                player.getDialogueInterpreter().open(node.getId(), node);
                break;
            case "store":
            case "withdraw":
                if (!familiar.isBurdenBeast()) {
                    player.getActionSender().sendMessage("This is not a beast of burden.");
                    break;
                }
                ((BurdenBeast) familiar).openInterface();
                break;
        }
        return true;
    }

}
