package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.data.DataShelf;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceMaxScrollContext;
import org.gielinor.net.packet.out.InterfaceMaxScrollPacket;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BanHammerPlugin extends OptionHandler {

    private static final Logger log = LoggerFactory.getLogger(BanHammerPlugin.class);

    private static final List<Rights> ALLOWED_RIGHTS = Collections.unmodifiableList(Arrays.asList(
        Rights.PLAYER_MODERATOR,
        Rights.GIELINOR_MODERATOR,
        Rights.DEVELOPER
    ));

    public static boolean canUse(Player player) {
        boolean allowed = ALLOWED_RIGHTS.contains(player.getRights());
        if (!allowed) {
            player.getInventory().remove(Item.BAN_HAMMER);
        }
        return allowed;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(Item.BAN_HAMMER).getConfigurations().put("option:remote control", this);
        ItemDefinition.forId(Item.BAN_HAMMER).getConfigurations().put("option:dox players", this);
        SMASH_OPTION_HANDLER.newInstance(arg);
        new BanHammerEquipPlugin().newInstance(arg);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        Item item = (Item) node;

        if (!canUse(player)) { // this function call removes from inventory if denied
            Marker marker = MarkerFactory.getMarker("NOTIFY_ADMIN");
            log.warn(marker, "[{}] attempted to [{}] a [{}]. How did they get it?",
                player.getName(), option, item.getName());
            return true;
        }

        if ("drop".equals(option) || "destroy".equals(option)) {
            player.getInventory().remove(item);
            return true;
        }

        switch (option) {
            case "remote control":
                return remoteControl(player);
            case "dox players":
                player.getActionSender().sendMessage("<img=0>Doxing Players!");
                player.getActionSender().sendMessage("<col=255>Username</col> - <col=800000>Player Id</col>");
                player.getActionSender().sendMessage("~~~~~");

                Repository.getPlayers().stream().forEach(onlinePlayer -> {
                    String playerName = onlinePlayer.getUsername();
                    int playerId = onlinePlayer.getPidn();

                    player.getActionSender().sendMessage("<col=255>" + playerName + "</col> - <col=800000>" + playerId + "</col>");
                });
                player.getActionSender().sendMessage("~~~~~");
                return true;
        }
        return true;
    }

    public static boolean remoteControl(Player player) {
        player.setAttribute("runscript", new RunScript() {

            @Override
            public boolean handle() {
                Player otherPlayer = null;
                String value = (String) getValue();

                if (value == null || value.trim().length() == 0) {
                    player.getActionSender().sendMessage("Enter a valid username");
                }

                /*
                 * We try and find the player based on id first,
                 * but if the entered value isn't an int we
                 * assume the value is a username and get the
                 * player using that instead.
                 */
                try {
                    int playerId = Integer.parseInt(value);
                    otherPlayer = Repository.getPlayerByPidn(playerId);
                } catch (NumberFormatException e) {
                    otherPlayer = Repository.getPlayerByName(value.trim());
                }

                if (otherPlayer == null) {
                    player.getActionSender().sendMessage("Could not find player!");
                    return true;
                }
                player.setAttribute("BAN_HAMMER_REMOTE_CONTROL", true);
                openOptions(player, otherPlayer);
                return true;
            }
        });
        player.getDialogueInterpreter().sendInput(true, "Enter username or player id:");
        return true;
    }

    private static void openOptions(Player player, Player otherPlayer) {
        player.setAttribute("OPTION_INTERFACE", "BAN_HAMMER");
        player.setAttribute("BAN_HAMMER_TARGET", otherPlayer);

        @SuppressWarnings("unchecked")
        List<String> punishmentTypes = (List<String>) DataShelf.fetchListMap("ban_hammer_punishments");
        if (punishmentTypes == null) {
            return;
        }

        for (int i = 0; i < 50; i++) {
            player.getActionSender().sendString(27703 + i, "");
            player.getActionSender().sendHideComponent(27703 + i, false);
        }

        player.getActionSender().sendString(27803, "Ban Hammer - <col=FF0000>" + otherPlayer.getUsername() + "</col>");
        player.getActionSender().sendHideComponent(27803, false);

        int index = 0;
        int scrollLength = 0;
        for (String punishmentOption : punishmentTypes) {
            player.getActionSender().sendString(27703 + index, punishmentOption);
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
                player.removeAttribute("BAN_HAMMER_TARGET");
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        }));
    }


    @Override
    public boolean isWalk() {
        return false;
    }

    public static class BanHammerEquipPlugin implements Plugin<Object> {

        /**
         * The whack option.
         */
        public static final Option SMASH = new Option("Throw Down", 1).setHandler(SMASH_OPTION_HANDLER);

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ItemDefinition.forId(Item.BAN_HAMMER).getConfigurations().put("equipment", this);
            return this;
        }

        @Override
        public Object fireEvent(String identifier, Object... args) {
            final Player player = (Player) args[0];
            switch (identifier) {
                case "equip":
                    player.getInteraction().set(SMASH);
                    break;
                case "unequip":
                    if (player.getInteraction().get(SMASH.getIndex()) == SMASH) {
                        player.getInteraction().remove(SMASH);
                    }
                    break;
            }
            return true;
        }

    }

    public static OptionHandler SMASH_OPTION_HANDLER = new OptionHandler() {

        @Override
        public boolean handle(Player player, Node node, String option) {
            player.getPulseManager().clear("interaction:attack:" + node.hashCode());
            if (!(node instanceof Player)) {
                return false;
            }
            Player target = (Player) node;

            player.setAttribute("BAN_HAMMER_REMOTE_CONTROL", false);
            openOptions(player, target);
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

}
