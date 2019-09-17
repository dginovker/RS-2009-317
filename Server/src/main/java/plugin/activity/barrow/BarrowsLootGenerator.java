package plugin.activity.barrow;

import static org.gielinor.utilities.misc.RandomUtil.random;

import com.google.common.primitives.Booleans;
import org.gielinor.content.periodicity.daily.impl.MinigameSpotlight;
import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Generates barrows loot.
 *
 * Separated from barrows-activity-related interactions, such that rolls can be done
 * without interference with it.
 *
 * <br><br><code>
 *     boolean[] killed = {true, true, true, true, true, true};<br>
 *     Container c = new BarrowsLootGenerator(player).generateLoot(killed, 1000);<br>
 * </code><br>
 *
 * Will generate the best loot with highest potential.
 *
 * @author Erik
 */
public class BarrowsLootGenerator {

    private static final Logger log = LoggerFactory.getLogger(BarrowsLootGenerator.class);

    private static final int BROTHERS_TOTAL = 6;

    private final Player player;

    public BarrowsLootGenerator(Player player) {
        this.player = Objects.requireNonNull(player);
    }

    public Container generateLoot() {
        return generateLoot(player.getSavedData().getActivityData().getBarrowsKilled(),
            player.getSavedData().getActivityData().getBarrowsKillCount(),
            player.getRights().isDeveloper());
    }

    public Container generateLoot(boolean[] brothersKilled, int killcount, boolean consoleOutput) {
        if (brothersKilled.length != BROTHERS_TOTAL)
            throw new IllegalArgumentException("invalid number of brothers killed: " + brothersKilled.length + ". Expected: " + BROTHERS_TOTAL + ".");
        if (killcount < 0 || killcount > 1000)
            throw new IllegalArgumentException("killcount out of bound [0..1000]: " + killcount + ".");
        final int brothersKilledCount = Booleans.asList(brothersKilled).stream().mapToInt(b -> b ? 1 : 0).sum();
        final int lootRoll = killcount + (brothersKilledCount << 1);
        final int uniqueRoll = calculateUniqueMaxRoll(brothersKilledCount);
        final List<Item> uniques = generateAvailableBarrowsEquipment(brothersKilled);
        Container loot = new Container(brothersKilledCount);

        if (consoleOutput) {
            player.getActionSender().sendConsoleMessage("-------------------------------------------------------------------");
            player.getActionSender().sendConsoleMessage("Generating barrows loot with " + brothersKilledCount + " brothers killed and " + killcount + " killcount.");
            player.getActionSender().sendConsoleMessage("Unique drop chance (per roll) is: " + uniqueRoll + ".");
            player.getActionSender().sendConsoleMessage("Normal table max roll is: " + lootRoll + ".");
            player.getActionSender().sendConsoleMessage("Unique drop table pool size: " + uniques.size() + ".");
            player.getActionSender().sendConsoleMessage("-------------------------------------------------------------------");
        }

        for (int i = 1; i <= brothersKilledCount; i++) {
            Item lootdrop;

            if (random(uniqueRoll) == 0) {
                lootdrop = random(uniques);
                // We remove it from the unique pool so that we don't generate the same equipment
                // twice in the same chest.
                uniques.remove(lootdrop);
                // Could be interesting tracking how many uniques enter the game
                log.info("Generated barrows unique [{}] for [{}].", lootdrop.getName(), player.getName());

            } else {
                lootdrop = generateNormalLootDrop(lootRoll);
            }

            loot.add(lootdrop);
            if (consoleOutput) {
                player.getActionSender().sendConsoleMessage(i + ": " + lootdrop.getCount() + " x " + lootdrop.getName());
            }
        }

        return loot;
    }

    private int calculateUniqueMaxRoll(int killedCount) {
        ActivityPlugin currentSpotlight = MinigameSpotlight.Companion.getCurrentSpotlight();
        boolean spotlight = currentSpotlight != null && "Barrows".equals(currentSpotlight.getName());
        int maxRoll = 350 - (45 * killedCount);
        // Increase chance when wearing ring of wealth.
        if (hasLuckImprovement())
            maxRoll -= killedCount * 2;
        // Further increased chance when barrows is the current minigame spotlight.
        if (spotlight)
            maxRoll -= killedCount;
        return maxRoll;
    }

    private boolean hasLuckImprovement() {
        Item ring = player.getEquipment().get(Equipment.SLOT_RING);
        if (ring != null) {
            switch (ring.getId()) {
                // All versions of the ring of wealth.
                case 2572:
                case 22572:
                case 31980:
                case 31982:
                case 31984:
                case 31986:
                case 31988:
                case 32785:
                case 40787:
                case 40788:
                case 40789:
                case 40790:
                    return true;
            }
        }
        return false;
    }

    private Item generateNormalLootDrop(int n) {
        final int roll = random(n);
        Item result;

        if (roll < 380) result = Item.of(Item.COINS, random(1, 4000));
        else if (roll < 505) result = Item.of(558, random(1, 500));  // Mind rune
        else if (roll < 630) result = Item.of(562, random(1, 150));  // Chaos rune
        else if (roll < 755) result = Item.of(560, random(1, 100));  // Death rune
        else if (roll < 880) result = Item.of(565, random(1, 40));   // Blood rune
        else if (roll < 1005) result = Item.of(4740, random(1, 35));  // Bolt rack
        else if (roll < 1008) result = Item.of(985);                  // Tooth half of a key
        else if (roll < 1011) result = Item.of(987);                  // Loop half of a key
        else/*(roll < 1012)*/ result = Item.of(21149);                // Dragon med helm

        return result;
    }

    private List<Item> generateAvailableBarrowsEquipment(boolean[] brothersKilled) {
        List<Item> results = new ArrayList<>();

        // Ahrim the Blighted
        if (brothersKilled[0]) {
            results.add(Item.of(24708));
            results.add(Item.of(24710));
            results.add(Item.of(24712));
            results.add(Item.of(24714));
        }

        // Dharok the Wretched
        if (brothersKilled[1]) {
            results.add(Item.of(24716));
            results.add(Item.of(24718));
            results.add(Item.of(24720));
            results.add(Item.of(24722));
        }

        // Guthan the Infested
        if (brothersKilled[2]) {
            results.add(Item.of(24724));
            results.add(Item.of(24726));
            results.add(Item.of(24728));
            results.add(Item.of(24730));
        }

        // Karil the Tainted
        if (brothersKilled[3]) {
            results.add(Item.of(24732));
            results.add(Item.of(24734));
            results.add(Item.of(24736));
            results.add(Item.of(24738));
        }

        // Torag the Corrupted
        if (brothersKilled[4]) {
            results.add(Item.of(24745));
            results.add(Item.of(24747));
            results.add(Item.of(24749));
            results.add(Item.of(24751));
        }

        // Verac the Defiled
        if (brothersKilled[5]) {
            results.add(Item.of(24753));
            results.add(Item.of(24755));
            results.add(Item.of(24757));
            results.add(Item.of(24759));
        }

        return results;
    }

    public static BarrowsLootGenerator createFor(Player player) {
        return new BarrowsLootGenerator(player);
    }
}
