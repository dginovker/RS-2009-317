package org.gielinor.net.packet.in;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.LoyaltyTitle;
import org.gielinor.game.system.SystemManager;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.ChatMessage;
import org.gielinor.game.world.update.flag.player.ChatFlag;
import org.gielinor.mqueue.message.impl.PublicChatMessage;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.utilities.string.TextUtils;

/**
 * Handles public chat messages.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ChatPacketHandler implements IncomingPacket {

    /**
     * Represents the chat entry logger.
     */
    private static final ChatEntryLogger ENTRY_LOGGER = new ChatEntryLogger();

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        player = PlayerOptionPacketHandler.getPlayer(player);
        int effects = packet.getS() & 0xFF;
        int colour = packet.getS() & 0xFF;
        int numChars = packet.toByteBuffer().limit() - 2;
        byte[] rawChatData = new byte[numChars];
        packet.get(rawChatData);
        byte[] chatData = new byte[numChars];
        for (int i = 0; i < numChars; i++) {
            chatData[i] = (byte) (rawChatData[numChars - i - 1] - 128);
        }
        String unpacked = TextUtils.decode(chatData, numChars);
        unpacked = TextUtils.filterText(unpacked); // Can probably remove
        byte[] packed = new byte[numChars];
        TextUtils.encode(packed, unpacked);

        NPC npc = NPCOptionPacketHandler.getNPC(player);
        if (npc != null && unpacked.startsWith("-")) {
            for (Player player1 : RegionManager.getLocalPlayers(npc, 15)) {
                if (player1 == null) {
                    continue;
                }
                player1.getActionSender().sendMessage(npc.getName() + ": <col=255>" + unpacked.substring(1));
            }
            npc.sendChat(unpacked.substring(1));
            return;
        }
        if (player.getDetails().getPortal().getMute().isPunished()) {
            player.getActionSender().sendMessage(player.getDetails().getPortal().getMute().toString());
            return;
        }
        ENTRY_LOGGER.log(player, unpacked);
        ChatMessage chatMessage = new ChatMessage(player, colour, effects, packed);

        if (unpacked.startsWith("/") && player.getCommunication().getClan() != null) {
            player.getCommunication().getClan().message(player, unpacked);
            return;
        }
        if (SystemManager.isActive()) {
            World.submit(new PublicChatMessage(player, chatMessage));
        }
        // meme http://i.imgur.com/cQLMgJx.png
        if (unpacked.equalsIgnoreCase("Harry is the best")) {
            if (!LoyaltyTitle.THE_TRUTHFUL.isUnlocked(player)) {
                player.getActionSender().sendMessage("You have unlocked the" + LoyaltyTitle.THE_TRUTHFUL.getFormattedTitle(player, false, null) + " title!");
                LoyaltyTitle.THE_TRUTHFUL.unlock(player);
            }
        }
        if (unpacked.equalsIgnoreCase("I am Logan")) {
            if (!LoyaltyTitle.WHO.isUnlocked(player)) {
                player.getActionSender().sendMessage("You have unlocked the" + LoyaltyTitle.WHO.getFormattedTitle(player, false, null) + " title!");
                LoyaltyTitle.WHO.unlock(player);
            }
        }
        if (unpacked.equalsIgnoreCase("Dude where's my deob?")) {
            if (!LoyaltyTitle.THE_WISHFUL.isUnlocked(player)) {
                player.getActionSender().sendMessage("You have unlocked the" + LoyaltyTitle.THE_WISHFUL.getFormattedTitle(player, false, null) + " title!");
                LoyaltyTitle.THE_WISHFUL.unlock(player);
            }
        }
        if (unpacked.equalsIgnoreCase("Where have the tables gone?")) {
            if (!LoyaltyTitle.THE_TOOL.isUnlocked(player)) {
                player.getActionSender().sendMessage("You have unlocked the" + LoyaltyTitle.THE_TOOL.getFormattedTitle(player, false, null) + " title!");
                LoyaltyTitle.THE_TOOL.unlock(player);
            }
        }

        player.getUpdateMasks().register(new ChatFlag(chatMessage));
    }

    /**
     * Gets the chat entry logger.
     *
     * @return the logger.
     */

    public static ChatEntryLogger getChatEntryLogger() {
        return ENTRY_LOGGER;
    }

    /**
     * Represents a class used to log chat entrys.
     *
     * @author 'Vexia
     */
    public static final class ChatEntryLogger {

        /**
         * Represents the date format to use.
         */
        private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        /**
         * Represents the list of chat entrys.
         */
        private static final List<ChatEntry> ENTRYS = new ArrayList<>();

        /**
         * Represents the time between updates.
         */
        private static final int UPDATE_TIME = 120000;

        /**
         * Represents the last update occurence.
         */
        private static long lastUpdate;

        /**
         * Constructs a new {@code ChatEntryLogger} {@code Object}.
         */
        public ChatEntryLogger() {
        }

        /**
         * Method used to update the logger and clears all entrys from 60 seconds ago.
         */
        public void update() {
            Iterator<ChatEntry> it = ENTRYS.iterator();
            ChatEntry entry;
            while (it.hasNext()) {
                entry = it.next();
                if (entry.getTimeStamp().getTime() < lastUpdate) {
                    it.remove();
                }
            }
            setUpdated();
        }

        /**
         * Method used to log a chat message.
         *
         * @param player  the player.
         * @param message the message.
         */
        public void log(final Player player, final String message) {
            player.getSavedData().getGlobalData().setChatPing(System.currentTimeMillis() + UPDATE_TIME);
            ENTRYS.add(ChatEntry.create(player, message));
            if (needsUpdate()) {
                update();
            }
        }

        /**
         * Gets the chat entrys of a player.
         *
         * @param player the player.
         * @return the chat entrys.
         */
        public List<ChatEntry> getEntrys(final Player player) {
            List<ChatEntry> entrys = new ArrayList<>();
            for (ChatEntry entry : ENTRYS) {
                if (entry.getName().equals(player.getUsername())) {
                    entrys.add(entry);
                }
            }
            return entrys;
        }

        /**
         * Gets the organized chat entrys.
         *
         * @param first  the first.
         * @param second the second.
         * @return the list of them organized.
         */
        public List<ChatEntry> getOrganized(List<ChatEntry> first, List<ChatEntry> second) {
            List<ChatEntry> organized = new ArrayList<>();
            organized.addAll(first);
            organized.addAll(second);
            Collections.sort(organized, Comparator.comparing(ChatEntry::getTimeStamp));
            return organized;
        }

        /**
         * A wrapper method for getting the organized entrys.
         *
         * @param player the player.
         * @param other  the other.
         * @return the entrys.
         */
        public List<ChatEntry> getOrganized(final Player player, final Player other) {
            return getOrganized(getEntrys(player), getEntrys(other));
        }

        /**
         * Checks if the logger needs an update.
         *
         * @return <code>True</code> if so.
         */
        public boolean needsUpdate() {
            return lastUpdate < System.currentTimeMillis();
        }

        /**
         * Sets the last update to two minutes ahead.
         */
        public void setUpdated() {
            ChatEntryLogger.lastUpdate = System.currentTimeMillis() + UPDATE_TIME;
        }

        /**
         * Gets the entrys.
         *
         * @return The entrys.
         */
        public static List<ChatEntry> getEntrys() {
            return ENTRYS;
        }

        /**
         * static block to set timezone.
         */
        static {
            DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
        }

        /**
         * Represents a chat entry.
         *
         * @author 'Vexia
         * @version 1.0
         */
        public static final class ChatEntry {

            /**
             * Represents the timestamp of the entry.
             */
            private final Date timeStamp;

            /**
             * Represents the name of the player.
             */
            private final String name;

            /**
             * Represents the message of the entry.
             */
            private final String message;

            /**
             * Constructs a new {@code ChatEntry} {@code Object}.
             *
             * @param timeStamp the timeStamp.
             * @param name      the name.
             * @param message   the message.
             */
            public ChatEntry(final Date timeStamp, final String name, final String message) {
                this.timeStamp = timeStamp;
                this.name = name;
                this.message = message;
            }

            /**
             * Creates a chat entry.
             *
             * @param player the player.
             * @param string the string.
             * @return the entry.
             */
            public static ChatEntry create(final Player player, final String string) {
                return new ChatEntry(new Date(), player.getUsername(), string);
            }

            @Override
            public String toString() {
                return "[" + DATE_FORMAT.format(timeStamp) + "]" + "[" + name + "] " + message;
            }

            /**
             * Gets the timeStamp.
             *
             * @return The timeStamp.
             */
            public Date getTimeStamp() {
                return timeStamp;
            }

            /**
             * Gets the message.
             *
             * @return The message.
             */
            public String getMessage() {
                return message;
            }

            /**
             * Gets the name.
             *
             * @return The name.
             */
            public String getName() {
                return name;
            }
        }
    }

}
