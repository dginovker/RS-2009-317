package org.gielinor.game.node.entity.player.info;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.parser.player.SavingModule;

/**
 * A player's {@link org.gielinor.game.node.entity.player.info.PerkManager}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PerkManager implements SavingModule {

    /**
     * A random instance.
     */
    private static final Random random = new Random();

    /**
     * The {@link org.gielinor.game.node.entity.player.Player}.
     */
    private final Player player;

    /**
     * The player's {@link Perk} mapping.
     */
    private final Map<Perk, Boolean> perks = new HashMap<>();

    /**
     * Constructs a new {@link org.gielinor.game.node.entity.player.info.PerkManager}.
     *
     * @param player The player.
     */
    public PerkManager(Player player) {
        this.player = Objects.requireNonNull(player);
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the player's {@link Perk} mapping.
     *
     * @return The perk map.
     */
    public Map<Perk, Boolean> getPerks() {
        return perks;
    }

    /**
     * Unlocks a {@link Perk}.
     *
     * @param perk The perk.
     */
    public void unlock(Perk perk) {
        perks.put(perk, false);
    }

    /**
     * Enables a {@link Perk}.
     *
     * @param perk The perk.
     */
    public void enable(Perk perk) {
        perks.put(perk, true);
    }

    /**
     * Disables a {@link Perk}.
     *
     * @param perk The perk.
     */
    public void disable(Perk perk) {
        perks.put(perk, false);
    }

    /**
     * Removes an unlocked {@link Perk}.
     *
     * @param perk The perk.
     */
    public void remove(Perk perk) {
        perks.remove(perk);
    }

    /**
     * Handles the usage of a {@link Perk}.
     *
     * @param perk The perk.
     */
    public void handlePerk(Perk perk) {
        switch (perk) {
            case TRY_AGAIN_BRO:
                final int perk_uses = player.getAttribute("try_again_bro_perk", (int) Perk.TRY_AGAIN_BRO.getTime()) - 1;
                if (perk_uses <= 0) {
                    player.getActionSender().sendMessage("<col=8C3434><shad=0>You have used the last of your Slayer commander perk!");
                    player.removeAttribute("try_again_bro_perk");
                    remove(perk);
                    break;
                } else {
                    player.saveAttribute("try_again_bro_perk", perk_uses);
                }

                player.getActionSender().sendMessage("<col=8C3434><shad=0>You have used the 'Try again bro' perk. You have " + perk_uses + " usages left.");
                break;
            case SLAYER_COMMANDER:
                int uses = player.getAttribute("slayer_commander_perk", (int) Perk.SLAYER_COMMANDER.getTime()) - 1;
                if (uses <= 0) {
                    player.getActionSender().sendMessage("<col=8C3434><shad=0>You have used the last of your Slayer commander perk!");
                    player.removeAttribute("slayer_commander_perk");
                    remove(perk);
                    break;
                } else {
                    player.saveAttribute("slayer_commander_perk", uses);
                }
                player.getActionSender().sendMessage("<col=8C3434><shad=0>You have used the Slayer commander perk. You have " + uses + " usages left.");
                break;
        }
    }

    /**
     * Checks if a {@link Perk} is triggered.
     *
     * @param perk The perk.
     * @return <code>True</code> if so.
     */
    public boolean isTriggered(Perk perk) {
        if (perk == null)
            throw new NullPointerException("Null perk!");
        if (!perk.enabled(player)) {
            return false;
        }
        switch (perk) {
            case ROCK_SMASHER:
                return random.nextDouble() < 0.35;
            case SEED_SAVIOR:
                return random.nextDouble() < 0.40;
            case BONE_SAVIOR:
                return random.nextDouble() < 0.40;
            case KEEN_SUMMONER:
            case KEEN_FISHERMAN:
                return random.nextDouble() < 0.10;
            case AVID_SUMMONER:
            case AVID_FISHERMAN:
                return random.nextDouble() < 0.25;
            case PRO_SUMMONER:
            case PRO_FISHERMAN:
                return random.nextDouble() < 0.50;
            case FIRESTARTER:
                return random.nextDouble() < 0.025;
        }
        return false;
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        byteBuffer.putInt(perks.size());
        for (Entry<Perk, Boolean> perk : perks.entrySet()) {
            byteBuffer.putInt(perk.getKey().getId());
            byteBuffer.put((byte) (perk.getValue() ? 1 : 2));
        }
        // byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int length = byteBuffer.getInt();
        while (length > 0) {
            int perkId = byteBuffer.getInt();
            int enabled = byteBuffer.get() & 0xFFFF;
            Perk perk = Perk.forId(perkId);
            if (perk != null) {
                perks.put(perk, enabled == 1);
            }
            length--;
        }
        player.getAppearance().sync();
    }
}
