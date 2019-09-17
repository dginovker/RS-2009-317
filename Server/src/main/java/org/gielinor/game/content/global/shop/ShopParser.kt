package org.gielinor.game.content.global.shop

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.gielinor.game.node.item.Item
import org.gielinor.util.extensions.toArray
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Class for loading shop definitions from JSON
 * and adding them to the enums found in Shops.
 *
 * @see Shops
 * @see Shop
 *
 * @author Corey
 */
class ShopParser {
    
    companion object {
        /**
         * The logger object for this class.
         */
        val log = LoggerFactory.getLogger(ShopParser::class.java)!!
    
        /**
         * The `HashMap` containing `ShopParser` objects with the `Enum` name as the key.
         * @see HashMap
         * @see ShopParser
         * @see Shops
         * @see Shops.name
         */
        val shops = HashMap<String, ShopParser>()
    
        /**
         * The path of the shop definition Json file.
         * @see Paths
         */
        val JSON_PATH = Paths.get("data", "definition", "shops.json")!!
    
        /**
         * Parses all shop definitions and stores them in the `shops` `HashMap`
         * using the defined Json path.
         * @see shops
         */
        fun loadShops() {
            shops.clear()
            
            val parser = JsonParser()
            Files.newBufferedReader(JSON_PATH, StandardCharsets.UTF_8).use { reader ->
                val result = parser.parse(reader).asJsonObject
                val entrySet = result.entrySet()
                for (entry in entrySet) {
                    try {
                        val key = entry.key
                        val values = entry.value as JsonObject
                        val shop = ShopParser()
                        
                        shop.npcs = values.getAsJsonArray("npcs")?.toList()?.map {
                            Integer.parseInt(it.toString())
                        }?.toIntArray() ?: throw IllegalStateException("Npcs are required")
                        
                        shop.general = values.get("general")?.asBoolean ?: false
                        
                        val accessibility = values.get("accessibility")?.asString ?: Shop.ShopAccessibility.PLAYERS_ONLY.name
                        shop.accessibility = Shop.ShopAccessibility.valueOf(accessibility)
                        
                        shop.currency = values.get("currency")?.asInt ?: -1
                        shop.title = values.get("title")?.asString ?: throw IllegalStateException("Shop title is required")
                        
                        val items =
                            try {
                                toArray(values.getAsJsonArray("items").map {
                                    val id = (it as JsonObject).get("id").asInt
                                    val quantity = it.get("quantity").asInt
                                    return@map Item(id, quantity)
                                }.toList())
                            } catch (e: IllegalStateException) {
                                log.warn("Found no items for [{}], defaulting to general store", key)
                                Shop.GENERAL_STORE_ITEMS
                            }
                        
                        shop.items = items
                        
                        shops.put(key, shop)
                    } catch (e: IllegalStateException) {
                        log.error("Invalid shop definition for [{}]: {}", entry.key, e)
                        continue
                    }
                }
            }
            
            log.info("Loaded ${shops.count()} shops")
        }
    }
    
    /**
     * The title of the shop.
     */
    lateinit var title: String
    
    /**
     * The items the shop sells.
     */
    lateinit var items: Array<Item>
    
    /**
     * Whether or not the shop is a general store
     */
    var general = false
    
    /**
     * The currency the shop uses.
     *
     * A value of -1 indicates a non-item currency shop such as points.
     */
    var currency = -1
    
    /**
     * Represents the category of players which have access to the shop.
     * @see Shop.ShopAccessibility
     */
    lateinit var accessibility: Shop.ShopAccessibility
    
    /**
     * The Npc Ids which are bound to the shop.
     */
    lateinit var npcs: IntArray
    
    /**
     *
     * @throws NoSuchFieldException If no entry in the `HashMap` is found
     * @see shops
     */
    @Throws(NoSuchFieldException::class)
    fun get(input: String): ShopParser {
        if (shops.count() == 0) {
            loadShops()
        }
        
        return shops[input] ?: throw NoSuchFieldException("Shop '$input' not found in $JSON_PATH")
    }
    
}
