package plugin.interaction.item.withobject;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.dialogue.SkillDialogueHandler.SkillDialogue;
import org.gielinor.game.content.skill.free.prayer.OfferBonesPulse;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

import plugin.skill.prayer.BoneBuryingOptionPlugin.Bones;

/**
 * Represents the {@link org.gielinor.game.interaction.UseWithHandler} for using bones on altars for experience.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class OfferBonesPlugin extends UseWithHandler {

    /**
     * The ids of altars.
     */
    private static final int[] ALTAR_IDS = new int[]{
        61, 409, 410, 411, 412, 2640, 3521, 4008,
        8749, 10639, 10640, 13179, 13180, 13181, 13182,
        13183, 13184, 13185, 13186, 13187, 13188, 13189,
        13190, 13191, 13192, 13193, 13194, 13195, 13196,
        13197, 13198, 13199, 18254, 19145, 20377, 20378,
        24343, 26286, 26287, 26288, 26289, 27306, 27338,
        27339, 27661, 28698, 30726, 32079, 34616, 36972,
        37630, 37985, 37990
    };

    /**
     * Constructs a new {@code OfferBonesPlugin} {@code Object}.
     */
    public OfferBonesPlugin() {
        super(526, 2859, 528, 3183, 3179, 530, 532, 3125, 4812, 3123, 534, 6812, 536, 4830, 4832, 6729, 4834);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int id : ALTAR_IDS) {
            addHandler(id, OBJECT_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        final Bones bones = Bones.forId(event.getUsedItem().getId());
        if (bones == null) {
            return false;
        }
        if (!(event.getUsedWith() instanceof GameObject)) {
            return false;
        }
        SkillDialogueHandler skillDialogueHandler = new SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, event.getUsedItem()) {

            @Override
            public void create(final int amount, int index) {
                player.getPulseManager().run(new OfferBonesPulse(player, event.getUsedItem(), event.getUsedWith().asObject(), amount, bones));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(event.getUsedItem());
            }

        };
        if (player.getInventory().getCount(event.getUsedItem()) == 1) {
            skillDialogueHandler.create(0, 1);
        } else {
            skillDialogueHandler.open();
        }
        return true;
    }

}
