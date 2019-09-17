package org.gielinor.game.node.entity.player.link.music;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MusicContext;
import org.gielinor.net.packet.context.StringContext;
import org.gielinor.net.packet.out.MusicPacket;
import org.gielinor.net.packet.out.StringPacket;
import org.gielinor.parser.player.SavingModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles a music playing for a player.
 *
 * @author Emperor
 */
public final class MusicPlayer implements SavingModule {

    private static final Logger log = LoggerFactory.getLogger(MusicPlayer.class);

    /**
     * The tutorial island music.
     */
    public static final int TUTORIAL_MUSIC = 62;

    /**
     * The default music id.
     */
    public static final int DEFAULT_MUSIC_ID = 76;

    /**
     * The configuration ids.
     */
    private static final int[] CONFIG_IDS = { 20, 21, 22, 23, 24, 25, 298, 311, 346, 414, 464, 598, 662, 721, 906, 1009, 1104, 1136, 180, 1202, 1381, 1394, };

    /**
     * The player.
     */
    private final Player player;

    /**
     * The currently unlocked songs.
     */
    private final Map<Integer, MusicEntry> unlocked;

    /**
     * The music id of the currently played song.
     */
    private int currentMusicId;

    /**
     * If a song is currently playing.
     */
    private boolean playing;

    /**
     * If the song is looping.
     */
    private boolean looping;

    /**
     * Constructs a new {@code MusicPlayer} {@code Object}.
     *
     * @param player The player.
     */
    public MusicPlayer(Player player) {
        this.player = player;
        this.unlocked = new HashMap<>();
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        if (!unlocked.isEmpty()) {
            byteBuffer.put((byte) 1);
            byteBuffer.putShort((short) unlocked.size());
            for (MusicEntry entry : unlocked.values()) {
                byteBuffer.putShort((short) entry.getId());
            }
        }
        byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int opcode;
        while ((opcode = byteBuffer.get()) != 0) {
            switch (opcode) {
                case 1:
                    int size = byteBuffer.getShort() & 0xFFFF;
                    for (int i = 0; i < size; i++) {
                        int musicId = byteBuffer.getShort() & 0xFFFF;
                        MusicEntry entry = MusicEntry.forId(musicId);
                        if (entry != null) {
                            unlocked.put(entry.getIndex(), entry);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Initializes the music player.
     */
    public void init() {
        refreshList();
        player.getConfigManager().set(19, looping ? 1 : 0);
        if (!unlocked.containsKey(TUTORIAL_MUSIC)) {
            unlock(TUTORIAL_MUSIC, false);
        }
        if (!isMusicPlaying()) {
            playDefault();
        }
//        if (!hasAirGuitar() && player.getEmotes().getUnlockedEmotes().contains(EmoteTabInterface.Emote.AIR_GUITAR)) {
//            player.getActionSender().sendMessage("As you no longer have all music unlocked, the Air Guitar emote is locked again.");
//            player.getEmotes().lock(EmoteData.AIR_GUITAR);
//        }
    }

    /**
     * Checks if the player has enough songs unlocked for the Air guitar emote.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasAirGuitar() {
        return unlocked.size() >= 500 || unlocked.size() == MusicEntry.getSongs().size();
    }

    /**
     * Checks if the player has unlocked the song.
     *
     * @param musicId The music id.
     * @return <code>True</code> if so.
     */
    public boolean hasUnlocked(int musicId) {
        MusicEntry entry = MusicEntry.forId(musicId);
        return entry != null && hasUnlockedIndex(entry.getIndex());
    }

    /**
     * Checks if the player has unlocked the song for the given list index.
     *
     * @param index The list index.
     * @return <code>True</code> if so.
     */
    public boolean hasUnlockedIndex(int index) {
        return unlocked.containsKey(index);
    }

    /**
     * Refreshes the music list.
     */
    public void refreshList() {
        int[] values = new int[CONFIG_IDS.length];
        for (MusicEntry entry : unlocked.values()) {
            int listIndex = entry.getIndex();
            int index = (listIndex + 1) / 32;
            if (index >= CONFIG_IDS.length) {
                log.warn("Music index out of bounds. Index={}, ListIndex={}, Id={}, Name={}.",
                    index, listIndex, entry.getId(), entry.getName());
                continue;
            }
            values[index] |= 1 << (listIndex - (index * 32));
        }
        for (int i = 0; i < CONFIG_IDS.length; i++) {
            player.getConfigManager().set(CONFIG_IDS[i], values[i]);
        }
    }

    /**
     * Called when a player leaves a music zone without entering a new one.
     */
    public void playDefault() {
        MusicEntry entry = MusicEntry.forId(DEFAULT_MUSIC_ID);
        if (entry != null) {
            play(entry);
        }
    }

    /**
     * Replays the song.
     */
    public void replay() {
        MusicEntry entry = MusicEntry.forId(currentMusicId);
        if (entry != null) {
            play(entry);
        }
    }

    /**
     * Plays the song.
     *
     * @param entry The song.
     */
    public void play(MusicEntry entry) {
        PacketRepository.send(MusicPacket.class, new MusicContext(player, entry.getId()));
        PacketRepository.send(StringPacket.class, new StringContext(player, entry.getName(), 4439));
        currentMusicId = entry.getId();
        playing = true;
    }

    /**
     * Unlocks and plays the music id.
     *
     * @param id The music id to unlock.
     */
    public void unlock(int id) {
        unlock(id, true);
    }

    /**
     * Unlocks the music id.
     *
     * @param id   The music id to unlock.
     * @param play If the song should be played.
     */
    public void unlock(int id, boolean play) {
        MusicEntry entry = MusicEntry.forId(id);
        if (entry == null) {
            log.warn("Missing music entry for id: {}.", id);
            return;
        }
        if (!unlocked.containsKey(entry.getIndex())) {
            unlocked.put(entry.getIndex(), entry);
            String period = "";
            if (!(entry.getName().endsWith("!") || entry.getName().endsWith(".") || entry.getName().endsWith("?"))) {
                period = ".";
            }
            if (!World.getConfiguration().isBetaEnabled()) {
                player.getActionSender().sendMessage("<col=FF0000>You have unlocked a new music track: " + entry.getName() + period + "</col>", 1);
            }
            refreshList();
//            if (!player.getEmotes().getUnlocked()[EmoteData.AIR_GUITAR] && hasAirGuitar()) {
//                player.getEmotes().unlock(EmoteData.AIR_GUITAR);
//                if (unlocked.size() >= 500) {
//                    player.getActionSender().sendMessage("You've unlocked 500 music tracks and can use a new emote!");
//                } else {
//                    player.getActionSender().sendMessage("You've unlocked all music tracks and can use a new emote!");
//                }
//            }
        }
        if (play) {
            play(entry);
        }
    }

    /**
     * Toggles the looping option.
     */
    public void toggleLooping() {
        looping = !looping;
        player.getConfigManager().set(19, looping ? 1 : 0);
    }

    /**
     * If music is currently playing.
     *
     * @return <code>True</code> if so.
     */
    private boolean isMusicPlaying() {
        return currentMusicId > 0 && playing;
    }

    /**
     * Gets the unlocked songs list.
     *
     * @return The unlocked.
     */
    public Map<Integer, MusicEntry> getUnlocked() {
        return unlocked;
    }

    /**
     * Gets the currentMusicId.
     *
     * @return The currentMusicId.
     */
    public int getCurrentMusicId() {
        return currentMusicId;
    }

    /**
     * Sets the currentMusicId.
     *
     * @param currentMusicId The currentMusicId to set.
     */
    public void setCurrentMusicId(int currentMusicId) {
        this.currentMusicId = currentMusicId;
    }

    /**
     * Gets the playing.
     *
     * @return The playing.
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Sets the playing.
     *
     * @param playing The playing to set.
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * Gets the looping.
     *
     * @return The looping.
     */
    public boolean isLooping() {
        return looping;
    }

    /**
     * Sets the looping.
     *
     * @param looping The looping to set.
     */
    public void setLooping(boolean looping) {
        this.looping = looping;
        player.getConfigManager().set(19, looping ? 1 : 0);
    }

}
