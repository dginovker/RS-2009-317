package org.gielinor.game.system.command.impl

import org.apache.commons.lang3.StringUtils
import org.gielinor.cache.def.impl.NPCDefinition
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.entity.player.info.Rights
import org.gielinor.game.system.command.Command
import org.gielinor.game.system.command.CommandDescription
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import plugin.npc.AutoSpawnNPC

/**
 * Spawns an NPC under the player.
 *
 * @author [Gielinor Logan G.](https://Gielinor.org)
 */
class SpawnNPCCommand : Command() {

    val log = LoggerFactory.getLogger(javaClass)

    override fun getRights(): Rights {
        return Rights.GIELINOR_MODERATOR
    }

    override fun canUse(player: Player): Boolean {
        return true
    }

    override fun getCommands(): Array<String> {
        return arrayOf("npc")
    }

    override fun init() {
        CommandDescription
            .add(CommandDescription("npc", "Spawns a temporary NPC", rights, "::npc <lt>npc_id>"))
    }

    override fun execute(player: Player, args: Array<String>) {
        if (args.size != 2) {
            player.actionSender.sendMessage("Use as ::npc <lt>npc id> (or npc name)")
            return
        }
        var def: NPCDefinition?
        val name = toString(args, 1)
        try {
            def = if (StringUtils.isNumeric(args[1]))
                NPCDefinition.forId(Integer.parseInt(args[1]))
            else
                NPCDefinition.forName(name)
            if (def == null) {
                def = NPCDefinition(Integer.parseInt(args[1]))
                def.id = Integer.parseInt(args[1])
                def.size = 1
                def.name = "Spawned NPC"
                def.examine = "An NPC spawned."
                NPCDefinition.getDefinitions().put(def.id, def)
            }
            // if (def.getSize() > 1 && player.getRights() !=
            // Rights.GIELINOR_MODERATOR) {
            // return;
            // }
            val autoSpawnNPC = AutoSpawnNPC(def.id, player.location, 6)
            // if ((StringUtils.isNumeric(args[1])) && Integer.parseInt(args[1])
            // > 30000) {
            // autoSpawnNPC.setAttribute("PET_OWNER", 30000 + player.getPidn());
            // }
            autoSpawnNPC.setTeleportTarget(autoSpawnNPC.location)
            autoSpawnNPC.setAttribute("RESPAWN_NPC", false)
            autoSpawnNPC.isRespawn = true
            autoSpawnNPC.isWalks = true
            autoSpawnNPC.walkRadius = 3
            autoSpawnNPC.init()
            if (!StringUtils.isNumeric(args[1])) {
                player.actionSender.sendMessage("Spawned NPC #" + autoSpawnNPC.id + " by name.")
            }
            /*try {
                DataSource.getGameConnection().use { connection ->
                    connection.prepareStatement(
                            "INSERT INTO npc_spawn (npc_id, x, y, z, walks, radius, face, description) VALUES(?, ?, ?, ?, ?, ?, ?, ?)").use { preparedStatement ->
                        preparedStatement.setInt(1, autoSpawnNPC.getId())
                        preparedStatement.setInt(2, autoSpawnNPC.getLocation().getX())
                        preparedStatement.setInt(3, autoSpawnNPC.getLocation().getY())
                        preparedStatement.setInt(4, autoSpawnNPC.getLocation().getZ())
                        preparedStatement.setBoolean(5, autoSpawnNPC.isWalks())
                        preparedStatement.setInt(6, if (autoSpawnNPC.isWalks()) 3 else 0)
                        preparedStatement.setInt(7, autoSpawnNPC.getDirection().ordinal)
                        preparedStatement.setString(8, autoSpawnNPC.getName().toUpperCase() + "_AUTOSPAWN")
                        preparedStatement.execute()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                player.dialogueInterpreter.sendPlaneMessage("Could not place NPC!")
            }

            player.dialogueInterpreter.sendPlaneMessage("Placed NPC successfully.")*/
        } catch (e: Exception) {
            log.error("NPC spawn error", e)
        }
    }
}
