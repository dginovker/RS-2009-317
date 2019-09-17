package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.crafting.GlassProduct;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents the glass making interface plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GlassInterface extends ComponentPlugin {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(884);

    /**
     * Represents the molten glass item.
     */
    private static final Item MOLTEN_GLASS = new Item(1775);

    /**
     * Represents the soda ash item.
     */
    private static final Item SODA_ASH = new Item(1781, 1);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(11462, this);
        return this;
    }

    @Override
    public boolean handle(final Player p, Component component, int opcode, int button, int slot, int itemId) {
        final GlassProduct glassProduct = GlassProduct.forId(button);
        if (glassProduct == null) {
            return true;
        }
        if (!p.getInventory().contains(1785, 1)) {
            p.getActionSender().sendMessage("You need a glassblowing pipe to do this.");
            return true;
        }
        if (!p.getInventory().contains(1775, 1)) {
            p.getActionSender().sendMessage("You need molten glass to do this.");
            return true;
        }
        if (p.getSkills().getLevel(Skills.CRAFTING) < glassProduct.getLevel()) {
            p.getActionSender().sendMessage("You need a crafting level of " + glassProduct.getLevel() + " to make this.");
            return true;
        }
        int amount = glassProduct.getOptionSelect().amountForId(button);
        if (amount == -1) {
            p.setAttribute("runscript", new RunScript() {

                @Override
                public boolean handle() {
                    int amount = (int) value;
                    make(p, glassProduct, amount);
                    return true;
                }
            });
            p.getDialogueInterpreter().sendInput(false, "Enter the amount.");
        }
        make(p, glassProduct, amount);
        return true;
    }

    /**
     * Method used to make a glass product.
     *
     * @param player       the player.
     * @param glassProduct the glass.
     * @param amount       the amount.
     */
    public static void make(final Player player, final GlassProduct glassProduct, final int amount) {
        player.getInterfaceState().close();
        player.animate(ANIMATION);
        World.submit(new Pulse(2, player) {

            int amt = amount;

            @Override
            public boolean pulse() {
                if (!player.getInventory().contains(1775, 1) || amt == 0) {
                    return true;
                }
                player.animate(ANIMATION);
                player.getInventory().remove(MOLTEN_GLASS);
                player.getInventory().remove(SODA_ASH);
                player.getInventory().add(new Item(glassProduct.getProduct(), 1));
                player.getSkills().addExperience(Skills.CRAFTING, glassProduct.getExperience());
                player.getActionSender().sendMessage("You make a " + new Item(glassProduct.getProduct()).getName().toLowerCase().replace("unpowered", "").trim() + ".");
                amt--;
                return false;
            }

        });
    }
}
