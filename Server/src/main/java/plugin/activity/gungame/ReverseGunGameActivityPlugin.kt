package plugin.activity.gungame

import org.gielinor.cache.def.impl.ItemDefinition
import org.gielinor.game.component.Component
import org.gielinor.game.content.activity.ActivityPlugin
import org.gielinor.game.content.skill.Skills
import org.gielinor.game.interaction.Option
import org.gielinor.game.node.Node
import org.gielinor.game.node.`object`.GameObject
import org.gielinor.game.node.entity.Entity
import org.gielinor.game.node.entity.combat.CombatStyle
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface
import org.gielinor.game.node.entity.impl.Animator
import org.gielinor.game.node.entity.lock.Lock
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.entity.player.link.Sidebar
import org.gielinor.game.node.entity.player.link.SpellBookManager
import org.gielinor.game.node.item.Item
import org.gielinor.game.world.GameConstants
import org.gielinor.game.world.World
import org.gielinor.game.world.map.Location
import org.gielinor.game.world.map.zone.ZoneBorders
import org.gielinor.net.packet.OperationCode
import org.gielinor.rs2.pulse.Pulse
import org.gielinor.util.extensions.color
import org.gielinor.util.extensions.highlight
import org.gielinor.util.extensions.next
import org.gielinor.utilities.string.TextUtils
import plugin.interaction.inter.EquipmentInterface
import java.util.*
import kotlin.collections.HashMap

/**
 * Constructs a new `ActivityPlugin` `Object`.
 */
class ReverseGunGameActivityPlugin : ActivityPlugin("Reverse gun game", false, true, true) {

    companion object {
        const val ENTRY_COST = 25000000
        val ENTRY_DIALOGUE = GunGameEntryDialogue()

        fun hasEntryCost(entryCost: Int, player: Player): Boolean {
            return player.inventory.contains(Item.COINS, entryCost)
                || player.bank.contains(Item.COINS, entryCost)
        }
    }

    private val INTEFACE_ID = 21346
    private val lobbyPlayers: ArrayList<Player> = arrayListOf()
    private val playingPlayers: ArrayList<Player> = arrayListOf()
    private val previousSessionPlayers: ArrayList<Player> = arrayListOf()
    private val killStreaks: HashMap<String, Int> = hashMapOf();

    private val lobbyZoneBorder = ZoneBorders(2523, 3373, 2533, 3377)
    private val playingZoneBorder = ZoneBorders(2501, 3356, 2533, 3387)
    private val spawnZoneBorder = ZoneBorders(2507, 3374, 2519, 3376)

    private val minutesPerGame = if (World.getConfiguration().isDevelopmentEnabled) 1 else 3
    private val minimumPlayers = if (World.getConfiguration().isDevelopmentEnabled) 1 else 2
    private val pulseTime = if (World.getConfiguration().isDevelopmentEnabled) 20 else 100
    private val respawnTime = if (World.getConfiguration().isDevelopmentEnabled) 1 else 1

    private val formattedEntryCost = "${TextUtils.getFormattedNumber(ReverseGunGameActivityPlugin.ENTRY_COST)} GP"

    private var minutes = 0
    private var lobbyJackpot = 0
    private var playingJackpot = 0

    private val gamePulse = object : Pulse(pulseTime) {
        override fun pulse(): Boolean {
            when (++minutes) {
                minutesPerGame -> checkGameSession()
                else -> lobbyPlayers.forEach { sendMinutesLeft(it) }
            }
            playingPlayers.forEach { updateGameInterface(it) }
            return false
        }
    }

    private fun updateGameInterface(player: Player) {
        val gunGameStage = player.savedData.activityData.gunGameStage
        player.actionSender.sendString(21349, "Your wave is " + gunGameStage.name.toLowerCase().replace("_", " ").trim())
    }

    private fun openOverlay(player: Player) {
        val current = player.interfaceState.overlay

        if (Objects.isNull(current))
            player.interfaceState.openOverlay(Component(INTEFACE_ID))
    }

    private fun clearOverlay(player: Player) {
        for (i in 21347..21355) {
            player.actionSender.sendString("", i)
        }
    }

    private fun sendMinutesLeft(player: Player) {
        player.actionSender.sendString(21349, "${minutesPerGame - minutes} more minutes left.")
        player.actionSender.sendMessage("Approximately ${minutesPerGame - minutes} minutes left till the next massacre.")
    }

    override fun actionButton(player: Player, interfaceId: Int, buttonId: Int, slot: Int, itemId: Int, opcode: Int): Boolean {
        EquipmentInterface.EQUIPMENT_INTERFACES
            .filter { interfaceId == it }
            .forEach {
                if (opcode == OperationCode.OPTION_OFFER_ONE) {
                    player.actionSender.sendMessage("You can't unequip items in this minigame!")
                }
            }
        return super.actionButton(player, interfaceId, buttonId, slot, itemId, opcode)
    }

    /**
     * Checks to see if the game is done. If not, it waits a minute.
     */
    private fun checkGameSession() {
        fun waitMinute() {
            minutes = minutesPerGame - 1
        }

        fun resetMinute() {
            minutes = 0
        }
        if (playingPlayers.size > 1) {
            lobbyPlayers.sendMessage("The current game session is not finished. Waiting 1 more minute.")
            waitMinute()
            return
        } else if (lobbyPlayers.size < minimumPlayers && lobbyPlayers.size != 0) {
            lobbyPlayers.sendMessage("Too little players to start a game. Waiting 1 more minute.")
            waitMinute()
            return
        } else if (lobbyPlayers.size == 0) {
            resetMinute()
            return
        }
        startGameSession()
        resetMinute()
    }

    private fun startGameSession() {
        playingPlayers.addAll(lobbyPlayers)
        previousSessionPlayers.addAll(lobbyPlayers)
        lobbyPlayers.clear()
        playingPlayers.forEach { player ->
            player.savedData.activityData.gunGameStage = GunGameStage.values()[0]
            openOverlay(player)
            clearOverlay(player)
            convertToNormalSpellbook(player)
            takeCoins(player) // make sure this is above any call to remove inventory (such as {@code applyGunGameStage()})
            spawn(player)
        }
        playingJackpot = lobbyJackpot
        lobbyJackpot = 0
        playingPlayers.sendMessage("The massacre has.. BEGUN!")
    }

    private fun convertToNormalSpellbook(player: Player) {
        player.setAttribute("previous-spell-book", player.spellBookManager.spellBook)
        player.spellBookManager.setSpellBook(SpellBookManager.SpellBook.MODERN)
        player.interfaceState.openTab(Sidebar.MAGIC_TAB, Component(SpellBookManager.SpellBook.MODERN.interfaceId))
    }

    private fun revertToOldSpellbook(player: Player) {
        val oldSpellbook: Int = player.getAttribute("previous-spell-book")
        player.spellBookManager.setSpellBook(SpellBookManager.SpellBook.forInterface(oldSpellbook))
        player.interfaceState.openTab(Sidebar.MAGIC_TAB, Component(oldSpellbook))
    }

    private fun giveStatBoost(player: Player) {
        for (i in Skills.ATTACK..Skills.SUMMONING) {
            val level = player.skills.getStaticLevel(i)
            val boost = 99 - level
            player.skills.updateLevel(i, boost, level + boost)
        }
    }

    private fun takeCoins(player: Player) {
        if (player.inventory.contains(Item.COINS, ReverseGunGameActivityPlugin.ENTRY_COST)) {
            player.inventory.remove(Item(Item.COINS, ENTRY_COST))
            player.actionSender.sendMessage("$formattedEntryCost has been taken from your inventory.")
            val coinsAmount = player.inventory.getCount(Item.COINS)
            if (coinsAmount > 0) {
                val coins = Item(Item.COINS, coinsAmount)
                player.bank.add(coins)
                player.inventory.remove(coins)
                player.actionSender.sendMessage("The amount of coins left in your inventory have been moved to your bank.")
            }
        } else if (player.bank.contains(Item.COINS, ReverseGunGameActivityPlugin.ENTRY_COST)) {
            player.bank.remove(Item(Item.COINS, ENTRY_COST))
            player.actionSender.sendMessage("$formattedEntryCost has been taken from your bank.")
        }
    }

    private fun decrementGunGameStage(player: Player) {
        player.savedData.activityData.gunGameStage = player.savedData.activityData.gunGameStage.next()
        applyGunGameStage(player)
    }

    private fun applyGunGameStage(player: Player) {
        val gunGameStage = player.savedData.activityData.gunGameStage
        clearInventoryAndEquipment(player)
        player.unlock()
        gunGameStage.equipment.forEach { item ->
            player.equipment.add(item, item.slot, true, false, -1, true)
            ItemDefinition.statsUpdate(player)
        }
        player.locks.equipmentLock = Lock()
        player.locks.equipmentLock.lock()
        gunGameStage.inventory?.forEach { player.inventory.add(it) }

        if (gunGameStage.autocastSpell != null) {
            player.getExtension<WeaponInterface>(WeaponInterface::class.java).selectAutoSpell(gunGameStage.autocastSpell.buttonId, true)
            player.actionSender.sendMessage("Your autocast has been set to ${TextUtils.formatDisplayName(gunGameStage.autocastSpell.name)}.")
        }
        updateGameInterface(player)
    }

    private fun makeAttackable(player: Player) {
        player.skullManager.isSkullCheckDisabled = true
        player.skullManager.isWilderness = true
        player.interaction.set(Option._P_ATTACK)
    }

    private fun makeUnattackable(player: Player) {
        player.skullManager.isSkullCheckDisabled = false
        player.skullManager.isWilderness = false
        player.interaction.remove(Option._P_ATTACK)
    }

    override fun newInstance(p: Player): ActivityPlugin {
        return this
    }

    override fun getSpawnLocation(): Location {
        return Location.getRandomLocation(lobbyZoneBorder)
    }

    private fun getRandomStartLocation(): Location {
        return Location.getRandomLocation(spawnZoneBorder)
    }

    override fun configure() {
        register(playingZoneBorder)
        World.submit(gamePulse)
    }

    override fun enter(entity: Entity): Boolean {
        if (entity is Player) {
            if (!entity.getAttribute("reverse_gun_game", false)) {
                return false
            }
            addLobbyPlayer(entity)
        }
        return super.enter(entity)
    }

    override fun leave(entity: Entity, logout: Boolean): Boolean {
        if (playingPlayers.contains(entity) && entity is Player) {
            playingPlayers.remove(entity)
            resetPlayer(entity)
            entity.actionSender.sendMessage("You have been removed from the game!")
            playingPlayers.sendMessage("${TextUtils.formatDisplayName(entity.username)} has been eliminated from the game!")
            if (playingPlayers.size == 1) {
                val winner = playingPlayers[0]
                declareWinner(winner)
            }
        } else if (lobbyPlayers.contains(entity) && entity is Player) {
            removeLobbyPlayer(entity)
        }
        if (logout) {
            entity.location = GameConstants.HOME_LOCATION
        }
        if (entity is Player)
            entity.asPlayer().interfaceState.closeOverlay()
        /*if (entity is Player) {
            entity.appearance.skullIcon = -1
            entity.updateMasks.register(AppearanceFlag(player))
        }*/
        return super.leave(entity, logout)
    }

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        if (target is GameObject) {
            if (target.definition.configurations["option:open"] != null || target.definition.name == "Gate") { // make doors/gates not open
                return true
            }
        }
        return false
    }

    private fun addLobbyPlayer(player: Player) {
        lobbyPlayers.add(player)
        lobbyJackpot += ENTRY_COST
        sendMinutesLeft(player)
        openOverlay(player)
        clearOverlay(player)
        player.actionSender.sendMessage("You have been put in the waiting room for the next massacre.")
        lobbyPlayers.sendMessage("The potential jackpot is now ${TextUtils.format(lobbyJackpot.toLong())} GP.")
    }

    private fun removeLobbyPlayer(player: Player) {
        lobbyPlayers.remove(player)
        lobbyJackpot -= ENTRY_COST
        lobbyPlayers.sendMessage("The potential jackpot is now ${TextUtils.format(lobbyJackpot.toLong())} GP.")
    }

    private fun declareWinner(winner: Player) {
        previousSessionPlayers.sendMessage("${TextUtils.formatDisplayName(winner.username)} has won the $name!".highlight())
        endGameSession(winner)
    }

    private fun endGameSession(winner: Player) {
        playingPlayers.forEach { player ->
            resetPlayer(player)
            if (hasEntryCost(ENTRY_COST, player)) {
                player.actionSender.sendMessage("You have enough money for the next massacre, so we have put you in it.")
                addLobbyPlayer(player)
                player.setAttribute("reverse_gun_game", true)
                player.teleport(spawnLocation)
            } else {
                player.actionSender.sendMessage("You don't seem to have enough money for the next massacre. You've been teleported home.")
                player.properties.teleportLocation = GameConstants.HOME_LOCATION
            }
            player.unlock()
            clearOverlay(player)
            player.actionSender.sendString(21349, "Winner is " + winner.name)
        }

        giveReward(winner)
        playingPlayers.clear()
        previousSessionPlayers.clear()
        playingJackpot = 0
        minutes = 0
    }

    private fun resetPlayer(player: Player) {
        clearInventoryAndEquipment(player)
        player.skills.restore()
        player.savedData.activityData.gunGameStage = null
        player.setAttribute("reverse_gun_game", false)
        revertToOldSpellbook(player)
        makeUnattackable(player)
    }

    private fun giveReward(winner: Player) {

        playingJackpot = (playingJackpot * 0.9).toInt()

        winner.inventory.add(Item(Item.COINS, playingJackpot))
        winner.dialogueInterpreter.sendDialogue("The gnomes take 10% to pay for the items you used!", "Repairs cost you know!")

        previousSessionPlayers.sendMessage("${TextUtils.formatDisplayName(winner.username)} won ${TextUtils.format(playingJackpot.toLong())} GP!".color("FF0000"))
    }

    override fun start(player: Player, login: Boolean, vararg args: Any): Boolean {
        lobbyPlayers.forEach {
            if (it.details.macAddress == player.details.macAddress && !World.getConfiguration().isDevelopmentEnabled) {
                player.actionSender.sendMessage("You can't play this minigame on two accounts!")
                return false
            }
        }
        player.properties.teleportLocation = spawnLocation
        player.setAttribute("reverse_gun_game", true)
        return true
    }

    override fun continueAttack(e: Entity, target: Node, style: CombatStyle, message: Boolean): Boolean {
        if (e is Player && target is Player) {
            if (inPlayingZone(e) && inPlayingZone(target) && playingPlayers.contains(e) && playingPlayers.contains(target)) {
                return true
            }
        }
        return false
    }

    override fun death(entity: Entity, killer: Entity): Boolean {
        if (entity is Player && killer is Player) {
            addRespawnPulse(entity)
            val lastImpactEntity = entity.impactHandler.lastImpactEntity
            if (lastImpactEntity != null && lastImpactEntity is Player) {
                handleKiller(lastImpactEntity)
            } else
                handleKiller(killer)

            updateGameInterface(entity)
        }
        return false
    }

    private fun handleKiller(killer: Player) {

        killStreaks.putIfAbsent(killer.username, 0)
        killStreaks.merge(killer.username, 1, { oldValue, one -> oldValue + one })

        val topPlayers = killStreaks.values.stream()
            .sorted { o1, o2 -> o1 - o2 }
            .limit(3)
            .toArray<String>({ length -> arrayOfNulls(length) })

        playingPlayers.map { player -> player.actionSender }.forEach { actionSender ->
            run {
                for (i in topPlayers.indices) {

                    val name = topPlayers[i]

                    actionSender.sendString(21354 - i, ""+killStreaks.getOrDefault(name, 0))

                    if (i == 0) actionSender.sendString("#1: " + name, 21347)
                    if (i == 1) actionSender.sendString("#2:" + name, 21348)
                    if (i == 2) actionSender.sendString("#3:" + name, 21350)
                }
            }
        }

        if (killer.savedData.activityData.gunGameStage != GunGameStage.lastStage) {
            decrementGunGameStage(killer)
        } else {
            declareWinner(killer)
        }
    }

    private fun addRespawnPulse(player: Player) {
        makeUnattackable(player)
        player.setTeleportTarget(player.location)
        player.lock()
        World.submit(object : Pulse(respawnTime) {
            override fun pulse(): Boolean {
                if (player.savedData.activityData.gunGameStage != null) {
                    player.unlock()
                    player.animator.forceAnimation(Animator.RESET_A)
                    spawn(player)
                }
                return true
            }
        })
    }

    private fun spawn(player: Player) {
        giveStatBoost(player)
        applyGunGameStage(player)
        makeAttackable(player)
        player.teleport(getRandomStartLocation())
    }

    private fun inPlayingZone(player: Player): Boolean {
        return playingZoneBorder.insideBorder(player) && !lobbyZoneBorder.insideBorder(player)
    }

    private fun ArrayList<Player>.sendMessage(message: String) {
        val msg = if (message.contains("<col=")) message else message.color("FF0000")
        forEach { it.actionSender.sendMessage(msg.color("FF0000")) }
    }

    private fun clearInventoryAndEquipment(player: Player) {
        player.inventory.clear()
        player.equipment.clear()
        player.appearance.sync()
    }

    override fun canUsePrayer(): Boolean {
        return false
    }
}
