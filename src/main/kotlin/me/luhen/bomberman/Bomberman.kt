package me.luhen.bomberman

import me.luhen.bomberman.commands.BombermanCommand
import me.luhen.bomberman.events.*
import me.luhen.bomberman.game.Game
import me.luhen.bomberman.utils.DataUtils
import me.luhen.bomberman.visual.GameScoreboard
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Bomberman : JavaPlugin() {

    var audience: BukkitAudiences? = null
    val messages = mutableMapOf<String, String>()
    val currentGames = mutableListOf<Game>()
    val gameFiles = mutableMapOf<String, Boolean>()
    val playersOnQueue = mutableListOf<Player>()
    val playersPlaying = mutableMapOf<Player, Game>()
    var isRunning = true
    val scoreboards = mutableMapOf<Player, GameScoreboard>()
    val allowedCommands = mutableListOf<String>()

    companion object{
        lateinit var instance: Bomberman
    }

    init {
        instance = this
    }


    override fun onEnable() {

        audience = BukkitAudiences.create(this)

        saveDefaultConfig()

        DataUtils.updateGameFiles()
        DataUtils.updateMessages()

        getCommand("bomberman")?.setExecutor(BombermanCommand)

        server.pluginManager.registerEvents(InteractionListener(), this)
        server.pluginManager.registerEvents(PickupItemListener(), this)
        server.pluginManager.registerEvents(PlayerJoinGameListener(), this)
        server.pluginManager.registerEvents(PlayerLeaveGameListener(), this)
        server.pluginManager.registerEvents(PlayerQuitListener(), this)
        server.pluginManager.registerEvents(DropPerkListener(), this)
        server.pluginManager.registerEvents(PlayerPlaceBombListener(), this)
        server.pluginManager.registerEvents(TriggerBombListener(), this)
        server.pluginManager.registerEvents(CancelInventoryListener(), this)
        server.pluginManager.registerEvents(CommandListener(), this)
        server.pluginManager.registerEvents(PlayerDamageListener(), this)
        server.pluginManager.registerEvents(PlayerDropItemListener(), this)


    }

    override fun onDisable() {
    }
}
