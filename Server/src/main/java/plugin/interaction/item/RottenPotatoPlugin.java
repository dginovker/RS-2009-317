package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.database.DataSource;
import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.anticheat.AntiMacroEvent;
import org.gielinor.game.content.anticheat.AntiMacroHandler;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.system.data.DataShelf;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceMaxScrollContext;
import org.gielinor.net.packet.out.InterfaceMaxScrollPacket;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.spring.service.impl.ObjectSpawnService;
import org.gielinor.utilities.misc.PlayerLoader;
import org.gielinor.utilities.misc.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents the Rotten Potato
 * {@link org.gielinor.game.interaction.OptionHandler}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class RottenPotatoPlugin extends OptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RottenPotatoPlugin.class);

    /**
     * Represents the sliced potato options.
     */
    private static final String[] OPTIONS = new String[]{ "eat", "slice", "peel", "mash", "drop" };

    /**
     * The ID of the Rotten Potato.
     */
    private static final int ROTTEN_POTATO = 5733;

    @Override
    public Plugin<Object> newInstance(Object arg) {
        for (String option : OPTIONS) {
            ItemDefinition.forId(ROTTEN_POTATO).getConfigurations().put("option:" + option, this);
        }
        new RottenPotatoDialogue().init();
        return this;
    }

    /**
     * Options for transmogrifying.
     */
    public static final Object[][] TRANSMOGRIFY = new Object[][]{ { "~ Myself ~", -1 }, { "TzTok-Jad", 2745 },
        { "Elvarg", 6087 }, { "Black Knight Titan", 221 }, { "King Black Dragon", 50 }, { "Wise Old Man", 3820 },
        { "Drunken Dwarf", 3837 }, { "Zanik", 5870 }, { "Penance Healer", 5420 }, { "Boat", 4771 },
        { "Strange Plant", 407 }, { "Invisible NPC", -2 }, { "Arma Hilt" }, { "Sara Hilt" }, { "Divine Shield" },
        { "Elysian Shield" } };

    @Override
    public boolean handle(Player player, Node node, String option) {
        Item item = (Item) node;
        // Should someone find themselves in possesion of a rotten potato somehow,
        // we still don't want to let them use it.
        if (!player.getDetails().getRights().isAdministrator() && !player.getDetails().getRights().isDeveloper()) {
            Marker marker = MarkerFactory.getMarker("NOTIFY_ADMIN");
            log.warn(marker, "[{}] attempted to [{}] a [{}]. How did they get it?",
                player.getName(), option, item.getName());
            player.getInventory().remove(item);
            return true;
        }
        // We are not interested in logging when administrators drop their rotten potato,
        // so this is handled separately now.
        if ("drop".equals(option)) {
            player.getInventory().remove(item);
            return true;
        }
        for (int childId = 27653; childId < 27755; childId++) {
            player.getActionSender().sendString(childId, "");
        }
        player.getDialogueInterpreter().open("RottenPotato", option);
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    /**
     * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin}
     * for the Rotten Potato.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static final class RottenPotatoDialogue extends DialoguePlugin {

        /**
         * Constructs a new {@code RottenPotatoDialogue} {@code Object}.
         */
        public RottenPotatoDialogue() {

        }

        /**
         * Constructs a new {@code RottenPotatoDialogue} {@code Object}.
         *
         * @param player
         *            the player.
         */
        public RottenPotatoDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new RottenPotatoDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            if (!player.getDetails().getRights().isAdministrator() && !player.getDetails().getRights().isDeveloper()) {
                return true;
            }
            if (player.getAttribute("REMOVE_OBJECT") != null
                && (player.getAttribute("REMOVE_OBJECT") instanceof int[])) {
                interpreter.sendOptions("Are you sure?", "Yes, remove.", "No, keep.");
                stage = 252;
                return true;
            }
            if (player.getAttribute("REMOVE_NPC") != null && (player.getAttribute("REMOVE_NPC") instanceof NPC)) {
                interpreter.sendOptions("Are you sure?", "Yes, remove.", "No, keep.");
                stage = 203;
                return true;
            }
            if (args.length == 2 && args[1] instanceof NPC) {
                if (player.getAttribute("SPAWN_NPC") != null) {
                    if (!(player.getAttribute("SPAWN_NPC") instanceof NPC)) {
                        end();
                        return true;
                    }
                    NPC npcSpawn = player.getAttribute("SPAWN_NPC");
                    npcSpawn.init();
                    npcSpawn.setAggressive(false);
                    interpreter.sendOptions("Options", "Keep spawn", "Rotate", "Walks", "Remove spawn");
                    stage = 202;
                    return true;
                }
                end();
                player.removeAttribute("RESPAWN_NPC");
                player.removeAttribute("SPAWN_NPC");
                return true;
            }
            switch ((String) args[0]) {
                case "eat":
                    interpreter.sendOptions("Op1", DataShelf.fetchStringArray("rotten_potato_eat"));
                    stage = 100;
                    break;
                case "slice":
                    player.removeAttribute("RESPAWN_NPC");
                    player.removeAttribute("SPAWN_NPC");
                    player.removeAttribute("REMOVE_NPC");
                    player.removeAttribute("REMOVE_OBJECT");
                    interpreter.sendOptions("Op2", DataShelf.fetchStringArray("rotten_potato_slice"));
                    stage = 200;
                    break;
                case "peel":
                    interpreter.sendOptions("Op3", DataShelf.fetchStringArray("rotten_potato_peel"));
                    stage = 300;
                    break;
                case "mash":
                    interpreter.sendOptions("Op4", DataShelf.fetchStringArray("rotten_potato_mash"));
                    stage = 400;
                    break;
            }
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 100:// eat options.
                    end();
                    switch (optionSelect) {
                        case FIVE_OPTION_ONE:
                            setSkills(player);
                            break;
                        case FIVE_OPTION_TWO:
                            emptyInventory(player);
                            break;
                        case FIVE_OPTION_THREE:
                            player.getActionSender().sendMessage("This command is not added yet.");
                            break;
                        case FIVE_OPTION_FOUR:
                            teleportToPlayer(player);
                            break;
                        case FIVE_OPTION_FIVE:
                            spawnAggresiveNpc(player);
                            break;
                    }
                    break;
                case 200:// slice
                    switch (optionSelect) {
                        case FIVE_OPTION_ONE:
                            interpreter.sendInput(false, "Enter NPC ID:");
                            player.setAttribute("runscript", new RunScript() {

                                @Override
                                public boolean handle() {
                                    int npcId = (int) getValue();
                                    if (npcId == -1) {
                                        end();
                                        return false;
                                    }
                                    player.setAttribute("SPAWN_NPC", npcId);
                                    stage = 201;
                                    interpreter.handle(-1, null);
                                    return false;
                                }
                            });
                            break;
                        case FIVE_OPTION_TWO:
                            end();
                            player.setAttribute("REMOVE_NPC", true);
                            interpreter.sendPlaneMessage(true, "Click NPC to remove.");
                            break;
                        case FIVE_OPTION_THREE:
                            interpreter.sendInput(false, "Enter Object ID:");
                            player.setAttribute("runscript", new RunScript() {

                                @Override
                                public boolean handle() {
                                    int objectId = (int) getValue();
                                    if (objectId == -1) {
                                        end();
                                        return false;
                                    }
                                    stage = 250;
                                    GameObject gameObject = new GameObject(objectId, player.getLocation(), 10, 0);
                                    player.setAttribute("SPAWN_OBJECT", gameObject);
                                    ObjectBuilder.add(gameObject);
                                    interpreter.handle(-1, null);
                                    return false;
                                }
                            });
                            break;
                        case FIVE_OPTION_FOUR:
                            end();
                            player.setAttribute("REMOVE_OBJECT", true);
                            interpreter.sendPlaneMessage(true, "Click object to remove.");
                            break;
                        case FIVE_OPTION_FIVE:
                            GameObject removeObject = RegionManager.getDeleteObject(player.getLocation().getX(),
                                player.getLocation().getY(), player.getLocation().getZ());
                            if (removeObject == null) {
                                end();
                                player.getDialogueInterpreter().sendPlaneMessage("Could not find object to remove.");
                                break;
                            }
                            end();
                            player.setAttribute("REMOVE_OBJECT", new int[]{ removeObject.getId(),
                                removeObject.getLocation().getX(), removeObject.getLocation().getY() });
                            player.getDialogueInterpreter().open("RottenPotato");
                            break;
                    }
                    break;
                case 201:
                    interpreter.sendPlaneMessage(true, "Click on the screen where you wish to", "spawn the new NPC.");
                    break;
                case 250:
                    interpreter.sendOptions("Select rotation", "Rotate Left", "Rotate Right", "Place Object",
                        "Remove Object");
                    stage = 251;
                    break;
                case 251:
                    if (player.getAttribute("SPAWN_OBJECT") == null) {
                        end();
                        return true;
                    }
                    GameObject gameObject = player.getAttribute("SPAWN_OBJECT");
                    switch (optionSelect) {
                        case FOUR_OPTION_ONE:
                            ObjectBuilder.remove(gameObject);
                            ObjectBuilder.add(gameObject = new GameObject(gameObject.getId(), gameObject.getLocation(),
                                gameObject.getType(), gameObject.rotateLeft()));
                            player.setAttribute("SPAWN_OBJECT", gameObject);
                            break;
                        case FOUR_OPTION_TWO:
                            ObjectBuilder.remove(gameObject);
                            ObjectBuilder.add(gameObject = new GameObject(gameObject.getId(), gameObject.getLocation(),
                                gameObject.getType(), gameObject.rotateRight()));
                            player.setAttribute("SPAWN_OBJECT", gameObject);
                            break;
                        case FOUR_OPTION_THREE:
                            end();
                            GameObject spawnedObject = player.getAttribute("SPAWN_OBJECT");
                            if (((ObjectSpawnService) World.getWorld().getApplicationContext().getBean("objectSpawnService"))
                                .insertSpawnedObject(spawnedObject)) {
                                player.getDialogueInterpreter().sendPlaneMessage("Placed object successfully.");
                            } else {
                                player.getDialogueInterpreter().sendPlaneMessage("Could not place object!");
                            }
                            break;
                        case FOUR_OPTION_FOUR:
                            end();
                            ObjectBuilder.remove(gameObject);
                            player.removeAttribute("SPAWN_OBJECT");
                            break;
                    }
                    break;
                case 252:
                    end();
                    int[] objectInfo = player.getAttribute("REMOVE_OBJECT");
                    player.removeAttribute("REMOVE_OBJECT");
                    GameObject removeObject = RegionManager.getDeleteObject(objectInfo[1], objectInfo[2],
                        player.getLocation().getZ());
                    if (removeObject == null) {
                        player.getDialogueInterpreter().sendPlaneMessage("Could not find object to remove.");
                        break;
                    }
                    ObjectBuilder.remove(removeObject);
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            if (((ObjectSpawnService) World.getWorld().getApplicationContext().getBean("objectSpawnService"))
                                .deleteSpawnedObject(player, objectInfo)) {
                                player.getDialogueInterpreter().sendPlaneMessage("Removed object successfully.");
                            } else {
                                player.getDialogueInterpreter().sendPlaneMessage("Could not remove object!");
                            }
                            break;
                        case TWO_OPTION_TWO:

                            break;
                    }
                    break;
                case 202:
                    // interpreter.sendOptions("Options", "Keep spawn", "Rotate",
                    // "Walks", "Remove spawn");
                    if (player.getAttribute("SPAWN_NPC") == null || !(player.getAttribute("SPAWN_NPC") instanceof NPC)) {
                        end();
                        break;
                    }
                    NPC npc = player.getAttribute("SPAWN_NPC");
                    player.setAttribute("RESPAWN_NPC", true);
                    switch (optionSelect) {
                        case FOUR_OPTION_ONE:
                            end();
                            try (Connection connection = DataSource.getGameConnection()) {
                                try (PreparedStatement preparedStatement = connection.prepareStatement(
                                    "INSERT INTO npc_spawn (npc_id, x, y, z, walks, radius, face, description) VALUES(?, ?, ?, ?, ?, ?, ?, ?)")) {
                                    preparedStatement.setInt(1, npc.getId());
                                    preparedStatement.setInt(2, npc.getLocation().getX());
                                    preparedStatement.setInt(3, npc.getLocation().getY());
                                    preparedStatement.setInt(4, npc.getLocation().getZ());
                                    preparedStatement.setBoolean(5, npc.isWalks());
                                    preparedStatement.setInt(6, npc.isWalks() ? 3 : 0);
                                    preparedStatement.setInt(7, npc.getDirection().ordinal());
                                    preparedStatement.setString(8, (npc.getName().toUpperCase() + "_AUTOSPAWN"));
                                    preparedStatement.execute();
                                }
                            } catch (IOException | SQLException ex) {
                                log.error("Failed to place NPC: [{}] at [{}].", npc, npc.getLocation(), ex);
                                player.getDialogueInterpreter().sendPlaneMessage("Could not place NPC!");
                                break;
                            }
                            player.getDialogueInterpreter().sendPlaneMessage("Placed NPC successfully.");
                            break;
                        case FOUR_OPTION_TWO:
                            npc.clear();
                            npc.setDirection(npc.rotate());
                            World.submit(new Pulse(2) {

                                @Override
                                public boolean pulse() {
                                    npc.init();
                                    player.setAttribute("SPAWN_NPC", npc);
                                    return true;
                                }
                            });
                            break;
                        case FOUR_OPTION_THREE:
                            npc.setWalks(!(npc.isWalks() || npc.getWalkRadius() > 0));
                            npc.setWalkRadius(npc.isWalks() ? 3 : 0);
                            player.setAttribute("SPAWN_NPC", npc);
                            interpreter.sendOptions("Options", "Keep spawn", "Rotate",
                                (npc.getWalkRadius() > 0) ? "Does not walk" : "Walks", "Remove spawn");
                            stage = 202;
                            break;
                        case FOUR_OPTION_FOUR:
                            end();
                            npc.clear();
                            break;
                    }
                    break;
                case 203:
                    end();
                    NPC removeNPC = player.getAttribute("REMOVE_NPC");
                    player.removeAttribute("REMOVE_NPC");
                    if (removeNPC == null || removeNPC.getProperties().getSpawnLocation() == null) {
                        player.getDialogueInterpreter().sendPlaneMessage("Could not find NPC to remove.");
                        break;
                    }
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            try (Connection connection = DataSource.getGameConnection()) {
                                int x = removeNPC.getProperties().getSpawnLocation().getX();
                                int y = removeNPC.getProperties().getSpawnLocation().getY();
                                int z = removeNPC.getProperties().getSpawnLocation().getZ();
                                try (PreparedStatement preparedStatement = connection.prepareStatement(
                                    "DELETE FROM npc_spawn WHERE npc_id = ? AND x = ? AND y = ? AND z = ? LIMIT 1")) {
                                    preparedStatement.setInt(1, removeNPC.getId());
                                    preparedStatement.setInt(2, x);
                                    preparedStatement.setInt(3, y);
                                    preparedStatement.setInt(4, z);
                                    preparedStatement.execute();
                                }
                            } catch (IOException | SQLException ex) {
                                log.error("Unable to remove NPC: [{}] at [{}].", removeNPC, removeNPC.getLocation(), ex);
                                player.getDialogueInterpreter().sendPlaneMessage("Could not remove NPC!");
                                break;
                            }
                            removeNPC.clear();
                            player.getDialogueInterpreter().sendPlaneMessage("Removed NPC successfully.");
                            break;
                        case TWO_OPTION_TWO:

                            break;
                    }
                    break;
                case 300:// peel
                    switch (optionSelect) {
                        case THREE_OPTION_ONE:
                            interpreter.sendOptions("Op3", "Open bank.", "Set PIN to 2468", "Wipe bank.");
                            stage = 310;
                            break;
                        case THREE_OPTION_TWO:
                            end();
                            sendAmes(player);
                            break;
                        case THREE_OPTION_THREE:
                            player.getDialogueInterpreter().sendInput(true, "Enter name:");
                            player.setAttribute("runscript", new RunScript() {

                                @Override
                                public boolean handle() {
                                    String playerName = (String) getValue();
                                    Player target = Repository.getPlayerByName(playerName);
                                    if (target == null) {
                                        playerName = playerName.replaceAll(" ", "_");
                                        target = PlayerLoader.getPlayerFile(playerName);
                                        if (target == null) {
                                            player.getActionSender()
                                                .sendMessage("The player \"" + playerName + "\" could not be found.");
                                            return false;
                                        }
                                    }
                                    player.getDialogueInterpreter().open("PlayerRights", target);
                                    return false;
                                }
                            });
                            break;
                    }
                    break;
                case 310:
                    end();
                    switch (optionSelect) {
                        case THREE_OPTION_ONE:
                            player.getBank().open();
                            break;
                        case THREE_OPTION_TWO:

                            break;
                        case THREE_OPTION_THREE:
                            wipeBank(player);
                            break;
                    }
                    break;
                case 400:
                    switch (optionSelect) {
                        case FOUR_OPTION_ONE:
                            end();
                            player.setAttribute("keep_logged_in", !player.getAttribute("keep_logged_in", false));
                            break;
                        case FOUR_OPTION_TWO:
                            player.getActionSender().sendLogout();
                            break;
                        case FOUR_OPTION_THREE:
                            DeathTask.startDeath(player, player);
                            end();
                            break;
                        case FOUR_OPTION_FOUR:
                            end();
                            transmogrify(player);
                            break;
                    }
                    break;
            }
            return true;
        }

        /**
         * Method used to set the skills of a player.
         *
         * @param player
         *            the player.
         */
        public static void setSkills(final Player player) {
            player.getDialogueInterpreter().sendInput(true, "Enter skill syntax(all/reset/name level)");
            player.setAttribute("runscript", new RunScript() {

                @Override
                public boolean handle() {
                    final String line = ((String) getValue()).replace("_", " ");
                    if (line.equals("all") || line.equals("reset")) {
                        boolean reset = line.equals("reset");
                        for (int i = 0; i < 24; i++) {
                            player.getSkills().setLevel(i, reset ? 1 : 99);
                            player.getSkills().setStaticLevel(i, reset ? 1 : 99);
                        }
                    } else {
                        String[] tokens = line.split(" ");
                        if (tokens == null) {
                            player.getActionSender().sendMessage("Invalid syntax! e.g (Enter skill syntax(all/name)");
                            return true;
                        }
                        final int id = Skills.getSkillByName(tokens[0]);
                        if (id == -1 || tokens.length < 1) {
                            player.getActionSender()
                                .sendMessage("Invalid syntax! e.g (Enter skill syntax(all/name level)");
                            return true;
                        }
                        player.getDialogueInterpreter().sendInput(false, "Enter the level:");

                        player.setAttribute("runscript", new RunScript() {

                            @Override
                            public boolean handle() {
                                int level = (int) getValue();
                                if (level > 99) {
                                    level = 99;
                                } else if (level < 1) {
                                    level = 1;
                                }
                                player.getSkills().setLevel(id, level);
                                player.getSkills().setStaticLevel(id, level);
                                return true;
                            }

                        });
                    }
                    player.getSkills().updateCombatLevel();
                    return true;
                }
            });
        }

        /**
         * Method used to send anti macro events to all players around you.
         *
         * @param player
         *            the player.
         */
        public static void sendAmes(final Player player) {
            AntiMacroEvent event = player.getAntiMacroHandler().getRandomEvent(-1);
            if (event == null) {
                return;
            }
            for (Player p : RegionManager.getLocalPlayers(player, 8)) {
                if (p == null || p == player || p.getDetails().getRights().isAdministrator() && !p.getDetails().getRights().isDeveloper()) {
                    continue;
                }
                p.getAntiMacroHandler().fireEvent(event.getName());
            }
        }

        /**
         * Gets the event.
         *
         * @param name
         *            the name.
         * @return the event.
         */
        public AntiMacroEvent getEvent(String name) {
            for (AntiMacroEvent event : AntiMacroHandler.EVENTS.values()) {
                if (event.getName().toLowerCase().equals(name.replace("_", " "))) {
                    return event;
                }
            }
            return null;
        }

        /**
         * Method used to empty an inventory.
         *
         * @param player
         *            the player.
         */
        public static void emptyInventory(final Player player) {
            for (Item i : player.getInventory().toArray()) {
                if (i == null) {
                    continue;
                }
                if (i.getId() != 5733) {
                    player.getInventory().remove(i);
                }
            }
            player.getInventory().refresh();
        }

        /**
         * Method used to wipe a bank.
         *
         * @param player
         *            the player.
         */
        public static void wipeBank(final Player player) {
            player.getBank().clear();
            player.getActionSender().sendMessage("Bank all wiped.");
        }

        /**
         * Builds the interface for transmogrifying.
         *
         * @param player
         *            the player.
         */
        @SuppressWarnings("unchecked")
        public static void transmogrify(final Player player) {
            player.setAttribute("OPTION_INTERFACE", "TRANSMOGRIFY");
            for (int childId = 27703; childId < 27803; childId++) {
                player.getActionSender().sendString(childId, "");
                player.getActionSender().sendHideComponent(childId, true);
            }
            player.getActionSender().sendString("Transmogrify me into...", 27803);
            List<Object> transmogrifyMap = (List<Object>) DataShelf.fetchListMap("transmogrify");
            if (transmogrifyMap == null) {
                return;
            }
            int index = 0;
            int scrollLength = 0;
            for (Object transmogrify : transmogrifyMap) {
                List<Object> transmogrifyList = (List) transmogrify;
                if (transmogrifyList.get(0) == null || transmogrifyList.get(1) == null) {
                    continue;
                }
                player.getActionSender().sendString(27703 + index, String.valueOf(transmogrifyList.get(0)));
                player.getActionSender().sendHideComponent(27703 + index, false);
                scrollLength += 20;
                index++;
            }
            PacketRepository.send(InterfaceMaxScrollPacket.class,
                new InterfaceMaxScrollContext(player, 27702, scrollLength));
            player.getInterfaceState().open(new Component(27700).setCloseEvent(new CloseEvent() {

                @Override
                public void close(Player player, Component component) {
                    player.removeAttribute("OPTION_INTERFACE");
                }

                @Override
                public boolean canClose(Player player, Component component) {
                    return true;
                }
            }));
        }

        /**
         * Method used to teleport to a player.
         *
         * @param player
         *            the player.
         */
        public static void teleportToPlayer(final Player player) {
            player.getDialogueInterpreter().sendInput(true, "Enter player's display name:");
            player.setAttribute("runscript", new RunScript() {

                @Override
                public boolean handle() {
                    final Player target = Repository.getPlayerByName((String) getValue());
                    if (target == null || !target.isActive()) {
                        player.getActionSender().sendMessage("That player is offline, or has privacy mode enabled.");
                        return true;
                    }
                    player.getProperties().setTeleportLocation(target.getLocation());
                    return true;
                }
            });
        }

        /**
         * Method used to spawn an aggresive npc.
         *
         * @param player
         *            the player.
         */
        public static void spawnAggresiveNpc(final Player player) {
            final NPC npc = NPC.create(50, player.getLocation());
            npc.setAggressive(true);
            npc.setRespawn(false);
            npc.init();
            npc.sendChat("Go away!");
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("RottenPotato") };
        }

    }

}
