package org.gielinor.game.node.entity.player;

import org.gielinor.constants.ToxicBlowpipeConstants;
import org.gielinor.content.loyaltytitle.LoyaltyPointMonitor;
import org.gielinor.content.pking.killstreaks.KillstreaksKt;
import org.gielinor.content.statistics.boards.HighestKillCountBoard;
import org.gielinor.content.statistics.boards.LatestKillsBoard;
import org.gielinor.database.DataSource;
import org.gielinor.game.component.QuestMenuManager;
import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.content.anticheat.AntiMacroHandler;
import org.gielinor.game.content.bot.Bot;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.eco.grandexchange.GrandExchange;
import org.gielinor.game.content.global.achievementold.AchievementRepository;
import org.gielinor.game.content.global.distraction.treasuretrail.TreasureTrailManager;
import org.gielinor.game.content.global.quest.QuestRepository;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.construction.HouseManager;
import org.gielinor.game.content.skill.member.farming.FarmingManager;
import org.gielinor.game.content.skill.member.hunter.HunterManager;
import org.gielinor.game.content.skill.member.slayer.SlayerManager;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarManager;
import org.gielinor.game.interaction.Interaction;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.combat.equipment.DegradableEquipment;
import org.gielinor.game.node.entity.combat.handlers.ChinchompaSwingHandler;
import org.gielinor.game.node.entity.combat.handlers.ToxicBlowpipeSwingHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.ai.AIPlayer;
import org.gielinor.game.node.entity.player.info.*;
import org.gielinor.game.node.entity.player.info.donor.DonorManager;
import org.gielinor.game.node.entity.player.info.donor.DonorStatus;
import org.gielinor.game.node.entity.player.info.login.LoginConfiguration;
import org.gielinor.game.node.entity.player.info.referral.ReferralManager;
import org.gielinor.game.node.entity.player.info.title.TitleManager;
import org.gielinor.game.node.entity.player.link.*;
import org.gielinor.game.node.entity.player.link.appearance.Appearance;
import org.gielinor.game.node.entity.player.link.audio.AudioManager;
import org.gielinor.game.node.entity.player.link.music.MusicPlayer;
import org.gielinor.game.node.entity.player.link.prayer.Prayer;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.node.entity.player.link.request.RequestManager;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.communication.CommunicationSystem;
import org.gielinor.game.system.monitor.PlayerMonitor;
import org.gielinor.game.world.GameConstants;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.*;
import org.gielinor.game.world.map.build.DynamicRegion;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.game.world.update.MapChunkRenderer;
import org.gielinor.game.world.update.NPCRenderer;
import org.gielinor.game.world.update.PlayerRenderer;
import org.gielinor.game.world.update.UpdateSequence;
import org.gielinor.game.world.update.flag.PlayerFlags;
import org.gielinor.game.world.update.flag.player.FaceEntityFlag;
import org.gielinor.game.world.update.flag.player.FaceLocationFlag;
import org.gielinor.game.world.update.flag.player.ForceChatFlag;
import org.gielinor.net.IoSession;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.DynamicSceneContext;
import org.gielinor.net.packet.context.SceneGraphContext;
import org.gielinor.net.packet.context.SkillContext;
import org.gielinor.net.packet.out.BuildDynamicScene;
import org.gielinor.net.packet.out.SkillLevel;
import org.gielinor.net.packet.out.UpdateSceneGraph;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.config.ServerVar;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.impl.*;
import org.gielinor.rs2.task.impl.LogoutTask;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Represents a player entity.
 *
 * @author Emperor
 * @author 'Vexia
 */
public class Player extends Entity {

    private static final Logger log = LoggerFactory.getLogger(Player.class);

    private static final long DEATH_PROTECTION_TIME_IN_SECONDS = TimeUnit.HOURS.toSeconds(2);

    private static final List<String> IGNORE_RESTRICTIONS = Collections.unmodifiableList(Arrays.asList(
        "Harry", "Stan", "Erik", "Logan", "Corey"
    ));

    /**
     * The {@link PlayerDetails} player details.
     */
    private final PlayerDetails details;

    /**
     * Constructs a new {@code InventoryListner} {@code ContainerListener}
     */
    private final InventoryListener inventoryListener = new InventoryListener(this);

    /**
     * Constructs a new {@code Inventory} {@link Container}.
     */
    private final Container inventory = new Container(28).register(inventoryListener);

    /**
     * Constructs a new {@code Equipment} {@code Object}.
     */
    private final Equipment equipment = new Equipment(this);

    /**
     * Constructs a new {@link BankContainer} {@link Object}.
     */
    private final BankContainer bank;
    
    /**
     * Constructs a new {@code PriceGuideContainer} {@code Object}.
     */
    private final PriceGuideContainer priceGuideContainer = new PriceGuideContainer(this);

    /**
     * The {@code PacketDisptatch} packet dispatcher.
     */
    private final PacketDispatch packetDispatch = new PacketDispatch(this);

    /**
     * The {@code SpellBookManager} spell book manager.
     */
    private final SpellBookManager spellBookManager = new SpellBookManager();

    /**
     * Constructs a new {@code RenderInfo} {@code Object}.
     */
    private final RenderInfo renderInfo = new RenderInfo(this);

    /**
     * Constructs a new {@code InterfaceState} {@code Object}.
     */
    private final InterfaceState interfaceState = new InterfaceState(this);

    /**
     * Constructs a new {@code Emotes} {@code Object}.
     */
    private final EmoteData emotes = new EmoteData(this);

    /**
     * Constructs a new {@code PlayerFlags} {@code Object}.
     */
    private final PlayerFlags playerFlags = new PlayerFlags();

    /**
     * Constructs a new {@code Appereance} {@code Object}.
     */
    private Appearance appearance = new Appearance(this);

    /**
     * Constructs a new {@code Settings} {@code Object}.
     */
    private final Settings settings = new Settings(this);

    /**
     * Constructs a new {@code DialogueInterpreter} {@code Object}.
     */
    private final DialogueInterpreter dialogueInterpreter = new DialogueInterpreter(this);

    /**
     * Constructs a new {@code BankPinHandler} {@code Object}.
     */
    private final BankPinHandler bankPin = new BankPinHandler(this);

    /**
     * Constructs a new {@code HintIconManager} {@code Object}.
     */
    private final HintIconManager hintIconManager = new HintIconManager();

    /**
     * Constructs a new {@code SlayerCredentials} {@code Object}.
     */
    private final SlayerManager slayer = new SlayerManager(this);

    /**
     * Constructs a new {@link QuestRepository}.
     */
    private final QuestRepository questRepository = new QuestRepository(this);

    /**
     * Constructs a new
     * {@link org.gielinor.game.content.global.achievementold.AchievementRepository}.
     */
    private final AchievementRepository achievementRepository = new AchievementRepository(this);

    /**
     * Constructs a new {@code Prayer} {@code Object}.
     */
    private final Prayer prayer = new Prayer(this);

    /**
     * Constructs a new {@code SkullManager} {@code Object}.
     */
    private final SkullManager skullManager = new SkullManager(this);

    /**
     * Constructs a new
     * {@link org.gielinor.game.content.eco.grandexchange.GrandExchange}
     * instance.
     */
    private final GrandExchange grandExchange = new GrandExchange(this);

    /**
     * Constructs a new {@code FamiliarManager} {@code Object}
     */
    private final FamiliarManager familiarManager = new FamiliarManager(this);

    /**
     * Constructs a new {@code HunterManager} {@code Object}.
     */
    private final HunterManager hunterManager = new HunterManager(this);

    /**
     * The audio manager.
     */
    private final AudioManager audioManager = new AudioManager(this);

    /**
     * The {@link org.gielinor.game.component.QuestMenuManager} instance.
     */
    private final QuestMenuManager questMenuManager = new QuestMenuManager(this);

    /**
     * Constructs a new {@code ConfigurationManager} {@code Object}.
     */
    private final ConfigurationManager configManager = new ConfigurationManager(this);

    /**
     * Constructs a new {@code SavedData} {@code Object}.
     */
    private final SavedData savedData;

    /**
     * Constructs a new {@code CommunicationSystem} {@code Object}.
     */
    private final CommunicationSystem communication = new CommunicationSystem(this);

    /**
     * Constructs a new {@code RequestManager} {@code Object}.
     */
    private final RequestManager requestManager = new RequestManager(this);

    /**
     * Constructs a new {@code FarmingManager} {@code Object}.
     */
    private final FarmingManager farmingManager = new FarmingManager(this);

    /**
     * The monitor which monitors player actions.
     */
    private final PlayerMonitor monitor = new PlayerMonitor();

    /**
     * The music player instance.
     */
    private final MusicPlayer musicPlayer = new MusicPlayer(this);

    /**
     * The barcrawl miniquest manager.
     */
    private final BarcrawlManager barcrawlManager = new BarcrawlManager(this);

    /**
     * The anti macro handler.
     */
    private final AntiMacroHandler antiMacroHandler = new AntiMacroHandler(this);

    /**
     * The {@link LoyaltyPointMonitor}.
     */
    private final LoyaltyPointMonitor loyaltyPointMonitor = new LoyaltyPointMonitor(this);

    /**
     * The {@link org.gielinor.game.node.entity.player.info.donor.DonorManager}.
     */
    private final DonorManager donorManager = new DonorManager(this);

    /**
     * The
     * {@link org.gielinor.game.node.entity.player.info.referral.ReferralManager}.
     */
    private final ReferralManager referralManager = new ReferralManager(this);

    /**
     * The player's
     * {@link org.gielinor.game.content.skill.member.construction.HouseManager}.
     */
    private final HouseManager houseManager = new HouseManager(this);

    /**
     * The player's
     * {@link org.gielinor.game.content.global.distraction.treasuretrail.TreasureTrailManager}.
     */
    private final TreasureTrailManager treasureTrailManager = new TreasureTrailManager(this);

    /**
     * The player's
     * {@link org.gielinor.game.node.entity.player.info.PerkManager}.
     */
    private final PerkManager perkManager = new PerkManager(this);

    /**
     * This player's
     * {@link org.gielinor.game.node.entity.player.info.title.TitleManager}.
     */
    private final TitleManager titleManager = new TitleManager(this);

    /**
     * The current running {@link Bot} for this player.
     */
    private Bot runningBot;

    /**
     * The boolean for the player playing.
     */
    private boolean playing;

    /**
     * If the player is artificial.
     */
    protected boolean artificial;

    /**
     * Constructs a new {@code Player} {@code Object}.
     *
     * @param details The player's details.
     */
    public Player(PlayerDetails details) {
        super(details.getUsername(), Location.getRandomLocation(GameConstants.START_LOCATION, 1, true));
        super.setActive(false);
        super.setInteraction(new Interaction(this));
        this.details = details;
        this.direction = Direction.SOUTH;
        this.bank = new BankContainer(this);
        this.savedData = new SavedData(getPidn());
    }

    @Override
    public void init() {
        if (!isArtificial()) {
            getProperties().setSpawnLocation(GameConstants.HOME_LOCATION);
            getDetails().getSession().setPlayer(this);
            getDetails().getSession().setLastPing(System.currentTimeMillis() + 10_000L);
        }
        LoginConfiguration.configureLobby(this);
        super.init();
    }

    @Override
    public void clear() {
        if (!allowRemoval()) {
            Repository.getDisconnectionQueue().add(this, true);
            return;
        }
        setPlaying(false);
        getWalkingQueue().reset();
        LogoutTask task = getExtension(LogoutTask.class);
        if (task != null) {
            task.fire(this);
        }
        if (familiarManager.hasFamiliar()) {
            familiarManager.getFamiliar().clear();
        }
        ActivityManager.handleDeclines(this);
        interfaceState.close();
        interfaceState.closeSingleTab();
        super.clear();
        getZoneMonitor().clear();
        communication.notifyPlayers(false);
        UpdateSequence.getRenderablePlayers().remove(this);
        // ((HighscoreService)
        // World.getWorld().getApplicationContext().getBean("highscoreService")).saveScores(this);
        // if (!World.getSettings().isDevMode() && getPidn() > 0 &&
        // !Ironman.isIronman(this) && !specialDetails()) {
        saveHiScores();
        // }
        Repository.getDisconnectionQueue().add(this);
    }

    public void saveHiScores() {
        if (!Constants.HIGHSCORES_ENABLED || isArtificial()) {
            return;
        }
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM `player_scores` WHERE pidn=?")) {
                stmt.setInt(1, getDetails().getPidn());
                stmt.executeUpdate();
            }
            if (!getRights().isAdministrator() && !Ironman.isIronman(this)
                && !getUsername().equalsIgnoreCase("iron man") && !specialDetails()) {
                try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO `player_scores` "
                    + "(`pidn`, `player_name`, `ranking`, `attack_experience`, `defence_experience`,"
                    + " `strength_experience`, `hitpoints_experience`, `ranged_experience`,"
                    + " `prayer_experience`, `magic_experience`, `cooking_experience`,"
                    + " `woodcutting_experience`, `fletching_experience`, `fishing_experience`,"
                    + " `firemaking_experience`, `crafting_experience`, `smithing_experience`,"
                    + " `mining_experience`, `herblore_experience`, `agility_experience`,"
                    + " `thieving_experience`, `slayer_experience`, `farming_experience`,"
                    + " `runecraft_experience`, `hunter_experience`, `construction_experience`, `summoning_experience`,"
                    + " `total_experience`, `total_level`) " + "VALUES (?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?)")) {

                    stmt.setInt(1, getDetails().getPidn());
                    stmt.setString(2, getUsername());

                    if (getDetails().getRights().isPlayerModerator()) {
                        stmt.setInt(3, 1);
                    } else {
                        stmt.setInt(3, 0);
                    }

                    //if (donorManager.getDonorStatus() != DonorStatus.NONE && !donorManager.isIconHidden()) {
                    //ranking = donorManager.isDonor() ? 4 : donorManager.isSuperDonor() ? 5 : 6;
                    // TODO members ranking in highscores
                    //}
                    int index = 4;
                    for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
                        stmt.setInt(index, (int) getSkills().getExperience(i));
                        index += 1;
                    }
                    stmt.setLong(index, getSkills().getTotalExperience());
                    index += 1;
                    stmt.setInt(index, getSkills().getTotalLevel());
                    stmt.addBatch();
                    stmt.executeBatch();
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Failed to update highscores for [{}].", details.getUsername(), ex);
        }
    }

    @Override
    public void tick() {
        super.tick();
        antiMacroHandler.pulse();
        //loyaltyPointMonitor.pulse();
        // if (!artificial && (System.currentTimeMillis() -
        // getSession().getLastPing()) > 10_000L) {
        // details.getSession().disconnect();
        // getSession().setLastPing(Long.MAX_VALUE);
        // }
    }

    @Override
    public void update() {
        super.update();
        if (playerFlags.isUpdateSceneGraph()) {
            updateSceneGraph(false);
        }
        PlayerRenderer.render(this);
        if (getAttribute("render_npcs", 1) == 1) {
            NPCRenderer.render(this);
        } else {
            getRenderInfo().getLocalNpcs().clear();
        }
        if (getAttribute("render_map_chunk", 1) == 1) {
            MapChunkRenderer.render(this);
        }
    }

    @Override
    public void reset() {
        super.reset();
        playerFlags.setUpdateSceneGraph(false);
        renderInfo.updateInformation();
        if (getSkills().isLifepointsUpdate()) {
            PacketRepository.send(SkillLevel.class, new SkillContext(this, Skills.HITPOINTS));
            getSkills().setLifepointsUpdate(false);
        }
    }

    @Override
    public boolean face(Entity entity) {
        if (entity == null) {
            return !getUpdateMasks().unregisterSynced(FaceEntityFlag.getOrdinal())
                || getUpdateMasks().register(new FaceEntityFlag(null));
        }
        return getUpdateMasks().register(new FaceEntityFlag(entity), true);
    }

    @Override
    public boolean faceLocation(Location location) {
        if (location == null) {
            getUpdateMasks().unregisterSynced(FaceLocationFlag.getOrdinal());
            return true;
        }
        return getUpdateMasks().register(new FaceLocationFlag(location), true);
    }

    @Override
    public boolean sendChat(String string) {
        return getUpdateMasks().register(new ForceChatFlag(string));
    }

    @Override
    public int getClientIndex() {
        return this.getIndex();
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        CombatStyle style = getProperties().getCombatPulse().getStyle();
        if (swing) {
            if (getProperties().getSpell() != null || getProperties().getAutocastSpell() != null) {
                return CombatStyle.MAGIC.getSwingHandler();
            }
            if (settings.isSpecialToggled()) {
                int itemId = equipment.getNew(3).getId();
                CombatSwingHandler combatSwingHandler1;
                if ((combatSwingHandler1 = style.getSwingHandler().getSpecial(itemId)) != null) {
                    return combatSwingHandler1;
                }
                packetDispatch.sendMessage("Unhandled special attack for item " + itemId + "!");
            }
        }
        if (style == CombatStyle.RANGE) {
            if (equipment.getNew(3).getId() == 10034) {
                return ChinchompaSwingHandler.getInstance();
            }
            if (equipment.getNew(3).getId() == ToxicBlowpipeConstants.TOXIC_BLOWPIPE_LOADED) {
                return ToxicBlowpipeSwingHandler.getInstance();
            }
        }
        return style.getSwingHandler();
    }

    @Override
    public void commenceDeath(Entity killer) {
        super.commenceDeath(killer);
        if (prayer.get(PrayerType.RETRIBUTION)) {
            prayer.startRetribution(killer);
        }
        if (prayer.get(PrayerType.WRATH)) {
            prayer.startWrath(killer);
        }
    }

    @Override
    public void finalizeDeath(Entity killer) {
        settings.setSpecialEnergy(100);
        settings.updateRunEnergy(settings.getRunEnergy() - 100);
        Player k = killer instanceof Player ? (Player) killer : this;

        if (!k.isActive()) {
            k = this;
        }

        getActionSender().sendMessage("Oh dear, you are dead!");
        getActionSender().sendTempMusic(90);

        if (!getZoneMonitor().handleDeath(killer) && !getProperties().isSafeZone()
                && !getDetails().getRights().isAdministrator() && !Ironman.isIronman(this)) {
            GroundItemManager.create(new Item(526), getLocation(), k);
            if (getSavedData().getGlobalData().getTotalPlayTime() >= DEATH_PROTECTION_TIME_IN_SECONDS
                    && ServerVar.fetch("player_drops_disabled", 0) != 1) {
                final Container[] c = DeathTask.getContainers(this);
                for (Item item : c[1].toArray()) {
                    if (item != null) {
                        GroundItem ground;
                        if (item.hasItemPlugin()) {
                            item = item.getPlugin().getDeathItem(item);
                        }
                        if (item.getDefinition().isTradeable()) {
                            ground = new GroundItem(item.getDropItem(), getLocation(), k);
                        } else {
                            ground = new GroundItem(item, getLocation(), 200, this);
                        }
                        if (killer instanceof Player && killer != this) {
                            // PvP, remove rune pouch
                            if (ground.getId() == Item.RUNE_POUCH) {
                                getSavedData().getGlobalData().getRunePouch().clear();
                                ground = null;
                            }
                        } else if (ground.getId() == Item.RUNE_POUCH) {
                            for (Item i : getSavedData().getGlobalData().getRunePouch().getItems()) {
                                GroundItemManager.create(new GroundItem(i, getLocation(), k));
                            }
                            getSavedData().getGlobalData().getRunePouch().clear();
                            c[0].add(new Item(Item.RUNE_POUCH));
                            ground = null;
                        }
                        if (ground != null) {
                            GroundItemManager.create(ground);
                        }
                    }
                }
                // stats and stuff
                if (killer instanceof Player) {
                    LatestKillsBoard.INSTANCE.addStatistic(k.name, this.name, k.getEquipment().get(Equipment.SLOT_WEAPON).getId());
                    k.getSavedData().getGlobalData().incrementPKPoints();
                    KillstreaksKt.death(this, k);
                    KillstreaksKt.check(k, this);
                    k.getActionSender().sendMessage("You now have " + k.getSavedData().getGlobalData().getPKPoints() + " PK points!");
                    HighestKillCountBoard.INSTANCE.addStatistic(k.getName(), k.getSavedData().getGlobalData().getKillstreak());
                }
                // end stats and stuff
                equipment.clear();
                inventory.clear();
                inventory.addAll(c[0]);
                familiarManager.dismiss();
            }
        }
        if (Ironman.isIronman(this)) {
            if (!getZoneMonitor().handleDeath(killer) && !getProperties().isSafeZone()) {
                GroundItemManager.create(new Item(526), getLocation(), this);
                final Container[] c = DeathTask.getContainers(this);
                if (!Ironman.isUltimateIronman(this)) {
                    for (Item item : c[1].toArray()) {
                        if (item != null) {
                            GroundItem ground;
                            if (item.hasItemPlugin()) {
                                item = item.getPlugin().getDeathItem(item);
                            }
                            ground = new GroundItem(item, getLocation(), 200, this);
                            ground.setRemainPrivate(true);
                            if (killer != null && killer instanceof Player && killer != this) {
                                // PvP, remove rune pouch
                                if (ground.getId() == Item.RUNE_POUCH) {
                                    ground = null;
                                }
                            }
                            if (ground != null && ground.getId() == Item.RUNE_POUCH) {
                                // getSavedData().getGlobalData().getRunePouch().clear();
                                // TODO Do we clear pouch on death?
                                c[0].add(new Item(Item.RUNE_POUCH));
                            }
                            if (ground != null) {
                                GroundItemManager.create(ground);
                            }
                        }
                    }
                }
                equipment.clear();
                inventory.clear();
                if (!Ironman.isUltimateIronman(this)) {
                    inventory.addAll(c[0]);
                }
                familiarManager.dismiss();
            }
        }
        if (familiarManager.hasPet()) {
            familiarManager.dismiss(true, true);
        }
        skullManager.setSkulled(false);
        removeAttribute("combat-time");
        getPrayer().reset();
        super.finalizeDeath(killer);
        appearance.sync();
    }

    @Override
    public boolean hasProtectionPrayer(CombatStyle style) {
        return prayer.get(style.getProtectionPrayer()) || prayer.get(style.getDeflectionPrayer());
    }

    @Override
    public int getDragonfireProtection(boolean fire) {
        int value = 0;
        if (fire) {
            if (hasFireResistance()) {
                value |= 0x2;
            }
        }
        Item item = equipment.get(Equipment.SLOT_SHIELD);
        if (item != null && (item.getId() == 11283 || item.getId() == 11284 || (fire && item.getId() == 1540)
            || (!fire && (item.getId() == 2890 || item.getId() == 9731)))) {
            value |= 0x4;
        }
        if (prayer.get(PrayerType.PROTECT_FROM_MAGIC)) {
            value |= 0x8;
        }
        int randomInt = RandomUtil.random(101);
        if (randomInt <= donorManager.getDonorStatus().getDragonImmunityChance()
            && donorManager.getDonorStatus().getDragonImmunityChance() != -1) {
            value |= 0x10;
        }
        setAttribute("fire_resistance", value);
        return value;
    }

    @Override
    public int getProjectileLockonIndex() {
        return -getClientIndex() - 1;
    }

    @Override
    public void fullRestore() {
        prayer.reset();
        settings.setSpecialEnergy(100);
        settings.updateRunEnergy(-100);
        super.fullRestore();
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        if (entity instanceof NPC && !((NPC) entity).getDefinition().hasAction("attack")
            && !entity.isIgnoreAttackRestrictions(this)) {
            getActionSender().sendMessage("You can't attack this npc.");
            return false;
        }
        if ((entity instanceof Player) && !Ironman.canAttack((Player) entity, this)) {
            return false;
        }
        return super.isAttackable(entity, style);
    }

    @Override
    public boolean isPoisonImmune() {
        return getAttribute("poison:immunity", -1) > World.getTicks();
    }

    @Override
    public void onImpact(final Entity entity, BattleState state) {
        super.onImpact(entity, state);
        boolean recoil = getEquipment().getNew(Equipment.SLOT_RING).getId() == 2550;
        if (state.getEstimatedHit() > 0) {
            if (getAttribute("vengeance", false)) {
                removeAttribute("vengeance");
                final int hit = (int) (state.getEstimatedHit() * 0.75);
                sendChat("Taste vengeance!");
                if (hit > -1) {
                    entity.getImpactHandler().manualHit(Player.this, hit, HitsplatType.NORMAL);
                }
            }
            if (recoil) {
                getImpactHandler().handleRecoilEffect(entity, state.getEstimatedHit());
            }
        }
        if (state.getSecondaryHit() > 0) {
            if (recoil) {
                getImpactHandler().handleRecoilEffect(entity, state.getSecondaryHit());
            }
        }
        if (state.getThirdHit() > 0) {
            if (recoil) {
                getImpactHandler().handleRecoilEffect(entity, state.getThirdHit());
            }
        }
        if (state.getFourthHit() > 0) {
            if (recoil) {
                getImpactHandler().handleRecoilEffect(entity, state.getFourthHit());
            }
        }
        DegradableEquipment.degrade(this, entity, false);
    }

    /**
     * Checks if the containers have this item.
     *
     * @param item the item.
     * @return {@code True} if so.
     */
    public boolean hasItem(Item item) {
        return getInventory().containsItem(item) || getBank().containsItem(item) || getEquipment().containsItem(item);
    }

    /**
     * Prioritizes inventory, then bank. No equipment.
     *
     * @param item the item.
     */
    public boolean removeItem(Item item) {
        if (inventory.containsItem(item)) {
            return inventory.remove(item);
        } else if (bank.containsItem(item)) {
            return bank.remove(item);
        }
        return false;
    }

    /**
     * Initializes the player for reconnection.
     */
    public void initReconnect() {
        getInterfaceState().setChatbox(null);
        getPulseManager().clear();
        getZoneMonitor().getZones().clear();
        getViewport().setCurrentPlane(RegionManager.forId(66666).getPlanes()[3]);
        configManager.reset();
        interfaceState.resetConfigurations();
        playerFlags.setLastSceneGraph(null);
        playerFlags.setUpdateSceneGraph(false);
        playerFlags.setLastViewport(new RegionChunk[Viewport.CHUNK_SIZE][Viewport.CHUNK_SIZE]);
        renderInfo.getLocalNpcs().clear();
        renderInfo.getLocalPlayers().clear();
        renderInfo.setLastLocation(null);
        renderInfo.setOnFirstCycle(true);
        for (int i = 0; i < renderInfo.getAppearanceStamps().length; i++) {
            renderInfo.getAppearanceStamps()[i] = 0;
        }
    }

    /**
     * Updates the player's scene graph.
     *
     * @param login If the player is logging in.
     */
    public void updateSceneGraph(boolean login) {
        Region region = getViewport().getRegion();
        if (region instanceof DynamicRegion || (region == null
            && (region = RegionManager.getRegionCache().get(location.getRegionId())) instanceof DynamicRegion
            || region == null)) {
            PacketRepository.send(BuildDynamicScene.class, new DynamicSceneContext(this, login));
        } else {
            PacketRepository.send(UpdateSceneGraph.class, new SceneGraphContext(this, login));
        }
    }

    /**
     * Toggles the debug mode.
     */
    public void toggleDebug() {
        setAttribute("debug", !getAttribute("debug", false));
    }

    /**
     * Checks if the player is allowed to be removed from the game.
     *
     * @return <code>True</code> if so.
     */
    public boolean allowRemoval() {
        return getAttribute("FORCE_KICK", false)
            || !(inCombat() || getSkills().getLifepoints() < 1 || DeathTask.isDead(this));
    }

    /**
     * Sends a message to the player if it's an administrator.
     *
     * @param string The message.
     */
    public void debug(String string) {
        if (isDebug()) {
            packetDispatch.sendMessage(string);
        }
    }

    /**
     * Checks if the player is debugging.
     *
     * @return <code>True</code> if so.
     */
    public boolean isDebug() {
        return getAttribute("debug", GameConstants.DEBUG);
    }

    /**
     * Sends the dialogue for something unavailable if they are not a member,
     * with the action.
     *
     * @param action      The action.
     * @param donorStatus The
     *                    {@link org.gielinor.game.node.entity.player.info.donor.DonorStatus}
     *                    required..
     * @return If this player's donor status is the required status.
     */
    public boolean sendDonorNotification(String action, DonorStatus donorStatus) {
        if (donorStatus == null ? getDonorManager().getDonorStatus().ordinal() < DonorStatus.SAPPHIRE_MEMBER.ordinal()
            : getDonorManager().getDonorStatus().ordinal() < donorStatus.ordinal()) {
            getDialogueInterpreter().sendPlaneMessage(
                "You must be a " + (donorStatus == null ? "member" : donorStatus.name().toLowerCase().replaceAll("_", " ")) + " " + action);
            return true;
        }
        return false;
    }

    /**
     * Sends the dialogue for something unavailable if they are not a member,
     * with the action.
     *
     * @param action The action.
     * @return If this player's donor status is the required status.
     */
    public boolean sendDonorNotification(String action) {
        return sendDonorNotification(action, null);
    }

    /**
     * Sends the dialogue for something unavailable if they are not a member,
     * with the action.
     *
     * @return If this player's donor status is the required status.
     */
    public boolean sendDonorNotification() {
        return sendDonorNotification("to do this.");
    }

    /**
     * Gets the {@code PlayerDetails}.
     *
     * @return the details.
     */
    public PlayerDetails getDetails() {
        return details;
    }

    /**
     * Gets the player's identification number.
     *
     * @return The player's identification number.
     */
    public int getPidn() {
        return getDetails().getPidn();
    }

    /**
     * Gets the player's rights.
     *
     * @return The rights.
     */
    public Rights getRights() {
        return getDetails().getRights();
    }

    /**
     * Checks if this player can interact with the given player.
     *
     * @param target The other player.
     * @return Whether or not the players can interact based on ranks.
     */
    public boolean canInteractWith(Player target) {
        if (ignoreRestrictions())
            return true;
        if (getRights() == Rights.REGULAR_PLAYER || getRights() == Rights.PLAYER_MODERATOR) {
            return target.ignoreRestrictions();
        }
        return Repository.getPlayers().contains(target);
    }

    /**
     * Whether or not this player can ignore restrictions such as trading,
     * shopping, etc.
     *
     * @return Whether or not they can ignore restrictions.
     */
    public boolean specialDetails() {
        return ignoreRestrictions();
    }

    /**
     * Whether or not this player can ignore restrictions such as trading,
     * shopping, etc.
     *
     * @return Whether or not they can ignore restrictions.
     */
    public boolean ignoreRestrictions() {
        for (String n : IGNORE_RESTRICTIONS) {
            if (getName().equalsIgnoreCase(n))
                return true;
        }
        return false;
    }

    /**
     * Gets the players {@code Session}.
     *
     * @return the {@code PlayerDetails} {@code Object} session.
     */
    public IoSession getSession() {
        return details.getSession();
    }

    /**
     * Gets the {@code Equipment}
     *
     * @return {@code Equipment}.
     */
    public Equipment getEquipment() {
        return equipment;
    }

    /**
     * Gets the bank.
     *
     * @return The bank.
     */
    public BankContainer getBank() {
        return bank;
    }
    
    /**
     * Gets the price guide container.
     *
     * @return The price guide container.
     */
    public PriceGuideContainer getPriceGuideContainer() {
        return priceGuideContainer;
    }

    /**
     * @return the inventory
     */
    public Container getInventory() {
        return inventory;
    }

    /**
     * Sets the playing flag.
     *
     * @param playing the flag to set.
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * Checks if the player is playing.
     *
     * @return <code>True</code> if so.
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Gets the renderInfo.
     *
     * @return The renderInfo.
     */
    public RenderInfo getRenderInfo() {
        return renderInfo;
    }

    /**
     * Gets the appearance.
     *
     * @return The appearance.
     */
    public Appearance getAppearance() {
        return appearance;
    }

    /**
     * Gets the playerFlags.
     *
     * @return The playerFlags.
     */
    public PlayerFlags getPlayerFlags() {
        return playerFlags;
    }

    /**
     * Gets the {@code PacketDispatch}.
     *
     * @return the packet dispatch.
     */
    public PacketDispatch getActionSender() {
        return packetDispatch;
    }

    /**
     * Gets the {@code Emotes}.
     *
     * @return the emotes.
     */
    public EmoteData getEmotes() {
        return emotes;
    }

    /**
     * @return the spellBookManager
     */
    public SpellBookManager getSpellBookManager() {
        return spellBookManager;
    }

    /**
     * Gets the settings.
     *
     * @return The settings.
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * @return the interface manager.
     */
    public InterfaceState getInterfaceState() {
        return interfaceState;
    }

    /**
     * Gets the dialogue interpreter.
     *
     * @return The dialogue interpreter.
     */
    public DialogueInterpreter getDialogueInterpreter() {
        return dialogueInterpreter;
    }

    /**
     * Gets the bank pin.
     *
     * @return The bank pin.
     */
    public BankPinHandler getBankPin() {
        return bankPin;
    }

    /**
     * @return the hintIconManager
     */
    public HintIconManager getHintIconManager() {
        return hintIconManager;
    }

    /**
     * Gets the slayer.
     *
     * @return The slayer.
     */
    public SlayerManager getSlayer() {
        return slayer;
    }

    /**
     * Checks if the player is an {@link AIPlayer}.
     *
     * @return <code>True</code> if so.
     */
    public boolean isArtificial() {
        return artificial;
    }

    /**
     * Sets if the player is an {@link AIPlayer}.
     *
     * @param artificial
     */
    public void setArtificial(boolean artificial) {
        this.artificial = artificial;
    }

    /**
     * @return the questRepository.
     */
    public QuestRepository getQuestRepository() {
        return questRepository;
    }

    /**
     * Gets the achievement repository.
     *
     * @return The achievement repository.
     */
    public AchievementRepository getAchievementRepository() {
        return achievementRepository;
    }

    /**
     * @return the prayer.
     */
    public Prayer getPrayer() {
        return prayer;
    }

    /**
     * @return the skullManager.
     */
    public SkullManager getSkullManager() {
        return skullManager;
    }

    /**
     * Gets the
     * {@link org.gielinor.game.content.eco.grandexchange.GrandExchange}
     * instance.
     *
     * @return The instance.
     */
    public GrandExchange getGrandExchange() {
        return grandExchange;
    }

    /**
     * @return the familiarManager.
     */
    public FamiliarManager getFamiliarManager() {
        return familiarManager;
    }

    /**
     * Gets the hunterManager.
     *
     * @return The hunterManager.
     */
    public HunterManager getHunterManager() {
        return hunterManager;
    }

    /**
     * Gets the audioManager.
     *
     * @return the audioManager
     */
    public AudioManager getAudioManager() {
        return audioManager;
    }

    /**
     * Gets the {@link org.gielinor.game.component.QuestMenuManager}.
     *
     * @return The {@code QuestMenuManager}.
     */
    public QuestMenuManager getQuestMenuManager() {
        return questMenuManager;
    }

    /**
     * Gets the configManager.
     *
     * @return The configManager.
     */
    public ConfigurationManager getConfigManager() {
        return configManager;
    }

    /**
     * Gets the communication.
     *
     * @return The communication.
     */
    public CommunicationSystem getCommunication() {
        return communication;
    }

    /**
     * Gets the requestManager.
     *
     * @return The requestManager.
     */
    public RequestManager getRequestManager() {
        return requestManager;
    }

    /**
     * Gets the savedData.
     *
     * @return The savedData.
     */
    public SavedData getSavedData() {
        return savedData;
    }

    /**
     * Gets the farmingManager.
     *
     * @return The farmingManager.
     */
    public FarmingManager getFarmingManager() {
        return farmingManager;
    }

    /**
     * Gets the monitor.
     *
     * @return The monitor.
     */
    public PlayerMonitor getMonitor() {
        return monitor;
    }

    /**
     * Gets the musicPlayer.
     *
     * @return The musicPlayer.
     */
    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    /**
     * Gets the barcrawlManager.
     *
     * @return The barcrawlManager.
     */
    public BarcrawlManager getBarcrawlManager() {
        return barcrawlManager;
    }

    /**
     * Gets the antiMacroHandler.
     *
     * @return The antiMacroHandler.
     */
    public AntiMacroHandler getAntiMacroHandler() {
        return antiMacroHandler;
    }

    /**
     * Gets the {@link LoyaltyPointMonitor}.
     *
     * @return The gielinor point monitor.
     */
    public LoyaltyPointMonitor getLoyaltyPointMonitor() {
        return loyaltyPointMonitor;
    }

    /**
     * Gets the
     * {@link org.gielinor.game.node.entity.player.info.donor.DonorManager}.
     *
     * @return The donor manager.
     */
    public DonorManager getDonorManager() {
        return donorManager;
    }

    /**
     * Gets the
     * {@link org.gielinor.game.node.entity.player.info.referral.ReferralManager}.
     *
     * @return The referral manager.
     */
    public ReferralManager getReferralManager() {
        return referralManager;
    }

    /**
     * Gets this player's
     * {@link org.gielinor.game.content.skill.member.construction.HouseManager}.
     *
     * @return The house manager.
     */
    public HouseManager getHouseManager() {
        return houseManager;
    }

    /**
     * Gets this player's
     * {@link org.gielinor.game.content.global.distraction.treasuretrail.TreasureTrailManager}.
     *
     * @return The treasure trail manager.
     */
    public TreasureTrailManager getTreasureTrailManager() {
        return treasureTrailManager;
    }

    /**
     * Gets this player's
     * {@link org.gielinor.game.node.entity.player.info.PerkManager}.
     *
     * @return The perk manager.
     */
    public PerkManager getPerkManager() {
        return perkManager;
    }

    /**
     * Sets the running {@link Bot}.
     *
     * @param runningBot The running {@link Bot}.
     */
    public void setRunningBot(Bot runningBot) {
        this.runningBot = runningBot;
    }

    /**
     * Gets the running {@link Bot}.
     *
     * @return The running {@link Bot}.
     */
    public Bot getRunningBot() {
        return runningBot;
    }

    /**
     * Gets this player's
     * {@link org.gielinor.game.node.entity.player.info.title.TitleManager}.
     *
     * @return The title manager.
     */
    public TitleManager getTitleManager() {
        return titleManager;
    }

    /**
     * Gets whether or not this player is hidden from another player.
     *
     * @param player The player to exclude.
     * @return Whether or not they are hidden.
     */
    public boolean isHidden(Player player) {
        if (player != null && player.getRights().isAdministrator()) {
            return false;
        }
        return false;
    }

    public long getNetworth() {
        long networth = 0;
        for (Item item : getInventory().toArray()) {
            if (item == null) {
                continue;
            }
            if (item.getId() == -1) {
                continue;
            }
            networth += item.getValue();
        }
        for (Item item : getEquipment().toArray()) {
            if (item == null) {
                continue;
            }
            if (item.getId() == -1) {
                continue;
            }
            networth += item.getValue();
        }
        return networth + getBankNetworth();
    }

    /**
     * Gets the player's bank networth.
     *
     * @return The bank networth.
     */
    public int getBankNetworth() {
        int networth = 0;
        for (Item item : getBank().toArray()) {
            if (item == null) {
                continue;
            }
            if (item.getId() == -1) {
                continue;
            }
            networth += item.getValue();
        }
        return networth;
    }

    /**
     * Sends a global news message to players for this player.
     *
     * @param colour  The colour of the text.
     * @param message The message.
     */
    public void sendGlobalNewsMessage(String colour, String message, int icon) {
        if (getSettings().getPrivateChatSetting() != 3) { // offline?
            for (Player onlinePlayer : Repository.getPlayers()) {
                if (onlinePlayer == null || !onlinePlayer.isActive()) {
                    continue;
                }
                if (getSettings().getPrivateChatSetting() == 2) { // friends only?
                    if (!getCommunication().hasContact(onlinePlayer.getPidn())) {
                        continue;
                    }
                }
                onlinePlayer.getActionSender()
                    .sendMessage("<col=" + colour + "><shad=000>" + (icon == -1 ? "" : "<img=" + icon + ">")
                        + "News: " + TextUtils.formatDisplayName(getName())
                        + (message.startsWith(" ") ? message : " " + message), 1);
            }
        }
    }

    @Override
    public String toString() {
        return Player.class.getName() + " [name=" + name + " pidn=" + getDetails().getPidn() + " rights=" + getRights()
            + " donor=" + (donorManager.getDonorStatus().name()) + " index=" + this.getIndex() + "]";
    }

    public InventoryListener getInventoryListener() {
        return inventoryListener;
    }

    public boolean isIronman() {
        return Ironman.isIronman(this);
    }

}
