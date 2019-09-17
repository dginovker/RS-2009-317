package bot.commands;

import bot.AccountLinkage
import bot.GielinorDiscordRoles
import de.btobastian.javacord.entities.Server
import de.btobastian.javacord.entities.User
import de.btobastian.javacord.entities.permissions.impl.ImplRole
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import org.gielinor.database.DataSource
import org.gielinor.database.loadVariableFromDatabase
import org.gielinor.database.performDatabaseFunction

class UpdateRankCommand : CommandExecutor {

    @Command(aliases = arrayOf("::updaterank"), description = "Updates a Discord user's rank and forums rank by comparing their forums account (requires ::verify) with their current Discord rank and determining which is superior.")
    fun onUpdateRankCommand(user: User, server: Server): String {
        val pidn = AccountLinkage.getPidnFromDatabase(user)
        if (pidn != null) {
            val primaryGroup = DataSource.getForumConnection().loadVariableFromDatabase("SELECT member_group_id FROM NEWcore_members WHERE member_id=$pidn") as Int?
            val gielinorDiscordRole = primaryGroup?.let { GielinorDiscordRoles.forForumsId(it) }
            val userRoles = user.getRoles(server).toMutableList()
            val currentDiscordRole = GielinorDiscordRoles.forDiscordId(userRoles[userRoles.size - 1].id)
            var superiorRole: GielinorDiscordRoles? = null
            if (gielinorDiscordRole != null && currentDiscordRole != null) {
                superiorRole = if (gielinorDiscordRole.ordinal < currentDiscordRole.ordinal) gielinorDiscordRole else currentDiscordRole
            }
            if (superiorRole != null && superiorRole.discordId != currentDiscordRole?.discordId) {
                val role = server.getRoleById(superiorRole.discordId) as ImplRole?
                if (role != null && !role.users.contains(user)) {
                    role.addUser(user)
                    if (superiorRole.forumsId != null) DataSource.getForumConnection().performDatabaseFunction("UPDATE NEWcore_members SET member_group_id=${superiorRole.forumsId} WHERE member_id=$pidn")
                    return "You have now been assigned ${role.name} to your Discord and forums account! \uD83C\uDF89"
                }
            }
            return "No new ranks to add! \uD83D\uDE26"
        }
        return "Your account isn't linked to a forums account! <:PogChamp:307458204340584460>\nDo ::verify and follow the directions!"
    }

}