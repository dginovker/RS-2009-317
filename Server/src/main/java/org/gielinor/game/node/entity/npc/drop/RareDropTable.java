package org.gielinor.game.node.entity.npc.drop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.node.item.ChanceItem;
import org.gielinor.utilities.misc.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the rare drop table.
 *
 * @author Emperor
 */
public final class RareDropTable {

    private static final Logger log = LoggerFactory.getLogger(RareDropTable.class);

    /**
     * The item id of the item representing the rare drop table slot in a drop table.
     */
    public static final int SLOT_ITEM_ID = 31;

    /**
     * The table rarity ratio.
     */
    private static int tableRarityRatio;

    /**
     * The rare drop table.
     */
    private static final List<ChanceItem> TABLE = new ArrayList<>();

    /**
     * Initializes the rare drop table.
     */
    public static void init() {
        File f = new File("data/essentials/list/rare_drop_table.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.contains(" //")) {
                    s = s.substring(0, s.indexOf(" //"));
                }
                String[] arg = s.replaceAll(" - ", ";").split(";");
                int id = Integer.parseInt(arg[1]);
                int amount = 1;
                int amount2 = amount;
                if (arg[2].contains("-")) {
                    String[] amt = arg[2].split("-");
                    amount = Integer.parseInt(amt[0]);
                    amount2 = Integer.parseInt(amt[1]);
                } else {
                    amount = Integer.parseInt(arg[2]);
                }
                DropFrequency df = DropFrequency.RARE;
                switch (arg[3].toLowerCase()) {
                    case "common":
                        df = DropFrequency.COMMON;
                        break;
                    case "uncommon":
                        df = DropFrequency.UNCOMMON;
                        break;
                    case "rare":
                        df = DropFrequency.RARE;
                        break;
                    case "very rare":
                        df = DropFrequency.VERY_RARE;
                        break;
                }
                TABLE.add(new ChanceItem(id, amount, amount2, 1000, 0.0, df));
            }
        } catch (Throwable t) {
            log.error("Error reading rare drops from [{}].", f.getAbsolutePath(), t);
        }
        int slot = 0;
        for (ChanceItem item : TABLE) {
            int rarity = 1000 / item.getDropFrequency().ordinal();
            item.setTableSlot(slot | ((slot += rarity) << 16));
        }
        tableRarityRatio = (int) (slot * 1.33);
    }

    /**
     * Retrieves a drop from the rare drop table.
     *
     * @return The chance item to drop (<b>can be null!</b>).
     */
    public static ChanceItem retrieve() {
        int slot = RandomUtil.random(tableRarityRatio);
        for (ChanceItem item : TABLE) {
            if ((item.getTableSlot() & 0xFFFF) <= slot && (item.getTableSlot() >> 16) > slot) {
                return item;
            }
        }
        return null;
    }

}
