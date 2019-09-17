package plugin.zone.rellekka;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.task.impl.LocationLogoutTask;
import org.gielinor.rs2.task.impl.LogoutTask;

/**
 * Handles the rellekka zone.
 *
 * @author Vexia
 */
public final class RellekkaZone extends MapZone implements Plugin<Object> {

    /**
     * Constructs a new {@code RellekkaZone} {@code Object}.
     */
    public RellekkaZone() {
        super("rellekka", true);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        // Probably remove?
        //	PluginManager.definePlugin(new SailorDialogue());
        //PluginManager.definePlugin(new JarvaldDialogue());
        PluginManager.definePlugin(new RellekaOptionHandler());//, new MariaGunnarsDialogue());
        PluginManager.definePlugin(new OptionHandler() {

            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                NPCDefinition.forId(5507).getConfigurations().put("option:ferry-rellekka", this);
                return this;
            }

            @Override
            public boolean handle(Player player, Node node, String option) {
                sail(player, "Relleka", new Location(2644, 3710, 0));
                return true;
            }

        });
        return this;
    }


    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (e instanceof Player) {
            final Player player = (Player) e;
            switch (target.getId()) {
//				case 4306:
//				case 4310:
//				case 4309:
//				case 4304:
//				case 4308:
//					player.getActionSender().sendMessage("Only Fremenniks may use this " + target.getName().toLowerCase() + ".");
//					return true;
                case 1301:
                    if (option.equals("Trade")) {
                        return false;
                    }
                    player.getActionSender().sendMessage("Only Fremenniks may change their shoes here.");
                    return true;
                case 4165:
                    player.getDialogueInterpreter().open(1288);
                    return true;
                case 4166:
                    player.getActionSender().sendMessage("This door is locked tightly shut.");
                    return true;
                case 1288:
                    player.getDialogueInterpreter().sendDialogues((NPC) target, null, "I have no interest in talking to you just now", "outerlander.");
                    return true;
                case 34286:
                    player.getDialogueInterpreter().sendDialogues(1289, null, "Outerlander... do not test my patience. I do not take", "kindly to people wandering in here and acting as though", "they own the place.");
                    return true;
                case 4148:
                    player.getDialogueInterpreter().sendDialogues(1278, null, "Hey, outerlander. You can't go through there. Talent", "only, backstage.");
                    return true;
                case 100:
                    player.getDialogueInterpreter().sendDialogue("You try to open the trapdoor but it won't budge! It looks like the", "trapdoor can only be opened from the other side.");
                    return true;
                case 4158:
                    player.getDialogueInterpreter().sendDialogues(1283, null, "Hey! Outerlander! Do you normally just barge into", "someones home and wander around as you please?");
                    return true;
                case 3936:
                case 5936:
                case 3680:
                case 2438:
                    if (option.getName().equals("Travel")) {
                        player.getDialogueInterpreter().open(target.getId(), target, true);
                        return true;
                    }
                    break;
            }
        }
        return super.interact(e, target, option);
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public void configure() {
        register(new ZoneBorders(2602, 3639, 2739, 3741));
    }

    /**
     * Sails a player using the relleka ships.
     *
     * @param player      the player.
     * @param name        the name.
     * @param destination the destination.
     */
    public static void sail(final Player player, final String name, final Location destination) {
        player.lock();
        //player.getInterfaceState().open(new Component(224));
        // TODO 317 224
        player.addExtension(LogoutTask.class, new LocationLogoutTask(5, destination));
        World.submit(new Pulse(1, player) {

            int count;

            @Override
            public boolean pulse() {
                switch (++count) {
                    case 5:
                        player.unlock();
                        player.getInterfaceState().close();
                        player.getProperties().setTeleportLocation(destination);
                        player.getDialogueInterpreter().sendDialogue("The ship arrives at " + name + ".");
                        return true;
                }
                return false;
            }

        });
    }

    /**
     * Handles options related to relleka.
     *
     * @author Vexia
     */
    public static final class RellekaOptionHandler extends OptionHandler {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ObjectDefinition.forId(4616).getConfigurations().put("option:cross", this);
            ObjectDefinition.forId(4615).getConfigurations().put("option:cross", this);
            ObjectDefinition.forId(5847).getConfigurations().put("option:climb-over", this);
            return this;
        }

        @Override
        public boolean handle(Player player, Node node, String option) {
            switch (option) {
                case "cross":
                    switch (node.getId()) {
                        case 4616:
                        case 4615:
                            crossRellekaBridge(player, (GameObject) node);
                            break;
                    }
                    break;
                case "climb-over":
                    switch (node.getId()) {
                        case 5847:
                            AgilityHandler.forceWalk(player, -1, player.getLocation(), player.getLocation().transform(0, player.getLocation().getY() <= 3657 ? 3 : -3, 0), Animation.create(840), 20, 1, null, 0);
                            break;
                    }
                    break;
            }
            return true;
        }

        /**
         * Crosses the relleka bridge.
         *
         * @param player the player.
         * @param node   the node.
         */
        private void crossRellekaBridge(Player player, GameObject node) {
            boolean east = node.getId() == 4616;
            player.lock(2);
            if (player.getLocation().equals(node.getLocation())) {
                AgilityHandler.forceWalk(player, -1, player.getLocation(), player.getLocation().transform(east ? -2 : 2, 0, 0), Animation.create(1115), 20, 1.0, null, 1);
                return;
            }
            AgilityHandler.forceWalk(player, -1, player.getLocation(), player.getLocation().transform(east ? -1 : 1, 0, 0), ForceMovement.WALK_ANIMATION, 10, 0.0, null);
            AgilityHandler.forceWalk(player, -1, player.getLocation().transform(east ? -1 : 1, 0, 0), player.getLocation().transform(east ? -3 : 3, 0, 0), Animation.create(1115), 20, 1.0, null, 1);
        }

    }
}
