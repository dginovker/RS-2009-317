package org.gielinor.game.content.global.shop;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.global.quest.QuestState;
import org.gielinor.game.content.global.quest.impl.GertrudesCat;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.donor.DonorItem;
import org.gielinor.game.node.entity.player.info.donor.DonorStatus;
import org.gielinor.game.node.item.Item;
import org.gielinor.parser.item.ItemConfiguration;
import plugin.activity.motherloadmine.CoalBagPlugin;
import plugin.activity.motherloadmine.GemBagPlugin;

/**
 * Represents a shop.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 * TODO Convert to SQL
 */
public enum Shops {
    SUMMONING_SHOP,
    
    GRACES_GRACEFUL_CLOTHING {
        @Override
        public boolean value(final Player player, final ShopViewer shopViewer, final Item item, final boolean sell) {
            if (item == null) {
                return true;
            }
            if (sell) {
                player.getActionSender().sendMessage("You cannot sell items to this shop.");
                return true;
            }
            int value = getPrice(item.getId());
            String name = "mark" + (value == 1 ? "" : "s") + " of grace";
            player.getActionSender().sendMessage(item.getName() + ": currently costs " + value + " " + name + ".");
            return true;
        }
        
        @Override
        public boolean buy(Player player, int slot, int amount, Shop shop) {
            if (amount < 0) {
                return true;
            }
            final Item item = shop.getContainer(player, 0).get(slot);
            if (item == null) {
                return true;
            }
            if (item.getCount() < 1) {
                player.getActionSender().sendMessage("There is no stock of that item at the moment.");
                return true;
            }
            if (item.getId() == DonorItem.HELL_KITTEN.getItemId() && player.getQuestRepository().getQuest(GertrudesCat.NAME).getState() != QuestState.COMPLETED) {
                player.getActionSender().sendMessage("You must have completed the quest Gertrude's Cat to buy that.");
                return true;
            }
            if (amount > item.getCount()) {
                amount = item.getCount();
            }
            final Item add = new Item(item.getId(), amount);
            if (player.getInventory().getMaximumAdd(add) < amount) {
                add.setCount(player.getInventory().getMaximumAdd(add));
            }
            if (add.getCount() < 1 || !player.getInventory().hasRoomFor(add)) {
                player.getActionSender().sendMessage("Not enough space in inventory.");
                return true;
            }
            final Item currency = new Item(shop.getCurrency(), add.getCount() * getPrice(add.getId()));
            if (!player.getInventory().contains(currency)) {
                player.getActionSender().sendMessage("You don't have enough marks of grace for that.");
                return true;
            }
            if (player.getInventory().remove(currency)) {
                if (getShop().isGeneral() && !getShop().getContainer(null, 1).contains(add.getId())) {
                    shop.getContainer(player, 0).remove(add);
                    shop.getContainer(player, 0).shift();
                } else {
                    shop.getContainer(player, 0).removeOrZero(new Item(item.getId(), amount));
                }
                player.getInventory().add(add);
                AchievementDiary.finalize(player, AchievementTask.BUY_SHOP);
                shop.update();
            }
            return true;
        }
        
        @Override
        public boolean sell(Player player, int slot, int amount, Shop shop) {
            player.getActionSender().sendMessage("You cannot sell items to this shop.");
            return true;
        }
        
        /**
         * Gets the price for an item.
         * @param itemId The id of the item.
         * @return The price.
         */
        public int getPrice(int itemId) {
            switch (itemId) {
                /**
                 * Graceful hood.
                 */
                case 14730:
                    return 35;
                
                /**
                 * Graceful cape.
                 */
                case 14732:
                    return 40;
                
                /**
                 * Graceful top.
                 */
                case 14734:
                    return 55;
                
                /**
                 * Graceful legs.
                 */
                case 14736:
                    return 60;
                
                /**
                 * Graceful gloves.
                 */
                case 14738:
                    return 30;
                
                /**
                 * Graceful boots.
                 */
                case 14740:
                    return 40;
                
                /**
                 * Amylase pack.
                 */
                case 14747:
                    return 10;
            }
            return -1;
        }
        
    },
    
    COLLECTIONS_AND_COLLECTIBLES,
    
    IRONMAN_FISHING,
    
    IRONMAN_HUNTER,
    
    IRONMAN_HERBLORE,
    
    IRONMAN_CRAFTING,
    
    IRONMAN_AXES,
    
    IRONMAN_GENERAL_STORE,
    AVAS,
    EDGEVILLE_GENERAL_STORE,
    AL_KHARID_GENERAL_STORE,
    VOID_KNIGHT_GENERAL_STORE,
    FALADOR_GENERAL_STORE,
    GUNSLIKS_ASSORTED_ITEMS,
    THE_LIGHTHOUSE_STORE,
    LLETYA_GENERAL_STORE,
    MOON_CLAN_GENERAL_STORE,
    LUMBRIDGE_GENERAL_STORE,
    TRADER_SVENS_BLACK_MARKET_GOODS,
    MISCELLANIAN_GENERAL_STORE,
    RAZMIRE_GENERAL_STORE,
    NARDAH_GENERAL_STORE,
    ARNOLDS_ECLECTIC_SUPPLIES,
    POLLNIVNEACH_GENERAL_STORE,
    PORT_PHASMATYS_GENERAL_STORE,
    RIMMINGTON_GENERAL_STORE,
    SHANTAY_PASS_SHOP,
    OBLIS_GENERAL_STORE,
    JIMINUAS_JUNGLE_STORE,
    BOLKOYS_VILLAGE_SHOP,
    QUARTERMASTERS_STORES,
    VARROCK_GENERAL_STORE,
    TRADER_STANS_TRADING_POST,
    ZANARIS_GENERAL_STORE,
    DAVONS_AMULET_STORE,
    LLETYA_ARCHERY_SHOP,
    VOID_KNIGHT_ARCHERY_STORE,
    BRIANS_ARCHERY_SUPPLIES,
    HICKTONS_ARCHERY_EMPORIUM,
    DARGAUDS_BOWS_AND_ARROWS,
    AARONS_ARCHERY_APPENDAGES,
    CROSSBOW_SHOP_DWARVEN_MINE,
    CROSSBOW_SHOP_KELDAGRIM,
    CROSSBOW_SHOP_WHITE_WOLF_MOUNTAIN,
    BOBS_BRILLIANT_AXES,
    BRIANS_BATTLEAXE_BAZAAR,
    CANDLE_SHOP_TYPE,
    CANDLE_SHOP_TYPE1,
    CHAINMAIL_SHOP,
    FANCY_CLOTHES_STORE,
    THESSALIAS_FINE_CLOTHES,
    FINE_FASHIONS,
    YRSAS_ACCOUTREMENTS,
    BARKERS_HABERDASHERY,
    LLETYA_SEAMSTRESS,
    DODGY_MIKES_SECOND_HANDLOTHING,
    ARHEINS_STORE,
    DWARVEN_SHOPPING_STORE,
    GENERAL_STOREC_CANIFIS_,
    BURTHORPE_SUPPLIES,
    AEMADS_ADVENTURING_SUPPLIES,
    KARAMJA_WINES_SPIRITS_AND_BEERS,
    AGMUNDI_QUALITY_CLOTHES,
    VERMUNDIS_CLOTHES_STALL,
    GRAND_TREE_GROCERIES,
    FRENITAS_COOKERY_SHOP,
    DOMMIKS_CRAFTING_STORE,
    ROMMIKS_CRAFTY_SUPPLIES,
    JAMILAS_CRAFT_STALL,
    VANESSAS_FARMING_SHOP,
    ALICES_FARMING_SHOP,
    SARAHS_FARMING_SHOP,
    WYDINS_FOOD_STORE,
    RUFUSS_MEAT_EMPORIUM,
    SOLIHIBS_FOOD_STALL,
    FRESH_MEAT,
    ARDOUGNE_BAKERS_STALL,
    KEEPA_KETTILONS_STORE,
    GIANNES_RESTAURANT,
    WARRIORS_GUILD_POTION_SHOP,
    WARRIORS_GUILD_FOOD_SHOP,
    WARRIORS_GUILD_ARMOURY,
    HERQUINS_GEMS,
    GEM_MERCHANT,
    GREEN_GEMSTONE_GEMS,
    GEM_TRADER_STALL,
    GEM_TRADER,
    GEM_STORE_MONKEY_COLONY,
    PELTERS_VEG_STALL,
    ARDOUGNE_SILVER_STALL,
    ARDOUGNE_SPICE_STALL,
    SHOP_OF_DISTATE,
    ARDOUGNE_FUR_TRADER,
    PEKSAS_HELMET_SHOP,
    SKULGRIMENS_BATTLE_GEAR,
    TEA_SHOP,
    FRINCOSS_FABULOUS_HERB_STORE,
    GRUDS_HERBLORE_STALL,
    ALECKS_HUNTER_EMPORIUM,
    
    NARDAH_HUNTER_SHOP,
    JEWELLERY_SHOP,
    ALI_DISCOUNT_WARES,
    ALI_THE_KEBAB_SELLER,
    KARIM_KEBAB_SELLER,
    KJUTS_KEBABS,
    MACE_SHOP,
    ALIS_DISCOUNT_WARES_RUNES,
    BETTYS_MAGIC_EMPORIUM,
    AUBURYS_RUNE_SHOP,
    WIZARDS_GUILD__RUNE_STORE,
    BABA_YAGAS_MAGIC_SHOP,
    MAGE_TRAINING_ARENA__REWARDS,
    LUNDAILS_ARENA___SIDE_RUNE_SHOP,
    TUTABS_MAGICAL_MARKET,
    VOID_KNIGHT_MAGIC_STORE,
    TZHAAR__MEJ_ROHS_RUNE_STORE,
    TZHAAR__HUR_LEKS_GEM_STORE,
    MAGIC_GUILD,
    NURMOFS_PICKAXE_SHOP,
    DROGOS_MINING_EMPORIUM,
    PICKAXE_IS_MINE,
    PET_SHOP1,
    ZENESHAS_PLATEBODY_SHOP,
    ARMOUR_SHOP,
    LOUIES_ARMOURED_LEGS_BAZAAR,
    SEDDUS_ADVENTURERS_STORE,
    RANAELS_SUPER_SKIRT_STORE,
    ZEKES_SUPERIOR_SCIMITARS,
    DAGAS_SCIMITAR_SMITHY,
    SHIELD_SHOP,
    NULODIN_SHOP,
    SPICE_SHOP,
    STAFF_SHOP1,
    FORTUNATOS_FINE_WINE,
    GERRANTS_FISHY_BUSINESS,
    HARRYS_FISHING_SHOP,
    BLADES_BY_URBI,
    GAIUS_TWO_HANDED_SHOP,
    GULLUCK_AND_SONS,
    VARROCK_SWORD_SHOP,
    ZEKES_SUPERIOR_SCIMITARS3,
    AUTHENTIC_THROWING_WEAPONS,
    HAPPY_HEROES_HEMPORIUM,
    JUKATS_DRAGON_SWORD_SHOP,
    NARDOKS_BONE_WEAPONS,
    QUALITY_WEAPONS_SHOP,
    QUARTERMASTERS_STORES3,
    DRAYNOR_SEED_MARKET,
    ROMILY_WEAKLAX,
    SLAYER_EQUIPMENT,
    BATTLE_RUNES,
    SMITHING_SMITHS_SHOP,
    TAMAYUS_SPEAR_STALL,
    TZHAAR__HUR_TELS_EQUIPMENT_STORE,
    VIGRS_WARHAMMERS,
    WEAPONS_GALORE,
    LUMBER_YARD_SHOP,
    THE_TOAD_AND_CHICKEN,
    MARTIN_THWAIT,
    DIANGO_TOY_STORE,
    OZIACH,
    SCAVIO,
    VALAINES,
    BEBADIN_VILLAGE_BARTERING,
    IRKSOL,
    MAGE_ARENA_STAVES,
    CULINOMANCER_FOOD,
    CULINOMANCER_ITEMS,
    SIR_TIFFY_CASHIEN,
    RASOLO_THE_WANDERING_MERCHANT,
    RICHARDS_WILDERNESS_CAPE_SHOP,
    BOUNTY_HUNTER_REWARDS,
    JATIXS_HERBLORE_SHOP,
    SUPPLIES_SALESMAN,
    AXES_GALORE,
    AMAZING_MAGIC,
    MISCELLANEOUS,
    MISCELLANEOUS_2,
    HORVIKS_ARMOUR_SHOP,
    LOWES_ARCHERY_EMPORIUM,
    THE_SHRIMP_AND_PARROT,
    MOON_CLAN_FINE_CLOTHES,
    LUNAR_EQUIPMENT,
    CRAFTING_SALESMAN,
    HUNTER_SUPPLIES,
    CUSTOM_FUR_CLOTHING,
    //new Item(7462),
    //new Item(10551),
    LOYALTY_POINT_SHOP,
    
    BLURBERRY_BAR,
    
    DONOR_SHOP {
        @Override
        public String getTitle(Player player) {
            return "Donor Shop (" + player.getDonorManager().getGielinorTokens() + " Gielinor tokens)";
        }
        
        @Override
        public boolean value(final Player player, final ShopViewer shopViewer, final Item item, final boolean sell) {
            if (item == null) {
                return true;
            }
            if (sell) {
                player.getActionSender().sendMessage("You cannot sell items to this shop.");
                return true;
            }
            DonorItem donorItem = DonorItem.forId(item.getId());
            if (donorItem == null) {
                return true;
            }
            int value = donorItem.getCost();
            String name = "Gielinor token" + (value == 1 ? "" : "s");
            String status = "";
            DonorStatus donorStatus = donorItem.getDonorStatus();
            if (donorStatus != null && donorStatus != DonorStatus.NONE) {
                status = " and " + donorStatus.name().toLowerCase().replaceAll("_", " ") + " status";
            }
            player.getActionSender().sendMessage(item.getName() + ": currently costs " + value + " " + name + status + ".");
            return true;
        }
        
        @Override
        public boolean buy(Player player, int slot, int amount, Shop shop) {
            if (amount < 0) {
                return true;
            }
            final Item item = shop.getContainer(player, 0).get(slot);
            if (item == null) {
                return true;
            }
            DonorItem donorItem = DonorItem.forId(item.getId());
            if (donorItem == null) {
                player.getActionSender().sendMessage("You cannot buy that.");
                return true;
            }
            if (player.getDonorManager().getDonorStatus().ordinal() < donorItem.getDonorStatus().ordinal()) {
                boolean last = donorItem.getDonorStatus().ordinal() == DonorStatus.values()[DonorStatus.values().length - 1].ordinal();
                String required = donorItem.getDonorStatus().name().toLowerCase().replace("_", " ") + (last ? " " : " or better ");
                player.getActionSender().sendMessage("You must be a " + required + "to buy that.");
                return true;
            }
            if (item.getCount() < 1) {
                player.getActionSender().sendMessage("There is no stock of that item at the moment.");
                return true;
            }
            if (item.getId() == DonorItem.HELL_KITTEN.getItemId() && player.getQuestRepository().getQuest(GertrudesCat.NAME).getState() != QuestState.COMPLETED) {
                player.getActionSender().sendMessage("You must have completed the quest Gertrude's Cat to buy that.");
                return true;
            }
            if (amount > item.getCount()) {
                amount = item.getCount();
            }
            final Item add = new Item(item.getId(), amount);
            if (player.getInventory().getMaximumAdd(add) < amount) {
                add.setCount(player.getInventory().getMaximumAdd(add));
            }
            if (add.getCount() < 1 || !player.getInventory().hasRoomFor(add)) {
                player.getActionSender().sendMessage("Not enough space in inventory.");
                return true;
            }
            int cost = add.getCount() * donorItem.getCost();
            if (player.getDonorManager().getGielinorTokens() < cost) {
                player.getActionSender().sendMessage("You don't have enough Gielinor tokens for that.");
                return true;
            }
            player.getDonorManager().decreaseGielinorTokens(cost);
            player.getInventory().add(add);
            player.getActionSender().sendString(3901, getTitle(player));
            shop.update();
            return true;
        }
        
        @Override
        public boolean sell(Player player, int slot, int amount, Shop shop) {
            player.getActionSender().sendMessage("You cannot sell items to this shop.");
            return true;
        }
    },
    
    KING_ROALDS_USELESS_JUNK {
        @Override
        public String getTitle(Player player) {
            return "King Roald's Useless Junk (" + player.getSavedData().getGlobalData().getRoaldPoints() + " Kills)";
        }
        
        @Override
        public boolean value(final Player player, final ShopViewer shopViewer, final Item item, final boolean sell) {
            if (item == null) {
                return true;
            }
            if (sell) {
                player.getActionSender().sendMessage("You cannot sell items to this shop.");
                return true;
            }
            boolean unlocked = player.getSavedData().getGlobalData().getUnlockedRoaldsItems().contains(item.getId());
            int value = unlocked ? getPrice(item.getId()) : getKills(item.getId());
            if (value == -1) {
                player.getActionSender().sendMessage("That item is not for sale.");
                return true;
            }
            String name = unlocked ? "coin" + (value == 1 ? "" : "s") : "guard kill" + (value == 1 ? "" : "s");
            player.getActionSender().sendMessage(item.getName() + ": currently costs " + value + " " + name + (unlocked ? "" : " to unlock") + ".");
            return true;
        }
        
        @Override
        public boolean buy(Player player, int slot, int amount, Shop shop) {
            if (amount < 0) {
                return true;
            }
            final Item item = shop.getContainer(player, 0).get(slot);
            if (item == null) {
                return true;
            }
            if (item.getCount() < 1) {
                player.getActionSender().sendMessage("There is no stock of that item at the moment.");
                return true;
            }
//            if (amount > item.getCount()) {
//                amount = item.getCount();
//            }
            final Item add = new Item(item.getId(), amount);
            if (player.getInventory().getMaximumAdd(add) < amount) {
                add.setCount(player.getInventory().getMaximumAdd(add));
            }
            if (add.getCount() < 1 || !player.getInventory().hasRoomFor(add)) {
                player.getActionSender().sendMessage("Not enough space in inventory.");
                return true;
            }
            boolean unlocked = player.getSavedData().getGlobalData().getUnlockedRoaldsItems().contains(item.getId());
            int value = unlocked ? getPrice(item.getId()) : getKills(item.getId());
            if (value == -1) {
                player.getActionSender().sendMessage("That item is not for sale.");
                return true;
            }
            int cost = add.getCount() * value;
            if (unlocked) {
                if (!player.getInventory().contains(new Item(Item.COINS, cost))) {
                    player.getActionSender().sendMessage("You don't have enough coins for that.");
                    return true;
                }
            } else {
                if (player.getSavedData().getGlobalData().getRoaldPoints() < cost) {
                    player.getActionSender().sendMessage("You don't have enough kills for that.");
                    return true;
                }
            }
            boolean canBuy = false;
            if (unlocked) {
                canBuy = player.getInventory().remove(new Item(Item.COINS, cost));
            } else {
                player.getSavedData().getGlobalData().decreaseRoaldPoints(cost);
                canBuy = true;
            }
            if (canBuy) {
                player.getInventory().add(add);
                player.getActionSender().sendString(3901, getTitle(player));
                shop.update();
                if (!unlocked) {
                    player.getActionSender().sendMessage("You can now buy this item with coins!");
                    player.getSavedData().getGlobalData().getUnlockedRoaldsItems().add(item.getId());
                }
            }
            return true;
        }
        
        @Override
        public boolean sell(Player player, int slot, int amount, Shop shop) {
            player.getActionSender().sendMessage("You cannot sell items to this shop.");
            return true;
        }
        
        /**
         * Gets the price for an item after unlocking with kills.
         * @param itemId The id of the item.
         * @return The price.
         */
        public int getPrice(int itemId) {
            switch (itemId) {
                /**
                 * Gloves.
                 */
                case 7453:
                    return 65;
                case 7454:
                    return 130;
                case 7455:
                    return 325;
                case 7456:
                    return 650;
                case 7457:
                    return 1300;
                case 7458:
                    return 1950;
                case 7459:
                    return 3250;
                case 7460:
                    return 6500;
                case 7461:
                    return 130000;
                case 7462:
                    return 130000;
                /**
                 * Other.
                 */
                case 7433:
                    return 45;
                case 7435:
                    return 65;
                case 7437:
                    return 422;
                case 7439:
                    return 2496;
                case 7441:
                    return 2158;
                case 7443:
                    return 4160;
                case 7445:
                    return 18720;
                case 7447:
                    return 10400;
                case 7449:
                    return 53950;
                case 7451:
                    return 33280;
            }
            return -1;
        }
        
        /**
         * Gets the kills to unlock purchasing this item.
         * @param itemId The id of the item.
         * @return The price.
         */
        public int getKills(int itemId) {
            switch (itemId) {
                /**
                 * Gloves.
                 */
                case 7453:
                    return 5;
                case 7454:
                    return 10;
                case 7455:
                    return 17;
                case 7456:
                    return 19;
                case 7457:
                    return 24;
                case 7458:
                    return 28;
                case 7459:
                    return 36;
                case 7460:
                    return 44;
                case 7461:
                    return 50;
                case 7462:
                    return 60;
                /**
                 * Other.
                 */
                case 7433:
                    return 1;
                case 7435:
                    return 3;
                case 7437:
                    return 3;
                case 7439:
                    return 5;
                case 7441:
                    return 6;
                case 7443:
                    return 8;
                case 7445:
                    return 10;
                case 7447:
                    return 11;
                case 7449:
                    return 17;
                case 7451:
                    return 18;
            }
            return -1;
        }
    },
    
    PROSPECTOR_PERCYS_NUGGET_SHOP {
        @Override
        public boolean value(final Player player, final ShopViewer shopViewer, final Item item, final boolean sell) {
            if (item == null) {
                return true;
            }
            if (sell) {
                if (!getShop().itemAllowed(player, item.getId())) {
                    player.getActionSender().sendMessage("You cannot sell this item to this shop.");
                    return true;
                }
                int value = (int) ((double) getPrice(item) * 0.8);
                player.getActionSender().sendMessage(item.getName() + ": shop will buy for " + value + " golden nugget" + (value != 1 ? "s" : "") + ".");
                return true;
            }
            int value = getPrice(item);
            String name = "golden nugget" + (value == 1 ? "" : "s");
            player.getActionSender().sendMessage(item.getName() + ": currently costs " + value + " " + name + ".");
            return true;
        }
        
        @Override
        public boolean buy(Player player, int slot, int amount, Shop shop) {
            if (amount < 0) {
                return true;
            }
            final Item item = shop.getContainer(player, 0).get(slot);
            if (item == null) {
                return true;
            }
            if (item.getCount() < 1) {
                player.getActionSender().sendMessage("There is no stock of that item at the moment.");
                return true;
            }
            if (item.getId() == GemBagPlugin.GEM_BAG && player.hasItem(item)) {
                player.getActionSender().sendMessage("You may only own 1 gem bag at a time.");
                return true;
            }
            if (item.getId() == CoalBagPlugin.COAL_BAG && player.hasItem(item)) {
                player.getActionSender().sendMessage("You may only own 1 coal bag at a time.");
                return true;
            }
            if (amount > item.getCount()) {
                amount = item.getCount();
            }
            final Item add = new Item(item.getId(), amount);
            if (player.getInventory().getMaximumAdd(add) < amount) {
                add.setCount(player.getInventory().getMaximumAdd(add));
            }
            if (add.getCount() < 1 || !player.getInventory().hasRoomFor(add)) {
                player.getActionSender().sendMessage("Not enough space in inventory.");
                return true;
            }
            final Item currency = new Item(shop.getCurrency(), add.getCount() * getPrice(add));
            if (!player.getInventory().contains(currency)) {
                player.getActionSender().sendMessage("You don't have enough golden nuggets for that.");
                return true;
            }
            if (player.getInventory().remove(currency)) {
                shop.getContainer(player, 0).removeOrZero(new Item(item.getId(), amount));
                player.getInventory().add(add);
                AchievementDiary.finalize(player, AchievementTask.BUY_SHOP);
                shop.update();
            }
            return true;
        }
        
        @Override
        public boolean sell(Player player, int slot, int amount, Shop shop) {
            final Item itemToSell = player.getInventory().get(slot);
            if (itemToSell == null || !getShop().itemAllowed(player, itemToSell.getId())) {
                player.getActionSender().sendMessage("You cannot sell this item to this shop.");
                return true;
            }
            int value = (int) ((double) getPrice(itemToSell) * 0.8);
            player.getInventory().remove(itemToSell);
            player.getInventory().add(new Item(shop.getCurrency(), value));
            player.getInventory().refresh();
            shop.update();
            AchievementDiary.finalize(player, AchievementTask.SELL_SHOP);
            return true;
        }
        
        private int getPrice(Item item) {
            return item.getDefinition().getConfiguration(ItemConfiguration.SHOP_PRICE);
        }
        
    },;
    
    /**
     * Represents the shop instance.
     */
    private final Shop shop;
    
    Shops() {
        this.shop = new Shop(this);
    }
    
    /**
     * Method wrapper to open a shop.
     *
     * @param player the player.
     */
    public void open(final Player player) {
        shop.open(player);
    }
    
    /**
     * Method used to get the shop by the npc id.
     *
     * @param id the id.
     * @return the shop.
     */
    public static Shops forId(int id) {
        for (Shops shop : Shops.values()) {
            for (int i = 0; i < shop.getShop().getNpcs().length; i++) {
                if (shop.getShop().getNpcs()[i] == id) {
                    return shop;
                }
            }
        }
        return null;
    }
    
    /**
     * Gets the dynamic title of this shop.
     *
     * @param player The player.
     * @return The title.
     */
    public String getTitle(Player player) {
        return shop.getTitle();
    }
    
    /**
     * Values an item in this shop.
     *
     * @param player     The player.
     * @param shopViewer The {@link org.gielinor.game.content.global.shop.ShopViewer}.
     * @param item       The item.
     * @param sell       If the item is being sold.
     * @return <code>True</code> if this shop was handled.
     */
    public boolean value(final Player player, final ShopViewer shopViewer, final Item item, final boolean sell) {
        return false;
    }
    
    /**
     * Buys an item from this shop.
     *
     * @param player The player.
     * @param slot   The slot.
     * @param amount The amount.
     * @param shop   The shop.
     * @return <code>True</code> if this shop was handled.
     */
    public boolean buy(Player player, int slot, int amount, Shop shop) {
        return false;
    }
    
    /**
     * Sells an item to this shop.
     *
     * @param player The player.
     * @param slot   The slot.
     * @param amount The amount.
     * @param shop   The shop.
     * @return <code>True</code> if this shop was handled.
     */
    public boolean sell(Player player, int slot, int amount, Shop shop) {
        return false;
    }
    
    /**
     * Gets the shop.
     *
     * @return the shop.
     */
    public Shop getShop() {
        return shop;
    }
    
}
