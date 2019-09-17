package plugin.skill.herblore;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.member.herblore.GrindingItem;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the plugin used to handle the grinding of an item.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GrindItemPlugin extends UseWithHandler {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(364);

    /**
     * Constructs a new {@code GrindItemPlugin} {@code Object}.
     */
    public GrindItemPlugin() {
        super(233);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (GrindingItem grind : GrindingItem.values()) {
            for (Item i : grind.getItems()) {
                addHandler(i.getId(), ITEM_TYPE, this);
            }
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        Player player = event.getPlayer();
        final Item itemToGrind = event.getUsedItem().getId() == 233 ? event.getBaseItem() : event.getUsedItem();
        final GrindingItem grind = GrindingItem.forItem(itemToGrind);
        SkillDialogueHandler handler = new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION, grind.getProduct()) {

            @Override
            public void create(int amount, int index) {
                player.getPulseManager().run(new Pulse(2, player) {

                    int count = 0;

                    @Override
                    public boolean pulse() {
                        Item remove = itemToGrind.getCount() > 1 ? new Item(itemToGrind.getId(), 1) : itemToGrind;
                        if (event.getPlayer().getInventory().remove(remove)) {
                            event.getPlayer().animate(ANIMATION);
                            event.getPlayer().getInventory().add(grind.getProduct());
                            event.getPlayer().getActionSender().sendMessage(grind.getMessage(), 1);
                            return (++count == amount);
                        }
                        return true;
                    }
                });
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(itemToGrind);
            }

        };
        if (player.getInventory().getCount(itemToGrind) == 1) {
            handler.create(1, 0);
        } else {
            handler.open();
        }
        return true;
    }
}
