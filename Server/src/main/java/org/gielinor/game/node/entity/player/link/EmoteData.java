package org.gielinor.game.node.entity.player.link;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.utilities.buffer.ByteBufferUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.interaction.inter.EmoteTabInterface.Emote;

/**
 * Represents the emote data.
 *
 * @author Emperor
 */
public final class EmoteData implements SavingModule {

    private static final Logger log = LoggerFactory.getLogger(EmoteData.class);

    /**
     * The player.
     */
    private final Player player;
    /**
     * The list of unlocked {@link plugin.interaction.inter.EmoteTabInterface.Emote}s.
     */
    private final List<Emote> UNLOCKED_EMOTES = new ArrayList<>();

    /**
     * Constructs a new {@code EmoteData} {@code Object}.
     *
     * @param player The player.
     */
    public EmoteData(Player player) {
        this.player = player;
    }

    /**
     * Unlocks an emote.
     *
     * @param emote The {@link plugin.interaction.inter.EmoteTabInterface.Emote} to unlock.
     */
    public void unlock(Emote emote) {
        if (!UNLOCKED_EMOTES.contains(emote)) {
            UNLOCKED_EMOTES.add(emote);
            refreshListConfigs();
        }
    }

    /**
     * Sets if an emote is unlocked.
     *
     * @param emote    The emote.
     * @param unlocked If the emote is unlocked.
     */
    public void setUnlocked(Emote emote, boolean unlocked) {
        if (unlocked && !UNLOCKED_EMOTES.contains(emote)) {
            UNLOCKED_EMOTES.add(emote);
        }
        if (!unlocked && UNLOCKED_EMOTES.contains(emote)) {
            UNLOCKED_EMOTES.remove(emote);
        }
    }

    /**
     * Locks an emote.
     *
     * @param emote The {@link plugin.interaction.inter.EmoteTabInterface.Emote} to lock.
     */
    public void lock(Emote emote) {
        if (UNLOCKED_EMOTES.contains(emote)) {
            UNLOCKED_EMOTES.remove(emote);
            refreshListConfigs();
        }
    }

    /**
     * Refreshes the emote list configurations.
     */
    public void refreshListConfigs() {
        for (Emote emote : Emote.values()) {
            if (emote.getConfigId() < 1) {
                continue;
            }
            if (emote == Emote.SKILLCAPE) {
                player.getConfigManager().force(emote.getConfigId(), hasMaxedSkill(player) ? 0 : 1, false);
                continue;
            }
            player.getConfigManager().force(emote.getConfigId(), UNLOCKED_EMOTES.contains(emote) ? 0 : 1, false);
        }
    }

    /**
     * Checks if the player has a 99.
     *
     * @param player The player.
     * @return <code>True</code> if they have at least one 99.
     */
    public boolean hasMaxedSkill(final Player player) {
        for (int index = 0; index < Skills.SKILL_NAME.length; index++) {
            if (player.getSkills().getStaticLevel(index) >= 99) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the list of unlocked {@link plugin.interaction.inter.EmoteTabInterface.Emote}s.
     *
     * @return The list.
     */
    public List<Emote> getUnlockedEmotes() {
        return UNLOCKED_EMOTES;
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        for (Emote emote : player.getEmotes().getUnlockedEmotes()) {
            byteBuffer.put((byte) 1);
            ByteBufferUtils.putRS2String(emote.name(), byteBuffer);
        }
        byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int opcode;
        while ((opcode = byteBuffer.get() & 0xFF) != 0) {
            if (opcode == 1) {
                final String emoteName = ByteBufferUtils.getRS2String(byteBuffer);
                Emote emote = Emote.forName(emoteName);
                if (emote == null) {
                    log.warn("Missing emote name [{}].", emoteName);
                    continue;
                }
                player.getEmotes().setUnlocked(emote, true);
            } else {
                log.warn("Unknown emote data opcode: {}.", opcode);
            }
        }
    }
}