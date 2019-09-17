package org.gielinor.game.node.entity.player.link;

import java.nio.ByteBuffer;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents a player's boss kill log.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         TODO Slayer
 */
public class BossKillLog implements SavingModule {

    /**
     * Represents fields to be inserted into the database.
     */
    public static final String[] FIELDS = new String[]{
        "pidn", "kree_arra_kills", "zilyana_kills", "graardor_kills", "kril_tsut_kills",
        "dagannoth_rex_kills", "dagannoth_prime_kills", "dagannoth_supreme_kills", "giant_mole_kills",
        "kalphite_queen_kills", "kbd_kills", "chaos_elemental_kills", "bork_kills", "barrelchest_kills",
        "corporeal_beast_kills"
    };
    /**
     * Gets the amount of Kree'arra kills the player has.
     */
    private int kreeArraKills = 0;
    /**
     * Gets the amount of Commander Zilyana kills the player has.
     */
    private int zilyanaKills = 0;
    /**
     * Gets the amount of General Graardor kills the player has.
     */
    private int graardorKills = 0;
    /**
     * Gets the amount of K'ril Tsutsaroth kills the player has.
     */
    private int krilTsutKills = 0;
    /**
     * Gets the amount of Dagannoth Rex kills the player has.
     */
    private int dagannothRexKills = 0;
    /**
     * Gets the amount of Dagannoth Prime kills the player has.
     */
    private int dagannothPrimeKills = 0;
    /**
     * Gets the amount of Dagannoth Supreme kills the player has.
     */
    private int dagannothSupremeKills = 0;
    /**
     * Gets the amount of Giant Mole kills the player has.
     */
    private int giantMoleKills = 0;
    /**
     * Gets the amount of Kalphite Queen kills the player has.
     */
    private int kalphiteQueenKills = 0;
    /**
     * Gets the amount of KBD kills the player has.
     */
    private int kbdKills = 0;
    /**
     * Gets the amount of Chaos Elemental kills the player has.
     */
    private int chaosElementalKills = 0;
    /**
     * Gets the amount of Bork kills the player has.
     */
    private int borkKills = 0;
    /**
     * Gets the amount of Barrelchest kills the player has.
     */
    private int barrelchestKills = 0;
    /**
     * Gets the amount of Corporeal Beast kills the player has.
     */
    private int corporealBeastKills = 0;

    /**
     * Constructs a new {@link BossKillLog}.
     */
    public BossKillLog() {
        /**
         * empty.
         */
    }

    /**
     * Gets the amount of Kree'arra kills the player has.
     *
     * @return The kills.
     */
    public int getKreeArraKills() {
        return kreeArraKills;
    }

    /**
     * Increases the amount of Kree'arra kills the player has.
     *
     * @param kreeArraKills The amount to increase by.
     */
    public void increaseKreeArraKills(int kreeArraKills) {
        this.kreeArraKills += kreeArraKills;
    }

    /**
     * Gets the amount of Commander Zilyana kills the player has.
     *
     * @return The kills.
     */
    public int getZilyanaKills() {
        return zilyanaKills;
    }

    /**
     * Increases the amount of Zilyana kills the player has.
     *
     * @param zilyanaKills The amount to increase by.
     */
    public void increaseZilyanaKills(int zilyanaKills) {
        this.zilyanaKills += zilyanaKills;
    }

    /**
     * Gets the amount of General Graardor kills the player has.
     *
     * @return The kills.
     */
    public int getGraardorKills() {
        return graardorKills;
    }

    /**
     * Increases the amount of General Graardor kills the player has.
     *
     * @param graardorKills The amount to increase by.
     */
    public void increaseGraardorKills(int graardorKills) {
        this.graardorKills += graardorKills;
    }

    /**
     * Gets the amount of K'ril Tsutsaroth kills the player has.
     *
     * @return The kills.
     */
    public int getKrilTsutKills() {
        return krilTsutKills;
    }

    /**
     * Increases the amount of K'ril Tsutsaroth kills the player has.
     *
     * @param krilTsutKills The amount to increase by.
     */
    public void increaseKrilTsutKills(int krilTsutKills) {
        this.krilTsutKills += krilTsutKills;
    }

    /**
     * Gets the amount of Dagannoth Rex kills the player has.
     *
     * @return The kills.
     */
    public int getDagannothRexKills() {
        return dagannothRexKills;
    }

    /**
     * Increases the amount of Dagannoth Rex kills the player has.
     *
     * @param dagannothRexKills The amount to increase by.
     */
    public void increaseDagannothRexKills(int dagannothRexKills) {
        this.dagannothRexKills += dagannothRexKills;
    }

    /**
     * Gets the amount of Dagannoth Prime kills the player has.
     *
     * @return The kills.
     */
    public int getDagannothPrimeKills() {
        return dagannothPrimeKills;
    }

    /**
     * Increases the amount of Dagannoth Prime kills the player has.
     *
     * @param dagannothPrimeKills The amount to increase by.
     */
    public void increaseDagannothPrimeKills(int dagannothPrimeKills) {
        this.dagannothPrimeKills += dagannothPrimeKills;
    }

    /**
     * Gets the amount of Dagannoth Supreme kills the player has.
     *
     * @return The kills.
     */
    public int getDagannothSupremeKills() {
        return dagannothSupremeKills;
    }

    /**
     * Increases the amount of Dagannoth Supreme kills the player has.
     *
     * @param dagannothSupremeKills The amount to increase by.
     */
    public void increaseDagannothSupremeKills(int dagannothSupremeKills) {
        this.dagannothSupremeKills += dagannothSupremeKills;
    }

    /**
     * Gets the amount of Giant Mole kills the player has.
     *
     * @return The kills.
     */
    public int getGiantMoleKills() {
        return giantMoleKills;
    }

    /**
     * Increases the amount of Giant Mole kills the player has.
     *
     * @param giantMoleKills The amount to increase by.
     */
    public void increaseGiantMoleKills(int giantMoleKills) {
        this.giantMoleKills += giantMoleKills;
    }

    /**
     * Gets the amount of Kalphite Queen kills the player has.
     *
     * @return The kills.
     */
    public int getKalphiteQueenKills() {
        return kalphiteQueenKills;
    }

    /**
     * Increases the amount of Kalphite Queen kills the player has.
     *
     * @param kalphiteQueenKills The amount to increase by.
     */
    public void increaseKalphiteQueenKills(int kalphiteQueenKills) {
        this.kalphiteQueenKills += kalphiteQueenKills;
    }

    /**
     * Gets the amount of KBD kills the player has.
     *
     * @return The kills.
     */
    public int getKbdKills() {
        return kbdKills;
    }

    /**
     * Increases the amount of KBD kills the player has.
     *
     * @param kbdKills The amount to increase by.
     */
    public void increaseKbdKills(int kbdKills) {
        this.kbdKills += kbdKills;
    }

    /**
     * Gets the amount of Chaos Elemental kills the player has.
     *
     * @return The kills.
     */
    public int getChaosElementalKills() {
        return chaosElementalKills;
    }

    /**
     * Increases the amount of Chaos Elemental kills the player has.
     *
     * @param chaosElementalKills The amount to increase by.
     */
    public void increaseChaosElementalKills(int chaosElementalKills) {
        this.chaosElementalKills += chaosElementalKills;
    }

    /**
     * Gets the amount of Bork kills the player has.
     *
     * @return The kills.
     */
    public int getBorkKills() {
        return borkKills;
    }

    /**
     * Increases the amount of Bork kills the player has.
     *
     * @param borkKills The amount to increase by.
     */
    public void increaseBorkKills(int borkKills) {
        this.borkKills += borkKills;
    }

    /**
     * Gets the amount of Barrelchest kills the player has.
     *
     * @return The kills.
     */
    public int getBarrelchestKills() {
        return barrelchestKills;
    }

    /**
     * Increases the amount of Barrelchest kills the player has.
     *
     * @param barrelchestKills The amount to increase by.
     */
    public void increaseBarrelchestKills(int barrelchestKills) {
        this.barrelchestKills += barrelchestKills;
    }

    /**
     * Gets the amount of Corporeal Beast kills the player has.
     *
     * @return The kills.
     */
    public int getCorporealBeastKills() {
        return corporealBeastKills;
    }

    /**
     * Increases the amount of Corporeal Beast kills the player has.
     *
     * @param corporealBeastKills The amount to increase by.
     */
    public void increaseCorporealBeastKills(int corporealBeastKills) {
        this.corporealBeastKills += corporealBeastKills;
    }

    /**
     * Sends the interface for the player.
     *
     * @param player The player.
     */
    public void sendInterface(Player player) {
        int childId = 612;
        int[] data = new int[]{
            kreeArraKills, zilyanaKills, graardorKills, krilTsutKills,
            dagannothRexKills, dagannothPrimeKills, dagannothSupremeKills, giantMoleKills,
            kalphiteQueenKills, kbdKills, chaosElementalKills, borkKills,
            barrelchestKills, player.getSavedData().getActivityData().getBarrowsChestCount()
        };
        // TODO Corporeal Beast
        for (int kills : data) {
            player.getActionSender().sendString(childId, TextUtils.getFormattedNumber(kills));
            childId++;
        }
        player.getInterfaceState().open(new Component(580));
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        SavedData.save(byteBuffer, kreeArraKills, 1);
        SavedData.save(byteBuffer, zilyanaKills, 2);
        SavedData.save(byteBuffer, graardorKills, 3);
        SavedData.save(byteBuffer, krilTsutKills, 4);
        SavedData.save(byteBuffer, dagannothRexKills, 5);
        SavedData.save(byteBuffer, dagannothPrimeKills, 6);
        SavedData.save(byteBuffer, dagannothSupremeKills, 7);
        SavedData.save(byteBuffer, giantMoleKills, 8);
        SavedData.save(byteBuffer, kalphiteQueenKills, 9);
        SavedData.save(byteBuffer, kbdKills, 10);
        SavedData.save(byteBuffer, chaosElementalKills, 11);
        SavedData.save(byteBuffer, borkKills, 12);
        SavedData.save(byteBuffer, barrelchestKills, 13);
        SavedData.save(byteBuffer, corporealBeastKills, 14);
        byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int opcode;
        while ((opcode = byteBuffer.get()) != 0) {
            switch (opcode) {
                case 1:
                    kreeArraKills = byteBuffer.getInt();
                    break;
                case 2:
                    zilyanaKills = byteBuffer.getInt();
                    break;
                case 3:
                    graardorKills = byteBuffer.getInt();
                    break;
                case 4:
                    krilTsutKills = byteBuffer.getInt();
                    break;
                case 5:
                    dagannothRexKills = byteBuffer.getInt();
                    break;
                case 6:
                    dagannothPrimeKills = byteBuffer.getInt();
                    break;
                case 7:
                    dagannothSupremeKills = byteBuffer.getInt();
                    break;
                case 8:
                    giantMoleKills = byteBuffer.getInt();
                    break;
                case 9:
                    kalphiteQueenKills = byteBuffer.getInt();
                    break;
                case 10:
                    kbdKills = byteBuffer.getInt();
                    break;
                case 11:
                    chaosElementalKills = byteBuffer.getInt();
                    break;
                case 12:
                    borkKills = byteBuffer.getInt();
                    break;
                case 13:
                    barrelchestKills = byteBuffer.getInt();
                    break;
                case 14:
                    corporealBeastKills = byteBuffer.getInt();
                    break;
            }
        }
    }
}
