package org.gielinor.game.content.skill.free.smithing;

import org.gielinor.game.content.skill.free.smithing.SmithingConstants.Slot;
import org.gielinor.game.node.item.Item;

/**
 * Represents a smithing interface
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         TODO since this skill is such a pain, I wasn't exactly sure how I wanted to
 *         TODO do this, so I threw everything into an enum for the interface
 */
public enum Smith {

    BRONZE(new Item(2349),
        new SmithSlot[]{
            new SmithSlot(Slot.DAGGER, new Item(1205), 1, 1),
            new SmithSlot(Slot.SWORD, new Item(1277), 1, 1),
            new SmithSlot(Slot.SCIMITAR, new Item(1321), 5, 2),
            new SmithSlot(Slot.LONG_SWORD, new Item(1291), 6, 2),
            new SmithSlot(Slot.TWO_HAND_SWORD, new Item(1307), 14, 3)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.AXE, new Item(1351), 1, 1),
            new SmithSlot(Slot.MACE, new Item(1422), 1, 1),
            new SmithSlot(Slot.WARHAMMER, new Item(1337), 9, 3),
            new SmithSlot(Slot.BATTLE_AXE, new Item(1375), 10, 3),
            new SmithSlot(Slot.CLAWS, new Item(3095), 13, 2), //new SmithSlot(new Item(-1))
        },
        new SmithSlot[]{
            new SmithSlot(Slot.CHAIN_BODY, new Item(1103), 11, 3),
            new SmithSlot(Slot.PLATE_LEGS, new Item(1075), 16, 3),
            new SmithSlot(Slot.PLATE_SKIRT, new Item(1087), 16, 3),
            new SmithSlot(Slot.PLATE_BODY, new Item(1117), 18, 5)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.MEDIUM_HELM, new Item(1139), 3, 1),
            new SmithSlot(Slot.FULL_HELM, new Item(1155), 7, 2),
            new SmithSlot(Slot.SQUARE_SHIELD, new Item(1173), 8, 2),
            new SmithSlot(Slot.KITE_SHIELD, new Item(1189), 12, 3),
            new SmithSlot(Slot.NAILS, new Item(4819, 15), 4, 1)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.DART_TIPS, new Item(819, 10), 4, 1),
            new SmithSlot(Slot.ARROWTIPS, new Item(39, 5), 5, 1),
            new SmithSlot(Slot.THROWING_KNIVES, new Item(864, 5), 7, 1)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.BOLTS, new Item(9375, 10), 3, 1),
            new SmithSlot(Slot.LIMBS, new Item(9420), 6, 1)
        }
    ),
    IRON(new Item(2351),
        new SmithSlot[]{
            new SmithSlot(Slot.DAGGER, new Item(1203), 15, 1),
            new SmithSlot(Slot.SWORD, new Item(1279), 19, 1),
            new SmithSlot(Slot.SCIMITAR, new Item(1323), 20, 2),
            new SmithSlot(Slot.LONG_SWORD, new Item(1293), 21, 2),
            new SmithSlot(Slot.TWO_HAND_SWORD, new Item(1309), 29, 3)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.AXE, new Item(1349), 16, 1),
            new SmithSlot(Slot.MACE, new Item(1420), 17, 1),
            new SmithSlot(Slot.WARHAMMER, new Item(1335), 24, 3),
            new SmithSlot(Slot.BATTLE_AXE, new Item(1363), 25, 3),
            new SmithSlot(Slot.CLAWS, new Item(3096), 28, 2)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.CHAIN_BODY, new Item(1101), 26, 3),
            new SmithSlot(Slot.PLATE_LEGS, new Item(1067), 31, 3),
            new SmithSlot(Slot.PLATE_SKIRT, new Item(1081), 31, 3),
            new SmithSlot(Slot.PLATE_BODY, new Item(1115), 33, 5)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.MEDIUM_HELM, new Item(1137), 18, 1),
            new SmithSlot(Slot.FULL_HELM, new Item(1153), 22, 2),
            new SmithSlot(Slot.SQUARE_SHIELD, new Item(1175), 23, 2),
            new SmithSlot(Slot.KITE_SHIELD, new Item(1191), 27, 3),
            new SmithSlot(Slot.NAILS, new Item(4820, 15), 19, 1)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.DART_TIPS, new Item(820, 10), 19, 1),
            new SmithSlot(Slot.ARROWTIPS, new Item(40, 15), 20, 1),
            new SmithSlot(Slot.THROWING_KNIVES, new Item(863, 5), 22, 1)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.BOLTS, new Item(9377, 10), 18, 1),
            new SmithSlot(Slot.LIMBS, new Item(9423), 23, 1)
        }
    ),
    STEEL(new Item(2353),
        new SmithSlot[]{
            new SmithSlot(Slot.DAGGER, new Item(1207), 30, 1),
            new SmithSlot(Slot.SWORD, new Item(1281), 34, 1),
            new SmithSlot(Slot.SCIMITAR, new Item(1325), 35, 2),
            new SmithSlot(Slot.LONG_SWORD, new Item(1295), 36, 2),
            new SmithSlot(Slot.TWO_HAND_SWORD, new Item(1311), 44, 3)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.AXE, new Item(1353), 31, 1),
            new SmithSlot(Slot.MACE, new Item(1424), 32, 1),
            new SmithSlot(Slot.WARHAMMER, new Item(1339), 39, 3),
            new SmithSlot(Slot.BATTLE_AXE, new Item(1365), 40, 3),
            new SmithSlot(Slot.CLAWS, new Item(3097), 43, 2)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.CHAIN_BODY, new Item(1105), 41, 3),
            new SmithSlot(Slot.PLATE_LEGS, new Item(1069), 46, 3),
            new SmithSlot(Slot.PLATE_SKIRT, new Item(1083), 46, 3),
            new SmithSlot(Slot.PLATE_BODY, new Item(1119), 48, 5),
            new SmithSlot(Slot.OIL_LANTERN_FRAME, new Item(4544), 49, 1)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.MEDIUM_HELM, new Item(1141), 33, 1),
            new SmithSlot(Slot.FULL_HELM, new Item(1157), 37, 2),
            new SmithSlot(Slot.SQUARE_SHIELD, new Item(1177), 38, 2),
            new SmithSlot(Slot.KITE_SHIELD, new Item(1193), 42, 3),
            new SmithSlot(Slot.NAILS, new Item(1539, 15), 34, 1)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.DART_TIPS, new Item(821, 10), 34, 1),
            new SmithSlot(Slot.ARROWTIPS, new Item(41, 15), 35, 1),
            new SmithSlot(Slot.THROWING_KNIVES, new Item(865, 5), 37, 1),
            new SmithSlot(Slot.OTHER, null, 1, 1),
            new SmithSlot(Slot.STUDS, new Item(2370), 36, 1)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.BOLTS, new Item(9378, 10), 33, 1),
            new SmithSlot(Slot.LIMBS, new Item(9425), 36, 1)
        }
    ),
    MITHRIL(new Item(2359),
        new SmithSlot[]{
            new SmithSlot(Slot.DAGGER, new Item(1209), 50, 1),
            new SmithSlot(Slot.SWORD, new Item(1285), 54, 1),
            new SmithSlot(Slot.SCIMITAR, new Item(1329), 55, 2),
            new SmithSlot(Slot.LONG_SWORD, new Item(1299), 56, 2),
            new SmithSlot(Slot.TWO_HAND_SWORD, new Item(1315), 64, 3)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.AXE, new Item(1355), 51, 1),
            new SmithSlot(Slot.MACE, new Item(1428), 52, 1),
            new SmithSlot(Slot.WARHAMMER, new Item(1343), 59, 3),
            new SmithSlot(Slot.BATTLE_AXE, new Item(1369), 60, 3),
            new SmithSlot(Slot.CLAWS, new Item(3099), 63, 2)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.CHAIN_BODY, new Item(1109), 61, 3),
            new SmithSlot(Slot.PLATE_LEGS, new Item(1071), 66, 3),
            new SmithSlot(Slot.PLATE_SKIRT, new Item(1085), 66, 3),
            new SmithSlot(Slot.PLATE_BODY, new Item(1121), 68, 5),
            new SmithSlot(Slot.OTHER, new Item(9416, 1), 59, 1)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.MEDIUM_HELM, new Item(1143), 53, 1),
            new SmithSlot(Slot.FULL_HELM, new Item(1159), 57, 2),
            new SmithSlot(Slot.SQUARE_SHIELD, new Item(1181), 58, 2),
            new SmithSlot(Slot.KITE_SHIELD, new Item(1197), 62, 3),
            new SmithSlot(Slot.NAILS, new Item(4822, 15), 54, 1)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.DART_TIPS, new Item(822, 10), 54, 1),
            new SmithSlot(Slot.ARROWTIPS, new Item(42, 15), 55, 1),
            new SmithSlot(Slot.THROWING_KNIVES, new Item(866, 15), 55, 1)//,
            //new SmithSlot(Slot.OTHER, new Item(9416, 1), 59, 1)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.BOLTS, new Item(9379, 10), 53, 1),
            new SmithSlot(Slot.LIMBS, new Item(9427), 56, 1)
        }
    ),
    ADAMANTITE(new Item(2361),
        new SmithSlot[]{
            new SmithSlot(Slot.DAGGER, new Item(1211), 70, 1),
            new SmithSlot(Slot.SWORD, new Item(1287), 74, 1),
            new SmithSlot(Slot.SCIMITAR, new Item(1331), 75, 2),
            new SmithSlot(Slot.LONG_SWORD, new Item(1301), 76, 2),
            new SmithSlot(Slot.TWO_HAND_SWORD, new Item(1317), 84, 3)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.AXE, new Item(1357), 71, 1),
            new SmithSlot(Slot.MACE, new Item(1430), 72, 1),
            new SmithSlot(Slot.WARHAMMER, new Item(1345), 79, 3),
            new SmithSlot(Slot.BATTLE_AXE, new Item(1371), 80, 3),
            new SmithSlot(Slot.CLAWS, new Item(3100), 83, 2)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.CHAIN_BODY, new Item(1111), 81, 3),
            new SmithSlot(Slot.PLATE_LEGS, new Item(1073), 86, 3),
            new SmithSlot(Slot.PLATE_SKIRT, new Item(1091), 86, 3),
            new SmithSlot(Slot.PLATE_BODY, new Item(1123), 88, 5)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.MEDIUM_HELM, new Item(1145), 73),
            new SmithSlot(Slot.FULL_HELM, new Item(1161), 77, 2),
            new SmithSlot(Slot.SQUARE_SHIELD, new Item(1183), 78, 2),
            new SmithSlot(Slot.KITE_SHIELD, new Item(1199), 82, 3),
            new SmithSlot(Slot.NAILS, new Item(4823, 15), 74)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.DART_TIPS, new Item(823, 10), 74),
            new SmithSlot(Slot.ARROWTIPS, new Item(43, 15), 75),
            new SmithSlot(Slot.THROWING_KNIVES, new Item(867, 5), 77)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.BOLTS, new Item(9380, 10), 73),
            new SmithSlot(Slot.LIMBS, new Item(9429), 76)
        }
    ),
    RUNITE(new Item(2363),
        new SmithSlot[]{
            new SmithSlot(Slot.DAGGER, new Item(1213), 85),
            new SmithSlot(Slot.SWORD, new Item(1289), 89),
            new SmithSlot(Slot.SCIMITAR, new Item(1333), 90),
            new SmithSlot(Slot.LONG_SWORD, new Item(1303), 91, 2),
            new SmithSlot(Slot.TWO_HAND_SWORD, new Item(1319), 99, 3)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.AXE, new Item(1359), 86),
            new SmithSlot(Slot.MACE, new Item(1432), 87),
            new SmithSlot(Slot.WARHAMMER, new Item(1347), 94, 3),
            new SmithSlot(Slot.BATTLE_AXE, new Item(1373), 95, 3),
            new SmithSlot(Slot.CLAWS, new Item(3101), 98, 2)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.CHAIN_BODY, new Item(1113), 96, 3),
            new SmithSlot(Slot.PLATE_LEGS, new Item(1079), 99, 3),
            new SmithSlot(Slot.PLATE_SKIRT, new Item(1093), 99, 3),
            new SmithSlot(Slot.PLATE_BODY, new Item(1127), 99, 5)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.MEDIUM_HELM, new Item(1147), 88),
            new SmithSlot(Slot.FULL_HELM, new Item(1163), 92, 2),
            new SmithSlot(Slot.SQUARE_SHIELD, new Item(1185), 93, 2),
            new SmithSlot(Slot.KITE_SHIELD, new Item(1201), 97, 3),
            new SmithSlot(Slot.NAILS, new Item(4824, 15), 89, 1)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.DART_TIPS, new Item(824, 10), 89),
            new SmithSlot(Slot.ARROWTIPS, new Item(44, 15), 90),
            new SmithSlot(Slot.THROWING_KNIVES, new Item(868, 5), 92)
        },
        new SmithSlot[]{
            new SmithSlot(Slot.BOLTS, new Item(9381, 10), 88),
            new SmithSlot(Slot.LIMBS, new Item(9431), 91)
        }
    );
    /**
     * The bar for these items
     */
    private final Item bar;
    /**
     * The items for this interface
     */
    private final SmithSlot[][] smithSlots;//Item[][] items;

    /**
     * Represents a smithing interface
     *
     * @param bar The bar
     */
    Smith(Item bar, SmithSlot[]
        ... smithSlots) {
        this.bar = bar;
        this.smithSlots = smithSlots;
    }

    /**
     * The bar for these items
     *
     * @return the bar
     */
    public Item getBar() {
        return bar;
    }

    /**
     * The items for this interface
     *
     * @return the items
     */
    public SmithSlot[][] getSmithSlots() {
        return smithSlots;
    }

    /**
     * Gets the items to show on this interface by bar id
     *
     * @param barId the id of the bar
     * @return the smith
     */
    public static Smith forId(int barId) {
        for (Smith smith : Smith.values()) {
            if (smith.getBar().getId() == barId) {
                return smith;
            }
        }
        return null;
    }

}
