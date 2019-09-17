package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.global.action.ClimbActionHandler;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used for managing ladders.
 *
 * @author Emperor
 * @version 2.0
 */
public final class LadderManagingPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.setOptionHandler("climb-up", this);
        ObjectDefinition.setOptionHandler("climb-down", this);
        ObjectDefinition.setOptionHandler("climb", this);
        ObjectDefinition.setOptionHandler("climb up", this);
        ObjectDefinition.setOptionHandler("climb down", this);
        return this;
    }


    @Override
    public boolean handle(final Player player, Node node, final String option) {
        if (node.getId() == 5264) {
            return false;
        }
        /**
         * Games room stairs up to castle.
         */
        if (node.getId() == 4627 && node.getLocation().equals(2205, 4935, 1)) {
            ClimbActionHandler.climb(player, null, Location.create(2899, 3565, 0));
            return true;
        }
        /**
         * Observatory (castle wars) dungeon stairs up.
         */
        if (node.getId() == 25429 && node.getLocation().equals(2355, 9395, 0)) {
            ClimbActionHandler.climb(player, null, Location.create(2458, 3185, 0));
            return true;
        }
        /**
         * Observatory (castle wars) dungeon stairs down.
         */
        if (node.getId() == 25432 && node.getLocation().equals(2458, 3186, 0)) {
            openComponent(player, Location.create(2355, 9394, 0), "Warning! Low level players beware. Goblins have taken<br>over and there are poisonous spiders!", "Proceed regardless", "Stay out");
            return true;
        }
        /**
         * Observatory final dungeon ladder up.
         */
        if (node.getId() == 25429 && node.getLocation().equals(2335, 9351, 0)) {
            ClimbActionHandler.climb(player, null, Location.create(2439, 3164, 0));
            return true;
        }
        /**
         * Observatory final dungeon ladder down.
         */
//        if (node.getId() == 25434 && node.getLocation().equals(2438, 3164, 0)) {
//            ClimbActionHandler.climb(player, null, Location.create(2335, 9350, 0));
//            return true;
//        }
        // DOWN = 2335, 9350, 0
        /**
         * Smoke dungeon well.
         */
        if (node.getId() == 6279 && node.getLocation().equals(Location.create(3359, 2971, 0))) {
            if (player.getSkills().getStaticLevel(Skills.SLAYER) < 35) {
                player.getDialogueInterpreter().sendPlaneMessage("You need a Slayer level of at least 35 to go down this well.");
                return true;
            }
            ClimbActionHandler.climb(player, null, Location.create(3206, 9379, 0));
            return true;
        }
        /**
         * Smoke dungeon rope.
         */
        if (node.getId() == 6439 && node.getLocation().equals(Location.create(3205, 9379, 0))) {
            ClimbActionHandler.climb(player, null, Location.create(3359, 2970, 0));
            return true;
        }
        /**
         * Boots of lightness, climb up.
         */
        if (node.getId() == 96) {
            ClimbActionHandler.climb(player, null, Location.create(2649, 9804, 0));
            return true;
        }
        /**
         * Boots of lightness, climb down.
         */
        if (node.getId() == 35121) {
            ClimbActionHandler.climb(player, null, Location.create(2641, 9763, 0));
            return true;
        }
        /**
         * Barbarian Assault ladder. Down
         */
        if (player.getLocation().getX() == 2533 && player.getLocation().getY() == 3572) {
            ClimbActionHandler.climb(player, null, Location.create(1877, 5457, 0));
            return true;
        }
        /**
         * Barbarian Assault ladder. Up
         */
        if (player.getLocation().getX() == 1877 && player.getLocation().getY() == 5457) {
            ClimbActionHandler.climb(player, null, Location.create(2533, 3572, 0));
            return true;
        }
        /**
         * Warriors' guild stairs up.
         */
        if (node.getId() == 1738 && node.getLocation().equals(2839, 3537, 0)) {
            ClimbActionHandler.climb(player, null, Location.create(2841, 3538, 1));
            return true;
        }
        if (node.getId() == 1739 && node.getLocation().equals(2839, 3571, 1)) {
            switch (option) {
                case "climb-up":
                    ClimbActionHandler.climb(player, null, Location.create(2840, 3539, 2));
                    return true;

                case "climb-down":
                    ClimbActionHandler.climb(player, null, Location.create(2841, 3538, 0));
                    return true;
            }
        }
        if (node.getId() == 1754) {
            /**
             * Heroes Guild.
             */
            if (node.getLocation().getX() == 2892 && node.getLocation().getY() == 3507) {
                if (player.getSkills().getStaticLevel(Skills.COOKING) >= 53 &&
                    player.getSkills().getStaticLevel(Skills.FISHING) >= 53 &&
                    player.getSkills().getStaticLevel(Skills.HERBLORE) >= 25 &&
                    player.getSkills().getStaticLevel(Skills.MINING) >= 50) {
                    ClimbActionHandler.climbLadder(player, (GameObject) node, option);
                    return true;
                }
                player.getDialogueInterpreter().sendPlaneMessage("You need 53 Cooking, 53 Fishing, 25 Herblore and", "50 Mining to go down this ladder.");
                return true;
            }
        }
        ClimbActionHandler.climbLadder(player, (GameObject) node, option);
        return true;
    }

    @Override
    public Location getDestination(Node n, Node object) {
        if (object.getId() == 96) {
            return Location.create(2641, 9763, 0);
        }
        return ClimbActionHandler.getDestination((GameObject) object);
    }

    /**
     * Opens the component.
     *
     * @param player   the player.
     * @param location the location.
     */
    public static void openComponent(Player player, Location location, String message, String yes, String no) {
        final Component component = new Component(259);
        player.getActionSender().sendString(265, message);
        player.getActionSender().sendString(266, yes);
        player.getActionSender().sendString(267, no);
        component.setPlugin(new WarningComponentPlugin(location));
        player.getInterfaceState().open(component);
    }

    public static class WarningComponentPlugin extends ComponentPlugin {

        /**
         * Represents the destination.
         */
        private final Location destination;

        /**
         * Constructs a new {@code LadderWarningPlugin} {@code Object}.
         *
         * @param destination the destination.
         */
        public WarningComponentPlugin(final Location destination) {
            this.destination = destination;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            return this;
        }

        @Override
        public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
            switch (button) {
                case 267:
                    player.getInterfaceState().close();
                    return true;
                case 266:
                    player.getInterfaceState().close();
                    ClimbActionHandler.climb(player, null, destination);
                    return true;
            }
            return true;
        }

    }
}
