package org.gielinor.game.node.entity.player.link;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.runecrafting.Pouch;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.IronmanMode;
import org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerType;
import org.gielinor.utilities.buffer.ByteBufferUtils;
import plugin.activity.duelarena.DuelRule;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Represents the quest data to save.
 *
 * @author 'Vexia
 * @author <a href="http://Gielinor.org/">Gielinor</a>
 */
public final class GlobalData implements SavingModule {

    /**
     * Represents fields to be inserted into the database.
     */
    public static final String[] FIELDS = new String[]{ "chat_ping", "tutor_claim", "luthas_task", "karamja_bananas",
        "silk_steal", "lumbridge_rope", "apprentice", "fritz_glass", "zaff_amount", "zaff_time", "wydin_employee",
        "draynor_recording", "essence_teleporter", "recoil_damage", "paid_waterfall", "paid_blacksmith",
        "play_time", "ectofuntus_bone_type", "ectofuntus_bone_ground", "yell_tag", "last_yell", "visibility",
        "combat_announced", "small_pouch_count", "medium_pouch_count", "large_pouch_count", "giant_pouch_count",
        "voting_points", "experience_modifier_time", "experience_modifier", "starter_package", "last_clan_chat",
        "loyalty_points", "xp_drops", "player_kills", "player_deaths", "prayer_book", "beta_status",
        "easter_event_stage", "easter_dyes_given", "easter_title", "walked_to_dorgeshkaan", "roald_points",
        "summoning_left_click", "login_streak", "killstreak", "pets_insured", "npc_kill_count",
        //"experience_modifiers_time", "experience_modifiers",
//        "accuracy_modifier_time", "accuracy_modifier","strength_modifier_time", "strength_modifier","defence_modifier_time", "defence_modifier",
//        "drop_modifier_time", "drop_modifier"
    };

    /**
     * Represents the last chat ping.
     */
    private long chatPing;

    /**
     * Represents the time between tutor claims.
     */
    private long tutorClaim;

    /**
     * Represents if Luthas has given us a task.
     */
    private boolean luthasTask;

    /**
     * Represents the stashed Karamja bananas.
     */
    private int karamjaBananas;

    /**
     * Represents the silk steal time.
     */
    private long silkSteal;

    /**
     * Represents the strong hold rewards. player_stronghold_reward
     */
    private boolean[] strongHoldRewards = new boolean[4];

    /**
     * Represents if the lumbridge rope has been tied;
     */
    private boolean lumbridgeRope;

    /**
     * Represents if the player has spoken to the apprentice.
     */
    private boolean apprentice;

    /**
     * Represents if the player has spoken to the friz glass blower.
     */
    private boolean fritzGlass;

    /**
     * Represents the Zaff npc staff amount.
     */
    private int zaffAmount = 8;

    /**
     * Represents the time until you can buy from Zaff.
     */
    private long zaffTime;

    /**
     * Represents if you're a Wydin employee.
     */
    private boolean wydinEmployee;

    /**
     * Represents if the draynor recording has been seen.
     */
    private boolean draynorRecording;

    /**
     * Represents the npc id of the essence teleporter.
     */
    private int essenceTeleporter;

    /**
     * The amount of recoil damage left (ring of recoil).
     */
    private int recoilDamage = 40;

    /**
     * Whether or not the player paid the waterfall fee.
     */
    private boolean paidWaterfall;

    /**
     * Whether or not the player paid the blacksmith fee (shilo village).
     */
    private boolean paidBlacksmith;

    /**
     * Represents how long the player has played the server.
     */
    private long playTime;

    /**
     * The time the player started playing.
     */
    private long startTime;

    /**
     * The type of bone the player has in the ectofuntus bone grinder.
     */
    private int ectofuntusBoneType;

    /**
     * Whether or not the ectofuntus bone grinder has been ground.
     */
    private boolean ectofuntusBonesGround;

    /**
     * The player's yell colour.
     */
    private long lastYell = 0;

    /**
     * The player's yell tag.
     */
    private String yellTag = "";

    /**
     * Whether or not the player is hidden.
     */
    private byte visibility;

    /**
     * Whether or not this player has reached max combat before.
     */
    private boolean combatAnnounced;

    /**
     * The last level milestone.
     */
    private int lastMilestone;

    /**
     * The next level milestone.
     */
    private int nextMilestone = 10;

    /**
     * The level milestones reached.
     */
    private boolean[] milestones = new boolean[10];

    /**
     * The experience milestones reached.
     */
    private boolean[] experienceMilestones = new boolean[30];

    /**
     * The small pouch container.
     */
    private Container smallPouch = new Container(Pouch.SMALL.getCapacity());

    /**
     * The medium pouch container.
     */
    private Container mediumPouch = new Container(Pouch.MEDIUM.getCapacity());

    /**
     * The large pouch container.
     */
    private Container largePouch = new Container(Pouch.LARGE.getCapacity());

    /**
     * The giant pouch container.
     */
    private Container giantPouch = new Container(Pouch.GIANT.getCapacity());

    /**
     * The voting points.
     */
    private int votingPoints;

    /**
     * The modifier time is in minutes.
     */
    private int
        experienceModifierTime,
        accuracyModifierTime,
        strengthModifierTime,
        defenceModifierTime,
        dropModifierTime;

    /**
     * The result value (respective to the modifier type) is the pre-modified value multiplied by the modifier value.
     */
    private double
        experienceModifier,
        accuracyModifier,
        strengthModifier,
        defenceModifier,
        dropModifier;

    /**
     * The experience modifiers time in minutes.
     */
    private int[] experienceModifiersTime;

    /**
     * Experience modifiers array whose elements are skill specific, the index of which is respective to a skill index.
     */
    private double[] experienceModifiers = new double[Skills.ENABLED_SKILLS.length];

    /**
     * The
     * {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}
     * the player is using.
     */
    private StarterPackage starterPackage;

    /**
     * The last clan chat the player was in.
     */
    private String lastClanChat;

    /**
     * The player's Gielinor points.
     */
    private long loyaltyPoints;

    /**
     * The player's loyalty login streak.
     */
    private int loginStreak;

    /**
     * The player's xp drops.
     */
    private long xpDrops;

    /**
     * The player's last duel rules.
     */
    private List<DuelRule> lastDuelRules = new ArrayList<>();

    /**
     * The player's preset duel rules.
     */
    private List<DuelRule> presetDuelRules = new ArrayList<>();

    /**
     * The {@link Player}'s player kills.
     */
    private int playerKills;

    /**
     * The player's deaths by players.
     */
    private int playerDeaths;

    /**
     * The player's killstreak
     */
    private int killstreak;

    /**
     * The player's prayer book.
     */
    private int prayerBook;

    /**
     * Whether or not the player has beta status.
     */
    private boolean betaStatus;

    /**
     * The stage of the Easter event the player is on.
     */
    private int easterEventStage;

    /**
     * The ids of children the player has delivered eggs to.
     */
    private List<Integer> eggsDelivered = new ArrayList<>();

    /**
     * If the player has given the Easter bunny the Blue dye.
     */
    private boolean easterDyesGiven;

    /**
     * Whether or not the player has unlocked the Easter event
     * {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle}.
     */
    private boolean easterTitle;

    /**
     * Whether or not the player has walked to Dorgesh-Kaan mine.
     */
    private boolean walkedToDorgeshKaan;

    /**
     * The player's King Roald points.
     */
    private int roaldPoints;

    /**
     * The list of items unlocked in Roald's chest.
     */
    private List<Integer> unlockedRoaldsItems = new ArrayList<>();

    /**
     * The rune pouch container.
     */
    private Container runePouch = new Container(3, ContainerType.ALWAYS_STACK);

    /**
     * The player's left-click option for Summoning.
     */
    private String leftClickOption = "Follower details";

    /**
     * The player's toxic blowpipe dart id.
     */
    private int blowpipeDartId;

    /**
     * The player's toxic blowpipe dart amount.
     */
    private int blowpipeDartAmount;

    /**
     * The player's toxic blowpipe Scales amount.
     */
    private int blowpipeDartScales;

    /**
     * The {@link Player}'s PK points.
     */
    private int pkPoints;

    /**
     * Represents the player's {@link org.gielinor.game.node.entity.npc.NPC} kill count
     */
    private int npcKillCount = 0;

    /**
     * Represents the amount of time the player has died at {@link plugin.activity.duelarena.DuelArenaActivityPlugin}
     */
    private int duelDeaths;

    private int petsInsured;

    /**
     * Represents the pets the player has lost.
     */
    private List<Integer> lostPets = new ArrayList<>();

    private int pidn;

    /**
     * Creates the new
     * {@link org.gielinor.game.node.entity.player.link.GlobalData} instance.
     */
    public GlobalData(int pidn) {
        this.pidn = pidn;
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        SavedData.save(byteBuffer, chatPing, 1);
        SavedData.save(byteBuffer, tutorClaim, 2);
        SavedData.save(byteBuffer, luthasTask, 3);
        SavedData.save(byteBuffer, karamjaBananas, 4);
        SavedData.save(byteBuffer, silkSteal, 5);
        SavedData.save(byteBuffer, strongHoldRewards, 6);
        SavedData.save(byteBuffer, lumbridgeRope, 7);
        SavedData.save(byteBuffer, apprentice, 8);
        SavedData.save(byteBuffer, fritzGlass, 9);
        SavedData.save(byteBuffer, zaffAmount, 10);
        SavedData.save(byteBuffer, zaffTime, 11);
        SavedData.save(byteBuffer, wydinEmployee, 12);
        SavedData.save(byteBuffer, draynorRecording, 13);
        SavedData.save(byteBuffer, essenceTeleporter, 14);
        SavedData.save(byteBuffer, recoilDamage, 15);
        SavedData.save(byteBuffer, paidWaterfall, 16);
        SavedData.save(byteBuffer, paidBlacksmith, 17);
        SavedData.save(byteBuffer, getTotalPlayTime(), 18);
        SavedData.save(byteBuffer, startTime, 19);
        SavedData.save(byteBuffer, ectofuntusBoneType, 20);
        SavedData.save(byteBuffer, ectofuntusBonesGround, 21);
        // Yell colour
        byteBuffer.put((byte) 22);
        ByteBufferUtils.putRS2String(yellTag, byteBuffer);
        SavedData.save(byteBuffer, visibility, 23);
        SavedData.save(byteBuffer, combatAnnounced, 24);
        SavedData.save(byteBuffer, milestones, 25);
        SavedData.save(byteBuffer, experienceMilestones, 26);
        // Small pouch
        byteBuffer.put((byte) 27);
        smallPouch.save(byteBuffer);
        // Medium pouch
        byteBuffer.put((byte) 28);
        mediumPouch.save(byteBuffer);
        // Large pouch
        byteBuffer.put((byte) 29);
        largePouch.save(byteBuffer);
        // Giant pouch
        byteBuffer.put((byte) 30);
        giantPouch.save(byteBuffer);
        SavedData.save(byteBuffer, votingPoints, 31);
        SavedData.save(byteBuffer, experienceModifierTime, 32);
        SavedData.save(byteBuffer, experienceModifier, 33);
        // Starter package
        if (starterPackage != null) {
            byteBuffer.put((byte) 34);
            byteBuffer.putInt(starterPackage.getId());
        }
        // Last clan chat
        if (lastClanChat != null) {
            byteBuffer.put((byte) 35);
            ByteBufferUtils.putRS2String(lastClanChat, byteBuffer);
        }
        SavedData.save(byteBuffer, loyaltyPoints, 36);
        SavedData.save(byteBuffer, xpDrops, 37);
        // Last duel rules
        byteBuffer.put((byte) 38);
        byteBuffer.putInt(lastDuelRules.size());
        for (DuelRule duelRule : lastDuelRules) {
            byteBuffer.put((byte) duelRule.ordinal());
        }
        // Preset duel rules
        byteBuffer.put((byte) 39);
        byteBuffer.putInt(presetDuelRules.size());
        for (DuelRule duelRule : presetDuelRules) {
            byteBuffer.put((byte) duelRule.ordinal());
        }
        SavedData.save(byteBuffer, playerKills, 40);
        SavedData.save(byteBuffer, playerDeaths, 41);
        SavedData.save(byteBuffer, prayerBook, 42);
        if (betaStatus) {
            byteBuffer.put((byte) 43);
        }
        if (easterTitle) {
            byteBuffer.put((byte) 44);
        }
        SavedData.save(byteBuffer, easterEventStage, 45);
        // Eggs delivered
        byteBuffer.put((byte) 46);
        byteBuffer.putInt(eggsDelivered.size());
        for (int npc : eggsDelivered) {
            byteBuffer.putInt(npc);
        }
        if (easterDyesGiven) {
            byteBuffer.put((byte) 47);
        }
        if (walkedToDorgeshKaan) {
            byteBuffer.put((byte) 48);
        }
        SavedData.save(byteBuffer, roaldPoints, 49);
        // Unlocked Roald items
        byteBuffer.put((byte) 50);
        byteBuffer.putInt(unlockedRoaldsItems.size());
        for (int itemId : unlockedRoaldsItems) {
            byteBuffer.putInt(itemId);
        }
        // Rune pouch
        byteBuffer.put((byte) 51);
        getRunePouch().save(byteBuffer);
        // Left-click summoning option
        byteBuffer.put((byte) 52);
        ByteBufferUtils.putString(leftClickOption, byteBuffer);
        // Blowpipe dart id
        byteBuffer.put((byte) 53);
        byteBuffer.putInt(blowpipeDartId);
        // Blowpipe dart amount
        byteBuffer.put((byte) 54);
        byteBuffer.putInt(blowpipeDartAmount);
        // Blowpipe dart amount
        byteBuffer.put((byte) 55);
        byteBuffer.putInt(blowpipeDartScales);
        byteBuffer.put((byte) 56);
        byteBuffer.putInt(pkPoints);
        byteBuffer.put((byte) 57);
        byteBuffer.putInt(lostPets.size());
        for (int lostPet : lostPets) {
            byteBuffer.putInt(lostPet);
        }
        SavedData.save(byteBuffer, experienceModifiersTime, 59);
        SavedData.save(byteBuffer, experienceModifiers, 60);
        SavedData.save(byteBuffer, accuracyModifierTime, 61);
        SavedData.save(byteBuffer, accuracyModifier, 62);
        SavedData.save(byteBuffer, strengthModifierTime, 63);
        SavedData.save(byteBuffer, strengthModifier, 64);
        SavedData.save(byteBuffer, defenceModifierTime, 65);
        SavedData.save(byteBuffer, defenceModifier, 66);
        SavedData.save(byteBuffer, dropModifierTime, 67);
        SavedData.save(byteBuffer, dropModifier, 68);
        byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int opcode;
        int length;

        while ((opcode = byteBuffer.get()) != 0) {
            switch (opcode) {
                case 1: chatPing = byteBuffer.getLong();break;
                case 2: tutorClaim = byteBuffer.getLong();break;
                case 3: luthasTask = byteBuffer.get() == 1;break;
                case 4: karamjaBananas = byteBuffer.getInt();break;
                case 5: silkSteal = byteBuffer.getLong();break;
                case 6:
                    for (byte index = 0; index < strongHoldRewards.length; index++)
                        strongHoldRewards[index] = byteBuffer.get() == 1;
                    break;
                case 7: lumbridgeRope = byteBuffer.get() == 1;break;
                case 8: apprentice = byteBuffer.get() == 1;break;
                case 9: fritzGlass = byteBuffer.get() == 1;break;
                case 10: zaffAmount = byteBuffer.getInt();break;
                case 11: zaffTime = byteBuffer.getInt();break;
                case 12: wydinEmployee = byteBuffer.get() == 1;break;
                case 13: draynorRecording = byteBuffer.get() == 1;break;
                case 14: essenceTeleporter = byteBuffer.getInt();break;
                case 15: recoilDamage = byteBuffer.getInt();break;
                case 16: paidWaterfall = byteBuffer.get() == 1;break;
                case 17: paidBlacksmith = byteBuffer.get() == 1;break;
                case 18: playTime = byteBuffer.getLong();break;
                case 19: startTime = byteBuffer.getLong();break;
                case 20: ectofuntusBoneType = byteBuffer.getInt();break;
                case 21: ectofuntusBonesGround = byteBuffer.get() == 1;break;
                case 22: yellTag = ByteBufferUtils.getRS2String(byteBuffer);break;
                case 23: visibility = byteBuffer.get();break;
                case 24: combatAnnounced = byteBuffer.get() == 1;break;
                case 25:
                    for (byte index = 0; index < milestones.length; index++)
                        milestones[index] = byteBuffer.get() == 1;
                    break;
                case 26:
                    for (byte index = 0; index < experienceMilestones.length; index++)
                        experienceMilestones[index] = byteBuffer.get() == 1;
                    break;
                case 27: smallPouch.parse(byteBuffer);break;
                case 28: mediumPouch.parse(byteBuffer);break;
                case 29: largePouch.parse(byteBuffer);break;
                case 30: giantPouch.parse(byteBuffer);break;
                case 31: votingPoints = byteBuffer.getInt();break;
                case 32: experienceModifierTime = byteBuffer.getInt();break;
                case 33: experienceModifier = byteBuffer.getDouble();break;
                case 34: starterPackage = StarterPackage.forId(byteBuffer.getInt() & 0xFFFF);break;
                case 35: lastClanChat = ByteBufferUtils.getRS2String(byteBuffer);break;
                case 36: loyaltyPoints = byteBuffer.getLong();break;
                case 37: xpDrops = byteBuffer.getLong();break;
                case 38:
                    length = byteBuffer.getInt();
                    while (length > 0) {
                        byte ordinal = byteBuffer.get();
                        if (ordinal < DuelRule.values().length) {
                            lastDuelRules.add(DuelRule.values()[ordinal]);
                        }
                        length--;
                    }
                    break;
                case 39:
                    length = byteBuffer.getInt();
                    while (length > 0) {
                        byte ordinal = byteBuffer.get();
                        if (ordinal < DuelRule.values().length) {
                            presetDuelRules.add(DuelRule.values()[ordinal]);
                        }
                        length--;
                    }
                    break;
                case 40: playerKills = byteBuffer.getInt();break;
                case 41: playerDeaths = byteBuffer.getInt();break;
                case 42: prayerBook = byteBuffer.getInt();break;
                case 43: betaStatus = true;break;
                case 44: easterTitle = true;break;
                case 45: easterEventStage = byteBuffer.getInt();break;
                case 46:
                    length = byteBuffer.getInt();
                    while (length > 0) {
                        eggsDelivered.add(byteBuffer.getInt());
                        length--;
                    }
                    break;
                case 47: easterDyesGiven = true;break;
                case 48: walkedToDorgeshKaan = true;break;
                case 49: roaldPoints = byteBuffer.getInt();break;
                case 50:
                    length = byteBuffer.getInt();
                    while (length > 0) {
                        int itemId = byteBuffer.getInt();
                        unlockedRoaldsItems.add(itemId);
                        length--;
                    }
                    break;
                case 51: getRunePouch().parse(byteBuffer);break;
                case 52: leftClickOption = ByteBufferUtils.getRS2String(byteBuffer);break;
                case 53: blowpipeDartId = byteBuffer.getInt();break;
                case 54: blowpipeDartAmount = byteBuffer.getInt();break;
                case 55: blowpipeDartScales = byteBuffer.getInt();break;
                case 56: pkPoints = byteBuffer.getInt();break;
                case 57:
                    length = byteBuffer.getInt();
                    while (length > 0) {
                        int lostPet = byteBuffer.getInt();
                        lostPets.add(lostPet);
                        length--;
                    }
                    break;
                case 59:
                    for (byte index = 0; index < experienceModifiersTime.length; index++)
                        experienceModifiersTime[index] = byteBuffer.getInt();
                    break;
                case 60:
                    for(byte index = 0; index < experienceModifiers.length; index++)
                        experienceModifiers[index] = byteBuffer.getDouble();
                    break;
                case 61: accuracyModifierTime = byteBuffer.getInt();break;
                case 62: accuracyModifier = byteBuffer.getDouble();break;
                case 63: strengthModifierTime = byteBuffer.getInt();break;
                case 64: strengthModifier = byteBuffer.getDouble();break;
                case 65: defenceModifierTime = byteBuffer.getInt();break;
                case 66: defenceModifier = byteBuffer.getDouble();break;
                case 67: dropModifierTime = byteBuffer.getInt();break;
                case 68: dropModifier = byteBuffer.getDouble();break;
            }
        }
    }

    /**
     * Gets the lumbridgeRope.
     *
     * @return The lumbridgeRope.
     */
    public boolean hasTiedLumbridgeRope() {
        return lumbridgeRope;
    }

    /**
     * Sets the lumbridgeRope.
     *
     * @param lumbridgeRope
     *            The lumbridgeRope to set.
     */
    public void setLumbridgeRope(boolean lumbridgeRope) {
        this.lumbridgeRope = lumbridgeRope;
    }

    /**
     * Gets the apprentice.
     *
     * @return The apprentice.
     */
    public boolean hasSpokenToApprentice() {
        return apprentice;
    }

    /**
     * Sets the apprentice.
     *
     * @param apprentice
     *            The apprentice to set.
     */
    public void setApprentice(boolean apprentice) {
        this.apprentice = apprentice;
    }

    /**
     * Gets the strongHoldRewards.
     *
     * @return The strongHoldRewards.
     */
    public boolean[] getStrongHoldRewards() {
        return strongHoldRewards;
    }

    /**
     * Sets the stronghold reward by index.
     *
     * @param index
     *            The index.
     * @param strongholdReward
     *            The stronghold reward status.
     */
    public void setStrongholdReward(int index, boolean strongholdReward) {
        this.strongHoldRewards[index] = strongholdReward;
    }

    /**
     * Gets the strong hold reward value.
     *
     * @param reward
     *            the reward.
     * @return <code>True</code> if so.
     */
    public boolean hasStrongholdReward(int reward) {
        return strongHoldRewards[reward - 1];
    }

    /**
     * Gets the chatPing.
     *
     * @return The chatPing.
     */
    public long getChatPing() {
        return chatPing;
    }

    /**
     * Sets the chatPing.
     *
     * @param chatPing
     *            The chatPing to set.
     */
    public void setChatPing(long chatPing) {
        this.chatPing = chatPing;
    }

    /**
     * Gets the tutorClaim.
     *
     * @return The tutorClaim.
     */
    public long getTutorClaim() {
        return tutorClaim;
    }

    /**
     * Sets the tutorClaim.
     *
     * @param tutorClaim
     *            The tutorClaim to set.
     */
    public void setTutorClaim(long tutorClaim) {
        this.tutorClaim = tutorClaim;
    }

    /**
     * Gets the luthasTask.
     *
     * @return The luthasTask.
     */
    public boolean isLuthasTask() {
        return luthasTask;
    }

    /**
     * Sets the luthasTask.
     *
     * @param luthasTask
     *            The luthasTask to set.
     */
    public void setLuthasTask(boolean luthasTask) {
        this.luthasTask = luthasTask;
    }

    /**
     * Gets the karamjaBananas.
     *
     * @return The karamjaBananas.
     */
    public int getKaramjaBananas() {
        return karamjaBananas;
    }

    /**
     * Sets the karamjaBananas.
     *
     * @param karamjaBananas
     *            The karamjaBananas to set.
     */
    public void setKaramjaBananas(int karamjaBananas) {
        this.karamjaBananas = karamjaBananas;
    }

    /**
     * Gets the silkSteal.
     *
     * @return The silkSteal.
     */
    public long getSilkSteal() {
        return silkSteal;
    }

    /**
     * Sets the silkSteal.
     *
     * @param silkSteal
     *            The silkSteal to set.
     */
    public void setSilkSteal(long silkSteal) {
        this.silkSteal = silkSteal;
    }

    /**
     * Gets the zaffAmount.
     *
     * @return The zaffAmount.
     */
    public int getZaffAmount() {
        return zaffAmount;
    }

    /**
     * Sets the zaffAmount.
     *
     * @param zaffAmount
     *            The zaffAmount to set.
     */
    public void setZaffAmount(int zaffAmount) {
        this.zaffAmount = zaffAmount;
    }

    /**
     * Gets the zaffTime.
     *
     * @return The zaffTime.
     */
    public long getZaffTime() {
        return zaffTime;
    }

    /**
     * Sets the zaffTime.
     *
     * @param zaffTime
     *            The zaffTime to set.
     */
    public void setZaffTime(long zaffTime) {
        this.zaffTime = zaffTime;
    }

    /**
     * Gets the draynorRecording.
     *
     * @return The draynorRecording.
     */
    public boolean isDraynorRecording() {
        return draynorRecording;
    }

    /**
     * Sets the draynorRecording.
     *
     * @param draynorRecording
     *            The draynorRecording to set.
     */
    public void setDraynorRecording(boolean draynorRecording) {
        this.draynorRecording = draynorRecording;
    }

    /**
     * Gets the wydinEmployee.
     *
     * @return The wydinEmployee.
     */
    public boolean isWydinEmployee() {
        return wydinEmployee;
    }

    /**
     * Sets the wydinEmployee.
     *
     * @param wydinEmployee
     *            The wydinEmployee to set.
     */
    public void setWydinEmployee(boolean wydinEmployee) {
        this.wydinEmployee = wydinEmployee;
    }

    /**
     * Gets the frizGlass.
     *
     * @return The frizGlass.
     */
    public boolean isFritzGlass() {
        return fritzGlass;
    }

    /**
     * Sets the frizGlass.
     *
     * @param frizGlass
     *            The frizGlass to set.
     */
    public void setFritzGlass(boolean frizGlass) {
        this.fritzGlass = frizGlass;
    }

    /**
     * Gets the essenceTeleporter.
     *
     * @return The essenceTeleporter.
     */
    public int getEssenceTeleporter() {
        return essenceTeleporter;
    }

    /**
     * Sets the essenceTeleporter.
     *
     * @param essenceTeleporter
     *            The essenceTeleporter to set.
     */
    public void setEssenceTeleporter(int essenceTeleporter) {
        this.essenceTeleporter = essenceTeleporter;
    }

    /**
     * Gets the recoilDamage.
     *
     * @return The recoilDamage.
     */
    public int getRecoilDamage() {
        return recoilDamage;
    }

    /**
     * Sets the recoilDamage.
     *
     * @param recoilDamage
     *            The recoilDamage to set.
     */
    public void setRecoilDamage(int recoilDamage) {
        this.recoilDamage = recoilDamage;
    }

    /**
     * Gets whether or not the player has paid the waterfall fee.
     *
     * @return Whether or not the player has paid the waterfall fee.
     */
    public boolean hasPaidWaterfall() {
        return paidWaterfall;
    }

    /**
     * Sets the waterfall fee paid.
     */
    public void setWaterfallPaid() {
        this.paidWaterfall = true;
    }

    /**
     * Gets whether or not the player has paid the blacksmith fee.
     *
     * @return Whether or not the player has paid the blacksmith fee.
     */
    public boolean hasPaidBlacksmith() {
        return paidBlacksmith;
    }

    /**
     * Sets the blacksmith fee paid.
     */
    public void setBlacksmithPaid() {
        this.paidBlacksmith = true;
    }

    /**
     * Reets the blacksmith fee paid.
     */
    public void resetBlacksmithPaid() {
        this.paidBlacksmith = false;
    }

    /**
     * Gets how long the player has played the server.
     *
     * @return The play time.
     */
    public long getPlayTime() {
        return playTime;
    }

    /**
     * Sets how long the player has played the server.
     *
     * @param playTime
     *            The play time.
     */
    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    /**
     * Gets the time the player logged into the server.
     *
     * @return The start time.
     */
    public long getStartTime() {
        return this.startTime;
    }

    /**
     * Gets the total time played in seconds.
     *
     * @return Total time played in seconds.
     */
    public long getTotalPlayTime() {
        return ((System.currentTimeMillis() - getStartTime()) / 1000L) + getPlayTime();
    }

    /**
     * Checks if the player can interact with other players, drop items, etc.
     *
     * @return <code>True</code> if so.
     */
    public boolean canInteractPlayTime(Player player, String message) {
        if (player.getRights().isAdministrator() || player.specialDetails()) {
            return true;
        }
        if (player.getSavedData().getGlobalData().getTotalPlayTime() < Constants.MINIMUM_PLAY_TIME) {
            player.getActionSender().sendMessage(message);
            return false;
        }
        return true;
    }

    /**
     * Sets the time the player logged into the server.
     */
    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Gets the type of bone the player has in the ectofuntus bone grinder.
     *
     * @return The bone type.
     */
    public int getEctofuntusBoneType() {
        return ectofuntusBoneType;
    }

    /**
     * Sets the type of bone the player has in the ectofuntus bone grinder.
     *
     * @param ectofuntusBoneType
     *            The type of bones.
     */
    public void setEctofuntusBoneType(int ectofuntusBoneType) {
        this.ectofuntusBoneType = ectofuntusBoneType;
    }

    /**
     * Gets whether or not the ectofuntus bone grinder has been ground.
     *
     * @return Whether or not the bones have been ground.
     */
    public boolean isEctofuntusBonesGround() {
        return ectofuntusBonesGround;
    }

    /**
     * Sets whether or not the ectofuntus bone grinder has been ground.
     *
     * @param ectofuntusBonesGround
     *            Whether or not the bones have been ground.
     */
    public void setEctofuntusBonesGround(boolean ectofuntusBonesGround) {
        this.ectofuntusBonesGround = ectofuntusBonesGround;
    }

    /**
     * Gets the time of last successful ::yell
     *
     * @return The time the player last used ::yell
     */
    public long getLastYell() {
        return lastYell;
    }

    public void setLastYell() {
        lastYell = System.currentTimeMillis();
    }

    /**
     * Sets the player's last yell time.
     *
     * @param time
     *            The time the player last used ::Yell
     */
    public void setLastYell(long time) {
        this.lastYell = time;
    }

    /**
     * Gets the player's yell colour.
     *
     * @return The yell colour.
     */
    public String getYellTag() {
        return yellTag;
    }

    /**
     * Sets the player's yell colour.
     *
     * @param yellTag
     *            The yell colour to set.
     */
    public void setYellTag(String yellTag) {
        this.yellTag = yellTag;
    }

    /**
     * Gets the player's visibility level.
     *
     * @return The visibility level.
     */
    public byte getVisibility() {
        return visibility;
    }

    /**
     * Sets the player's visibility level.
     *
     * @param visibility
     *            The visibility level.
     */
    public void setVisibility(byte visibility) {
        this.visibility = visibility;
    }

    /**
     * Gets whether or not this player has reached max combat before.
     *
     * @return Whether or not it was announced.
     */
    public boolean hasAnnouncedCombat() {
        return combatAnnounced;
    }

    /**
     * Sets whether or not this player has reached max combat before.
     *
     * @param combatAnnounced
     *            Whether or not it was announced.
     */
    public void setCombatAnnounced(boolean combatAnnounced) {
        this.combatAnnounced = combatAnnounced;
    }

    /**
     * Gets the last level milestone.
     *
     * @return The last milestone.
     */
    public int getLastMilestone() {
        return lastMilestone;
    }

    /**
     * Sets the last level milestone.
     *
     * @param lastMilestone
     *            The last milestone.
     */
    public void setLastMilestone(int lastMilestone) {
        this.lastMilestone = lastMilestone;
    }

    /**
     * Gets the next level milestone.
     *
     * @return The next milestone.
     */
    public int getNextMilestone() {
        return nextMilestone;
    }

    /**
     * Sets the next level milestone.
     *
     * @param nextMilestone
     *            The next milestone.
     */
    public void setNextMilestone(int nextMilestone) {
        this.nextMilestone = nextMilestone;
    }

    /**
     * Gets the level milestones reached.
     *
     * @return The milestones.
     */
    public boolean[] getMilestones() {
        return milestones;
    }

    /**
     * Gets whether or not the given level milestone was reached.
     *
     * @param index
     *            The index of the milestone (10 = 1, 20 = 2, 30 = 3).
     * @return The milestone.
     */
    public boolean getMilestone(int index) {
        return milestones[index];
    }

    /**
     * Sets a milestone completed by index.
     *
     * @param index
     *            The index of the milestone (10 = 1, 20 = 2, 30 = 3).
     */
    public void setMilestone(int index) {
        this.milestones[index] = true;
    }

    /**
     * Sets a milestone completed by index.
     *
     * @param index
     *            The index of the milestone (10 = 1, 20 = 2, 30 = 3).
     */
    public void setMilestone(int index, boolean milestone) {
        this.milestones[index] = milestone;
    }

    /**
     * Gets whether or not the given experience milestone was reached.
     *
     * @param skillId
     *            The id of the skill.
     * @return The milestone.
     */
    public boolean getExperienceMilestone(int skillId) {
        return experienceMilestones[skillId];
    }

    /**
     * Gets the experience milestones.
     *
     * @return The milestones.
     */
    public boolean[] getExperienceMilestones() {
        return experienceMilestones;
    }

    /**
     * Sets a milestone completed by index.
     *
     * @param skillId
     *            The id of the skill.
     */
    public void setExperienceMilestone(int skillId) {
        this.experienceMilestones[skillId] = true;
    }

    /**
     * Sets a milestone completed by index.
     *
     * @param skillId
     *            The id of the skill.
     */
    public void setExperienceMilestone(int skillId, boolean experienceMilestone) {
        this.experienceMilestones[skillId] = experienceMilestone;
    }

    /**
     * The small pouch container.
     */
    public Container getSmallPouch() {
        return smallPouch;
    }

    public void setSmallPouch(Container smallPouch) {
        this.smallPouch = smallPouch;
    }

    /**
     * The medium pouch container.
     */
    public Container getMediumPouch() {
        return mediumPouch;
    }

    public void setMediumPouch(Container mediumPouch) {
        this.mediumPouch = mediumPouch;
    }

    /**
     * The large pouch container.
     */
    public Container getLargePouch() {
        return largePouch;
    }

    public void setLargePouch(Container largePouch) {
        this.largePouch = largePouch;
    }

    /**
     * The giant pouch container.
     */
    public Container getGiantPouch() {
        return giantPouch;
    }

    public void setGiantPouch(Container giantPouch) {
        this.giantPouch = giantPouch;
    }

    /**
     * Gets the voting points.
     *
     * @return The voting points.
     */
    public int getVotingPoints() {
        return votingPoints;
    }

    /**
     * Sets the voting points.
     *
     * @param votingPoints
     *            The voting points to set.
     */
    public void setVotingPoints(int votingPoints) {
        this.votingPoints = votingPoints;
    }

    /**
     * Increases the voting points.
     *
     * @param votingPoints
     *            The voting points to increase by.
     */
    public void increaseVotingPoints(int votingPoints) {
        this.votingPoints += votingPoints;
    }

    /**
     * Decreases the voting points.
     *
     * @param votingPoints
     *            The voting points to decrease by.
     */
    public int decreaseVotingPoints(int votingPoints) {
        this.votingPoints -= votingPoints;
        return this.votingPoints;
    }

    /**
     * Gets the experience modifier time in minutes.
     *
     * @return The experience modifier time.
     */
    public int getExperienceModifierTime() {
        return experienceModifierTime;
    }

    /**
     * Gets the experience modifiers time in minutes.
     *
     * @return The experience modifiers time.
     */
    public int[] getExperienceModifiersTime() {
        return experienceModifiersTime;
    }

    public boolean hasAnyExperienceModifier(){
        return IntStream.of(experienceModifiersTime).anyMatch(timeLeft -> timeLeft > 0);
    }
    public boolean hasAnyExperienceModifier(int... skillIDs){
        for(int skillID : skillIDs)
            if(experienceModifiersTime[skillID] > 0)
                return true;
        return false;
    }
    public boolean hasAllExperienceModifier(int... skillIDs){
        for(int skillID : skillIDs)
            if(experienceModifiersTime[skillID] < 1)
                return false;

        return true;
    }
    /**
     * Sets the experience modifier time in minutes.
     *
     * @param experienceModifierTime
     *            The experience modifier time.
     */
    public void setExperienceModifierTime(int experienceModifierTime) {
        this.experienceModifierTime = experienceModifierTime;
    }
    public void setExperienceModifiersTime(int[] experienceModifiersTime) {
        this.experienceModifiersTime = experienceModifiersTime;
    }
    /**
     * Decreases the experience modifier by 1.
     */
    public void decreasesExperienceModifierTime() {
        this.experienceModifierTime -= 1;
    }

    /**
     * Gets the experience modifier.
     *
     * @return The experience modifier.
     */
    public double getExperienceModifier() {
        return experienceModifier;
    }

    /**
     * Gets the experience modifiers.
     *
     * @return The experience modifier.
     */
    public double[] getExperienceModifiers() {
        return experienceModifiers;
    }

    /**
     * Sets the experience modifier.
     *
     * @param experienceModifier
     *            The experience modifier.
     */
    public void setExperienceModifier(double experienceModifier) {
        this.experienceModifier = experienceModifier;
    }
    public void setExperienceModifiers(double[] experienceModifiers) {
        this.experienceModifiers = experienceModifiers;
    }

    /**
     * Gets the
     * {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}
     * the player is using.
     *
     * @return The
     *         {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
     */
    public StarterPackage getStarterPackage() {
        return starterPackage;
    }

    /**
     * Sets the
     * {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}
     * the player is using.
     *
     * @param starterPackage
     *            The
     *            {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
     */
    public void setStarterPackage(StarterPackage starterPackage) {
        this.starterPackage = starterPackage;
    }

    /**
     * Gets the
     * {@link org.gielinor.game.node.entity.player.info.IronmanMode}
     * the player is using (if any).
     *
     * @return The
     * {@link org.gielinor.game.node.entity.player.info.IronmanMode}.
     */
    public IronmanMode getIronmanMode() {
        if (starterPackage != null) {
            return IronmanMode.forStarterId(starterPackage.getId());
        }
        return IronmanMode.NONE;
    }

    /**
     * Gets the last clan chat the player was in.
     *
     * @return The clan chat. TODO 317 use pidn
     */
    public String getLastClanChat() {
        return lastClanChat;
    }

    /**
     * Sets the last clan chat the player was in.
     *
     * @return The clan chat.
     */
    public void setLastClanChat(String lastClanChat) {
        this.lastClanChat = lastClanChat;
    }

    /**
     * The player's Gielinor points.
     */
    public long getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(long loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public long increaseLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints += loyaltyPoints;
        return this.loyaltyPoints;
    }

    public long decreaseLoyaltyPoints(int loyaltyPoints) {
        if (this.loyaltyPoints == 0) {
            return 0;
        }
        this.loyaltyPoints -= loyaltyPoints;
        return this.loyaltyPoints;
    }

    /**
     * The player's xp drops.
     */
    public long getXpDrops() {
        return xpDrops;
    }

    public void setXpDrops(long xpDrops) {
        this.xpDrops = xpDrops;
    }

    public void increaseXpDrops(long xpDrops) {
        this.xpDrops += xpDrops;
    }

    /**
     * The last Duel Arena rules used.
     */
    public List<DuelRule> getLastDuelRules() {
        return lastDuelRules;
    }

    public void setLastDuelRules(List<DuelRule> lastDuelRules) {
        this.lastDuelRules = lastDuelRules;
    }

    /**
     * The saved preset Duel Arena rules.
     */
    public List<DuelRule> getPresetDuelRules() {
        return presetDuelRules;
    }

    public void setPresetDuelRules(List<DuelRule> presetDuelRules) {
        this.presetDuelRules = presetDuelRules;
    }

    /**
     * Gets the player's player kills.
     *
     * @return
     * 		The kills.
     */
    public int getPlayerKills() {
        return playerKills;
    }

    /**
     * Sets the player's player kills.
     *
     * @param playerKills
     * 		The kills.
     */
    public void setPlayerKills(int playerKills) {
        this.playerKills = playerKills;
    }

    /**
     * Increases the {@link Player}'s player kills.
     *
     * @return
     * 		The kills after being incremented.
     */
    public void increasePlayerKills() {
        playerKills++;
    }

    /**
     * Gets the player's player deaths.
     *
     * @return The deaths.
     */
    public int getPlayerDeaths() {
        return playerDeaths;
    }

    /**
     * Sets the player's player deaths.
     *
     * @param playerDeaths
     *            The deaths.
     */
    public void setPlayerDeaths(int playerDeaths) {
        this.playerDeaths = playerDeaths;
    }

    /**
     * Increases the player's player deaths.
     *
     * @return The deaths.
     */
    public void increasePlayerDeaths() {
        playerDeaths++;
    }

    /**
     * Gets the player's prayer book.
     *
     * @return The prayer book.
     */
    public int getPrayerBook() {
        if (prayerBook != 21356 && prayerBook != 5608) {
            return 5608;
        }
        return prayerBook;
    }

    /**
     * Sets the player's prayer book.
     *
     * @param prayerBook
     *            The prayer book.
     */
    public void setPrayerBook(int prayerBook) {
        if (prayerBook != 21356 && prayerBook != 5608) {
            prayerBook = 5608;
        }
        this.prayerBook = prayerBook;
    }

    /**
     * Whether or not the player has beta status.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasBetaStatus() {
        return betaStatus;
    }

    /**
     * Sets whether or not the player has beta status.
     *
     * @param betaStatus
     *            If the player has beta status.
     */
    public void setBetaStatus(boolean betaStatus) {
        this.betaStatus = betaStatus;
    }

    /**
     * Whether or not the player has unlocked the Easter event
     * {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle}.
     *
     * @return <code>True</code> if so.
     */
    public boolean unlockedEasterTitle() {
        return easterTitle;
    }

    /**
     * Sets whether or not the player has unlocked the Easter event
     * {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle}.
     *
     * @param easterTitle
     *            If the player has unlocked the Easter event
     *            {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle}.
     */
    public void setEasterTitle(boolean easterTitle) {
        this.easterTitle = easterTitle;
    }

    /**
     * Gets the stage of the Easter event the player is on.
     *
     * @return The stage.
     */
    public int getEasterEventStage() {
        return easterEventStage;
    }

    /**
     * Sets the stage of the Easter event the player is on.
     *
     * @param easterEventStage
     *            The stage.
     */
    public void setEasterEventStage(int easterEventStage) {
        this.easterEventStage = easterEventStage;
    }

    /**
     * Gets the list of eggs delivered.
     */
    public List<Integer> getEggsDelivered() {
        return eggsDelivered;
    }

    /**
     * Sets if the player has given the Easter bunny dyes.
     */
    public void setGivenDyes() {
        this.easterDyesGiven = true;
    }

    /**
     * Gets if the player has given the Easter bunny dyes.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasGivenDyes() {
        return easterDyesGiven;
    }

    /**
     *
     */
    public boolean hasWalkedToDorgeshKaan() {
        return walkedToDorgeshKaan;
    }

    /**
     *
     */
    public void setWalkedToDorgeshKaan(boolean walkedToDorgeshKaan) {
        this.walkedToDorgeshKaan = walkedToDorgeshKaan;
    }

    /**
     *
     */
    public int getRoaldPoints() {
        return roaldPoints;
    }

    public void increaseRoaldPoints() {
        roaldPoints += 1;
    }

    public int setRoaldPoints(int amount) {
        roaldPoints += amount;
        return roaldPoints;
    }

    public int decreaseRoaldPoints(int amount) {
        roaldPoints -= amount;
        return roaldPoints;
    }

    public List<Integer> getUnlockedRoaldsItems() {
        return unlockedRoaldsItems;
    }

    /**
     * Gets the rune pouch container.
     *
     * @return The container.
     */
    public Container getRunePouch() {
        return runePouch;
    }

    /**
     * Gets the player's left-click option for Summoning.
     *
     * @return The left-click option.
     */
    public String getLeftClickOption() {
        return leftClickOption;
    }

    /**
     * Sets the player's left-click option for Summoning.
     *
     * @param leftClickOption
     *            The left-click option.
     */
    public void setLeftClickOption(String leftClickOption) {
        this.leftClickOption = leftClickOption;
    }

    /**
     * The player's toxic blowpipe dart id.
     */
    public int getBlowpipeDartId() {
        return blowpipeDartId;
    }

    public void setBlowpipeDartId(int blowpipeDartId) {
        this.blowpipeDartId = blowpipeDartId;
    }

    /**
     * The player's toxic blowpipe scales amount.
     */
    public int getBlowpipeDartScales() {
        return blowpipeDartScales;
    }

    public void setBlowpipeDartScales(int blowpipeDartScales) {
        this.blowpipeDartScales = blowpipeDartScales;
    }

    public void decreaseBlowpipeDartScales(int blowpipeDartScales) {
        this.blowpipeDartScales -= blowpipeDartScales;
    }

    /**
     * The player's toxic blowpipe dart amount.
     */
    public int getBlowpipeDartAmount() {
        return blowpipeDartAmount;
    }

    public void setBlowpipeDartAmount(int blowpipeDartAmount) {
        this.blowpipeDartAmount = blowpipeDartAmount;
    }

    public void decreaseBlowpipeDartAmount(int blowpipeDartAmount) {
        this.blowpipeDartAmount -= blowpipeDartAmount;
    }

    public int getPKPoints() {
        return pkPoints;
    }

    public void setPKPoints(int pkPoints) {
        this.pkPoints = pkPoints;
    }

    public void incrementPKPoints() {
        pkPoints++;
    }

    public void incrementNpcKillCount() {
        npcKillCount++;
    }

    public void increaseDuelDeaths() {
        duelDeaths++;
    }

    public int getLoginStreak() {
        return loginStreak;
    }

    public void incrementStreak() {
        loginStreak++;
    }

    public void resetStreak() {
        loginStreak = 0;
    }

    public void setLoginStreak(int streak) {
        this.loginStreak = streak;
    }

    public int getKillstreak() {
        return killstreak;
    }

    public void setKillstreak(int killstreak) {
        this.killstreak = killstreak;
    }

    public void resetKillstreak() {
        killstreak = 0;
    }

    public List<Integer> getLostPets() {
        return lostPets;
    }

    public int getPetsInsured() {
        return petsInsured;
    }

    public void setPetsInsured(int petsInsured) {
        this.petsInsured = petsInsured;
    }

    public void incrementPetsInsured() {
        petsInsured++;
    }

    public int getDuelDeaths() {
        return duelDeaths;
    }

    public void setDuelDeaths(int duelDeaths) {
        this.duelDeaths = duelDeaths;
    }

    public int getNpcKillCount() {
        return npcKillCount;
    }

    public void setNpcKillCount(int npcKillCount) {
        this.npcKillCount = npcKillCount;
    }

    public int getAccuracyModifierTime() {
        return accuracyModifierTime;
    }

    public int getStrengthModifierTime() {
        return strengthModifierTime;
    }

    public int getDefenceModifierTime() {
        return defenceModifierTime;
    }

    public int getDropModifierTime() {
        return dropModifierTime;
    }

    public double getAccuracyModifier() {
        return accuracyModifier;
    }

    public double getStrengthModifier() {
        return strengthModifier;
    }

    public double getDefenceModifier() {
        return defenceModifier;
    }

    public double getDropModifier() {
        return dropModifier;
    }

    public void setAccuracyModifierTime(int accuracyModifierTime) {
        this.accuracyModifierTime = accuracyModifierTime;
    }

    public void setStrengthModifierTime(int strengthModifierTime) {
        this.strengthModifierTime = strengthModifierTime;
    }

    public void setDefenceModifierTime(int defenceModifierTime) {
        this.defenceModifierTime = defenceModifierTime;
    }

    public void setDropModifierTime(int dropModifierTime) {
        this.dropModifierTime = dropModifierTime;
    }

    public void setAccuracyModifier(double accuracyModifier) {
        this.accuracyModifier = accuracyModifier;
    }

    public void setStrengthModifier(double strengthModifier) {
        this.strengthModifier = strengthModifier;
    }

    public void setDefenceModifier(double defenceModifier) {
        this.defenceModifier = defenceModifier;
    }

    public void setDropModifier(double dropModifier) {
        this.dropModifier = dropModifier;
    }
}
