package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.system.communication.ClanCommunication;
import org.gielinor.game.system.communication.ClanRank;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContactContext;
import org.gielinor.net.packet.out.ContactPackets;
import org.gielinor.utilities.misc.RunScript;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the {@link org.gielinor.net.packet.IncomingPacket} for a clan chat setting.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ClanChatPacketHandler implements IncomingPacket {

    /**
     * The clan chat setting packet opcode.
     */
    private static final int CLAN_CHAT_SETTING = 93;
    /**
     * The clan chat friend rank opcode.
     */
    private static final int CLAN_CHAT_FRIEND_RANK = 242;

    @Override
    public void decode(Player player, int opcode, PacketBuilder packetBuilder) {
        final ClanCommunication clanCommunication = ClanCommunication.get(player.getName(), true);
        if (clanCommunication == null) {
            player.getActionSender().sendMessage("Something went wrong, please report this on forums with : " + opcode);
            return;
        }
        switch (opcode) {
            case CLAN_CHAT_SETTING:
                byte settingId = (byte) packetBuilder.get();
                byte settingValue = (byte) packetBuilder.get();
                switch (settingId) {
                    /*
                     * Clan chat name.
                     */
                    case 0:
                        switch (settingValue) {
                            case 1:
                                clanCommunication.setName("Chat disabled");
                                clanCommunication.clean(true);
                                player.getActionSender().sendString(clanCommunication.getName(), 32343);
                                break;
                            case 2:
                                player.setAttribute("runscript", new RunScript() {

                                    @Override
                                    public boolean handle() {
                                        String name = TextUtils.formatDisplayName((String) value);
                                        if (name.isEmpty()) {
                                            name = "Chat disabled";
                                        }
                                        if (!name.equals("Chat disabled")) {
                                            name = name.length() > 12 ? name.substring(0, 12) : name;
                                            if (clanCommunication.getName().equals("Chat disabled")) {
                                                player.getActionSender().sendMessage("Your clan channel has now been enabled!");
                                                player.getActionSender().sendMessage("Join your channel by clicking 'Join Chat' and typing: " + player.getUsername());
                                            }
                                        } else {
                                            clanCommunication.clean(true);
                                        }
                                        clanCommunication.setName(name);
                                        player.getActionSender().sendString(name, 32343);
                                        clanCommunication.update();
                                        return true;
                                    }
                                });
                                player.getDialogueInterpreter().sendInput(true, "Enter clan prefix:");
                                break;
                        }
                        break;
                    /*
                     * Clan chat join rank.
                     */
                    case 3:
                        clanCommunication.setJoinRequirement(ClanRank.forSettingValue(settingValue));
                        player.getActionSender().sendString(clanCommunication.getJoinRequirement().getInfo(), 32344);
                        break;
                    /*
                     * Clan chat message rank.
                     */
                    case 6:
                        clanCommunication.setMessageRequirement(ClanRank.forSettingValue(settingValue));
                        player.getActionSender().sendString(clanCommunication.getMessageRequirement().getInfo(), 32345);
                        break;
                    /*
                     * Clan chat kick rank.
                     */
                    case 9:
                        clanCommunication.setKickRequirement(ClanRank.forSettingValue(settingValue));
                        player.getActionSender().sendString(clanCommunication.getKickRequirement().getInfo(), 32346);
                        break;
                }
                if (player.isDebug()) {
                    player.getActionSender().sendDebugPacket(opcode, "ClanChatSetting", "Setting ID: " + settingId, "Setting Value: " + settingValue);
                }
                break;
            case CLAN_CHAT_FRIEND_RANK:
                byte rankId = (byte) packetBuilder.get();
                long playerNameLong = packetBuilder.getLong();
                String playerName = TextUtils.longToString(playerNameLong);
                clanCommunication.rank(playerName, ClanRank.forValue(rankId));
                PacketRepository.send(ContactPackets.class, new ContactContext(player, playerName, player.getCommunication().showActive(playerName)));
                if (player.isDebug()) {
                    player.getActionSender().sendDebugPacket(opcode, "ClanChatFriendRank", "Rank ID: " + rankId, "Player name Long: " + playerNameLong, "Player name: " + TextUtils.longToString(playerNameLong));
                }
                break;
        }
    }
}
