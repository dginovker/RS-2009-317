package org.gielinor.game.node.entity.player.info;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.ConfigurationManager.Configuration;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.SpecialRegenerationPulse;
import plugin.activity.duelarena.DuelRule;

import java.nio.ByteBuffer;

/**
 * Holds a player's settings.
 *
 * @author Emperor
 */
public final class Settings {

    /**
     * Represents fields to be inserted into the database.
     */
    public static final String[] FIELDS = new String[]{
        "pidn", "brightness", "music_volume", "sound_effect_volume",
        "area_sound_volume", "single_mouse_button", "mouse_movement", "disable_chat_effects",
        "split_private_chat", "accept_aid", "run_toggled",
        "public_chat_setting", "private_chat_setting", "clan_chat_setting",
        "trade_setting", "assist_setting", "run_energy",
        "side_panel_transparent", "remaining_xp", "roof_removal",
        "data_orbs", "chatbox_transparent", "click_through_chatbox",
        "side_panels_bottom", "side_panel_hotkeys", "auto_retaliating",
        "special_energy", "attack_style_index"
    };
    /**
     * The player.
     */
    private final Player player;

    /**
     * The run energy.
     */
    private double runEnergy = 100.0;

    /**
     * The player's weight.
     */
    private double weight;

    /**
     * The brightness setting.
     */
    private int brightness = 2;

    /**
     * The music volume.
     */
    private int musicVolume;

    /**
     * The sound effects volume.
     */
    private int soundEffectVolume;

    /**
     * The area sounds volume.
     */
    private int areaSoundVolume;

    /**
     * If the player has the single mouse button setting enabled.
     */
    private boolean singleMouseButton;

    /**
     * If the player has the mouse movement setting enabled.
     */
    private boolean mouseMovement;

    /**
     * If the chat effects should be disabled.
     */
    private boolean disableChatEffects;

    /**
     * If the private chat should be split from public chat.
     */
    private boolean splitPrivateChat;

    /**
     * If the player has the accept aid setting enabled.
     */
    private boolean acceptAid;

    /**
     * If the player's run button is toggled.
     */
    private boolean runToggled;

    /**
     * The public chat setting.
     */
    private int publicChatSetting = 0;

    /**
     * The private chat setting.
     */
    private int privateChatSetting = 0;

    /**
     * The clan chat setting.
     */
    private int clanChatSetting = 0;

    /**
     * The trade setting.
     */
    private int tradeSetting = 0;

    /**
     * The assist setting.
     */
    private int assistSetting = 0;

    /**
     * If the special attack is toggled.
     */
    private boolean specialToggled;
    /**
     * The current special energy the player has left.
     */
    private int specialEnergy = 100;
    /**
     * The current attack style index.
     */
    private int attackStyleIndex = 0;
    /**
     * If the player's side-panel is opaque or transparent.
     */
    private boolean sidePanelTransparent;
    /**
     * If the remaining xp tooltips are on.
     */
    private boolean remainingXP;
    /**
     * If roofs are removed.
     */
    private boolean roofRemoval;
    /**
     * If data orbs are shown.
     */
    private boolean dataOrbs;
    /**
     * If the chatbox is transparent.
     */
    private boolean chatboxTransparent;
    /**
     * If the chatbox can be clicked through.
     */
    private boolean clickThroughChatbox;
    /**
     * If the side-panels are bottom-line.
     */
    private boolean sidePanelsBottom;
    /**
     * If the side-panels can be closed with their hotkey.
     */
    private boolean sidePanelHotkeys;
    private boolean hitIcons;
    private boolean healthBar;
    private boolean tweening;
    private int gameframe;
    private boolean hitmarks;
    private boolean cursors;
    private boolean hdTextures;
    private boolean fog;
    private boolean contextMenu;
    /**
     *
     */
    public static final int[] SPECIAL_ATTACK_TEXT = new int[]{
        24732, 24735, 24738, 24741, 24744, 24747, 24750,
        24753, 24756, 24759, 24762, 24765, 24768, 24771,
        24774, 24777, 24780, 24783, 24786, 24789, 24792
    };

    /**
     * Constructs a new {@code Settings} {@code Object}.
     *
     * @param player The player.
     */
    public Settings(Player player) {
        this.player = player;
    }

    /**
     * Updates the settings.
     */
    public void update() {
        player.getConfigManager().set(Configuration.BRIGHTNESS, brightness + 1);
        player.getConfigManager().set(Configuration.MUSIC_VOLUME, musicVolume);
        player.getConfigManager().set(Configuration.EFFECT_VOLUME, soundEffectVolume);
        player.getConfigManager().set(Configuration.MOUSE_BUTTON, singleMouseButton);
        player.getConfigManager().set(Configuration.MOUSE_MOVEMENT, mouseMovement);
        player.getConfigManager().set(Configuration.CHAT_EFFECT, disableChatEffects);
        player.getConfigManager().set(Configuration.SPLIT_PRIVATE, splitPrivateChat);
        player.getConfigManager().set(Configuration.ACCEPT_AID, acceptAid);
        player.getConfigManager().set(Configuration.RETALIATE, player.getProperties().isRetaliating());
        player.getConfigManager().set(Configuration.RUNNING, runToggled);
        player.getConfigManager().set(1054, clanChatSetting);
        player.getConfigManager().set(1055, assistSetting);
        player.getConfigManager().set(300, specialEnergy * 10);
        player.getConfigManager().set(43, attackStyleIndex);
        player.getActionSender().sendRunEnergy();
        updateChatSettings();
        Pulse pulse = player.getAttribute("energy-restore", null);
        if (pulse == null || !pulse.isRunning()) {
            int specialRegenerationPulseTicks = (int) (50 * player.getDonorManager().getDonorStatus().getSpecialRegenerationModifier());
            pulse = new SpecialRegenerationPulse(specialRegenerationPulseTicks, player);
            pulse.setTicksPassed(1);
            World.submit(pulse);
            player.setAttribute("energy-restore", pulse);
        }
    }

    /**
     * Toggles the attack style index.
     *
     * @param index The index.
     */
    public void toggleAttackStyleIndex(int index) {
        this.attackStyleIndex = index;
        player.getConfigManager().set(43, attackStyleIndex);
    }

    /**
     * Updates the chat settings.
     */
    public void updateChatSettings() {
        player.getSession().write(new PacketBuilder(206).put(publicChatSetting).put(privateChatSetting).put(tradeSetting));
    }

    /**
     * Sets the chat settings.
     *
     * @param pub   The public chat setting.
     * @param priv  The private chat setting.
     * @param trade The trade setting.
     */
    public void setChatSettings(int pub, int priv, int trade) {
        boolean update = false;
        if (publicChatSetting != pub) {
            publicChatSetting = pub;
            update = true;
        }
        if (privateChatSetting != priv) {
            privateChatSetting = priv;
            update = true;
            player.getCommunication().notifyPlayers(privateChatSetting != 2, true);
        }
        if (tradeSetting != trade) {
            tradeSetting = trade;
            update = true;
        }
        if (update) {
            updateChatSettings();
        }
    }

    /**
     * Sets the chat settings.
     *
     * @param pub   The public chat setting.
     * @param priv  The private chat setting.
     * @param trade The trade setting.
     */
    public void setChatSettingsSQL(int pub, int priv, int trade) {
        publicChatSetting = pub;
        privateChatSetting = priv;
        player.getCommunication().notifyPlayers(privateChatSetting != 2, true);
        tradeSetting = trade;
    }

    /**
     * Writes the settings on the byte buffer.
     *
     * @param byteBuffer The byte buffer.
     */
    public void save(ByteBuffer byteBuffer) {
        // Basic settings
        int[] toSave = new int[]{
            brightness, musicVolume, soundEffectVolume, areaSoundVolume,
            singleMouseButton ? 1 : 0, disableChatEffects ? 1 : 0,
            splitPrivateChat ? 1 : 0, acceptAid ? 1 : 0,
            runToggled ? 1 : 0, publicChatSetting, privateChatSetting, clanChatSetting,
            tradeSetting, assistSetting, (int) runEnergy, isSidePanelTransparent() ? 1 : 0,
            isRemainingXP() ? 1 : 0, isRoofRemoval() ? 1 : 0, isDataOrbs() ? 1 : 0, isChatboxTransparent() ? 1 : 0,
            isClickThroughChatbox() ? 1 : 0, isSidePanelsBottom() ? 1 : 0, isSidePanelHotkeys() ? 1 : 0 };
        byteBuffer.put((byte) 1);
        for (int save : toSave) {
            byteBuffer.put((byte) save);
        }
        if (!player.getProperties().isRetaliating()) {
            byteBuffer.put((byte) 2);
        }
        if (specialEnergy != 100) {
            byteBuffer.put((byte) 3).put((byte) specialEnergy);
        }
        if (attackStyleIndex != 0) {
            byteBuffer.put((byte) 4).put((byte) attackStyleIndex);
        }
        byteBuffer.put((byte) 0);
    }

    /**
     * Parses the settings from the byte buffer.
     *
     * @param buffer The byte buffer.
     */
    public void parse(ByteBuffer buffer) {
        int opcode;
        while ((opcode = buffer.get() & 0xFF) != 0) {
            switch (opcode) {
                case 1:
                    brightness = buffer.get();
                    musicVolume = buffer.get();
                    soundEffectVolume = buffer.get();
                    areaSoundVolume = buffer.get();
                    singleMouseButton = buffer.get() == 1;
                    disableChatEffects = buffer.get() == 1;
                    splitPrivateChat = buffer.get() == 1;
                    acceptAid = buffer.get() == 1;
                    runToggled = buffer.get() == 1;
                    publicChatSetting = buffer.get();
                    privateChatSetting = buffer.get();
                    clanChatSetting = buffer.get();
                    tradeSetting = buffer.get();
                    assistSetting = buffer.get();
                    runEnergy = buffer.get();
                    sidePanelTransparent = buffer.get() == 1;
                    remainingXP = buffer.get() == 1;
                    roofRemoval = buffer.get() == 1;
                    dataOrbs = buffer.get() == 1;
                    chatboxTransparent = buffer.get() == 1;
                    clickThroughChatbox = buffer.get() == 1;
                    sidePanelsBottom = buffer.get() == 1;
                    sidePanelHotkeys = buffer.get() == 1;
                    break;
                case 2:
                    player.getProperties().setRetaliating(false);
                    break;
                case 3:
                    specialEnergy = buffer.get() & 0xFF;
                    break;
                case 4:
                    attackStyleIndex = buffer.get();
                    break;
            }
        }
    }

    /**
     * Toggles the special attack bar.
     */
    public void toggleSpecialBar() {
        setSpecialToggled(!specialToggled);
    }

    /**
     * Toggles the special attack bar.
     *
     * @param enable If the special attack should be enabled.
     */
    public void setSpecialToggled(boolean enable) {
        if (DuelRule.NO_SPECIAL.enforce(player, true)) {
            return;
        }
        specialToggled = enable;
        player.getConfigManager().set(301, specialToggled ? 1 : 0);
    }

    /**
     * Toggles the special attack bar.
     *
     * @param enable If the special attack should be enabled.
     */
    public void setSpecialToggledSQL(boolean enable) {
        specialToggled = !specialToggled;
    }

    /**
     * Checks if the special attack bar is toggled.
     *
     * @return <code>True</code> if so.
     */
    public boolean isSpecialToggled() {
        return specialToggled;
    }

    /**
     * Drains an amount of special attack energy.
     *
     * @param amount The amount to drain.
     * @return <code>True</code> if succesful, <code>False</code> if the special attack energy amount hasn't changed after calling this method.
     */
    public boolean drainSpecial(int amount) {
        if (!specialToggled) {
            return false;
        }
        if (player.getAttribute("INF_SPEC") != null) {
            return true;
        }
        setSpecialToggled(false);
        if (amount > specialEnergy) {
            player.getActionSender().sendMessage("You do not have enough special attack energy left.");
            return false;
        }
        setSpecialEnergy(specialEnergy - amount);
        return true;
    }

    /**
     * Sets the special energy amount.
     *
     * @param value The amount to set.
     */
    public void setSpecialEnergy(int value) {
        if (player.getAttribute("INF_SPEC") != null) {
            value = 100;
        }
        specialEnergy = value;
        player.getConfigManager().set(300, specialEnergy * 10);
    }

    /**
     * Sets the special energy amount.
     *
     * @param value The amount to set.
     */
    public void setSpecialEnergySQL(int value) {
        if (player.getAttribute("INF_SPEC") != null) {
            value = 100;
        }
        specialEnergy = value;
    }

    /**
     * Gets the amount of special energy left.
     *
     * @return The amount of energy.
     */
    public int getSpecialEnergy() {
        return specialEnergy;
    }

    /**
     * Toggles the retaliating button.
     */
    public void toggleRetaliating() {
        player.getProperties().setRetaliating(!player.getProperties().isRetaliating());
        player.getConfigManager().set(172, player.getProperties().isRetaliating() ? 1 : 0, true);
    }

    /**
     * Toggles the singleMouseButton.
     */
    public void toggleMouseButton() {
        // TODO
        singleMouseButton = !singleMouseButton;
        player.getConfigManager().set(Configuration.MOUSE_BUTTON, singleMouseButton ? 1 : 0, true);
    }

    /**
     * Toggles the mouse movement.
     */
    public void toggleMouseMovement() {
        mouseMovement = !mouseMovement;
        player.getConfigManager().set(Configuration.MOUSE_MOVEMENT, mouseMovement ? 1 : 0, true);
    }

    /**
     * Toggles the disableChatEffects.
     */
    public void toggleChatEffects() {
        disableChatEffects = !disableChatEffects;
        player.getConfigManager().set(Configuration.CHAT_EFFECT, disableChatEffects ? 1 : 0, true);
    }

    /**
     * Toggles the splitPrivateChat.
     */
    public void toggleSplitPrivateChat() {
        splitPrivateChat = !splitPrivateChat;
        player.getConfigManager().set(Configuration.SPLIT_PRIVATE, splitPrivateChat ? 1 : 0, true);
    }

    /**
     * Toggles the acceptAid.
     */
    public void toggleAcceptAid() {
        acceptAid = !acceptAid;
        if (player.isIronman()) {
            player.getActionSender().sendMessage(Ironman.ACCEPT_AID);
            acceptAid = false;
        }
        player.getConfigManager().set(Configuration.ACCEPT_AID, acceptAid ? 1 : 0, true);
    }

    /**
     * Toggles the run button.
     */
    public void toggleRun() {
        setRunToggled(!runToggled);
    }

    /**
     * Toggles the run button.
     *
     * @param enabled If the run button should be enabled.
     */
    public void setRunToggled(boolean enabled) {
        runToggled = enabled;
        player.getConfigManager().set(173, runToggled ? 1 : 0, true);
    }

    /**
     * Toggles the run button.
     *
     * @param enabled If the run button should be enabled.
     */
    public void setRunToggledSQL(boolean enabled) {
        runToggled = enabled;
    }

    /**
     * Decreases the run energy with the given amount (drain parameter).
     * <br>To increase, use a negative drain value.
     *
     * @param drain The drain amount.
     */
    public void updateRunEnergy(double drain) {
        if (player.getAttribute("INFINITE_RUN") != null) {
            runEnergy = 100;
            player.getActionSender().sendRunEnergy();
            return;
        }
        runEnergy -= drain;
        if (runEnergy < 0) {
            runEnergy = 0.0;
        } else if (runEnergy > 100) {
            runEnergy = 100.0;
        }
        player.getActionSender().sendRunEnergy();
    }

    /**
     * Updates the weight.
     */
    public void updateWeight() {
        weight = 0.0;
        for (int slot = 0; slot < 28; slot++) {
            Item item = player.getInventory().get(slot);
            if (item == null) {
                continue;
            }
            if (item.getDefinition().getWeightId() > 0 && !item.getDefinition().isEquipment()) {
                item = new Item(item.getDefinition().getWeightId(), item.getCount());
            }
            double value = item.getDefinition().getConfiguration(ItemConfiguration.WEIGHT, 0.0);
            weight += value;
        }
        for (int slot = 0; slot < 11; slot++) {
            Item item = player.getEquipment().get(slot);
            if (item == null) {
                continue;
            }
            if (item.getDefinition().getWeightId() > 0 && item.getDefinition().isEquipment()) {
                item = new Item(item.getDefinition().getWeightId(), item.getCount());
            }
            weight += item.getDefinition().getConfiguration(ItemConfiguration.WEIGHT, 0.0);
        }
    }

    /**
     * Gets the weight.
     *
     * @return The weight.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gets the brightness.
     *
     * @return The brightness.
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * Sets the brightness.
     *
     * @param brightness The brightness to set.
     */
    public void setBrightness(int brightness) {
        this.brightness = brightness;
        player.getConfigManager().set(Configuration.BRIGHTNESS, brightness + 1);
    }

    /**
     * Sets the brightness.
     *
     * @param brightness The brightness to set.
     */
    public void setBrightnessSQL(int brightness) {
        this.brightness = brightness;
    }

    /**
     * Gets the musicVolume.
     *
     * @return The musicVolume.
     */
    public int getMusicVolume() {
        return musicVolume;
    }

    /**
     * Sets the musicVolume.
     *
     * @param musicVolume The musicVolume to set.
     */
    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
        player.getConfigManager().force(168, musicVolume, true);
    }

    /**
     * Sets the musicVolume.
     *
     * @param musicVolume The musicVolume to set.
     */
    public void setMusicVolumeSQL(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    /**
     * Gets the soundEffectVolume.
     *
     * @return The soundEffectVolume.
     */
    public int getSoundEffectVolume() {
        return soundEffectVolume;
    }

    /**
     * Sets the soundEffectVolume.
     *
     * @param soundEffectVolume The soundEffectVolume to set.
     */
    public void setSoundEffectVolume(int soundEffectVolume) {
        this.soundEffectVolume = soundEffectVolume;
        player.getConfigManager().force(169, soundEffectVolume, true);
    }

    /**
     * Sets the soundEffectVolume.
     *
     * @param soundEffectVolume The soundEffectVolume to set.
     */
    public void setSoundEffectVolumeSQL(int soundEffectVolume) {
        this.soundEffectVolume = soundEffectVolume;
    }

    /**
     * Gets the areaSoundVolume.
     *
     * @return The areaSoundVolume.
     */
    public int getAreaSoundVolume() {
        return areaSoundVolume;
    }

    /**
     * Sets the areaSoundVolume.
     *
     * @param areaSoundVolume The areaSoundVolume to set.
     */
    public void setAreaSoundVolume(int areaSoundVolume) {
        this.areaSoundVolume = areaSoundVolume;
        // player.getConfigManager().force(872, areaSoundVolume, true);
    }

    /**
     * Sets the areaSoundVolume.
     *
     * @param areaSoundVolume The areaSoundVolume to set.
     */
    public void setAreaSoundVolumeSQL(int areaSoundVolume) {
        this.areaSoundVolume = areaSoundVolume;
    }

    /**
     * Gets the singleMouseButton.
     *
     * @return The singleMouseButton.
     */
    public boolean isSingleMouseButton() {
        return singleMouseButton;
    }

    /**
     * Gets whether or not mouse movement is enabled.
     *
     * @return <code>True</code> if so.
     */
    public boolean isMouseMovement() {
        return mouseMovement;
    }

    /**
     * Sets the single mouse button.
     *
     * @param singleMouseButton The single mouse button setting.
     */
    public void setSingleMouseButtonSQL(boolean singleMouseButton) {
        this.singleMouseButton = singleMouseButton;
    }

    /**
     * Sets the mouse movement.
     *
     * @param mouseMovement The mouse movement setting.
     */
    public void setMouseMovement(boolean mouseMovement) {
        this.mouseMovement = mouseMovement;
    }

    /**
     * Sets the mouse movement.
     *
     * @param mouseMovement The mouse movement setting.
     */
    public void setMouseMovementSQL(boolean mouseMovement) {
        this.mouseMovement = mouseMovement;
        player.getConfigManager().set(Configuration.MOUSE_MOVEMENT, mouseMovement ? 1 : 0, true);
    }

    /**
     * Gets the disableChatEffects.
     *
     * @return The disableChatEffects.
     */
    public boolean isDisableChatEffects() {
        return disableChatEffects;
    }

    /**
     * Toggles the chat effects on or off.
     *
     * @param disableChatEffects The chat effects to set.
     */
    public void setChatEffectsSQL(boolean disableChatEffects) {
        this.disableChatEffects = disableChatEffects;
    }

    /**
     * Gets the splitPrivateChat.
     *
     * @return The splitPrivateChat.
     */
    public boolean isSplitPrivateChat() {
        return splitPrivateChat;
    }

    /**
     * Toggles the split private chat on or off.
     *
     * @param splitPrivateChat The split private chat setting.
     */
    public void setSplitPrivateChat(boolean splitPrivateChat) {
        this.splitPrivateChat = splitPrivateChat;
        player.getConfigManager().set(287, splitPrivateChat ? 1 : 0);
    }

    /**
     * Toggles the split private chat on or off.
     *
     * @param splitPrivateChat The split private chat setting.
     */
    public void setSplitPrivateChatSQL(boolean splitPrivateChat) {
        this.splitPrivateChat = splitPrivateChat;
    }

    /**
     * Gets the acceptAid.
     *
     * @return The acceptAid.
     */
    public boolean isAcceptAid() {
        return acceptAid;
    }

    /**
     * Toggles the accept aid on or off.
     *
     * @param acceptAid Whether or not accept aid is on or off.
     */
    public void setAcceptAidSQL(boolean acceptAid) {
        this.acceptAid = acceptAid;
    }

    /**
     * Gets the runToggled.
     *
     * @return The runToggled.
     */
    public boolean isRunToggled() {
        return runToggled;
    }

    /**
     * Gets the publicChatSetting.
     *
     * @return The publicChatSetting.
     */
    public int getPublicChatSetting() {
        return publicChatSetting;
    }

    /**
     * Sets the publicChatSetting.
     *
     * @param publicChatSetting The publicChatSetting to set.
     */
    public void setPublicChatSetting(int publicChatSetting) {
        this.publicChatSetting = publicChatSetting;
        updateChatSettings();
    }

    /**
     * Gets the privateChatSetting.
     *
     * @return The privateChatSetting.
     */
    public int getPrivateChatSetting() {
        return privateChatSetting;
    }

    /**
     * Sets the privateChatSetting.
     *
     * @param privateChatSetting The privateChatSetting to set.
     */
    public void setPrivateChatSetting(int privateChatSetting) {
        this.privateChatSetting = privateChatSetting;
        updateChatSettings();
    }

    /**
     * Gets the clanChatSetting.
     *
     * @return The clanChatSetting.
     */
    public int getClanChatSetting() {
        return clanChatSetting;
    }

    /**
     * Sets the clanChatSetting.
     *
     * @param clanChatSetting The clanChatSetting to set.
     */
    public void setClanChatSetting(int clanChatSetting) {
        this.clanChatSetting = clanChatSetting;
        player.getConfigManager().set(1054, clanChatSetting);
    }

    /**
     * Sets the clanChatSetting.
     *
     * @param clanChatSetting The clanChatSetting to set.
     */
    public void setClanChatSettingSQL(int clanChatSetting) {
        this.clanChatSetting = clanChatSetting;
    }

    /**
     * Gets the tradeSetting.
     *
     * @return The tradeSetting.
     */
    public int getTradeSetting() {
        return tradeSetting;
    }

    /**
     * Sets the tradeSetting.
     *
     * @param tradeSetting The tradeSetting to set.
     */
    public void setTradeSetting(int tradeSetting) {
        this.tradeSetting = tradeSetting;
        updateChatSettings();
    }

    /**
     * Gets the assistSetting.
     *
     * @return The assistSetting.
     */
    public int getAssistSetting() {
        return assistSetting;
    }

    /**
     * Sets the assistSetting.
     *
     * @param assistSetting The assistSetting to set.
     */
    public void setAssistSetting(int assistSetting) {
        this.assistSetting = assistSetting;
        player.getConfigManager().set(1055, assistSetting);
    }

    /**
     * @return the runEnergy
     */
    public double getRunEnergy() {
        return runEnergy;
    }

    /**
     * @param runEnergy the runEnergy to set
     */
    public void setRunEnergy(double runEnergy) {
        this.runEnergy = runEnergy;
    }

    /**
     * Gets the attackStyleIndex.
     *
     * @return The attackStyleIndex.
     */
    public int getAttackStyleIndex() {
        return attackStyleIndex;
    }

    /**
     * Sets the attackStyleIndex.
     *
     * @param attackStyleIndex The attackStyleIndex to set.
     */
    public void setAttackStyleIndex(int attackStyleIndex) {
        this.attackStyleIndex = attackStyleIndex;
    }

    /**
     * Sets the frame mode.
     *
     * @param frameMode The id of the frame mode.
     */
    public void setFrameMode(int frameMode) {
        player.getActionSender().sendInterfaceConfig(100, frameMode == 0 ? 1 : 0);
        player.getActionSender().sendInterfaceConfig(101, frameMode == 1 ? 1 : 0);
        player.getActionSender().sendInterfaceConfig(102, frameMode == 2 ? 1 : 0);
    }

    /**
     * Toggles the side-panel transparency.
     */
    public void toggleSidePanelTransparent() {
        setSidePanelTransparent(!isSidePanelTransparent(), true);
    }

    /**
     * Toggles the side-panel transparency.
     *
     * @param sidePanelTransparent If the side-panel should is transparent.
     */
    public void setSidePanelTransparent(boolean sidePanelTransparent, boolean update) {
        this.sidePanelTransparent = sidePanelTransparent;
        if (update) {
            player.getInterfaceState().force(103, sidePanelTransparent ? 1 : 0, true);
        }
    }

    /**
     * Toggles the remaining xp tooltip.
     */
    public void toggleRemainingXP() {
        setRemainingXP(!isRemainingXP(), true);
    }

    /**
     * Toggles the remaining xp tooltip.
     *
     * @param remainingXP If remaining xp tooltips are on.
     */
    public void setRemainingXP(boolean remainingXP, boolean update) {
        this.remainingXP = remainingXP;
        if (update) {
            player.getInterfaceState().force(104, remainingXP ? 1 : 0, true);
        }
    }

    /**
     * Toggles roof-removal.
     */
    public void toggleRoofRemoval() {
        setRoofRemoval(!isRoofRemoval(), true);
    }

    /**
     * Toggles roof-removal.
     *
     * @param roofRemoval If roofs should be removed.
     */
    public void setRoofRemoval(boolean roofRemoval, boolean update) {
        this.roofRemoval = roofRemoval;
        if (update) {
            player.getInterfaceState().force(105, roofRemoval ? 1 : 0, true);
        }
    }

    /**
     * Toggles data orbs.
     */
    public void toggleDataOrbs() {
        setDataOrbs(!isDataOrbs(), true);
    }

    /**
     * Toggles data orbs.
     *
     * @param dataOrbs If data orbs are shown.
     */
    public void setDataOrbs(boolean dataOrbs, boolean update) {
        this.dataOrbs = dataOrbs;
        if (update) {
            player.getInterfaceState().force(106, dataOrbs ? 1 : 0, true);
        }
    }

    /**
     * Toggles the chatbox transparency.
     */
    public void toggleChatboxTransparent() {
        setChatboxTransparent(!isChatboxTransparent(), true);
    }

    /**
     * Toggles the chatbox transparency.
     *
     * @param chatboxTransparent If the chatbox should is transparent.
     */
    public void setChatboxTransparent(boolean chatboxTransparent, boolean update) {
        this.chatboxTransparent = chatboxTransparent;
        if (update) {
            player.getInterfaceState().force(107, chatboxTransparent ? 1 : 0, true);
        }
    }

    /**
     * Toggles click through chatbox.
     */
    public void toggleClickThroughChatbox() {
        setClickThroughChatbox(!isClickThroughChatbox(), true);
    }

    /**
     * Toggles click through chatbox.
     *
     * @param clickThroughChatbox If the chatbox can be clicked through.
     */
    public void setClickThroughChatbox(boolean clickThroughChatbox, boolean update) {
        this.clickThroughChatbox = clickThroughChatbox;
        if (update) {
            player.getInterfaceState().force(234, clickThroughChatbox ? 1 : 0, true);
        }
    }

    /**
     * Toggles side-panels bottom.
     */
    public void toggleSidePanelsBottom() {
        setSidePanelsBottom(!isSidePanelsBottom(), true);
    }

    /**
     * Toggles side-panels bottom.
     *
     * @param sidePanelsBottom If the side-panels are old-school box or bottom.
     */
    public void setSidePanelsBottom(boolean sidePanelsBottom, boolean update) {
        this.sidePanelsBottom = sidePanelsBottom;
        if (update) {
            player.getInterfaceState().force(109, sidePanelsBottom ? 1 : 0, true);
        }
    }

    /**
     * Toggles if side-panels can be closed by their hotkey.
     */
    public void toggleSidePanelHotkeys() {
        setSidePanelHotkeys(!isSidePanelHotkeys(), true);
    }

    /**
     * Toggles if side-panels can be closed by their hotkey.
     *
     * @param sidePanelHotkeys If side-panels can be closed by their hotkey.
     */
    public void setSidePanelHotkeys(boolean sidePanelHotkeys, boolean update) {
        this.sidePanelHotkeys = sidePanelHotkeys;
        if (update) {
            player.getInterfaceState().force(110, sidePanelHotkeys ? 1 : 0, true);
        }
    }

    /**
     * If the player's side-panel is opaque or transparent.
     */
    public boolean isSidePanelTransparent() {
        return sidePanelTransparent;
    }

    /**
     * If the remaining xp tooltips are on.
     */
    public boolean isRemainingXP() {
        return remainingXP;
    }

    /**
     * If roofs are removed.
     */
    public boolean isRoofRemoval() {
        return roofRemoval;
    }

    /**
     * If data orbs are shown.
     */
    public boolean isDataOrbs() {
        return dataOrbs;
    }

    /**
     * If the chatbox is transparent.
     */
    public boolean isChatboxTransparent() {
        return chatboxTransparent;
    }

    /**
     * If the chatbox can be clicked through.
     */
    public boolean isClickThroughChatbox() {
        return clickThroughChatbox;
    }

    /**
     * If the side-panels are bottom-line.
     */
    public boolean isSidePanelsBottom() {
        return sidePanelsBottom;
    }

    /**
     * If the side-panels can be closed with their hotkey.
     */
    public boolean isSidePanelHotkeys() {
        return sidePanelHotkeys;
    }


    public boolean isHitIcons() {
        return hitIcons;
    }

    public void setHitIcons(boolean hitIcons) {
        this.hitIcons = hitIcons;
        player.getInterfaceState().force(InterfaceConfiguration.HIT_ICONS, hitIcons, true);
    }

    public boolean isHealthBar() {
        return healthBar;
    }

    public void setHealthBar(boolean healthBar) {
        this.healthBar = healthBar;
        player.getInterfaceState().force(InterfaceConfiguration.HEALTH_BARS, healthBar, true);
    }

    public boolean isTweening() {
        return tweening;
    }

    public void setTweening(boolean tweening) {
        this.tweening = tweening;
        player.getInterfaceState().force(InterfaceConfiguration.TWEENING, tweening, true);
    }

    public int getGameframe() {
        return gameframe;
    }

    public void toggleGameframe() {
        switch (gameframe) {
//            case 602:
//                gameframe = 530;
//                break;
            case 530:
                gameframe = 474;
                break;
            case 474:
                gameframe = 530;
                break;
            default:
                gameframe = 474;
                break;
        }
        player.getInterfaceState().force(InterfaceConfiguration.GAMEFRAME, gameframe, true);
    }

    public boolean isHitmarks() {
        return hitmarks;
    }

    public void setHitmarks(boolean hitmarks) {
        this.hitmarks = hitmarks;
        player.getInterfaceState().force(InterfaceConfiguration.HITSPLATS, hitmarks, true);
    }

    public boolean isCursors() {
        return cursors;
    }

    public void setCursors(boolean cursors) {
        this.cursors = cursors;
        player.getInterfaceState().force(InterfaceConfiguration.CURSORS, cursors, true);
    }

    public boolean isHdTextures() {
        return hdTextures;
    }

    public void setHdTextures(boolean hdTextures) {
        this.hdTextures = hdTextures;
        player.getInterfaceState().force(InterfaceConfiguration.HD_TEXTURES, hdTextures, true);
    }

    public boolean isFog() {
        return fog;
    }

    public void setFog(boolean fog) {
        this.fog = fog;
        player.getInterfaceState().force(InterfaceConfiguration.FOG, fog, true);
    }

    public boolean isContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(boolean contextMenu) {
        this.contextMenu = contextMenu;
        player.getInterfaceState().force(InterfaceConfiguration.CONTEXT_MENU, contextMenu, true);
    }
}
