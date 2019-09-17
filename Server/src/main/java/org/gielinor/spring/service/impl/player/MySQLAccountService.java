package org.gielinor.spring.service.impl.player;

import com.google.common.primitives.Booleans;
import org.gielinor.database.DataSource;
import org.gielinor.game.content.eco.grandexchange.offer.GEOfferDispatch;
import org.gielinor.game.content.eco.grandexchange.offer.GrandExchangeOffer;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.AchievementState;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueScrollPlugin;
import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.content.global.quest.QuestState;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.farming.FarmingManager;
import org.gielinor.game.content.skill.member.farming.compost.CompostBin;
import org.gielinor.game.content.skill.member.farming.compost.CompostManager;
import org.gielinor.game.content.skill.member.farming.wrapper.PatchWrapper;
import org.gielinor.game.content.skill.member.slayer.Master;
import org.gielinor.game.content.skill.member.slayer.Task;
import org.gielinor.game.content.skill.member.slayer.Tasks;
import org.gielinor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarManager;
import org.gielinor.game.content.skill.member.summoning.pet.Pet;
import org.gielinor.game.content.skill.member.summoning.pet.PetDetails;
import org.gielinor.game.content.skill.member.summoning.pet.Pets;
import org.gielinor.game.node.entity.combat.CombatSpell;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.LoyaltyTitle;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.entity.player.info.Settings;
import org.gielinor.game.node.entity.player.info.donor.DonorStatus;
import org.gielinor.game.node.entity.player.info.login.Response;
import org.gielinor.game.node.entity.player.info.login.Starter;
import org.gielinor.game.node.entity.player.info.referral.Referred;
import org.gielinor.game.node.entity.player.link.*;
import org.gielinor.game.node.entity.player.link.appearance.Gender;
import org.gielinor.game.node.entity.player.link.music.MusicEntry;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.node.entity.state.StatePulse;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.communication.ClanCommunication;
import org.gielinor.game.system.communication.ClanRank;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.config.Constants;
import org.gielinor.spring.service.AccountService;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import plugin.activity.duelarena.DuelRule;
import plugin.interaction.inter.EmoteTabInterface.Emote;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * An {@link org.gielinor.spring.service.AccountService} extension for loading
 * and saving players via MySQL in bulk.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
@Service("mysqlAccountService")
public class MySQLAccountService extends AccountService {

    private static final Logger log = LoggerFactory.getLogger(MySQLAccountService.class);

    @Override
    public void savePlayer(Player player) {
        long startTime = System.nanoTime();

        try (Connection forumConnection = DataSource.getForumConnection();
             PreparedStatement preparedStatement = forumConnection.prepareStatement(
                 "INSERT INTO NEWcore_members (member_id, member_title) VALUES (?, ?) ON DUPLICATE KEY UPDATE member_title = VALUES(member_title)")) {
            preparedStatement.setInt(1, player.getPidn());
            preparedStatement.setString(2, player.getTitleManager().getLoyaltyTitle() == null ? "" : player.getTitleManager().getLoyaltyTitle().getForumTitle(player));
        } catch (IOException | SQLException ex) {
            log.error("Error inserting into [NEWcore_members] for [{}].", player.getName(), ex);
        }

        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_location (pidn, x, y, z) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE x = VALUES(x), y = VALUES(y), z = VALUES(z)")) {
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setInt(2, player.getLocation().getX());
                preparedStatement.setInt(3, player.getLocation().getY());
                preparedStatement.setInt(4, player.getLocation().getZ());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_inventory WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_inventory (pidn, inventory_slot, item_id, item_quantity, item_charge) VALUES(?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int index = 0; index < player.getInventory().capacity(); index++) {
                    Item item = player.getInventory().get(index);
                    if (item == null) {
                        continue;
                    }
                    preparedStatement.setInt(2, index);
                    preparedStatement.setInt(3, item.getId());
                    preparedStatement.setInt(4, item.getCount());
                    preparedStatement.setInt(5, item.getCharge());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_bank WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_bank (pidn, bank_slot, item_id, item_quantity, item_charge) VALUES(?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int index = 0; index < player.getBank().capacity(); index++) {
                    Item item = player.getBank().get(index);
                    if (item == null) {
                        continue;
                    }
                    preparedStatement.setInt(2, index);
                    preparedStatement.setInt(3, item.getId());
                    preparedStatement.setInt(4, item.getCount());
                    preparedStatement.setInt(5, item.getCharge());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_equipment WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_equipment (pidn, equipment_slot, item_id, item_quantity, item_charge) VALUES(?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int index = 0; index < player.getEquipment().capacity(); index++) {
                    Item item = player.getEquipment().get(index);
                    if (item == null) {
                        continue;
                    }
                    preparedStatement.setInt(2, index);
                    preparedStatement.setInt(3, item.getId());
                    preparedStatement.setInt(4, item.getCount());
                    preparedStatement.setInt(5, item.getCharge());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_rune_inventory WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_rune_inventory (pidn, inventory_slot, item_id, item_quantity, item_charge) VALUES(?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int index = 0; index < player.getSavedData().getGlobalData().getRunePouch().capacity(); index++) {
                    Item item = player.getSavedData().getGlobalData().getRunePouch().get(index);
                    if (item == null) {
                        continue;
                    }
                    preparedStatement.setInt(2, index);
                    preparedStatement.setInt(3, item.getId());
                    preparedStatement.setInt(4, item.getCount());
                    preparedStatement.setInt(5, item.getCharge());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_global_data " + "(pidn, " + TextUtils.implode(GlobalData.FIELDS, ',')
                    + ") VALUES " + "(?, " + getFieldBinds(GlobalData.FIELDS) + ")" + "ON DUPLICATE KEY UPDATE "
                    + getUpdateFields(GlobalData.FIELDS) + ";")) {
                GlobalData globalData = player.getSavedData().getGlobalData();
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setLong(2, globalData.getChatPing());
                preparedStatement.setLong(3, globalData.getTutorClaim());
                preparedStatement.setBoolean(4, globalData.isLuthasTask());
                preparedStatement.setInt(5, globalData.getKaramjaBananas());
                preparedStatement.setLong(6, globalData.getSilkSteal());
                preparedStatement.setBoolean(7, globalData.hasTiedLumbridgeRope());
                preparedStatement.setBoolean(8, globalData.hasSpokenToApprentice());
                preparedStatement.setBoolean(9, globalData.isFritzGlass());
                preparedStatement.setInt(10, globalData.getZaffAmount());
                preparedStatement.setLong(11, globalData.getZaffTime());
                preparedStatement.setBoolean(12, globalData.isWydinEmployee());
                preparedStatement.setBoolean(13, globalData.isDraynorRecording());
                preparedStatement.setInt(14, globalData.getEssenceTeleporter());
                preparedStatement.setInt(15, globalData.getRecoilDamage());
                preparedStatement.setBoolean(16, globalData.hasPaidWaterfall());
                preparedStatement.setBoolean(17, globalData.hasPaidBlacksmith());
                preparedStatement.setLong(18, globalData.getTotalPlayTime());
                preparedStatement.setInt(19, globalData.getEctofuntusBoneType());
                preparedStatement.setBoolean(20, globalData.isEctofuntusBonesGround());
                preparedStatement.setString(21, globalData.getYellTag());

                preparedStatement.setLong(22, globalData.getLastYell());

                preparedStatement.setByte(23, globalData.getVisibility());
                preparedStatement.setBoolean(24, globalData.hasAnnouncedCombat());
                preparedStatement.setInt(25, globalData.getSmallPouch().itemCount());
                preparedStatement.setInt(26, globalData.getMediumPouch().itemCount());
                preparedStatement.setInt(27, globalData.getLargePouch().itemCount());
                preparedStatement.setInt(28, globalData.getGiantPouch().itemCount());
                preparedStatement.setInt(29, globalData.getVotingPoints());
                preparedStatement.setInt(30, globalData.getExperienceModifierTime());
                preparedStatement.setDouble(31, globalData.getExperienceModifier());
                int starterPackage = globalData.getStarterPackage() == null ? 0
                    : globalData.getStarterPackage().getId();
                preparedStatement.setByte(32, (byte) starterPackage);
                preparedStatement.setString(33, globalData.getLastClanChat());
                preparedStatement.setLong(34, globalData.getLoyaltyPoints());
                preparedStatement.setLong(35, globalData.getXpDrops());
                preparedStatement.setInt(36, globalData.getPlayerKills());
                preparedStatement.setInt(37, globalData.getPlayerDeaths());
                preparedStatement.setInt(38, globalData.getPrayerBook());
                preparedStatement.setBoolean(39, globalData.hasBetaStatus());
                preparedStatement.setInt(40, globalData.getEasterEventStage());
                preparedStatement.setBoolean(41, globalData.hasGivenDyes());
                preparedStatement.setBoolean(42, globalData.unlockedEasterTitle());
                preparedStatement.setBoolean(43, globalData.hasWalkedToDorgeshKaan());
                preparedStatement.setInt(44, globalData.getRoaldPoints());
                preparedStatement.setString(45, globalData.getLeftClickOption());
                preparedStatement.setInt(46, globalData.getLoginStreak());
                preparedStatement.setInt(47, globalData.getKillstreak());
                preparedStatement.setInt(48, globalData.getPetsInsured());
                preparedStatement.setInt(49, globalData.getNpcKillCount());
//                preparedStatement.setArray(50, connection.createArrayOf(GlobalData.FIELDS[50], Arrays.stream(globalData.getExperienceModifiersTime()).boxed().toArray()));
//                preparedStatement.setArray(51, connection.createArrayOf(GlobalData.FIELDS[51], Arrays.stream(globalData.getExperienceModifiers()).boxed().toArray()));
//
//                preparedStatement.setInt(52, globalData.getAccuracyModifierTime());
//                preparedStatement.setDouble(53, globalData.getAccuracyModifier());
//                preparedStatement.setInt(54, globalData.getStrengthModifierTime());
//                preparedStatement.setDouble(55, globalData.getStrengthModifier());
//                preparedStatement.setInt(56, globalData.getDefenceModifierTime());
//                preparedStatement.setDouble(57, globalData.getDefenceModifier());
//                preparedStatement.setInt(58, globalData.getDropModifierTime());
//                preparedStatement.setDouble(59, globalData.getDropModifier());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_stronghold_reward WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_stronghold_reward (pidn, `index`) VALUES(?, ?)")) {
                boolean[] stronghold = player.getSavedData().getGlobalData().getStrongHoldRewards();
                if (Booleans.contains(stronghold, true)) {
                    preparedStatement.setInt(1, player.getPidn());
                    for (byte index = 0; index < stronghold.length; index++) {
                        if (stronghold[index]) {
                            preparedStatement.setByte(2, index);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_milestone (pidn, next_milestone, last_milestone) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE next_milestone = VALUES(next_milestone), last_milestone = VALUES(last_milestone)")) {
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setShort(2, (short) player.getSavedData().getGlobalData().getNextMilestone());
                preparedStatement.setShort(3, (short) player.getSavedData().getGlobalData().getLastMilestone());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_duel_arena WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_duel_arena (pidn, rule_ordinal, preset) VALUES(?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                if (player.getSavedData().getGlobalData().getPresetDuelRules() != null
                    && player.getSavedData().getGlobalData().getPresetDuelRules().size() > 0) {
                    for (DuelRule duelRule : player.getSavedData().getGlobalData().getPresetDuelRules()) {
                        preparedStatement.setInt(2, duelRule.ordinal());
                        preparedStatement.setBoolean(3, true);
                        preparedStatement.addBatch();
                    }
                }
                if (player.getSavedData().getGlobalData().getLastDuelRules() != null
                    && player.getSavedData().getGlobalData().getLastDuelRules().size() > 0) {
                    for (DuelRule duelRule : player.getSavedData().getGlobalData().getLastDuelRules()) {
                        preparedStatement.setInt(2, duelRule.ordinal());
                        preparedStatement.setBoolean(3, false);
                        preparedStatement.addBatch();
                    }
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_lost_pets (pidn, lost_pet_id) VALUES(?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                if (player.getSavedData().getGlobalData().getLostPets() != null
                    && player.getSavedData().getGlobalData().getLostPets().size() > 0) {
                    for (int lostPet : player.getSavedData().getGlobalData().getLostPets()) {
                        preparedStatement.setInt(2, lostPet);
                        preparedStatement.addBatch();
                    }
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_roald_item WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_roald_item (pidn, item_id) VALUES(?, ?)")) {
                if (player.getSavedData().getGlobalData().getUnlockedRoaldsItems().size() != 0) {
                    preparedStatement.setInt(1, player.getPidn());
                    for (int itemId : player.getSavedData().getGlobalData().getUnlockedRoaldsItems()) {
                        preparedStatement.setInt(2, itemId);
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_boss_kill_log  ("
                + TextUtils.implode(BossKillLog.FIELDS, ',') + ") VALUES  (" + getFieldBinds(BossKillLog.FIELDS)
                + ") ON DUPLICATE KEY UPDATE " + getUpdateFields(BossKillLog.FIELDS) + ";")) {
                BossKillLog bossKillLog = player.getSavedData().getBossKillLog();
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setInt(2, bossKillLog.getKreeArraKills());
                preparedStatement.setInt(3, bossKillLog.getZilyanaKills());
                preparedStatement.setInt(4, bossKillLog.getGraardorKills());
                preparedStatement.setInt(5, bossKillLog.getKrilTsutKills());
                preparedStatement.setInt(6, bossKillLog.getDagannothRexKills());
                preparedStatement.setInt(7, bossKillLog.getDagannothPrimeKills());
                preparedStatement.setInt(8, bossKillLog.getDagannothSupremeKills());
                preparedStatement.setInt(9, bossKillLog.getGiantMoleKills());
                preparedStatement.setInt(10, bossKillLog.getKalphiteQueenKills());
                preparedStatement.setInt(11, bossKillLog.getKbdKills());
                preparedStatement.setInt(12, bossKillLog.getChaosElementalKills());
                preparedStatement.setInt(13, bossKillLog.getBorkKills());
                preparedStatement.setInt(14, bossKillLog.getBarrelchestKills());
                preparedStatement.setInt(15, bossKillLog.getCorporealBeastKills());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_activity_data  ("
                + TextUtils.implode(ActivityData.FIELDS, ',') + ") VALUES  (" + getFieldBinds(ActivityData.FIELDS)
                + ") ON DUPLICATE KEY UPDATE " + getUpdateFields(ActivityData.FIELDS) + ";")) {
                ActivityData activityData = player.getSavedData().getActivityData();
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setInt(2, activityData.getPestPoints());
                preparedStatement.setInt(3, activityData.getWarriorGuildTokens());
                preparedStatement.setInt(4, activityData.getBountyHunterRate());
                preparedStatement.setInt(5, activityData.getBountyRogueRate());
                preparedStatement.setInt(6, activityData.getBarrowsKillCount());
                preparedStatement.setInt(7, activityData.getRandomBarrowsBrother());
                preparedStatement.setInt(8, activityData.getBarrowsChestStatus());
                preparedStatement.setInt(9, activityData.getBarrowsChestCount());
                preparedStatement.setInt(10, activityData.getBarbarianAssaultPoints());
                preparedStatement.setInt(11, activityData.getBarrowTunnelIndex());
                preparedStatement.setBoolean(12, activityData.isElnockSupplies());
                preparedStatement.setLong(13, activityData.getLastBorkBattle());
                preparedStatement.setInt(14, activityData.getEasyClueScrolls());
                preparedStatement.setInt(15, activityData.getMediumClueScrolls());
                preparedStatement.setInt(16, activityData.getHardClueScrolls());
                preparedStatement.setInt(17, activityData.getSlayerPoints());
                preparedStatement.setInt(18, activityData.getAccumulativeSlayerTasks());
                preparedStatement.setBoolean(19, activityData.isCompletedFightCaves());
                preparedStatement.setLong(20, activityData.getLastTaskReset());
                preparedStatement.setLong(21, activityData.getLastTaskBlock());
                preparedStatement.setLong(22, activityData.getLastGunGameEntry());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_quest_data  ("
                + TextUtils.implode(QuestData.FIELDS, ',') + ") VALUES  (" + getFieldBinds(QuestData.FIELDS)
                + ") ON DUPLICATE KEY UPDATE " + getUpdateFields(QuestData.FIELDS) + ";")) {
                QuestData questData = player.getSavedData().getQuestData();
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setBoolean(2, questData.hasKilledTreeSpirit());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_skill WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_skill (pidn, skill_id, dynamic_level, static_level, experience) VALUES(?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
                    preparedStatement.setInt(2, i);
                    preparedStatement.setInt(3,
                        (int) (i == Skills.HITPOINTS ? player.getSkills().getLifepoints()
                            : i == Skills.PRAYER ? player.getSkills().getPrayerPoints()
                            : player.getSkills().getLevel(i)));
                    preparedStatement.setInt(4, player.getSkills().getStaticLevel(i));
                    preparedStatement.setLong(5, (long) player.getSkills().getExperience(i));
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_total_skill WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_total_skill (pidn, total_level, total_experience) VALUES(?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setInt(2, player.getSkills().getTotalLevel());
                preparedStatement.setLong(3, player.getSkills().getTotalExperience());
                preparedStatement.executeBatch();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_setting  ("
                + TextUtils.implode(Settings.FIELDS, ',') + ") VALUES  (" + getFieldBinds(Settings.FIELDS)
                + ") ON DUPLICATE KEY UPDATE " + getUpdateFields(Settings.FIELDS) + ";")) {
                Settings playerSettings = player.getSettings();
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setByte(2, (byte) playerSettings.getBrightness());
                preparedStatement.setByte(3, (byte) playerSettings.getMusicVolume());
                preparedStatement.setByte(4, (byte) playerSettings.getSoundEffectVolume());
                preparedStatement.setByte(5, (byte) playerSettings.getAreaSoundVolume());
                preparedStatement.setBoolean(6, playerSettings.isSingleMouseButton());
                preparedStatement.setBoolean(7, playerSettings.isMouseMovement());
                preparedStatement.setBoolean(8, playerSettings.isDisableChatEffects());
                preparedStatement.setBoolean(9, playerSettings.isSplitPrivateChat());
                preparedStatement.setBoolean(10, playerSettings.isAcceptAid());
                preparedStatement.setBoolean(11, playerSettings.isRunToggled());
                preparedStatement.setByte(12, (byte) playerSettings.getPublicChatSetting());
                preparedStatement.setByte(13, (byte) playerSettings.getPrivateChatSetting());
                preparedStatement.setByte(14, (byte) playerSettings.getClanChatSetting());
                preparedStatement.setByte(15, (byte) playerSettings.getTradeSetting());
                preparedStatement.setByte(16, (byte) playerSettings.getAssistSetting());
                preparedStatement.setInt(17, (int) playerSettings.getRunEnergy());
                preparedStatement.setBoolean(18, playerSettings.isSidePanelTransparent());
                preparedStatement.setBoolean(19, playerSettings.isRemainingXP());
                preparedStatement.setBoolean(20, playerSettings.isRoofRemoval());
                preparedStatement.setBoolean(21, playerSettings.isDataOrbs());
                preparedStatement.setBoolean(22, playerSettings.isChatboxTransparent());
                preparedStatement.setBoolean(23, playerSettings.isClickThroughChatbox());
                preparedStatement.setBoolean(24, playerSettings.isSidePanelsBottom());
                preparedStatement.setBoolean(25, playerSettings.isSidePanelHotkeys());
                preparedStatement.setBoolean(26, player.getProperties().isRetaliating());
                preparedStatement.setInt(27, playerSettings.getSpecialEnergy());
                preparedStatement.setByte(28, (byte) playerSettings.getAttackStyleIndex());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_emote WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_emote (pidn, id) VALUES(?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (Emote emote : player.getEmotes().getUnlockedEmotes()) {
                    preparedStatement.setInt(2, emote.getId());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_gender (pidn, gender) VALUES (?, ?) ON DUPLICATE KEY UPDATE gender = VALUES(gender)")) {
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setInt(2, player.getAppearance().getGender().ordinal());
                preparedStatement.execute();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_appearance WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_appearance (pidn, slot, look, colour) VALUES(?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int slot = 0; slot < player.getAppearance().getAppearanceCache().length; slot++) {
                    preparedStatement.setInt(2, slot);
                    preparedStatement.setInt(3, player.getAppearance().getAppearanceCache()[slot].getLook());
                    preparedStatement.setInt(4, player.getAppearance().getAppearanceCache()[slot].getColour());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_game_attribute WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_game_attribute (pidn, type, `key`, `value`) VALUES (?,?,?,?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (String key : player.getGameAttributes().getSavedAttributes()) {
                    Object value = player.getGameAttributes().getAttributes().get(key);
                    String type = "STRING";
                    if (value == null) {
                        type = "BYTE";
                        value = "-1";
                    } else if (value instanceof Integer) {
                        type = "INTEGER";
                    } else if (value instanceof Short) {
                        type = "SHORT";
                    } else if (value instanceof Byte) {
                        type = "BYTE";
                    } else if (value instanceof Long) {
                        type = "LONG";
                    } else if (value instanceof Boolean) {
                        type = "BOOLEAN";
                    } else {
                        log.warn("Unable to save attribute [{}={}] for [{}].",
                            key, value, player.getName());
                    }
                    preparedStatement.setString(2, type);
                    preparedStatement.setString(3, key);
                    preparedStatement.setString(4, String.valueOf(value));
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_quest WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_quest (pidn, id, stage, state) VALUES (?,?,?,?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (Map.Entry<String, Quest> quest : player.getQuestRepository().getQuests().entrySet()) {
                    if (quest == null) {
                        continue;
                    }
                    preparedStatement.setInt(2, quest.getValue().getId());
                    preparedStatement.setInt(3, quest.getValue().getStage());
                    preparedStatement.setInt(4, quest.getValue().getState().ordinal());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_achievement WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_achievement (pidn, name, state) VALUES (?,?,?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (Map.Entry<String, AchievementDiary> diary : player.getAchievementRepository().getDiaries()
                    .entrySet()) {
                    preparedStatement.setString(2, diary.getKey());
                    preparedStatement.setInt(3, diary.getValue().getState().ordinal());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_achievement_data WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_achievement_data (pidn, task, task_amount, task_complete) VALUES (?, ?,?,?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (Map.Entry<AchievementTask, Integer> task : player.getAchievementRepository().getAchievements()
                    .entrySet()) {
                    preparedStatement.setString(2, task.getKey().name());
                    preparedStatement.setInt(3, task.getValue());
                    preparedStatement.setBoolean(4,
                        player.getAchievementRepository().getFinishedAchievements().contains(task.getKey()));
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_spellbook (pidn, id) VALUES(?, ?) ON DUPLICATE KEY UPDATE id = VALUES(id)")) {
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setInt(2, player.getSpellBookManager().getSpellBook());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_music WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_music (pidn, music_id) VALUES(?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (MusicEntry musicEntry : player.getMusicPlayer().getUnlocked().values()) {
                    preparedStatement.setInt(2, musicEntry.getId());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_configuration WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_configuration (pidn, config_index, config_value) VALUES(?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int index = 0; index < player.getConfigManager().getSavedConfigurations().length; index++) {
                    int value = player.getConfigManager().getSavedConfigurations()[index];
                    if (value == 0) {
                        continue;
                    }
                    preparedStatement.setInt(2, index);
                    preparedStatement.setInt(3, value);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_interface_configuration WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_interface_configuration (pidn, config_index, config_value) VALUES(?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int index = 0; index < player.getInterfaceState().getSavedConfigurations().length; index++) {
                    int value = player.getInterfaceState().getSavedConfigurations()[index];
                    if (value == 0) {
                        continue;
                    }
                    preparedStatement.setInt(2, index);
                    preparedStatement.setInt(3, value);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_autocast WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_autocast (pidn, spellbook_id, spell_id) VALUES(?, ?, ?)")) {
                if (player.getProperties().getAutocastSpell() != null) {
                    preparedStatement.setInt(1, player.getPidn());
                    CombatSpell spell = player.getProperties().getAutocastSpell();
                    preparedStatement.setInt(2, spell.getBook().ordinal());
                    preparedStatement.setInt(3, spell.getSpellId());
                    preparedStatement.executeUpdate();
                }
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_barcrawl WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_barcrawl (pidn, bar_index) VALUES(?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int index = 0; index < player.getBarcrawlManager().getBars().length; index++) {
                    if (!player.getBarcrawlManager().getBars()[index]) {
                        continue;
                    }
                    preparedStatement.setInt(2, index);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_monitor (pidn, duplication_flag, macro_flag, last_flag) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE duplication_flag = VALUES(duplication_flag), macro_flag = VALUES(macro_flag), last_flag = VALUES(last_flag)" +
                    "")) {
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setByte(2, (byte) player.getMonitor().getDuplicationLog().getFlag());
                preparedStatement.setByte(3, (byte) player.getMonitor().getMacroFlag());
                preparedStatement.setLong(4, player.getMonitor().getDuplicationLog().getLastIncreaseFlag());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_quick_prayer WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_quick_prayer (pidn, prayer_ordinal) VALUES(?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (PrayerType prayerType : player.getPrayer().getQuickPrayer().getQuickPrayers()) {
                    preparedStatement.setInt(2, prayerType.ordinal());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_treasure_trail WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_treasure_trail (pidn, clue, stage) VALUES (?, ?, ?)")) {
                if (player.getTreasureTrailManager().hasTrail()) {
                    preparedStatement.setInt(1, player.getPidn());
                    preparedStatement.setInt(2, (player.getTreasureTrailManager().getClueId() << 16
                        | player.getTreasureTrailManager().getTrailLength()));
                    preparedStatement.setInt(3, player.getTreasureTrailManager().getTrailStage());
                    preparedStatement.executeUpdate();
                }
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_perk WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_perk (pidn, perk_id, enabled) VALUES(?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (Map.Entry<Perk, Boolean> perk : player.getPerkManager().getPerks().entrySet()) {
                    preparedStatement.setInt(2, perk.getKey().getId());
                    preparedStatement.setBoolean(3, perk.getValue());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_title WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_title (pidn, id, enabled) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE enabled = VALUES (enabled)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (LoyaltyTitle loyaltyTitle : player.getTitleManager().getUnlockedTitles()) {
                    preparedStatement.setInt(2, loyaltyTitle.getId());
                    preparedStatement.setBoolean(3, player.getTitleManager().isEnabled(loyaltyTitle));
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_donor (pidn, donor_status, gielinor_tokens, icon_hidden) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE donor_status = VALUES (donor_status), gielinor_tokens = VALUES(gielinor_tokens), icon_hidden = VALUES(icon_hidden)")) {
                preparedStatement.setInt(1, player.getPidn());
                preparedStatement.setInt(2, player.getDonorManager().getDonorStatus().getId());
                preparedStatement.setInt(3, player.getDonorManager().getGielinorTokens());
                preparedStatement.setBoolean(4, player.getDonorManager().isIconHidden());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_bank_data (pidn, tab_amounts) VALUES (?, ?) ON DUPLICATE KEY UPDATE tab_amounts = VALUES(tab_amounts)")) {
                if (player.getBank().getBankData().getTabAmounts() != null) {
                    String tabAmounts = TextUtils.implode(player.getBank().getBankData().getTabAmounts(), ':');
                    preparedStatement.setInt(1, player.getPidn());
                    preparedStatement.setString(2, tabAmounts);
                    preparedStatement.executeUpdate();
                }
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_state WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_state (pidn, id, val) VALUES(?, ?, ?)")) {
                if (player.getStateManager().isSaveRequired()) {
                    preparedStatement.setInt(1, player.getPidn());
                    for (EntityState state : player.getStateManager().getStates().keySet()) {
                        StatePulse pulse = player.getStateManager().getStates().get(state);
                        if (pulse == null) {
                            log.warn("Pulse for state [{}] is missing.", state);
                            continue;
                        }
                        if (pulse.isSaveRequired()) {
                            preparedStatement.setInt(2, state.ordinal());
                            preparedStatement.setLong(3, pulse.getSaveValue());
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                }
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_farming_equipment WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_farming_equipment (pidn, slot, item_id, item_count, item_charge) VALUES(?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int index = 0; index < player.getFarmingManager().getEquipment().getContainer()
                    .capacity(); index++) {
                    Item item = player.getFarmingManager().getEquipment().getContainer().get(index);
                    if (item == null) {
                        continue;
                    }
                    preparedStatement.setInt(2, index);
                    preparedStatement.setInt(3, item.getId());
                    preparedStatement.setInt(4, item.getCount());
                    preparedStatement.setInt(5, item.getCharge());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_compost_bin WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_compost_bin (pidn, bin_id, bin_timestamp) VALUES(?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                CompostManager compostManager = player.getFarmingManager().getCompostManager();
                for (CompostBin compostBin : compostManager.getBins()) {
                    preparedStatement.setInt(2, compostBin.getWrapperId());
                    preparedStatement.setLong(3, compostBin.getTimeStamp());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_compost_bin_inventory WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_compost_bin_inventory (pidn, bin_id, item_slot, item_id, item_count, item_charge) VALUES(?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                CompostManager compostManager = player.getFarmingManager().getCompostManager();
                for (CompostBin compostBin : compostManager.getBins()) {
                    preparedStatement.setInt(2, compostBin.getWrapperId());
                    for (int itemSlot = 0; itemSlot < compostBin.getContainer().toArray().length; itemSlot++) {
                        Item item = compostBin.getContainer().get(itemSlot);
                        if (item == null) {
                            continue;
                        }
                        preparedStatement.setInt(3, itemSlot);
                        preparedStatement.setInt(4, item.getId());
                        preparedStatement.setInt(5, item.getCount());
                        preparedStatement.setInt(6, item.getCharge());
                        preparedStatement.addBatch();
                    }
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_patch WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_patch (pidn, patch_id, patch_position) VALUES(?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                FarmingManager farmingManager = player.getFarmingManager();
                for (PatchWrapper patchWrapper : farmingManager.getPatches()) {
                    if (patchWrapper == null || patchWrapper.getNode() == null
                        || patchWrapper.getPatch().getNodePosition(patchWrapper.getNode()) == -1) {
                        continue;
                    }
                    preparedStatement.setInt(2, patchWrapper.getWrapperId());
                    preparedStatement.setInt(3, patchWrapper.getPatch().getNodePosition(patchWrapper.getNode()));
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_patch_cycle WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_patch_cycle (pidn, patch_id, compost_threshold, growth_time, harvest_amount, protection) VALUES(?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                FarmingManager farmingManager = player.getFarmingManager();
                for (PatchWrapper patchWrapper : farmingManager.getPatches()) {
                    preparedStatement.setInt(2, patchWrapper.getWrapperId());
                    preparedStatement.setInt(3, patchWrapper.getCycle().getCompostThreshold());
                    preparedStatement.setLong(4, patchWrapper.getCycle().getGrowthTime());
                    preparedStatement.setInt(5, patchWrapper.getCycle().getHarvestAmount());
                    preparedStatement.setBoolean(6, patchWrapper.getCycle().isProtected());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_friend_list WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_friend_list (pidn, friend_pidn, clan_rank) VALUES(?, ?, ?)")) {
                ClanCommunication clanCommunication = ClanCommunication.get(player.getName());
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
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_ignore_list WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_ignore_list (pidn, ignore_pidn) VALUES(?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int pidn : player.getCommunication().getBlocked().keySet()) {
                    preparedStatement.setInt(2, pidn);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_slayer WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_slayer (pidn, master_id, task, task_amount) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE master_id = VALUES(master_id), task = VALUES(task), task_amount = VALUES(task_amount)")) {
                if (player.getSlayer().getMaster() != null) {
                    preparedStatement.setInt(1, player.getPidn());
                    preparedStatement.setInt(2, player.getSlayer().getMaster().getNpc());
                    preparedStatement.setString(3, player.getSlayer().getTask() == null ? ""
                        : Tasks.forValue(player.getSlayer().getTask()).name());
                    preparedStatement.setInt(4,
                        player.getSlayer().getTask() == null ? 0 : player.getSlayer().getAmount());
                    preparedStatement.executeUpdate();
                }
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_slayer_kill_log WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_slayer_kill_log (pidn, task, kill_count) VALUES (?, ?, ?)")) {
                SlayerKillLog slayerKillLog = player.getSavedData().getSlayerKillLog();
                if (slayerKillLog.getKillLog().size() != 0) {
                    preparedStatement.setInt(1, player.getPidn());
                    for (Map.Entry<String, Integer> kill : slayerKillLog.getKillLog().entrySet()) {
                        String task = kill.getKey();
                        int killCount = kill.getValue();
                        preparedStatement.setString(2, task);
                        preparedStatement.setInt(3, killCount);
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                }
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_slayer_option WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_slayer_option (pidn, ordinal) VALUES(?, ?)")) {
                if (player.getSavedData().getActivityData().hasLearnedSlayerOptions()) {
                    preparedStatement.setInt(1, player.getPidn());
                    for (int ordinal = 0; ordinal < player.getSavedData().getActivityData()
                        .getLearnedSlayerOptions().length; ordinal++) {
                        if (!player.getSavedData().getActivityData().hasLearnedSlayerOption(ordinal)) {
                            continue;
                        }
                        preparedStatement.setByte(2, (byte) ordinal);
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                }
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_disabled_slayer WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_disabled_slayer (pidn, `index`, task) VALUES(?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (int index = 0; index < player.getSavedData().getActivityData()
                    .getDisabledTasks().length; index++) {
                    preparedStatement.setInt(2, (byte) index);
                    preparedStatement.setString(3, player.getSavedData().getActivityData().getDisabledTasks()[index]);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_barrow WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_barrow (pidn, barrow_index) VALUES(?, ?)")) {
                if (player.getSavedData().getActivityData().hasKilledBarrowBrother()) {
                    preparedStatement.setInt(1, player.getPidn());
                    for (int ordinal = 0; ordinal < player.getSavedData().getActivityData()
                        .getBarrowsKilled().length; ordinal++) {
                        if (!player.getSavedData().getActivityData().isBarrowsBrotherKilled(ordinal)) {
                            continue;
                        }
                        preparedStatement.setByte(2, (byte) ordinal);
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                }
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_familiar WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_familiar (pidn, id, ticks, special_points) VALUES(?, ?, ?, ?)")) {
                if (player.getFamiliarManager().hasFamiliar()) {
                    Familiar familiar = player.getFamiliarManager().getFamiliar();
                    preparedStatement.setInt(1, player.getPidn());
                    preparedStatement.setInt(2, familiar.getOriginalId());
                    preparedStatement.setInt(3, familiar.getTicks());
                    preparedStatement.setInt(4, familiar.getSpecialPoints());
                    preparedStatement.execute();
                }
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_pet_detail WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_pet_detail (pidn, item_id, hunger, growth, stage, insured) VALUES(?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (Map.Entry<Integer, PetDetails> entry : player.getFamiliarManager().getPetDetails().entrySet()) {
                    preparedStatement.setInt(2, entry.getKey());
                    preparedStatement.setDouble(3, entry.getValue().getHunger());
                    preparedStatement.setDouble(4, entry.getValue().getGrowth());
                    preparedStatement.setDouble(5, entry.getValue().getStage());
                    preparedStatement.setInt(6, entry.getValue().isInsured() ? 1 : 0);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_pet WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            if (player.getFamiliarManager().hasPet()) {
                try (PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO player_pet (pidn, item_id) VALUES(?, ?)")) {
                    Pets pet = ((Pet) player.getFamiliarManager().getFamiliar()).getPet();
                    preparedStatement.setInt(1, player.getPidn());
                    preparedStatement.setInt(2, pet.getBabyItemId());
                    preparedStatement.execute();
                }
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_grand_exchange_hist WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO grand_exchange_hist VALUES(?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (GrandExchangeOffer grandExchangeOffer : player.getGrandExchange().getHistory()) {
                    if (grandExchangeOffer == null) {
                        continue;
                    }
                    preparedStatement.setByte(2, (byte) grandExchangeOffer.getIndex());
                    preparedStatement.setBoolean(3, grandExchangeOffer.isSell());
                    preparedStatement.setInt(4, grandExchangeOffer.getItemId());
                    preparedStatement.setInt(5, grandExchangeOffer.getTotalCoinExchange());
                    preparedStatement.setInt(6, grandExchangeOffer.getCompletedAmount());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_grand_exchange_offer WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_grand_exchange_offer VALUES(?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (GrandExchangeOffer grandExchangeOffer : player.getGrandExchange().getGrandExchangeOffers()) {
                    if (grandExchangeOffer == null) {
                        continue;
                    }
                    preparedStatement.setByte(2, (byte) grandExchangeOffer.getIndex());
                    preparedStatement.setLong(3, grandExchangeOffer.getUid());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_referral WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO referral VALUES(?, ?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getPidn());
                for (Referred referred : player.getReferralManager().getReferrals()) {
                    if (referred == null) {
                        continue;
                    }
                    preparedStatement.setInt(2, referred.getPidn());
                    preparedStatement.setString(3, referred.getUsername());
                    preparedStatement.setLong(4, referred.getReferredRequestTime());
                    preparedStatement.setLong(5, referred.getReferredTime());
                    preparedStatement.setBoolean(6, referred.isRequested());
                    preparedStatement.setBoolean(7, referred.isNotified());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_clan WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            ClanCommunication clanCommunication = player.getCommunication().getClan();
            if (clanCommunication != null && clanCommunication.getName() != null) {
                try (PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO player_clan VALUES(?, ?, ?, ?, ?, ?)")) {
                    preparedStatement.setInt(1, player.getPidn());
                    preparedStatement.setString(2, clanCommunication.getName());
                    preparedStatement.setByte(3, (byte) clanCommunication.getJoinRequirement().ordinal());
                    preparedStatement.setByte(4, (byte) clanCommunication.getMessageRequirement().ordinal());
                    preparedStatement.setByte(5, (byte) clanCommunication.getKickRequirement().ordinal());
                    preparedStatement.setByte(6, (byte) clanCommunication.getLootRequirement().ordinal());
                    preparedStatement.execute();
                }
            }
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM player_familiar_inventory WHERE pidn = ?")) {
                prepareStatement.setInt(1, player.getPidn());
                prepareStatement.execute();
            }
            if (player.getFamiliarManager().hasFamiliar()
                && player.getFamiliarManager().getFamiliar().isBurdenBeast()) {
                BurdenBeast burdenBeast = (BurdenBeast) player.getFamiliarManager().getFamiliar();
                try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO player_familiar_inventory (pidn, inventory_slot, item_id, item_quantity, item_charge) VALUES(?, ?, ?, ?, ?)")) {
                    preparedStatement.setInt(1, player.getPidn());
                    for (int index = 0; index < burdenBeast.getContainer().capacity(); index++) {
                        Item item = burdenBeast.getContainer().get(index);
                        if (item == null) {
                            continue;
                        }
                        preparedStatement.setInt(2, index);
                        preparedStatement.setInt(3, item.getId());
                        preparedStatement.setInt(4, item.getCount());
                        preparedStatement.setInt(5, item.getCharge());
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Error saving [{}].", player.getName(), ex);
        }

        long timeFinished = System.nanoTime();
        long timeDifferenceInMillis = TimeUnit.NANOSECONDS.toMillis(timeFinished - startTime);
        log.info("{} - save took {} millis.", player.getName(), timeDifferenceInMillis);
    }

    /**
     * Gets the field binds.
     *
     * @return The field binds.
     */
    private String getFieldBinds(String[] fields) {
        String[] binds = fields.clone();
        Arrays.fill(binds, "?");
        return TextUtils.implode(binds, ',');
    }

    /**
     * Gets the update field block.
     *
     * @return The update fields.
     */
    private String getUpdateFields(String[] fields) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String field : fields) {
            stringBuilder.append(field).append(" = ").append("VALUES(").append(field).append("),");
        }
        return TextUtils.format(stringBuilder.toString(), ',');
    }

    /**
     * Loads the player's character data.
     *
     * @param player
     *            The player.
     * @return The response.
     */
    public Response loadPlayer(Player player) {
        Response response = Response.LOGIN_OK;
        /**
         * Checks if we need to convert from binary files.
         */
        File file = new File(
            Constants.BINARY_PLAYER_SAVE_DIRECTORY + "convert" + File.separator + player.getName() + ".dat");
        if (file.exists()) {
            return ((BinaryAccountService) World.getWorld().getApplicationContext().getBean("binaryAccountService"))
                .loadPlayer(player, Constants.BINARY_PLAYER_SAVE_DIRECTORY + "convert");
        }
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT x, y, z FROM player_location WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        player.setLocation(Location.create(resultSet.getShort("x"), resultSet.getShort("y"),
                            resultSet.getByte("z")));
                    }
                } catch (SQLException ex) {
                    log.error("Error selecting from [player_location] for [{}].", player.getName(), ex);
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_inventory WHERE pidn = ?")) {
                List<Item> itemList;
                int networth = 0;
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    itemList = new ArrayList<>();
                    while (resultSet.next()) {
                        if (resultSet.wasNull()) {
                            continue;
                        }
                        int slot = resultSet.getInt("inventory_slot");
                        int id = resultSet.getInt("item_id");
                        int amount = resultSet.getInt("item_quantity");
                        int charges = resultSet.getInt("item_charge");
                        Item item = new Item(id, amount, charges);
                        item.setIndex(slot);
                        itemList.add(item);
                    }
                    networth += player.getInventory().parseSQL(itemList);
                }
                player.getMonitor().increaseNetworth(networth);
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_bank WHERE pidn = ?")) {
                List<Item> itemList;
                int networth = 0;
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    itemList = new ArrayList<>();
                    while (resultSet.next()) {
                        if (resultSet.wasNull()) {
                            continue;
                        }
                        int slot = resultSet.getInt("bank_slot");
                        int id = resultSet.getInt("item_id");
                        int amount = resultSet.getInt("item_quantity");
                        int charges = resultSet.getInt("item_charge");
                        Item item = new Item(id, amount, charges);
                        item.setIndex(slot);
                        itemList.add(item);
                    }
                    networth += player.getBank().parseSQL(itemList);
                }
                player.getMonitor().increaseNetworth(networth);
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_equipment WHERE pidn = ?")) {
                List<Item> itemList;
                int networth = 0;
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    itemList = new ArrayList<>();
                    while (resultSet.next()) {
                        if (resultSet.wasNull()) {
                            continue;
                        }
                        int slot = resultSet.getInt("equipment_slot");
                        int id = resultSet.getInt("item_id");
                        int amount = resultSet.getInt("item_quantity");
                        int charges = resultSet.getInt("item_charge");
                        Item item = new Item(id, amount, charges);
                        item.setIndex(slot);
                        itemList.add(item);
                    }
                    networth += player.getEquipment().parseSQL(itemList);
                }
                player.getMonitor().increaseNetworth(networth);
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_rune_inventory WHERE pidn = ?")) {
                List<Item> itemList;
                int networth = 0;
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    itemList = new ArrayList<>();
                    while (resultSet.next()) {
                        if (resultSet.wasNull()) {
                            continue;
                        }
                        int slot = resultSet.getInt("inventory_slot");
                        int id = resultSet.getInt("item_id");
                        int amount = resultSet.getInt("item_quantity");
                        int charges = resultSet.getInt("item_charge");
                        Item item = new Item(id, amount, charges);
                        item.setIndex(slot);
                        itemList.add(item);
                    }
                    networth += player.getSavedData().getGlobalData().getRunePouch().parseSQL(itemList);
                }
                player.getMonitor().increaseNetworth(networth);
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_global_data WHERE pidn = ?")) {
                GlobalData globalData = player.getSavedData().getGlobalData();
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        globalData.setChatPing(resultSet.getLong("chat_ping"));
                        globalData.setTutorClaim(resultSet.getLong("tutor_claim"));
                        globalData.setLuthasTask(resultSet.getBoolean("luthas_task"));
                        globalData.setKaramjaBananas(resultSet.getInt("karamja_bananas"));
                        globalData.setSilkSteal(resultSet.getLong("silk_steal"));
                        globalData.setLumbridgeRope(resultSet.getBoolean("lumbridge_rope"));
                        globalData.setApprentice(resultSet.getBoolean("apprentice"));
                        globalData.setFritzGlass(resultSet.getBoolean("fritz_glass"));
                        globalData.setZaffAmount(resultSet.getInt("zaff_amount"));
                        globalData.setZaffTime(resultSet.getLong("zaff_time"));
                        globalData.setWydinEmployee(resultSet.getBoolean("wydin_employee"));
                        globalData.setDraynorRecording(resultSet.getBoolean("draynor_recording"));
                        globalData.setEssenceTeleporter(resultSet.getInt("essence_teleporter"));
                        globalData.setRecoilDamage(resultSet.getInt("recoil_damage"));
                        if (resultSet.getBoolean("paid_waterfall")) {
                            globalData.setWaterfallPaid();
                        }
                        if (resultSet.getBoolean("paid_blacksmith")) {
                            globalData.setBlacksmithPaid();
                        }
                        globalData.setPlayTime(resultSet.getLong("play_time"));
                        globalData.setEctofuntusBoneType(resultSet.getInt("ectofuntus_bone_type"));
                        globalData.setEctofuntusBonesGround(resultSet.getBoolean("ectofuntus_bone_ground"));
                        globalData.setYellTag(resultSet.getString("yell_tag"));
                        globalData.setLastYell(resultSet.getLong("last_yell"));
                        globalData.setVisibility(resultSet.getByte("visibility"));
                        globalData.setCombatAnnounced(resultSet.getBoolean("combat_announced"));
                        int smallPouch = resultSet.getInt("small_pouch_count");
                        int mediumPouch = resultSet.getInt("medium_pouch_count");
                        int largePouch = resultSet.getInt("large_pouch_count");
                        int giantPouch = resultSet.getInt("giant_pouch_count");
                        while (smallPouch > 0) {
                            globalData.getSmallPouch().add(new Item(7936));
                        }
                        while (mediumPouch > 0) {
                            globalData.getMediumPouch().add(new Item(7936));
                        }
                        while (largePouch > 0) {
                            globalData.getLargePouch().add(new Item(7936));
                        }
                        while (giantPouch > 0) {
                            globalData.getGiantPouch().add(new Item(7936));
                        }
                        globalData.setVotingPoints(resultSet.getInt("voting_points"));
                        globalData.setExperienceModifierTime(resultSet.getInt("experience_modifier_time"));
                        globalData.setExperienceModifier(resultSet.getDouble("experience_modifier"));
                        globalData.setStarterPackage(Starter.StarterPackage.forId(resultSet.getByte("starter_package")));
                        globalData.setLastClanChat(resultSet.getString("last_clan_chat"));
                        globalData.setLoyaltyPoints(resultSet.getLong("loyalty_points"));
                        globalData.setXpDrops(resultSet.getLong("xp_drops"));
                        globalData.setPlayerKills(resultSet.getInt("player_kills"));
                        globalData.setPlayerDeaths(resultSet.getInt("player_deaths"));
                        globalData.setPrayerBook(resultSet.getInt("prayer_book"));
                        globalData.setBetaStatus(resultSet.getBoolean("beta_status"));
                        globalData.setEasterEventStage(resultSet.getInt("easter_event_stage"));
                        if (resultSet.getBoolean("easter_dyes_given")) {
                            globalData.setGivenDyes();
                        }
                        globalData.setEasterTitle(resultSet.getBoolean("easter_title"));
                        if (resultSet.getBoolean("walked_to_dorgeshkaan")) {
                            globalData.setWalkedToDorgeshKaan(true);
                        }
                        globalData.setRoaldPoints(resultSet.getInt("roald_points"));
                        globalData.setLeftClickOption(resultSet.getString("summoning_left_click"));
                        globalData.setLoginStreak(resultSet.getInt("login_streak"));
                        globalData.setLoginStreak(resultSet.getInt("killstreak"));
                        globalData.setPetsInsured(resultSet.getInt("pets_insured"));
                        globalData.setNpcKillCount(resultSet.getInt("npc_kill_count"));
//                        globalData.setExperienceModifiersTime((int[]) resultSet.getArray("experience_modifiers_time").getArray());
//                        globalData.setExperienceModifiers((double[]) resultSet.getArray("experience_modifiers").getArray());
//
//                        globalData.setAccuracyModifierTime(resultSet.getInt("accuracy_modifier_time"));
//                        globalData.setAccuracyModifier(resultSet.getDouble("accuracy_modifier"));
//                        globalData.setStrengthModifierTime(resultSet.getInt("strength_modifier_time"));
//                        globalData.setStrengthModifier(resultSet.getDouble("strength_modifier"));
//                        globalData.setDefenceModifierTime(resultSet.getInt("defence_modifier_time"));
//                        globalData.setDefenceModifier(resultSet.getDouble("defence_modifier"));
//                        globalData.setDropModifierTime(resultSet.getInt("drop_modifier_time"));
//                        globalData.setDropModifier(resultSet.getDouble("drop_modifier"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT `index` FROM player_stronghold_reward WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        player.getSavedData().getGlobalData().setStrongholdReward(resultSet.getByte("index"), true);
                    }
                } catch (SQLException ex) {
                    log.error("Error selecting from [player_stronghold] for [{}].", player.getName(), ex);
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT next_milestone, last_milestone FROM player_milestone WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        player.getSavedData().getGlobalData().setNextMilestone(resultSet.getShort("next_milestone"));
                        player.getSavedData().getGlobalData().setLastMilestone(resultSet.getShort("last_milestone"));
                    }
                } catch (SQLException ex) {
                    log.error("Error selecting from [player_milestone] for [{}].", player.getName(), ex);
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_duel_arena WHERE pidn = ?")) {
                List<DuelRule> presetRules = new ArrayList<>();
                List<DuelRule> lastRules = new ArrayList<>();
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        if (resultSet.getBoolean("preset")) {
                            presetRules.add(DuelRule.values()[resultSet.getInt("rule_ordinal")]);
                        } else {
                            lastRules.add(DuelRule.values()[resultSet.getInt("rule_ordinal")]);
                        }
                    }
                }
                player.getSavedData().getGlobalData().setPresetDuelRules(presetRules);
                player.getSavedData().getGlobalData().setLastDuelRules(lastRules);
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT `item_id` FROM player_roald_item WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        player.getSavedData().getGlobalData().getUnlockedRoaldsItems().add(resultSet.getInt("item_id"));
                    }
                } catch (SQLException ex) {
                    log.error("Error selecting from [player_roald_item] for [{}].", player.getName(), ex);
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_boss_kill_log WHERE pidn = ?")) {
                BossKillLog bossKillLog = player.getSavedData().getBossKillLog();
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        bossKillLog.increaseKreeArraKills(resultSet.getInt("kree_arra_kills"));
                        bossKillLog.increaseZilyanaKills(resultSet.getInt("zilyana_kills"));
                        bossKillLog.increaseGraardorKills(resultSet.getInt("graardor_kills"));
                        bossKillLog.increaseKrilTsutKills(resultSet.getInt("kril_tsut_kills"));
                        bossKillLog.increaseDagannothRexKills(resultSet.getInt("dagannoth_rex_kills"));
                        bossKillLog.increaseDagannothPrimeKills(resultSet.getInt("dagannoth_prime_kills"));
                        bossKillLog.increaseDagannothSupremeKills(resultSet.getInt("dagannoth_supreme_kills"));
                        bossKillLog.increaseGiantMoleKills(resultSet.getInt("giant_mole_kills"));
                        bossKillLog.increaseKalphiteQueenKills(resultSet.getInt("kalphite_queen_kills"));
                        bossKillLog.increaseKbdKills(resultSet.getInt("kbd_kills"));
                        bossKillLog.increaseChaosElementalKills(resultSet.getInt("chaos_elemental_kills"));
                        bossKillLog.increaseBorkKills(resultSet.getInt("bork_kills"));
                        bossKillLog.increaseBarrelchestKills(resultSet.getInt("barrelchest_kills"));
                        bossKillLog.increaseCorporealBeastKills(resultSet.getInt("corporeal_beast_kills"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_activity_data WHERE pidn = ?")) {
                ActivityData activityData = player.getSavedData().getActivityData();
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        activityData.setWarriorGuildTokens(resultSet.getInt("warrior_guild_tokens"));
                        activityData.updateBountyHunterRate(resultSet.getInt("bounty_hunter_rate"));
                        activityData.setBountyRogueRate(resultSet.getInt("bounty_rogue_rate"));
                        activityData.setBarrowsKillCount(resultSet.getInt("barrows_kill_count"));
                        activityData.setRandomBarrowsBrother(resultSet.getInt("random_barrows_brother"));
                        activityData.setBarrowsChestStatus(resultSet.getInt("barrows_chest_status"));
                        activityData.setBarrowsChestCount(resultSet.getInt("barrows_chest_count"));
                        activityData.setBarbarianAssaultPoints(resultSet.getInt("barbarian_assault_points"));
                        activityData.setBarrowTunnelIndex(resultSet.getInt("barrow_tunnel_index"));
                        activityData.setElnockSupplies(resultSet.getBoolean("elnock_supplies"));
                        activityData.setLastBorkBattle(resultSet.getLong("last_bork_battle"));
                        activityData.setEasyClueScrolls(resultSet.getInt("easy_clue_scrolls"));
                        activityData.setMediumClueScrolls(resultSet.getInt("medium_clue_scrolls"));
                        activityData.setHardClueScrolls(resultSet.getInt("hard_clue_scrolls"));
                        activityData.setSlayerPoints(resultSet.getInt("slayer_points"));
                        activityData.setAccumulativeSlayerTasks(resultSet.getInt("accumulative_slayer_tasks"));
                        activityData.setCompletedFightCaves(resultSet.getBoolean("completed_fight_caves"));
                        activityData.setLastTaskReset(resultSet.getLong("last_reset_task"));
                        activityData.setLastTaskBlock(resultSet.getLong("last_reset_block"));
                        activityData.setLastGunGameEntry(resultSet.getLong("last_gun_game_entry"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_quest_data WHERE pidn = ?")) {
                QuestData questData = player.getSavedData().getQuestData();
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        questData.setKilledTreeSpirit(resultSet.getBoolean("killed_tree_spirit"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_skill WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int skillId = resultSet.getInt("skill_id");
                        int dynamicLevel = resultSet.getInt("dynamic_level");
                        int staticLevel = resultSet.getInt("static_level");
                        double skillExperience = resultSet.getDouble("experience");
                        player.getSkills().setLevel(skillId, dynamicLevel, false);
                        player.getSkills().setStaticLevel(skillId, staticLevel, false);
                        player.getSkills().setExperience(skillId, skillExperience, false);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_setting WHERE pidn = ?")) {
                Settings playerSettings = player.getSettings();
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        playerSettings.setBrightnessSQL(resultSet.getByte("brightness"));
                        playerSettings.setMusicVolumeSQL(resultSet.getByte("music_volume"));
                        playerSettings.setSoundEffectVolumeSQL(resultSet.getByte("sound_effect_volume"));
                        playerSettings.setAreaSoundVolumeSQL(resultSet.getByte("area_sound_volume"));
                        playerSettings.setSingleMouseButtonSQL(resultSet.getBoolean("single_mouse_button"));
                        playerSettings.setMouseMovement(resultSet.getBoolean("mouse_movement"));
                        playerSettings.setChatEffectsSQL(resultSet.getBoolean("disable_chat_effects"));
                        playerSettings.setSplitPrivateChatSQL(resultSet.getBoolean("split_private_chat"));
                        playerSettings.setAcceptAidSQL(resultSet.getBoolean("accept_aid"));
                        playerSettings.setRunToggledSQL(resultSet.getBoolean("run_toggled"));
                        playerSettings.setChatSettingsSQL(resultSet.getByte("public_chat_setting"),
                            resultSet.getByte("private_chat_setting"), resultSet.getByte("trade_setting"));
                        playerSettings.setClanChatSettingSQL(resultSet.getByte("clan_chat_setting"));
                        // playerSettings.setAssistSetting(resultSet.getByte("assist_setting"));
                        playerSettings.setRunEnergy(resultSet.getInt("run_energy"));
                        playerSettings.setSidePanelTransparent(resultSet.getBoolean("side_panel_transparent"), false);
                        playerSettings.setRemainingXP(resultSet.getBoolean("remaining_xp"), false);
                        playerSettings.setRoofRemoval(resultSet.getBoolean("roof_removal"), false);
                        playerSettings.setDataOrbs(resultSet.getBoolean("data_orbs"), false);
                        playerSettings.setChatboxTransparent(resultSet.getBoolean("chatbox_transparent"), false);
                        playerSettings.setClickThroughChatbox(resultSet.getBoolean("click_through_chatbox"), false);
                        playerSettings.setSidePanelsBottom(resultSet.getBoolean("side_panels_bottom"), false);
                        playerSettings.setSidePanelHotkeys(resultSet.getBoolean("side_panel_hotkeys"), false);
                        player.getProperties().setRetaliating(resultSet.getBoolean("auto_retaliating"));
                        playerSettings.setSpecialEnergySQL(resultSet.getInt("special_energy"));
                        playerSettings.setAttackStyleIndex(resultSet.getInt("attack_style_index"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_emote WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Emote emote = Emote.forId(resultSet.getInt("id"));
                        if (emote == null) {
                            continue;
                        }
                        player.getEmotes().setUnlocked(emote, true);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT gender FROM player_gender WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        player.getAppearance().setGender(resultSet.getInt("gender") == 1 ? Gender.FEMALE : Gender.MALE);
                    }
                } catch (SQLException ex) {
                    log.error("Error selecting from [player_gender] for [{}].", player.getName(), ex);
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_appearance WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int look = resultSet.getInt("look");
                        int slot = resultSet.getInt("slot");
                        int colour = resultSet.getInt("colour");
                        player.getAppearance().getAppearanceCache()[slot].setLook(look);
                        player.getAppearance().getAppearanceCache()[slot].setColour(colour);
                    }
                } catch (SQLException ex) {
                    log.error("Error selecting from [player_appearance] for [{}].", player.getName(), ex);
                }
                player.getAppearance().sync();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_game_attribute WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String type = resultSet.getString("type");
                        String key = resultSet.getString("key");
                        String val = resultSet.getString("value");
                        Object value = null;
                        switch (type) {
                            case "INTEGER":
                                value = Integer.valueOf(val);
                                break;
                            case "SHORT":
                                value = Short.valueOf(val);
                                break;
                            case "BYTE":
                                value = Byte.valueOf(val);
                                break;
                            case "LONG":
                                value = Long.valueOf(val);
                                break;
                            case "STRING":
                                value = val;
                                break;
                            case "BOOLEAN":
                                if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false")) {
                                    value = Boolean.parseBoolean(val);
                                    break;
                                }
                                value = (Integer.parseInt(val) == 1);
                                break;
                        }
                        player.getGameAttributes().saveAttribute(key, value);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_quest WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        int stage = resultSet.getInt("stage");
                        int state = resultSet.getInt("state");
                        final QuestState questState = state == 2 ? QuestState.COMPLETED
                            : state == 1 ? QuestState.STARTED : QuestState.NOT_STARTED;
                        final Quest quest = player.getQuestRepository().forId(id);
                        if (quest == null) {
                            log.warn("Invalid quest id [{}] for [{}].", id, player.getName());
                            continue;
                        }
                        player.getQuestRepository().getQuests().remove(quest.getName());
                        quest.setStage(stage);
                        quest.setState(questState);
                        player.getQuestRepository().getQuests().put(quest.getName(), quest);
                        if (player.getQuestRepository().isComplete(quest)) {
                            player.getQuestRepository().incrementPoints(quest.getQuestPoints());
                        }
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_achievement WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        AchievementDiary achievementDiary = player.getAchievementRepository().getDiary(name);
                        if (achievementDiary == null) {
                            log.warn("Invalid achievement diary name: [{}] for [{}].", name, player.getName());
                            continue;
                        }
                        int state = resultSet.getByte("state");
                        player.getAchievementRepository().getDiaries().remove(name);
                        achievementDiary.setState(AchievementState.values()[state]);
                        player.getAchievementRepository().getDiaries().put(name, achievementDiary);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_achievement_data WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String task = resultSet.getString("task");
                        AchievementTask achievementTask = AchievementTask.forName(task);
                        if (achievementTask == null) {
                            log.warn("Invalid achievement diary task: [{}] for [{}].", task, player.getName());
                            continue;
                        }
                        int amount = resultSet.getInt("task_amount");
                        boolean taskComplete = resultSet.getBoolean("task_complete");
                        if (taskComplete) {
                            player.getAchievementRepository().getFinishedAchievements().add(achievementTask);
                        }
                        player.getAchievementRepository().getAchievements().put(achievementTask, amount);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT id FROM player_spellbook WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        int id = resultSet.getInt("id");
                        if (SpellBookManager.SpellBook.forInterface(id) != null) {
                            player.getSpellBookManager().setSpellBook(SpellBookManager.SpellBook.forInterface(id));
                        }
                    }
                } catch (SQLException ex) {
                    log.error("Error selecting from [player_spellbook] for [{}].", player.getName(), ex);
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_music WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int musicId = resultSet.getInt("music_id");
                        MusicEntry musicEntry;
                        if ((musicEntry = MusicEntry.forId(musicId)) == null) {
                            continue;
                        }
                        player.getMusicPlayer().getUnlocked().put(musicEntry.getIndex(), musicEntry);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_configuration WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        player.getConfigManager().getSavedConfigurations()[resultSet.getInt("config_index")] = resultSet
                            .getInt("config_value");
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_interface_configuration WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        player.getInterfaceState().getSavedConfigurations()[resultSet
                            .getInt("config_index")] = resultSet.getInt("config_value");
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_autocast WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        int spellbookId = resultSet.getInt("spellbook_id");
                        int spellId = resultSet.getInt("spell_id");
                        player.getProperties().setAutocastSpell(
                            (CombatSpell) SpellBookManager.SpellBook.values()[spellbookId].getSpell(spellId));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT bar_index FROM player_barcrawl WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        player.getBarcrawlManager().complete(resultSet.getInt("bar_index"));
                        player.getBarcrawlManager().setStarted(true);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT duplication_flag, macro_flag, last_flag FROM player_monitor WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        player.getMonitor().getDuplicationLog().flag(resultSet.getByte("duplication_flag"));
                        player.getMonitor().setMacroFlag(resultSet.getByte("macro_flag"));
                        player.getMonitor().getDuplicationLog().setLastIncreaseFlag(resultSet.getLong("last_flag"));
                    }
                } catch (SQLException ex) {
                    log.error("Error selecting from [player_monitor] for [{}].", player.getName(), ex);
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_quick_prayer WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        PrayerType prayerType = PrayerType.forOrdinal(resultSet.getInt("prayer_ordinal"));
                        if (prayerType == null) {
                            continue;
                        }
                        player.getPrayer().getQuickPrayer().getQuickPrayers().add(prayerType);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT clue, stage FROM player_treasure_trail WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        if (resultSet.first()) {
                            int clue = resultSet.getInt("clue");
                            int stage = resultSet.getInt("stage");
                            player.getTreasureTrailManager().setClueId(clue >> 16 & 0xFFFF);
                            player.getTreasureTrailManager().setTrailLength(clue & 0xFFFF);
                            player.getTreasureTrailManager().setTrailStage(stage);
                            ClueScrollPlugin clueScrollPlugin = ClueScrollPlugin.getClueScrolls()
                                .get(player.getTreasureTrailManager().getClueId());
                            if (clueScrollPlugin != null) {
                                player.getTreasureTrailManager().setLevel(clueScrollPlugin.getLevel());
                            }
                        }
                    }
                } catch (SQLException ex) {
                    log.error("Error selecting from [player_treasure_trail] for [{}].", player.getName(), ex);
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT perk_id, enabled FROM player_perk WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Perk perk = Perk.forId(resultSet.getInt("perk_id"));
                        if (perk == null) {
                            continue;
                        }
                        player.getPerkManager().unlock(perk);
                        if (resultSet.getBoolean("enabled")) {
                            player.getPerkManager().enable(perk);
                        }
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT id, enabled FROM player_title WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        LoyaltyTitle loyaltyTitle = LoyaltyTitle.forId(resultSet.getInt("id"));
                        if (loyaltyTitle == null) {
                            log.warn("Invalid loyalty title: [{}].", resultSet.getInt("id"));
                            continue;
                        }
                        loyaltyTitle.unlock(player);
                        if (resultSet.getBoolean("enabled")) {
                            loyaltyTitle.set(player, true);
                        }
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT donor_status, gielinor_tokens, icon_hidden FROM player_donor WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        DonorStatus donorStatus = DonorStatus.Companion.forId(resultSet.getInt("donor_status"));
                        if (donorStatus != null) {
                            player.getDonorManager().setDonorStatus(donorStatus);
                            player.getDonorManager().setIconHidden(resultSet.getBoolean("icon_hidden"));
                        }
                        player.getDonorManager().setGielinorTokens(resultSet.getInt("gielinor_tokens"));
                    }
                } catch (Exception ex) {
                    log.error("Exception reading from [player_donor] for [{}].", player.getName(), ex);
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_bank_data WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        String[] tabAmountData = resultSet.getString("tab_amounts").split(":");
                        int[] tabAmounts = new int[tabAmountData.length];
                        for (int index = 0; index < tabAmountData.length; index++) {
                            tabAmounts[index] = Integer.parseInt(tabAmountData[index]);
                        }
                        player.getBank().getBankData().setTabAmounts(tabAmounts);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT id, val FROM player_state WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        EntityState entityState = EntityState.values()[resultSet.getInt("id")];
                        if (entityState == null) {
                            continue;
                        }
                        player.getStateManager().put(entityState,
                            entityState.getPulse().parseValue(player, resultSet.getLong("val")));
                    }
                } catch (SQLException ex) {
                    log.error("Exception reading from [player_state] for [{}].", player.getName(), ex);
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_farming_equipment WHERE pidn = ?")) {
                List<Item> itemList;
                int networth = 0;
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    itemList = new ArrayList<>();
                    while (resultSet.next()) {
                        int slot = resultSet.getInt("slot");
                        int itemId = resultSet.getInt("item_id");
                        int itemCount = resultSet.getInt("item_count");
                        int itemCharge = resultSet.getInt("item_charge");
                        Item item = new Item(itemId, itemCount, itemCharge);
                        item.setIndex(slot);
                        itemList.add(item);
                    }
                    networth += player.getFarmingManager().getEquipment().getContainer().parseSQL(itemList);
                }
                player.getMonitor().increaseNetworth(networth);
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_compost_bin WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        CompostBin compostBin = new CompostBin(resultSet.getInt("bin_id"));
                        compostBin.setTimeStamp(resultSet.getLong("bin_timestamp"));
                        player.getFarmingManager().getCompostManager().getBins().add(compostBin);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_compost_bin_inventory WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    CompostBin compostBin = null;
                    if (resultSet.first()) {
                        compostBin = player.getFarmingManager().getCompostManager().getBin(resultSet.getInt("bin_id"));
                    }
                    while (resultSet.next()) {
                        if (compostBin == null) {
                            log.warn("No compost bin found for id {} for [{}].",
                                resultSet.getInt("bin_id"), player.getName());
                            continue;
                        }
                        compostBin.getContainer().add(new Item(resultSet.getInt("item_id"),
                                resultSet.getInt("item_count"), resultSet.getInt("item_charge")), false,
                            resultSet.getInt("item_slot"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM player_patch_cycle LEFT JOIN player_patch ON player_patch_cycle.pidn = player_patch.pidn AND player_patch_cycle.patch_id = player_patch.patch_id WHERE player_patch_cycle.pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        PatchWrapper patchWrapper = player.getFarmingManager()
                            .getPatchWrapper(resultSet.getInt("patch_id"));
                        if (patchWrapper == null || patchWrapper.getPatch() == null
                            || patchWrapper.getPatch().getNodes() == null) {
                            log.warn("No patch-wrapper found for id {} for [{}].",
                                resultSet.getInt("patch_id"), player.getName());
                            continue;
                        }
                        if (resultSet.getInt("patch_position") > patchWrapper.getPatch().getNodes().length) {
                            log.warn("Out of bound patch position found for id {} for [{}].",
                                resultSet.getInt("patch_id"), player.getName());
                            continue;
                        }
                        patchWrapper.setNode(patchWrapper.getPatch().getNodes()[resultSet.getInt("patch_position")]);
                        patchWrapper.getCycle().setCompostThreshold(resultSet.getInt("compost_threshold"));
                        patchWrapper.getCycle().setGrowthTime(resultSet.getLong("growth_time"));
                        patchWrapper.getCycle().setHarvestAmount(resultSet.getInt("harvest_amount"));
                        patchWrapper.getCycle().setProtection(resultSet.getBoolean("protection"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT friend_pidn, username FROM player_friend_list LEFT JOIN player_detail ON friend_pidn = player_detail.pidn WHERE player_friend_list.pidn = ?")) {
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
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT ignore_pidn, username FROM player_ignore_list LEFT JOIN player_detail ON ignore_pidn = player_detail.pidn WHERE player_ignore_list.pidn = ?")) {
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
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_slayer WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int masterId = resultSet.getInt("master_id");
                        String task = resultSet.getString("task");
                        int taskAmount = resultSet.getInt("task_amount");
                        if (masterId >= 0 && Master.forId(masterId) != null) {
                            player.getSlayer().setMaster(Master.forId(masterId));
                        }
                        if (task != null && !Objects.equals(task, "null")) {
                            Task t = Tasks.forName(task);
                            if (t != null) {
                                player.getSlayer().setTask(t);
                                player.getSlayer().setAmount(taskAmount);
                            }
                        }
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_slayer_kill_log WHERE pidn = ?")) {
                SlayerKillLog slayerKillLog = player.getSavedData().getSlayerKillLog();
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Task task = Tasks.forName(resultSet.getString("task"));
                        if (task == null || Tasks.forValue(task) == null) {
                            continue;
                        }
                        slayerKillLog.setKillCount(Tasks.forValue(task), resultSet.getInt("kill_count"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_slayer_option WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        player.getSavedData().getActivityData().setLearnedSlayerOption(resultSet.getByte("ordinal"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT `index`, task FROM player_disabled_slayer WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        player.getSavedData().getActivityData().disableSlayerTask(resultSet.getInt("index"),
                            resultSet.getString("task"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_barrow WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        player.getSavedData().getActivityData().setBarrowsKilled(resultSet.getByte("barrow_index"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_familiar WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int npcId = resultSet.getInt("id");
                        if (npcId != -1) {
                            Familiar familiar = FamiliarManager.getFamiliars().get(npcId);
                            if (familiar != null) {
                                familiar = familiar.construct(player, npcId);
                                familiar.setTicks(resultSet.getInt("ticks"));
                                familiar.setSpecialPoints(resultSet.getInt("special_points"));
                                player.getFamiliarManager().setFamiliar(familiar);
                            } else {
                                log.warn("Failed to find familiar id [{}] for [{}].",
                                    npcId, player.getName());
                            }
                        }
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_pet_detail WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    PetDetails petDetails;
                    while (resultSet.next()) {
                        int itemId = resultSet.getInt("item_id");
                        petDetails = new PetDetails(0);
                        petDetails.setHunger(resultSet.getDouble("hunger"));
                        petDetails.setGrowth(resultSet.getDouble("growth"));
                        petDetails.setStage(resultSet.getInt("stage"));
                        player.getFamiliarManager().getPetDetails().put(itemId, petDetails);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_pet WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    PetDetails petDetails;
                    if (resultSet.first()) {
                        int itemId = resultSet.getInt("item_id");
                        petDetails = player.getFamiliarManager().getPetDetails().get(itemId);
                        Pets pets = Pets.forId(itemId);
                        if (pets == null) {
                            log.warn("Failed to find pet [{}] for [{}].",
                                itemId, player.getName());
                        } else {
                            if (petDetails == null) {
                                petDetails = new PetDetails(pets.getGrowthRate() == 0.0 ? 100.0 : 0.0);
                                player.getFamiliarManager().getPetDetails().put(itemId, petDetails);
                            }
                            player.getFamiliarManager().setFamiliar(
                                new Pet(player, petDetails, itemId, pets.getNpcId(petDetails.getStage())));
                        }
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_grand_exchange_hist WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    GrandExchangeOffer grandExchangeOffer;
                    while (resultSet.next()) {
                        grandExchangeOffer = player.getGrandExchange().getHistory()[resultSet
                            .getInt("offer_index")] = new GrandExchangeOffer(resultSet.getShort("item_id"),
                            resultSet.getBoolean("sell"));
                        grandExchangeOffer.setTotalCoinExchange(resultSet.getInt("total_coin_exchange"));
                        grandExchangeOffer.setCompletedAmount(resultSet.getInt("completed_amount"));
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_grand_exchange_offer WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    GrandExchangeOffer grandExchangeOffer;
                    while (resultSet.next()) {
                        byte offerIndex = resultSet.getByte("offer_index");
                        long offerUid = resultSet.getLong("offer_uid");
                        grandExchangeOffer = player.getGrandExchange()
                            .getGrandExchangeOffers()[offerIndex] = GEOfferDispatch.forUID(offerUid);
                        if (grandExchangeOffer != null) {
                            grandExchangeOffer.setIndex(offerIndex);
                        }
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM player_referral WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Referred referral = new Referred(resultSet.getInt("other_pidn"),
                            resultSet.getString("other_username"), resultSet.getLong("request_time"),
                            resultSet.getLong("referred_time"), resultSet.getBoolean("requested"));
                        referral.setNotified(resultSet.getBoolean("notified"));
                        player.getReferralManager().getReferrals().add(referral);
                    }
                }
            }
            ClanCommunication clanCommunication = player.getCommunication().getClan();
            if (clanCommunication != null) {
                try (PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM player_clan WHERE pidn = ?")) {
                    preparedStatement.setInt(1, player.getPidn());
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.first()) {
                            clanCommunication.setName(resultSet.getString("name"));
                            clanCommunication.setJoinRequirement(ClanRank.values()[resultSet.getByte("join_rank")]);
                            clanCommunication
                                .setMessageRequirement(ClanRank.values()[resultSet.getByte("message_rank")]);
                            clanCommunication.setKickRequirement(ClanRank.values()[resultSet.getByte("kick_rank")]);
                            clanCommunication.setLootRequirement(ClanRank.values()[resultSet.getByte("loot_rank")]);
                        }
                    }
                }
            }
            if (player.getFamiliarManager().hasFamiliar()
                && player.getFamiliarManager().getFamiliar().isBurdenBeast()) {
                BurdenBeast burdenBeast = (BurdenBeast) player.getFamiliarManager().getFamiliar();
                try (PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM player_familiar_inventory WHERE pidn = ?")) {
                    List<Item> itemList;
                    int networth = 0;
                    preparedStatement.setInt(1, player.getPidn());
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        itemList = new ArrayList<>();
                        while (resultSet.next()) {
                            if (resultSet.wasNull()) {
                                continue;
                            }
                            int slot = resultSet.getInt("inventory_slot");
                            int id = resultSet.getInt("item_id");
                            int amount = resultSet.getInt("item_quantity");
                            int charges = resultSet.getInt("item_charge");
                            Item item = new Item(id, amount, charges);
                            item.setIndex(slot);
                            itemList.add(item);
                        }
                        networth += burdenBeast.getContainer().parseSQL(itemList);
                    }
                    player.getMonitor().increaseNetworth(networth);
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Failed to load [{}].", player.getName(), ex);
            response = Response.ERROR_LOADING_PROFILE;
        }
        return response;
    }

}
