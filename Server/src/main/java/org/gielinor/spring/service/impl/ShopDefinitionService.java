package org.gielinor.spring.service.impl;

import org.gielinor.spring.service.DefinitionService;
import org.springframework.stereotype.Service;

/**
 * Represents the {@link org.gielinor.spring.service.DefinitionService} for a {@link org.gielinor.game.content.global.shop.ShopDefinition}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
@Service("shopService")
public class ShopDefinitionService implements DefinitionService {

    @Override
    public void initializeDefinitions() {
//        List<ShopRecord> shopRecords = serverDSL.select().from(Tables.SHOP).fetchInto(ShopRecord.class);
//        //String title, Item[] items, int[] npcs, boolean general, int currency
//        for (ShopRecord shopRecord : shopRecords) {
//            int[] npcs = shopRecord.getNpcs() == null ? new int[]{} : TextUtils.toIntArray(shopRecord.getNpcs().split(":"));
//            ShopDefinition.getShops().put(shopRecord.getId(), new Shop(shopRecord.getName(), null, npcs,
//                    shopRecord.getGeneralStore() == 1, shopRecord.getCurrency(),
//                    shopRecord.getType() == ShopType.REGULAR ? 0 : shopRecord.getType() == ShopType.IRON_MAN ? 1 : 2));
//        }
//        List<ShopInventoryRecord> shopInventoryRecords = serverDSL.select().from(Tables.SHOP_INVENTORY).fetchInto(ShopInventoryRecord.class);
//        for (ShopInventoryRecord shopInventoryRecord : shopInventoryRecords) {
//            Shop shop = ShopDefinition.getShops().get(shopInventoryRecord.getId());
//
//        }
    }
}
