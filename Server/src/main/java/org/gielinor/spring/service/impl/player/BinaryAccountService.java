package org.gielinor.spring.service.impl.player;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.combat.CombatSpell;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.login.Response;
import org.gielinor.game.node.entity.player.link.SpellBookManager;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.system.communication.ClanCommunication;
import org.gielinor.game.system.communication.ClanRank;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.config.DatabaseDetails;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.spring.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.io.Closeables;

/**
 * An {@link org.gielinor.spring.service.AccountService} extension for loading
 * and saving players via binary files.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *
 */
@Service("binaryAccountService")
public class BinaryAccountService extends AccountService {

    private static final Logger log = LoggerFactory.getLogger(BinaryAccountService.class);

    /**
     * The end of file opcode.
     */
    public static final int EOF = 0;

    /**
     * The player's location opcode.
     */
    private static final int LOCATION = 1;

    /**
     * The player's inventory opcode.
     */
    private static final int INVENTORY = 2;

    /**
     * The player's equipment opcode.
     */
    private static final int EQUIPMENT = 3;

    /**
     * The player's bank opcode.
     */
    private static final int BANK = 4;

    /**
     * The player's skills opcode.
     */
    private static final int SKILLS = 5;

    /**
     * The player's settings opcode.
     */
    private static final int SETTINGS = 6;

    /**
     * The player's emotes opcode.
     */
    private static final int EMOTES = 7;

    /**
     * The player's appearance opcode.
     */
    private static final int APPEARANCE = 8;

    /**
     * The player's attributes.
     */
    private static final int ATTRIBUTES = 9;

    /**
     * The player's Slayer.
     */
    private static final int SLAYER = 10;

    /**
     * The player's quests.
     */
    private static final int QUESTS = 11;

    /**
     * The player's achievements.
     */
    private static final int ACHIEVEMENTS = 12;

    /**
     * The player's spellbook.
     */
    private static final int SPELLBOOK = 13;

    /**
     * The player's music.
     */
    private static final int MUSIC = 14;

    /**
     * The player's configuration.
     */
    private static final int CONFIGURATION = 15;

    /**
     * The player's interface configuration.
     */
    private static final int INTERFACE_CONFIGURATION = 16;

    /**
     * The player's autocast.
     */
    private static final int AUTOCAST = 17;

    /**
     * The player's saved data.
     */
    private static final int SAVED_DATA = 18;

    /**
     * The player's Barcrawl.
     */
    private static final int BARCRAWL = 19;

    /**
     * The player's Farming.
     */
    private static final int FARMING = 20;

    /**
     * The player's monitor.
     */
    private static final int MONITOR = 21;

    /**
     * The player's quick prayers.
     */
    private static final int QUICK_PRAYERS = 22;

    /**
     * The player's treasure trail.
     */
    private static final int TREASURE_TRAIL = 23;

    /**
     * The player's states.
     */
    private static final int STATES = 24;

    /**
     * The player's {@link org.gielinor.game.content.anticheat.AntiMacroEvent}.
     */
    private static final int ANTIMACRO_EVENT = 25;

    /**
     * The player's
     * {@link org.gielinor.game.content.skill.member.summoning.familiar.FamiliarManager}.
     */
    private static final int FAMILIAR = 27;

    /**
     * The player's
     * {@link org.gielinor.game.node.entity.player.info.PerkManager}.
     */
    private static final int PERK = 28;

    /**
     * The player's
     * {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle}.
     */
    private static final int LOYALTY_TITLE = 29;

    /**
     * The player's
     * {@link org.gielinor.game.node.entity.player.info.donor.DonorManager}.
     */
    private static final int DONOR = 30;

    /**
     * The player's {@link org.gielinor.rs2.model.container.impl.bank.BankData}.
     */
    private static final int BANK_DATA = 31;

    /**
     * The player's
     * {@link org.gielinor.game.content.eco.grandexchange.GrandExchange}.
     */
    private static final int GRAND_EXCHANGE = 32;

    /**
     * The player's
     * {@link org.gielinor.game.node.entity.player.info.referral.ReferralManager}.
     */
    private static final int REFERRAL_MANAGER = 33;

    /**
     * The player's
     * {@link org.gielinor.game.content.skill.member.construction.HouseManager}.
     */
    private static final int HOUSE_MANAGER = 34;

    @Override
    public void savePlayer(Player player) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096 << 2);

        long networth = 0;

        byteBuffer.put((byte) LOCATION);
        byteBuffer.putShort((short) player.getLocation().getX());
        byteBuffer.putShort((short) player.getLocation().getY());
        byteBuffer.put((byte) player.getLocation().getZ());

        if (!player.getInventory().isEmpty()) {
            networth += player.getInventory().save(byteBuffer.put((byte) INVENTORY));
        }

        if (!player.getEquipment().isEmpty()) {
            networth += player.getEquipment().save(byteBuffer.put((byte) EQUIPMENT));
        }

        if (!player.getBank().isEmpty()) {
            networth += player.getBank().save(byteBuffer.put((byte) BANK));
        }

        player.getSkills().save(byteBuffer.put((byte) SKILLS));

        player.getSettings().save(byteBuffer.put((byte) SETTINGS));

        player.getEmotes().save(byteBuffer.put((byte) EMOTES));

        player.getAppearance().save(byteBuffer.put((byte) APPEARANCE));

        if (!player.getGameAttributes().getSavedAttributes().isEmpty()) {
            player.getGameAttributes().dump(byteBuffer.put((byte) ATTRIBUTES));
        }

        player.getSlayer().save(byteBuffer.put((byte) SLAYER));

        player.getQuestRepository().save(byteBuffer.put((byte) QUESTS));

        if (player.getAchievementRepository().getDiaries().entrySet().size() > 0) {
            player.getAchievementRepository().save(byteBuffer.put((byte) ACHIEVEMENTS));
        }

        player.getSpellBookManager().save(byteBuffer.put((byte) SPELLBOOK));

        player.getMusicPlayer().save(byteBuffer.put((byte) MUSIC));

        player.getConfigManager().save(byteBuffer.put((byte) CONFIGURATION));

        player.getInterfaceState().save(byteBuffer.put((byte) INTERFACE_CONFIGURATION));

        if (player.getProperties().getAutocastSpell() != null) {
            CombatSpell spell = player.getProperties().getAutocastSpell();
            byteBuffer.put((byte) AUTOCAST);
            byteBuffer.put((byte) spell.getBook().ordinal());
            byteBuffer.putInt(spell.getSpellId());
        }

        player.getSavedData().save(byteBuffer.put((byte) SAVED_DATA));

        player.getBarcrawlManager().save(byteBuffer.put((byte) BARCRAWL));

        player.getFarmingManager().save(byteBuffer.put((byte) FARMING));

        player.getMonitor().checkNetworth(player, networth);
        player.getMonitor().save(byteBuffer.put((byte) MONITOR));

        byteBuffer.put((byte) QUICK_PRAYERS);

        byteBuffer.putInt(player.getPrayer().getQuickPrayer().getQuickPrayers().size());

        for (PrayerType prayerType : player.getPrayer().getQuickPrayer().getQuickPrayers()) {
            byteBuffer.put((byte) prayerType.ordinal());
        }

        if (player.getTreasureTrailManager().hasTrail()) {
            player.getTreasureTrailManager().save(byteBuffer.put((byte) TREASURE_TRAIL));
        }

        if (player.getStateManager().isSaveRequired()) {
            player.getStateManager().save(byteBuffer.put((byte) STATES));
        }

        if (player.getAntiMacroHandler().isSaveRequired()) {
            player.getAntiMacroHandler().save(byteBuffer.put((byte) ANTIMACRO_EVENT));
        }

        player.getFamiliarManager().save(byteBuffer.put((byte) FAMILIAR));

        if (player.getPerkManager().getPerks().size() > 0) {
            player.getPerkManager().save(byteBuffer.put((byte) PERK));
        }

        if (player.getTitleManager().getUnlockedTitles().size() > 0) {
            player.getTitleManager().save(byteBuffer.put((byte) LOYALTY_TITLE));
        }

        player.getDonorManager().save(byteBuffer.put((byte) DONOR));

        player.getBank().getBankData().save(byteBuffer.put((byte) BANK_DATA));

        player.getGrandExchange().save(byteBuffer.put((byte) GRAND_EXCHANGE));

        player.getReferralManager().save(byteBuffer.put((byte) REFERRAL_MANAGER));

        player.getHouseManager().save(byteBuffer.put((byte) HOUSE_MANAGER));

        /**
         * Done with writing.
         */
        byteBuffer.put((byte) 0);
        byteBuffer.flip();
        File file = new File(Constants.BINARY_PLAYER_SAVE_DIRECTORY + player.getName() + ".dat");
        RandomAccessFile randomAccessFile = null;
        FileChannel fileChannel = null;
        boolean exceptionThrown = true;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            fileChannel = randomAccessFile.getChannel();
            fileChannel.write(byteBuffer);
            exceptionThrown = false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.gc();
                Closeables.close(fileChannel, exceptionThrown);
                Closeables.close(randomAccessFile, exceptionThrown);
                System.gc();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveMySQL(player);
    }

    /**
     * Saves additional information for a player via MySQL.
     *
     * @param player
     *            The player.
     */
    private static void saveMySQL(Player player) {
        String dbn = DatabaseDetails.getGameSingleton().dbname;
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM " + dbn + ".player_friend_list WHERE pidn=?")) {
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.executeUpdate();
            }
            ClanCommunication clanCommunication = ClanCommunication.get(player.getName());
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO " + dbn + ".player_friend_list VALUES(?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int pidn : player.getCommunication().getContacts().keySet()) {
                    preparedStatement.setInt(2, pidn);
                    if (clanCommunication == null || clanCommunication.getClanMember(pidn) == null) {
                        preparedStatement.setByte(3, (byte) ClanRank.NOT_IN_CLAN.ordinal());
                    } else {
                        preparedStatement.setByte(3,
                            (byte) clanCommunication.getClanMember(pidn).getClanRank().ordinal());
                    }
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM " + dbn + ".player_ignore_list WHERE pidn=?")) {
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO " + dbn + ".player_ignore_list VALUES(?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int pidn : player.getCommunication().getBlocked().keySet()) {
                    preparedStatement.setInt(2, pidn);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
        } catch (SQLException | IOException ex) {
            log.error("Failed to save MySQL for [{}].", player.getName(), ex);
        }
    }

    @Override
    public Response loadPlayer(Player player) {
        File file = new File(Constants.BINARY_PLAYER_SAVE_DIRECTORY + player.getName() + ".dat");

        if (!file.exists()) {
            log.info("File for [{}] does not exist. Attempting to convert.", player.getName());
            file = new File(Constants.BINARY_PLAYER_SAVE_DIRECTORY + "convert" + File.separator + player.getName() + ".dat");
            if (file.exists()) {
                player.setAttribute("convert_account", true);
            }
        }

        if (!file.exists()) {
            log.info("New character created [{}].", player.getName());
            return Response.LOGIN_OK;
        }

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
             FileChannel fileChannel = randomAccessFile.getChannel()) {
            ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            int opcode;
            int length;
            long networth = 0;
            int[] opcodeHistory = new int[5];

            while ((opcode = byteBuffer.get() & 0xFF) != 0) {
                switch (opcode) {
                    case LOCATION:
                        int x = byteBuffer.getShort() & 0xFFFF;
                        int y = byteBuffer.getShort() & 0xFFFF;
                        int z = byteBuffer.get() & 0xFF;
                        player.setLocation(new Location(x, y, z));
                        break;
                    case INVENTORY:
                        networth += player.getInventory().parse(byteBuffer);
                        break;
                    case EQUIPMENT:
                        networth += player.getEquipment().parse(byteBuffer);
                        break;
                    case BANK:
                        networth += player.getBank().parse(byteBuffer);
                        break;
                    case SKILLS:
                        player.getSkills().parse(byteBuffer);
                        break;
                    case SETTINGS:
                        player.getSettings().parse(byteBuffer);
                        break;
                    case EMOTES:
                        player.getEmotes().parse(byteBuffer);
                        break;
                    case APPEARANCE:
                        player.getAppearance().parse(byteBuffer);
                        break;
                    case ATTRIBUTES:
                        player.getGameAttributes().parse(byteBuffer);
                        break;
                    case SLAYER:
                        player.getSlayer().parse(byteBuffer);
                        break;
                    case QUESTS:
                        player.getQuestRepository().parse(byteBuffer);
                        break;
                    case ACHIEVEMENTS:
                        player.getAchievementRepository().parse(byteBuffer);
                        break;
                    case SPELLBOOK:
                        player.getSpellBookManager().parse(byteBuffer);
                        break;
                    case MUSIC:
                        player.getMusicPlayer().parse(byteBuffer);
                        break;
                    case CONFIGURATION:
                        player.getConfigManager().parse(byteBuffer);
                        break;
                    case INTERFACE_CONFIGURATION:
                        player.getInterfaceState().parse(byteBuffer);
                        break;
                    case AUTOCAST:
                        player.getProperties().setAutocastSpell((CombatSpell) SpellBookManager.SpellBook.values()[byteBuffer.get()].getSpell(byteBuffer.getInt() & 0xFFFF));
                        break;
                    case SAVED_DATA:
                        player.getSavedData().parse(byteBuffer);
                        break;
                    case BARCRAWL:
                        player.getBarcrawlManager().parse(byteBuffer);
                        break;
                    case FARMING: // TODO
                        player.getFarmingManager().parse(byteBuffer);
                        break;
                    case MONITOR:
                        player.getMonitor().parse(byteBuffer);
                        break;
                    case QUICK_PRAYERS:
                        length = byteBuffer.getInt();
                        while (length > 0) {
                            PrayerType prayerType = PrayerType.forOrdinal(byteBuffer.get());
                            if (prayerType != null) {
                                player.getPrayer().getQuickPrayer().getQuickPrayers().add(prayerType);
                            }
                            length--;
                        }
                        break;
                    case TREASURE_TRAIL:
                        player.getTreasureTrailManager().parse(byteBuffer);
                        break;
                    case STATES:
                        player.getStateManager().parse(byteBuffer);
                        break;
                    case ANTIMACRO_EVENT:
                        player.getAntiMacroHandler().parse(byteBuffer);
                        break;
                    case 26:
                        break;
                    case FAMILIAR: // TODO - so save in player_familiar then
                        // TODO player_familiar_container (pidn, familiar_id,
                        // item_slot, item_id, item_count, item_charge)
                        player.getFamiliarManager().parse(byteBuffer);
                        break;
                    case PERK:
                        player.getPerkManager().parse(byteBuffer);
                        break;
                    case LOYALTY_TITLE:
                        player.getTitleManager().parse(byteBuffer);
                        break;
                    case DONOR:
                        player.getDonorManager().parse(byteBuffer);
                        break;
                    case BANK_DATA:
                        player.getBank().getBankData().parse(byteBuffer);
                        break;
                    case GRAND_EXCHANGE:
                        player.getGrandExchange().parse(byteBuffer);
                        break;
                    case REFERRAL_MANAGER:
                        player.getReferralManager().parse(byteBuffer);
                        break;
                    case HOUSE_MANAGER:
                        player.getHouseManager().parse(byteBuffer);
                        break;
                    /*
                     * Unhandled opcodes.
                     */
                    default:
                        log.warn("Encountered unknown opcode [{}] during player read.", opcode);
                        break;
                }
                System.arraycopy(opcodeHistory, 0, opcodeHistory, 1, opcodeHistory.length - 2 + 1);
                opcodeHistory[0] = opcode;
            }
            player.getMonitor().setNetworth(networth);
        } catch (Exception ex) {
            log.error("Exception while reading charsave: [{}].", player.getName(), ex);
        }

        loadMySQL(player);
        return Response.LOGIN_OK;
    }

    public Response loadPlayer(Player player, String directory) {
        File file = new File(directory + File.separator + player.getName() + ".dat");
        player.setAttribute("convert_account", true);
        boolean exceptionThrown = true;

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
             FileChannel fileChannel = randomAccessFile.getChannel()) {
            ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            int opcode;
            int length;
            long networth = 0;
            int[] opcodeHistory = new int[5];

            while ((opcode = byteBuffer.get() & 0xFF) != 0) {
                switch (opcode) {
                    case LOCATION:
                        int x = byteBuffer.getShort() & 0xFFFF;
                        int y = byteBuffer.getShort() & 0xFFFF;
                        int z = byteBuffer.get() & 0xFF;
                        player.setLocation(new Location(x, y, z));
                        break;
                    case INVENTORY:
                        networth += player.getInventory().parse(byteBuffer);
                        break;
                    case EQUIPMENT:
                        networth += player.getEquipment().parse(byteBuffer);
                        break;
                    case BANK:
                        networth += player.getBank().parse(byteBuffer);
                        break;
                    case SKILLS:
                        player.getSkills().parse(byteBuffer);
                        break;
                    case SETTINGS:
                        player.getSettings().parse(byteBuffer);
                        break;
                    case EMOTES:
                        player.getEmotes().parse(byteBuffer);
                        break;
                    case APPEARANCE:
                        player.getAppearance().parse(byteBuffer);
                        break;
                    case ATTRIBUTES:
                        player.getGameAttributes().parse(byteBuffer);
                        break;
                    case SLAYER:
                        player.getSlayer().parse(byteBuffer);
                        break;
                    case QUESTS:
                        player.getQuestRepository().parse(byteBuffer);
                        break;
                    case ACHIEVEMENTS:
                        player.getAchievementRepository().parse(byteBuffer);
                        break;
                    case SPELLBOOK:
                        player.getSpellBookManager().parse(byteBuffer);
                        break;
                    case MUSIC:
                        player.getMusicPlayer().parse(byteBuffer);
                        break;
                    case CONFIGURATION:
                        player.getConfigManager().parse(byteBuffer);
                        break;
                    case INTERFACE_CONFIGURATION:
                        player.getInterfaceState().parse(byteBuffer);
                        break;
                    case AUTOCAST:
                        player.getProperties()
                            .setAutocastSpell((CombatSpell) SpellBookManager.SpellBook.values()[byteBuffer.get()]
                                .getSpell(byteBuffer.getInt() & 0xFFFF));
                        break;
                    case SAVED_DATA:
                        player.getSavedData().parse(byteBuffer);
                        break;
                    case BARCRAWL:
                        player.getBarcrawlManager().parse(byteBuffer);
                        break;
                    case FARMING: // TODO
                        player.getFarmingManager().parse(byteBuffer);
                        break;
                    case MONITOR:
                        player.getMonitor().parse(byteBuffer);
                        break;
                    case QUICK_PRAYERS:
                        length = byteBuffer.getInt();
                        while (length > 0) {
                            PrayerType prayerType = PrayerType.forOrdinal(byteBuffer.get());
                            if (prayerType != null) {
                                player.getPrayer().getQuickPrayer().getQuickPrayers().add(prayerType);
                            }
                            length--;
                        }
                        break;
                    case TREASURE_TRAIL:
                        player.getTreasureTrailManager().parse(byteBuffer);
                        break;
                    case STATES:
                        player.getStateManager().parse(byteBuffer);
                        break;
                    case ANTIMACRO_EVENT:
                        player.getAntiMacroHandler().parse(byteBuffer);
                        break;
                    case 26:
                        break;
                    case FAMILIAR: // TODO - so save in player_familiar then
                        // TODO player_familiar_container (pidn, familiar_id,
                        // item_slot, item_id, item_count, item_charge)
                        player.getFamiliarManager().parse(byteBuffer);
                        break;
                    case PERK:
                        player.getPerkManager().parse(byteBuffer);
                        break;
                    case LOYALTY_TITLE:
                        player.getTitleManager().parse(byteBuffer);
                        break;
                    case DONOR:
                        player.getDonorManager().parse(byteBuffer);
                        break;
                    case BANK_DATA:
                        player.getBank().getBankData().parse(byteBuffer);
                        break;
                    case GRAND_EXCHANGE:
                        player.getGrandExchange().parse(byteBuffer);
                        break;
                    case REFERRAL_MANAGER:
                        player.getReferralManager().parse(byteBuffer);
                        break;
                    case HOUSE_MANAGER:
                        player.getHouseManager().parse(byteBuffer);
                        break;
                    /*
                     * Unhandled opcodes.
                     */
                    default:
                        log.warn("Encountered unknown opcode [{}] during player read!", opcode);
                        break;
                }

                System.arraycopy(opcodeHistory, 0, opcodeHistory, 1, opcodeHistory.length - 2 + 1);
                opcodeHistory[0] = opcode;
            }
            player.getMonitor().setNetworth(networth);
        } catch (Exception ex) {
            log.error("Failed to read player save [{}].", player.getName(), ex);
        }

        World.submit(new Pulse(5) {

            @Override
            public boolean pulse() {
                try {
                    Files.delete(file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        loadMySQL(player);
        return Response.LOGIN_OK;
    }

    /**
     * Loads additional information for a player via MySQL.
     *
     * @param player
     *            The player.
     */
    private static void loadMySQL(Player player) {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT friend_pidn, username FROM " + DatabaseDetails.getGameSingleton().dbname + ".player_friend_list LEFT JOIN "
                    + DatabaseDetails.getGameSingleton().dbname + ".player_detail ON friend_pidn = player_detail.pidn WHERE "
                    + DatabaseDetails.getGameSingleton().dbname + ".player_friend_list.pidn=?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        if (resultSet.getString("username") == null) {
                            continue;
                        }
                        player.getCommunication().getContacts().put(resultSet.getInt("friend_pidn"),
                            resultSet.getString("username"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT ignore_pidn, username FROM "
                + DatabaseDetails.getGameSingleton().dbname + ".player_ignore_list LEFT JOIN player_detail ON ignore_pidn = "
                + DatabaseDetails.getGameSingleton().dbname + ".player_detail.pidn WHERE player_ignore_list.pidn=?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        if (resultSet.getString("username") == null) {
                            continue;
                        }
                        player.getCommunication().getBlocked().put(resultSet.getInt("ignore_pidn"),
                            resultSet.getString("username"));
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Failed to save MySQL for [{}].", player.getName(), ex);
        }
    }

}
