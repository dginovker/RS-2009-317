package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.plugin.Plugin;

import plugin.interaction.inter.EmoteTabInterface.Emote;

/**
 * Represents the Rubber chicken option handler.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class RubberChickenPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(4566).getConfigurations().put("option:operate", this);
        ItemDefinition.forId(4566).getConfigurations().put("option:dance", this);
        new RubberChickenEquipPlugin().newInstance(arg);
        WHACK_OPTION_HANDLER.newInstance(arg);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (System.currentTimeMillis() < player.getAttribute("emote_end", 0L)) {
            player.getActionSender().sendMessage("You're already doing an emote!");
            return true;
        }
        if (player.getProperties().getCombatPulse().isAttacking() || player.inCombat()) {
            player.getActionSender().sendMessage("You can't perform an emote while being in combat.");
            return true;
        }
        player.getPulseManager().clear();
        Emote emote = Emote.CHICKEN_DANCE;
        player.setAttribute("emote_end", (long) (System.currentTimeMillis() + 4000));
        Item item = player.getEquipment().get(Equipment.SLOT_WEAPON);
        if (item != null) {
            switch (item.getId()) {
                case 4084:
                    player.getActionSender().sendAnimation(1483);
                    return true;
            }
        }

        if (emote.play(player)) {
            return true;
        }
        player.removeAttribute("emote_end");//Rigging this for now...
        return false;
    }

    public static OptionHandler WHACK_OPTION_HANDLER = new OptionHandler() {

        @Override
        public boolean handle(Player player, Node node, String option) {
            player.getPulseManager().clear("interaction:attack:" + node.hashCode());
            if (!(node instanceof Player)) {
                return false;
            }
            Player target = (Player) node;
            if (target.inCombat()) {
                return true;
            }
            player.lock(1);
            player.face(target);
            player.playAnimation(new Animation(1833));
            return true;
        }

        @Override
        public boolean isWalk() {
            return true;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            return this;
        }

        @Override
        public boolean isDelayed(Player player) {
            return false;
        }

    };

    public static class RubberChickenEquipPlugin implements Plugin<Object> {

        /**
         * The whack option.
         */
        public static final Option WHACK = new Option("Whack", 1).setHandler(WHACK_OPTION_HANDLER);

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ItemDefinition.forId(4566).getConfigurations().put("equipment", this);
            return this;
        }

        @Override
        public Object fireEvent(String identifier, Object... args) {
            final Player player = (Player) args[0];
            switch (identifier) {
                case "equip":
                    player.getInteraction().set(WHACK);
                    break;
                case "unequip":
                    if (player.getInteraction().get(WHACK.getIndex()) == WHACK) {
                        player.getInteraction().remove(WHACK);
                    }
                    break;
            }
            return true;
        }

    }

}