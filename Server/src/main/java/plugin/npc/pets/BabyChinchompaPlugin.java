package plugin.npc.pets;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.skill.member.summoning.pet.Pet;
import org.gielinor.game.content.skill.member.summoning.pet.Pets;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;
import java.util.Arrays;
import java.util.List;

/**
 * @author Erik
 */
public class BabyChinchompaPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (!"metamorphosis".equals(option)) {
            return false;
        }

        Pet familiar;

        if (node instanceof Pet) {
            familiar = (Pet) node;
            if (familiar.getOwner() != player) {
                player.getActionSender().sendMessage("It's not yours!");
                return true;
            }
        } else {
            return false;
        }

        final int id = familiar.getId();

        // Before anything else we try to metamorph into the yellow one
        if (id != 26759 && (RandomUtil.random(512) == 0 || player.getInventory().contains(Item.ROTTEN_POTATO))) {
            metamorphosis(player, familiar, Pets.GOLD_CHINCHOMPA);
            return true;
        }

        // Need to figure out what colors are available to metamorph into
        List<Pets> available;
        switch (id) {
            case 26756:
                available = Arrays.asList(Pets.GREY_CHINCHOMPA, Pets.BLACK_CHINCHOMPA);
                break;
            case 26757:
                available = Arrays.asList(Pets.RED_CHINCHOMPA, Pets.BLACK_CHINCHOMPA);
                break;
            case 26758:
                available = Arrays.asList(Pets.RED_CHINCHOMPA, Pets.GREY_CHINCHOMPA);
                break;
            case 26759:
                available = Arrays.asList(Pets.RED_CHINCHOMPA, Pets.GREY_CHINCHOMPA, Pets.BLACK_CHINCHOMPA);
                break;
            default:
                throw new AssertionError("Invalid chinchompa: " + id);
        }

        // TODO need to make it give you the correct one on pick-up after this.
        Pets morph = RandomUtil.random(available);
        metamorphosis(player, familiar, morph);
        return true;
    }

    private void metamorphosis(Player player, Pet familiar, Pets pet) {
        int itemId = pet.getItemId(0);
        int npcId = pet.getNpcId(0);
        familiar.clear();
        Pet morphed = new Pet(player, familiar.getDetails(), itemId, npcId);
        player.getFamiliarManager().setFamiliar(morphed);
        player.getFamiliarManager().spawnFamiliar(false);

        if (pet == Pets.GOLD_CHINCHOMPA)
            morphed.sendChat("Squeaka squeaka!");
        else if (RandomUtil.random(16) == 0)
            morphed.sendChat("Squeak squeak!");
    }

    @Override
    public Plugin<Object> newInstance(Object arg) {
        NPCDefinition.forId(26756).getConfigurations().put("option:metamorphosis", this);
        NPCDefinition.forId(26757).getConfigurations().put("option:metamorphosis", this);
        NPCDefinition.forId(26758).getConfigurations().put("option:metamorphosis", this);
        NPCDefinition.forId(26759).getConfigurations().put("option:metamorphosis", this);
        return this;
    }

}
