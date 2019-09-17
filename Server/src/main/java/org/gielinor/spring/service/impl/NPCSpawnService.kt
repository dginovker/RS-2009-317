package org.gielinor.spring.service.impl

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.gielinor.cache.def.impl.NPCDefinition
import org.gielinor.game.node.entity.npc.NPC
import org.gielinor.game.world.map.Direction
import org.gielinor.game.world.map.Location
import org.gielinor.spring.service.SpawnService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Npc Spawn Parser
 * @author Corey
 */
@Service("npcSpawnService")
class NPCSpawnService : SpawnService {
    
    private val log = LoggerFactory.getLogger(NPCSpawnService::class.java)
    private val jsonPath = Paths.get("data", "definition", "npc_spawns.json")!!
    
    override fun loadSpawns() {
        val parser = JsonParser()
        
        Files.newBufferedReader(jsonPath, StandardCharsets.UTF_8).use { reader ->
            val result = parser.parse(reader).asJsonArray
            result.forEach {
                val spawn = it as JsonObject
                
                val npcId = spawn.get("npc_id").asInt
                val definition = NPCDefinition.forId(npcId)
                
                if (definition == null) {
                    log.warn("Missing NPC definition for [{}].", npcId)
                    return@forEach
                }
                
                val location = spawn.get("location").asJsonObject
                val npcLocation = Location.create(location.get("x").asInt, location.get("y").asInt, location.get("z")?.asInt ?: 0)
                
                val direction = Direction.valueOf(spawn.get("face").asString)
                val radius = spawn.get("radius").asInt
                val walks = spawn.get("walks").asBoolean
                
                val npc = NPC.create(npcId, npcLocation)
                npc.walkRadius = radius
                npc.isWalks = walks
                npc.direction = direction
                npc.setAttribute("spawned:npc", true)
                npc.init()
            }
            
            log.info("Parsed {} world NPC spawns", result.count())
        }
    }
}
