package plugin.interaction.city;

import java.util.List;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.PickupHandler;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the plugin used to handle node interactions in falador.
 * @author 'Vexia
 * @version 1.0
 */
public final class FaladorNodePlugin extends OptionHandler {

    /**
     * Represents the opening of a cupboard animation.
     */
    private static final Animation OPEN_ANIMATION = new Animation(536);

    /**
     * Represents the closing of a cupboard animation.
     */
    private static final Animation CLOSE_ANIMATION = new Animation(535);

    /**
     * Represents the portrait item.
     */
    private static final Item PORTRAIT = new Item(666);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2271).getConfigurations().put("option:open", this);//sir vyvans cupboard (closed)
        ObjectDefinition.forId(2272).getConfigurations().put("option:shut", this);//sir vyvans cupboard (open)
        ObjectDefinition.forId(2272).getConfigurations().put("option:search", this);//sir vyvans cupboard (open)
        //dwarven mine
        ObjectDefinition.forId(30868).getConfigurations().put("option:squeeze-through", this);
        ObjectDefinition.forId(5020).getConfigurations().put("option:ride", this);
        ItemDefinition.forId(245).getConfigurations().put("option:take", this);
        //fally park.
        NPCDefinition.forId(2290).getConfigurations().put("option:talk-to", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        final int id = node instanceof GameObject ? ((GameObject) node).getId() : node instanceof NPC ? ((NPC) node).getId() : ((Item) node).getId();
        switch (id) {
            case 2290:
                player.getDialogueInterpreter().open(id, node);
                return true;
            case 245:
                if (node.getLocation().equals(new Location(2931, 3515, 0))) {
                    final List<NPC> npcs = RegionManager.getLocalNpcs(player);
                    for (NPC n : npcs) {
                        if (n.getId() == 188) {
                            n.sendChat("Hands off zamorak's wine!");
                            n.getProperties().getCombatPulse().attack(player);
                        }
                    }
                } else {
                    return PickupHandler.take(player, (GroundItem) node);
                }
                break;
            case 5020:
                player.getDialogueInterpreter().sendPlaneMessage("You must visit Keldagrim before you are allowed to ride mine carts.");
                break;
            case 30868:
                if (player.getSkills().getLevel(Skills.AGILITY) < 42) {
                    player.getActionSender().sendMessage("You need an agility level of 42 to do this.");
                    return true;
                }
                Location dest = player.getLocation().equals(new Location(3035, 9806, 0)) ? new Location(3028, 9806, 0) : new Location(3035, 9806, 0);
                player.animate(new Animation(2240));
                ForceMovement movement = new ForceMovement(player, player.getLocation(), dest, new Animation(2240)) {

                    @Override
                    public void stop() {
                        super.stop();
                    }
                };
                movement.run(player, 8);
                World.submit(new Pulse(7, player) {

                    @Override
                    public boolean pulse() {
                        player.animate(new Animation(2240));
                        return true;
                    }
                });
                break;
            case 2271:
                ObjectBuilder.replace((GameObject) node, ((GameObject) node).transform(2272));
                player.animate(OPEN_ANIMATION);
                break;
            case 2272:
                switch (option) {
                    case "shut":
                        ObjectBuilder.replace((GameObject) node, ((GameObject) node).transform(2271));
                        player.animate(CLOSE_ANIMATION);
                        break;
                    case "search":
                        if (player.getInventory().containsItem(PORTRAIT)) {
                            player.getDialogueInterpreter().sendPlaneMessage("There is just a load of junk in here.");
                            return true;
                        } else {

                            if (!player.getInventory().add(PORTRAIT)) {
                                GroundItemManager.create(PORTRAIT, player);
                            }
                            player.getDialogueInterpreter().sendPlaneMessage("You find a small portrait in here which you take.");
                        }
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof NPC) {
            final NPC npc = (NPC) n;
            if (npc.getId() == 2290) {
                return Location.create(2997, 3374, 0);
            }
        }
        return null;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    @Override
    public boolean isWalk(final Player player, Node node) {
        return !(node instanceof Item);
    }
}
