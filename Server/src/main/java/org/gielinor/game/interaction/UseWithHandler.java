package org.gielinor.game.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.player.FaceLocationFlag;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * Handles the "use {@code node a} with {@code node b}" option.
 *
 * @author Emperor
 */
public abstract class UseWithHandler implements Plugin<Object> {

    /**
     * The item type.
     */
    public static final int ITEM_TYPE = 0;

    /**
     * The NPC type.
     */
    public static final int NPC_TYPE = 1;

    /**
     * The object type.
     */
    public static final int OBJECT_TYPE = 2;

    /**
     * The player type.
     */
    public static final int PLAYER_TYPE = 3;

    /**
     * The ground item type.
     */
    public static final int GROUND_ITEM_TYPE = 4;
    
    /**
     * A flag to allow all possible nodes to be handled.
     */
    public static final boolean ALL_NODES_ALLOWED = true;

    /**
     * The handlers.
     */
    private static final Map<Integer, List<UseWithHandler>> HANDLERS = new HashMap<>();

    /**
     * The allowed node ids.
     */
    private int[] allowedNodes;
    
    /**
     * Whether or not all nodes are allowed.
     */
    private boolean allNodesAllowed;

    /**
     * Constructs a new {@code UseWithHandler.java} {@code Object}.
     *
     * @param allowedNodes
     */
    public UseWithHandler(int... allowedNodes) {
        this.allowedNodes = allowedNodes;
    }

    public UseWithHandler(boolean allNodesAllowed) {
        this.allNodesAllowed = allNodesAllowed;
    }
    
    /**
     * Adds a handler.
     *
     * @param id      The node id.
     * @param type    The node type (0=item, 1=NPC, 2=object, 3=player).
     * @param handler The handler.
     */
    public static void addHandler(int id, int type, UseWithHandler handler) {
        int key = id | type << 16;
        List<UseWithHandler> handlers = HANDLERS.computeIfAbsent(key, k -> new ArrayList<>());
        if (type == PLAYER_TYPE) {
            if (handler.allowedNodes == null) {
                handler.allowedNodes = new int[]{ id };
            } else {
                int[] array = handler.allowedNodes;
                handler.allowedNodes = new int[handler.allowedNodes.length + 1];
                System.arraycopy(array, 0, handler.allowedNodes, 0, array.length);
                handler.allowedNodes[handler.allowedNodes.length - 1] = id;
            }
        }
        handlers.add(handler);
    }

    /**
     * Runs the event.
     *
     * @param event The event.
     */
    public static void run(final NodeUsageEvent event) {
        Node n = event.getUsedWith();
        List<UseWithHandler> handler;
        if (n instanceof Item) {
            handler = HANDLERS.get(event.getUsed().getId());
            if (handler == null) {
                handler = HANDLERS.get(event.getUsedWith().getId());
            }
        } else if (n instanceof NPC) {
            handler = HANDLERS.get(n.getId() | NPC_TYPE << 16);
        } else if (n instanceof GameObject) {
            handler = HANDLERS.get(n.getId() | OBJECT_TYPE << 16);
        } else if (n instanceof Player) {
            handler = HANDLERS.get(event.getUsed().getId() | PLAYER_TYPE << 16);
        } else {
            handler = HANDLERS.get(n.getId() | NPC_TYPE << 16);
        }
        if (handler == null) {
            if (n instanceof Item && !(event.getUsed() instanceof Player)) {
                event.getPlayer().getPulseManager().runUnhandledAction(event.getPlayer());
            } else {
                event.getPlayer().getPulseManager().run(new MovementPulse(event.getPlayer(), event.getUsedWith()) {

                    @Override
                    public boolean pulse() {
                        event.getPlayer().getActionSender().sendMessage("Nothing interesting happens.");
                        return true;
                    }
                }, "movement");
            }
            return;
        }
        final List<UseWithHandler> handlers = handler;
        if (n instanceof Item && !(event.getUsed() instanceof Player)) {
            event.getPlayer().getPulseManager().run(new Pulse(1, event.getPlayer(), event.getUsed(), event.getUsedWith()) {

                @Override
                public boolean pulse() {
                    boolean handled = false;
                    for (UseWithHandler h : handlers) {
                        if (!h.nodeAllowed(event.getUsedWith().getId()) &&
                            !h.nodeAllowed(event.getUsedItem().getId()) &&
                            !h.allNodesAllowed ||
                            !h.handle(event)) {
                            continue;
                        }
                        event.getPlayer().debug("Handler=" + h.getClass().getSimpleName() + ", used item=" + event.getUsedItem() + ", used with=" + event.getUsedWith());
                        handled = true;
                        break;
                    }
                    if (!handled) {
                        event.getPlayer().getActionSender().sendMessage("Nothing interesting happens.");
                    }
                    return true;
                }
            });
            return;
        }
        if (handler.get(0).isWalks()) {
            event.getPlayer().getPulseManager().run(new MovementPulse(event.getPlayer(), event.getUsedWith(), handler.get(0)) {

                @Override
                public boolean pulse() {
                    event.getPlayer().faceLocation(FaceLocationFlag.getFaceLocation(event.getPlayer(), event.getUsedWith()));
                    boolean handled = false;
                    Item used = (Item) event.getUsed();
                    for (UseWithHandler h : handlers) {
                        if ((used != null && !h.nodeAllowed(used.getId())) || !h.handle(event)) {
                            continue;
                        }
                        event.getPlayer().debug("Handler=" + h.getClass().getSimpleName() + ", used item=" + event.getUsedItem() + ", used with=" + event.getUsedWith());
                        handled = true;
                        break;
                    }
                    if (!handled) {
                        event.getPlayer().getActionSender().sendMessage("Nothing interesting happens.");
                    }
                    return true;
                }
            }, "movement");
            return;
        }
        event.getPlayer().faceLocation(FaceLocationFlag.getFaceLocation(event.getPlayer(), event.getUsedWith()));
        boolean handled = false;
        Item used = (Item) event.getUsed();
        for (UseWithHandler h : handlers) {
            if ((used != null && !h.nodeAllowed(used.getId())) || !h.handle(event)) {
                continue;
            }
            event.getPlayer().debug("Handler=" + h.getClass().getSimpleName() + ", used item=" + event.getUsedItem() + ", used with=" + event.getUsedWith());
            handled = true;
            break;
        }
        if (!handled) {
            event.getPlayer().getActionSender().sendMessage("Nothing interesting happens.");
        }
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Gets the valid children for the wrapper id.
     *
     * @param wrapper the wrapper id.
     * @return the valid children.
     */
    public int[] getValidChildren(int wrapper) {
        final ObjectDefinition definition = ObjectDefinition.forId(wrapper);
        final List<Integer> list = new ArrayList<>();
        if (definition.getChildrenIds() == null) {
            System.err.println("Null child wrapper in option handler wrapperId=" + wrapper);
            return new int[]{ wrapper };
        }
        for (int child : definition.getChildrenIds()) {
            if (child != -1 && !list.contains(child)) {
                list.add(child);
            }
        }
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Method used to get the destination to go to, leave null to go to proper one.
     *
     * @param player the player.
     * @return the location.
     */
    public Location getDestination(Player player, Node with) {
        return null;
    }


    /**
     * Checks if the node is allowed to be used with the base node.
     *
     * @param nodeId The node id.
     * @return <code>True</code> if so.
     */
    public boolean nodeAllowed(int nodeId) {
        if (allNodesAllowed) {
            return true;
        }
        for (int id : allowedNodes) {
            if (nodeId == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the interaction option.
     *
     * @param event The node usage event.
     * @return <code>True</code> if successful.
     */
    public abstract boolean handle(NodeUsageEvent event);

    public boolean isWalks() {
        return true;
    }

}
