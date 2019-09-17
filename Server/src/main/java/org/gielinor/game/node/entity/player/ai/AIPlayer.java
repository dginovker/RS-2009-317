package org.gielinor.game.node.entity.player.ai;

import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.interaction.DestinationFlag;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.PlayerDetails;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.impl.MovementPulse;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents an <b>A</b>rtificial <b>I</b>ntelligent <b>P</b>layer.
 *
 * @author Emperor
 */
public class AIPlayer extends Player {

    /**
     * The current UID.
     */
    private static int currentUID = 0x1;

    /**
     * The active Artificial intelligent players mapping.
     */
    private static final Map<Integer, AIPlayer> botMapping = new HashMap<>();

    /**
     * The aip control dialogue.
     */
    private static final AIPControlDialogue CONTROL_DIAL = new AIPControlDialogue();

    /**
     * The AIP's UID.
     */
    private final int uid;

    /**
     * The start location of the AIP.
     */
    private final Location startLocation;

    /**
     * The username.
     */
    private String username;

    /**
     * The player controlling this AIP.
     */
    private Player controller;

    /**
     * Constructs a new {@code AIPlayer} {@code Object}.
     *
     * @param name The name of the AIP.
     * @param l    The location.
     */
    public AIPlayer(String name, Location l) {
        super(new PlayerDetails(name, "aipl+" + name, ArtificialSession.getSingleton()));
        super.setLocation(startLocation = l);
        super.setArtificial(true);
        this.username = TextUtils.formatDisplayName(name);
        this.uid = currentUID++;
    }

    @Override
    public void init() {
        getProperties().setSpawnLocation(startLocation);
        getInterfaceState().openDefaultTabs();
        getSession().setPlayer(this);
        botMapping.put(uid, this);
        super.init();
        getSettings().setRunToggled(true);
        getInteraction().set(new Option("Control", 3).setHandler(new OptionHandler() {

            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                return null;
            }

            @Override
            public boolean handle(Player p, Node node, String option) {
                DialoguePlugin dial = CONTROL_DIAL.newInstance(p);
                if (dial != null && dial.open(AIPlayer.this)) {
                    p.getDialogueInterpreter().setDialogue(dial);
                }
                return true;
            }

            @Override
            public boolean isWalk() {
                return false;
            }

        }));
    }

    /**
     * Handles the following.
     *
     * @param e The entity to follow.
     */
    public void follow(final Entity e) {
        getPulseManager().run(new MovementPulse(this, e, DestinationFlag.FOLLOW_ENTITY) {

            @Override
            public boolean pulse() {
                face(e);
                return false;
            }
        }, "movement");
    }

    @Override
    public void clear() {
        botMapping.remove(uid);
        super.clear();
    }

    @Override
    public void reset() {
        if (getPlayerFlags().isUpdateSceneGraph()) {
            getPlayerFlags().setLastSceneGraph(getLocation());
        }
        super.reset();
    }

    /**
     * Gets the UID.
     *
     * @return the UID.
     */
    public int getUid() {
        return uid;
    }

    /**
     * Deregisters an AIP.
     *
     * @param uid The player's UID.
     */
    public static void deregister(int uid) {
        if (!botMapping.containsKey(uid)) {
            System.err.println("Could not deregister AIP#" + uid + ": UID not added to the mapping!");
            return;
        }
        AIPlayer player = botMapping.get(uid);
        if (player != null) {
            player.clear();
        }
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Gets the AIP for the given UID.
     *
     * @param uid The UID.
     * @return The AIPlayer.
     */
    public static AIPlayer get(int uid) {
        return botMapping.get(uid);
    }

    /**
     * @return the startLocation.
     */
    public Location getStartLocation() {
        return startLocation;
    }

    /**
     * Gets the controller.
     *
     * @return The controller.
     */
    public Player getController() {
        return controller;
    }

    /**
     * Sets the controller.
     *
     * @param controller The controller to set.
     */
    public void setController(Player controller) {
        this.controller = controller;
    }

}
