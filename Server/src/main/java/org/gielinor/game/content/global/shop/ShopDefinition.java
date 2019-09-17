package org.gielinor.game.content.global.shop;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a {@link org.gielinor.game.content.global.shop.Shop} definition.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ShopDefinition {

    /**
     * The {@link java.util.Map} of {@link org.gielinor.game.content.global.shop.Shop}s.
     */
    private static final Map<Integer, Shop> SHOPS = new HashMap<>();

    /**
     *
     */

    /**
     * Gets the {@link org.gielinor.game.content.global.shop.Shop} {@link java.util.Map}.
     *
     * @return The list of shops.
     */
    public static Map<Integer, Shop> getShops() {
        return SHOPS;
    }
}
