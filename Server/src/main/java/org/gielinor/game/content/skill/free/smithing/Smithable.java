package org.gielinor.game.content.skill.free.smithing;

import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.content.skill.Craftable;
import org.gielinor.game.node.item.Item;

/**
 * Represents a smithable item
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         <p>
 *         TODO Rewrite this, need to organize it much better
 */
public enum Smithable {

    BRONZE_ARROWTIPS(new Item(2349, 1), new Craftable(new Item(39, 5), 5, 12.5)),
    BRONZE_DART_TIP(new Item(2349, 1), new Craftable(new Item(819, 10), 4, 12.5)),
    BRONZE_KNIFE(new Item(2349, 1), new Craftable(new Item(864, 5), 7, 12.5)),
    BRONZE_BOLTS(new Item(2349, 1), new Craftable(new Item(9375, 10), 3, 12)),
    BRONZE_PLATELEGS(new Item(2349, 3), new Craftable(new Item(1075, 1), 14, 37.5)),
    BRONZE_PLATESKIRT(new Item(2349, 3), new Craftable(new Item(1087, 1), 14, 37.5)),
    BRONZE_CHAINBODY(new Item(2349, 3), new Craftable(new Item(1103, 1), 11, 37.5)),
    BRONZE_PLATEBODY(new Item(2349, 5), new Craftable(new Item(1117, 1), 18, 62.5)),
    BRONZE_MED_HELM(new Item(2349, 1), new Craftable(new Item(1139, 1), 3, 12.5)),
    BRONZE_FULL_HELM(new Item(2349, 2), new Craftable(new Item(1155, 1), 7, 12.5)),
    BRONZE_SQ_SHIELD(new Item(2349, 2), new Craftable(new Item(1173, 1), 8, 25)),
    BRONZE_KITESHIELD(new Item(2349, 3), new Craftable(new Item(1189, 1), 12, 37.5)),
    BRONZE_DAGGER(new Item(2349, 1), new Craftable(new Item(1205, 1), 1, 12.5)),
    BRONZE_SWORD(new Item(2349, 1), new Craftable(new Item(1277, 1), 4, 12.5)),
    BRONZE_LONGSWORD(new Item(2349, 2), new Craftable(new Item(1291, 1), 6, 25)),
    BRONZE_2H_SWORD(new Item(2349, 3), new Craftable(new Item(1307, 1), 14, 37.5)),
    BRONZE_SCIMITAR(new Item(2349, 2), new Craftable(new Item(1321, 1), 5, 25)),
    BRONZE_WARHAMMER(new Item(2349, 3), new Craftable(new Item(1337, 1), 9, 37.5)),
    BRONZE_AXE(new Item(2349, 1), new Craftable(new Item(1351, 1), 1, 12.5)),
    BRONZE_BATTLEAXE(new Item(2349, 3), new Craftable(new Item(1375, 1), 10, 37.5)),
    BRONZE_MACE(new Item(2349, 1), new Craftable(new Item(1422, 1), 2, 12.5)),
    BRONZE_CLAWS(new Item(2349, 2), new Craftable(new Item(3095, 1), 13, 25)),
    BRONZE_NAILS(new Item(2349, 1), new Craftable(new Item(4819, 15), 4, 12.5)),
    BRONZE_LIMBS(new Item(2349, 1), new Craftable(new Item(9420, 1), 6, 12.5)),
    IRON_ARROWTIPS(new Item(2351, 1), new Craftable(new Item(40, 15), 20, 25)),
    IRON_DART_TIP(new Item(2351, 1), new Craftable(new Item(820, 10), 19, 25)),
    IRON_KNIFE(new Item(2351, 1), new Craftable(new Item(863, 5), 22, 25)),
    IRON_PLATELEGS(new Item(2351, 3), new Craftable(new Item(1067, 1), 31, 75)),
    IRON_PLATESKIRT(new Item(2351, 3), new Craftable(new Item(1081, 1), 31, 75)),
    IRON_CHAINBODY(new Item(2351, 3), new Craftable(new Item(1101, 1), 26, 75)),
    IRON_PLATEBODY(new Item(2351, 5), new Craftable(new Item(1115, 1), 33, 125)),
    IRON_MED_HELM(new Item(2351, 1), new Craftable(new Item(1137, 1), 18, 25)),
    IRON_FULL_HELM(new Item(2351, 2), new Craftable(new Item(1153, 1), 22, 50)),
    IRON_SQ_SHIELD(new Item(2351, 2), new Craftable(new Item(1175, 1), 23, 50)),
    IRON_KITESHIELD(new Item(2351, 3), new Craftable(new Item(1191, 1), 27, 75)),
    IRON_DAGGER(new Item(2351, 1), new Craftable(new Item(1203, 1), 15, 25)),
    IRON_SWORD(new Item(2351, 1), new Craftable(new Item(1279, 1), 19, 25)),
    IRON_LONGSWORD(new Item(2351, 2), new Craftable(new Item(1293, 1), 21, 50)),
    IRON_2H_SWORD(new Item(2351, 3), new Craftable(new Item(1309, 1), 29, 75)),
    IRON_SCIMITAR(new Item(2351, 2), new Craftable(new Item(1323, 1), 20, 50)),
    IRON_WARHAMMER(new Item(2351, 3), new Craftable(new Item(1335, 1), 24, 75)),
    IRON_AXE(new Item(2351, 1), new Craftable(new Item(1349, 1), 16, 25)),
    IRON_BATTLEAXE(new Item(2351, 3), new Craftable(new Item(1363, 1), 25, 75)),
    IRON_MACE(new Item(2351, 1), new Craftable(new Item(1420, 1), 17, 25)),
    IRON_CLAWS(new Item(2351, 2), new Craftable(new Item(3096, 1), 28, 50)),
    IRON_NAILS(new Item(2351, 1), new Craftable(new Item(4820, 15), 20, 25)),
    IRON_BOLTS(new Item(2351, 1), new Craftable(new Item(9377, 10), 18, 17)),
    IRON_LIMBS(new Item(2351, 1), new Craftable(new Item(9423, 1), 23, 25)),
    STEEL_ARROWTIPS(new Item(2353, 1), new Craftable(new Item(41, 15), 35, 37.5)),
    STEEL_DART_TIP(new Item(2353, 1), new Craftable(new Item(821, 10), 34, 37.5)),
    STEEL_KNIFE(new Item(2353, 1), new Craftable(new Item(865, 5), 37, 37.5)),
    STEEL_PLATELEGS(new Item(2353, 3), new Craftable(new Item(1069, 1), 46, 112.5)),
    STEEL_PLATESKIRT(new Item(2353, 3), new Craftable(new Item(1083, 1), 46, 112.5)),
    STEEL_CHAINBODY(new Item(2353, 3), new Craftable(new Item(1105, 1), 41, 112.5)),
    STEEL_PLATEBODY(new Item(2353, 5), new Craftable(new Item(1119, 1), 48, 187.5)),
    STEEL_MED_HELM(new Item(2353, 1), new Craftable(new Item(1141, 1), 33, 37.5)),
    STEEL_FULL_HELM(new Item(2353, 2), new Craftable(new Item(1157, 1), 37, 75)),
    STEEL_SQ_SHIELD(new Item(2353, 2), new Craftable(new Item(1177, 1), 38, 75)),
    STEEL_KITESHIELD(new Item(2353, 3), new Craftable(new Item(1193, 1), 42, 112.5)),
    STEEL_DAGGER(new Item(2353, 1), new Craftable(new Item(1207, 1), 30, 37.5)),
    STEEL_SWORD(new Item(2353, 1), new Craftable(new Item(1281, 1), 34, 37.5)),
    STEEL_LONGSWORD(new Item(2353, 2), new Craftable(new Item(1295, 1), 36, 75)),
    STEEL_2H_SWORD(new Item(2353, 3), new Craftable(new Item(1311, 1), 44, 112.5)),
    STEEL_SCIMITAR(new Item(2353, 2), new Craftable(new Item(1325, 1), 35, 75)),
    STEEL_WARHAMMER(new Item(2353, 3), new Craftable(new Item(1339, 1), 39, 112.5)),
    STEEL_AXE(new Item(2353, 1), new Craftable(new Item(1353, 1), 31, 37.5)),
    STEEL_BATTLEAXE(new Item(2353, 3), new Craftable(new Item(1365, 1), 40, 112.5)),
    STEEL_MACE(new Item(2353, 1), new Craftable(new Item(1424, 1), 32, 37.5)),
    STEEL_NAILS(new Item(2353, 1), new Craftable(new Item(1539, 15), 34, 37.5)),
    STEEL_STUDS(new Item(2353, 1), new Craftable(new Item(2370, 15), 36, 37.5)),
    STEEL_CLAWS(new Item(2353, 2), new Craftable(new Item(3097, 1), 43, 37.5)),
    BULLSEYE_LANTERN(new Item(2353, 1), new Craftable(new Item(4544, 1), 49, 37.5)),
    STEEL_BOLTS(new Item(2353, 1), new Craftable(new Item(9378, 10), 33, 37)),
    STEEL_LIMBS(new Item(2353, 1), new Craftable(new Item(9425, 1), 36, 37.5)),
    MITHRIL_ARROWTIPS(new Item(2359, 1), new Craftable(new Item(42, 15), 55, 50)),
    MITHRIL_DART_TIP(new Item(2359, 1), new Craftable(new Item(822, 10), 54, 50)),
    MITHRIL_KNIFE(new Item(2359, 1), new Craftable(new Item(866, 5), 57, 50)),
    MITHRIL_PLATELEGS(new Item(2359, 3), new Craftable(new Item(1071, 1), 66, 150)),
    MITHRIL_PLATESKIRT(new Item(2359, 3), new Craftable(new Item(1085, 1), 66, 150)),
    MITHRIL_CHAINBODY(new Item(2359, 3), new Craftable(new Item(1109, 1), 61, 150)),
    MITHRIL_PLATEBODY(new Item(2359, 5), new Craftable(new Item(1121, 1), 68, 250)),
    MITHRIL_MED_HELM(new Item(2359, 1), new Craftable(new Item(1143, 1), 50, 50)),
    MITHRIL_FULL_HELM(new Item(2359, 2), new Craftable(new Item(1159, 1), 57, 100)),
    MITHRIL_SQ_SHIELD(new Item(2359, 2), new Craftable(new Item(1181, 1), 58, 100)),
    MITHRIL_KITESHIELD(new Item(2359, 3), new Craftable(new Item(1197, 1), 62, 150)),
    MITHRIL_DAGGER(new Item(2359, 1), new Craftable(new Item(1209, 1), 50, 50)),
    MITHRIL_SWORD(new Item(2359, 1), new Craftable(new Item(1285, 1), 50, 50)),
    MITHRIL_LONGSWORD(new Item(2359, 2), new Craftable(new Item(1299, 1), 56, 100)),
    MITHRIL_2H_SWORD(new Item(2359, 3), new Craftable(new Item(1315, 1), 64, 150)),
    MITHRIL_SCIMITAR(new Item(2359, 2), new Craftable(new Item(1329, 1), 55, 100)),
    MITHRIL_WARHAMMER(new Item(2359, 3), new Craftable(new Item(1343, 1), 59, 150)),
    MITHRIL_AXE(new Item(2359, 1), new Craftable(new Item(1355, 1), 51, 50)),
    MITHRIL_BATTLEAXE(new Item(2359, 3), new Craftable(new Item(1369, 1), 60, 150)),
    MITHRIL_MACE(new Item(2359, 1), new Craftable(new Item(1428, 1), 52, 50)),
    MITHRIL_CLAWS(new Item(2359, 2), new Craftable(new Item(3099, 1), 63, 100)),
    MITHRIL_NAILS(new Item(2359, 1), new Craftable(new Item(4822, 15), 54, 50)),
    MITHRIL_BOLTS(new Item(2359, 1), new Craftable(new Item(9379, 10), 53, 50)),
    MITH_GRAPPLE_TIP(new Item(2359, 1), new Craftable(new Item(9416, 1), 59, 50)),
    MITHRIL_LIMBS(new Item(2359, 1), new Craftable(new Item(9427, 1), 56, 50)),
    ADAMANT_ARROWTIPS(new Item(2361, 1), new Craftable(new Item(43, 15), 75, 62.5)),
    ADAMANT_DART_TIP(new Item(2361, 1), new Craftable(new Item(823, 10), 74, 62.5)),
    ADAMANT_KNIFE(new Item(2361, 1), new Craftable(new Item(867, 5), 77, 62.5)),
    ADAMANT_PLATELEGS(new Item(2361, 3), new Craftable(new Item(1073, 1), 86, 187.5)),
    ADAMANT_PLATESKIRT(new Item(2361, 3), new Craftable(new Item(1091, 1), 86, 187.5)),
    ADAMANT_CHAINBODY(new Item(2361, 3), new Craftable(new Item(1111, 1), 81, 187.5)),
    ADAMANT_PLATEBODY(new Item(2361, 5), new Craftable(new Item(1123, 1), 88, 312.5)),
    ADAMANT_MED_HELM(new Item(2361, 1), new Craftable(new Item(1145, 1), 73, 62.5)),
    ADAMANT_FULL_HELM(new Item(2361, 2), new Craftable(new Item(1161, 1), 77, 125)),
    ADAMANT_SQ_SHIELD(new Item(2361, 2), new Craftable(new Item(1183, 1), 78, 125)),
    ADAMANT_KITESHIELD(new Item(2361, 3), new Craftable(new Item(1199, 1), 82, 187.5)),
    ADAMANT_DAGGER(new Item(2361, 1), new Craftable(new Item(1211, 1), 70, 62.5)),
    ADAMANT_SWORD(new Item(2361, 1), new Craftable(new Item(1287, 1), 74, 62.5)),
    ADAMANT_LONGSWORD(new Item(2361, 2), new Craftable(new Item(1301, 1), 76, 125)),
    ADAMANT_2H_SWORD(new Item(2361, 3), new Craftable(new Item(1317, 1), 84, 187.5)),
    ADAMANT_SCIMITAR(new Item(2361, 2), new Craftable(new Item(1331, 1), 75, 125)),
    ADDY_WARHAMMER(new Item(2361, 3), new Craftable(new Item(1345, 1), 79, 187.5)),
    ADAMANT_AXE(new Item(2361, 1), new Craftable(new Item(1357, 1), 71, 62.5)),
    ADAMANT_BATTLEAXE(new Item(2361, 3), new Craftable(new Item(1371, 1), 80, 187.5)),
    ADAMANT_MACE(new Item(2361, 1), new Craftable(new Item(1430, 1), 72, 62.5)),
    ADAMANT_CLAWS(new Item(2361, 2), new Craftable(new Item(3100, 1), 83, 125)),
    ADAMANTITE_NAILS(new Item(2361, 1), new Craftable(new Item(4823, 15), 74, 62.5)),
    ADAMANT_BOLTS(new Item(2361, 1), new Craftable(new Item(9380, 10), 73, 62)),
    ADAMANTITE_LIMBS(new Item(2361, 1), new Craftable(new Item(9429, 1), 76, 62.5)),
    RUNE_ARROWTIPS(new Item(2363, 1), new Craftable(new Item(44, 15), 90, 62.5)),
    RUNE_DART_TIP(new Item(2363, 1), new Craftable(new Item(824, 10), 89, 62.5)),
    RUNE_KNIFE(new Item(2363, 1), new Craftable(new Item(868, 5), 92, 62.5)),
    RUNE_PLATELEGS(new Item(2363, 3), new Craftable(new Item(1079, 1), 99, 225)),
    RUNE_PLATESKIRT(new Item(2363, 3), new Craftable(new Item(1093, 1), 99, 225)),
    RUNE_CHAINBODY(new Item(2363, 3), new Craftable(new Item(1113, 1), 96, 225)),
    RUNE_PLATEBODY(new Item(2363, 5), new Craftable(new Item(1127, 1), 99, 375)),
    RUNE_MED_HELM(new Item(2363, 1), new Craftable(new Item(1147, 1), 88, 75)),
    RUNE_FULL_HELM(new Item(2363, 2), new Craftable(new Item(1163, 1), 92, 150)),
    RUNE_SQ_SHIELD(new Item(2363, 2), new Craftable(new Item(1185, 1), 93, 150)),
    RUNE_KITESHIELD(new Item(2363, 3), new Craftable(new Item(1201, 1), 97, 225)),
    RUNE_DAGGER(new Item(2363, 1), new Craftable(new Item(1213, 1), 85, 75)),
    RUNE_SWORD(new Item(2363, 1), new Craftable(new Item(1289, 1), 89, 75)),
    RUNE_LONGSWORD(new Item(2363, 2), new Craftable(new Item(1303, 1), 91, 150)),
    RUNE_2H_SWORD(new Item(2363, 3), new Craftable(new Item(1319, 1), 99, 225)),
    RUNE_SCIMITAR(new Item(2363, 2), new Craftable(new Item(1333, 1), 90, 150)),
    RUNE_WARHAMMER(new Item(2363, 3), new Craftable(new Item(1347, 1), 94, 225)),
    RUNE_AXE(new Item(2363, 1), new Craftable(new Item(1359, 1), 86, 75)),
    RUNE_BATTLEAXE(new Item(2363, 3), new Craftable(new Item(1373, 1), 95, 225)),
    RUNE_MACE(new Item(2363, 1), new Craftable(new Item(1432, 1), 87, 75)),
    RUNE_CLAWS(new Item(2363, 2), new Craftable(new Item(3101, 1), 98, 150)),
    RUNE_NAILS(new Item(2363, 1), new Craftable(new Item(4824, 15), 89, 62.5)),
    RUNITE_BOLTS(new Item(2363, 1), new Craftable(new Item(9381, 10), 88, 62.5)),
    RUNITE_LIMBS(new Item(2363, 1), new Craftable(new Item(9431, 1), 91, 62.5));

    /**
     * The bar item, with amount
     */
    private final Item bar;

    /**
     * The craftable for this smithable
     */
    private final Craftable craftable;

    /**
     * The mapping of smithables by bar
     */
    private final static Map<Integer, Smithable> smithables = new HashMap<>();

    static {
        for (Smithable sm : Smithable.values()) {
            smithables.put(sm.getBar().getId(), sm);
        }
    }

    /**
     * Represents a smithable item
     *
     * @param bar       the smithable bar
     * @param craftable the craftable for this smithable
     */
    private Smithable(Item bar, Craftable craftable) {
        this.bar = bar;
        this.craftable = craftable;
    }

    /**
     * The bar item, with amount
     *
     * @return the bar
     */
    public Item getBar() {
        return bar;
    }

    /**
     * The craftable for this smithable
     *
     * @return the craftable
     */
    public Craftable getCraftable() {
        return craftable;
    }

    /**
     * Gets the smithable by the product id for the craftable
     *
     * @param id the id of the produced item
     * @return the smithable
     */
    public static Smithable forId(int id) {
        for (Smithable smithable : Smithable.values()) {
            if (smithable.getCraftable().getItem().getId() == id) {
                return smithable;
            }
        }
        return null;
    }

    public double getBaseExperience() {
        if (name().startsWith("BRONZE")) {
            return 12.5;
        }
        if (name().startsWith("IRON")) {
            return 25;
        }
        if (name().startsWith("STEEL")) {
            return 37.5;
        }
        if (name().startsWith("MITHRIL") || name().startsWith("MITH_")) {
            return 50;
        }
        if (name().startsWith("ADAMANT")) {
            return 62.5;
        }
        if (name().startsWith("RUNE") || name().startsWith("RUNITE")) {
            return 75;
        }
        throw new IllegalArgumentException("No smithable found for " + name() + "!");
    }
}
