package plugin.skill.runecrafting;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.free.runecrafting.Altar;
import org.gielinor.game.content.skill.free.runecrafting.EnchantTiaraPulse;
import org.gielinor.game.content.skill.free.runecrafting.Talisman;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The plugin for enchanting a tiara.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class EnchantTiaraPlugin extends UseWithHandler {

    /**
     * Constructs a new {@code EnchantTiaraPlugin} {@code Object}.
     */
    public EnchantTiaraPlugin() {
        super(1438, 1448, 1444, 1440, 1442, 5516, 1446, 1454, 1452, 1462, 1458, 1456, 1450, 1460);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (Altar altar : Altar.values()) {
            addHandler(altar.getObject(), OBJECT_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        Player player = event.getPlayer();
        final Altar altar = Altar.forObject(((GameObject) event.getUsedWith()));
        if (altar.getTalisman() != Talisman.forItem(event.getUsedItem())) {
            return true;
        }
        new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION, Talisman.forItem(event.getUsedItem()).getTiara().getTiara()) {

            @Override
            public void create(int amount, int index) {
                player.getPulseManager().run(new EnchantTiaraPulse(player, event.getUsedItem(), (Talisman.forItem(event.getUsedItem()).getTiara()), amount));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(event.getUsedItem());
            }

        }.open();
        return true;
    }

}
