package plugin.skill.fletching;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.fletching.items.grapple.GrapplePulse;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The plugin for creating a mith grapple.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class MithGrapplePlugin extends UseWithHandler {

    /**
     * Represents the rope grapple.
     */
    private static final Item ROPE_GRAPPLE = new Item(9419);

    /**
     * Represents the mith grapple.
     */
    private static final Item MITH_GRAPPLE = new Item(9418);

    /**
     * Represents the rope item.
     */
    private static final Item ROPE = new Item(954);

    /**
     * Constructs a new {@code MithGrapplePlugin} {@code Object}.
     */
    public MithGrapplePlugin() {
        super(9416, 954);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(9142, ITEM_TYPE, this);
        addHandler(9418, ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        final Item first = event.getUsedItem();
        final Item second = event.getBaseItem();
        if (first.getId() == 9418 && second.getId() != 954) {
            return false;
        }
        if (first.getId() == 9418) {
            if (player.getInventory().remove(ROPE) && player.getInventory().remove(MITH_GRAPPLE)) {
                player.getInventory().add(ROPE_GRAPPLE);
            }
            return true;
        }
        if (player.getSkills().getLevel(Skills.FLETCHING) < 59) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a Fletching level of 59 in order to do that.");
            return true;
        }
        SkillDialogueHandler skillDialogueHandler = new SkillDialogueHandler(player,
            SkillDialogueHandler.SkillDialogue.ONE_OPTION, MITH_GRAPPLE) {

            @Override
            public void create(final int amount, final int index) {
                player.getPulseManager().run(new GrapplePulse(player, first, amount));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(first);
            }

        };
        if (player.getInventory().getCount(new Item(9416)) > 1 && player.getInventory().getCount(new Item(9142)) > 1) {
            skillDialogueHandler.open();
            return true;
        }
        skillDialogueHandler.create(1, 0);
        return true;
    }

}
