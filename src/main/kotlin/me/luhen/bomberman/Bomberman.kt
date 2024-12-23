package me.luhen.bomberman

import me.luhen.bomberman.commands.BombermanCommand
import me.luhen.bomberman.events.InteractionListener
import me.luhen.bomberman.events.PickupItemListener
import me.luhen.bomberman.events.PlayerJoinGameListener
import me.luhen.bomberman.events.PlayerLeaveGameListener
import me.luhen.bomberman.game.Game
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Bomberman : JavaPlugin() {

    var audience: BukkitAudiences? = null
    val messages = mutableMapOf<String, String>()
    val currentGames = mutableListOf<Game>()
    val gameFiles = mutableMapOf<YamlConfiguration, Boolean>()
    val playersOnQueue = mutableListOf<Player>()
    val playersPlaying = mutableMapOf<Player, Game>()

    companion object{
        lateinit var instance: Bomberman
    }

    init {
        instance = this
    }


    override fun onEnable() {

        audience = BukkitAudiences.create(this)

        saveDefaultConfig()

        val gamesDir = File(dataFolder, "games")
        if(!gamesDir.exists()){
            gamesDir.mkdir()
        }
        gamesDir.listFiles()?.forEach {
            val config = YamlConfiguration.loadConfiguration(it)
            gameFiles[config] = false
        }

        getCommand("bomberman")?.setExecutor(BombermanCommand)

        server.pluginManager.registerEvents(InteractionListener(), this)
        server.pluginManager.registerEvents(PickupItemListener(), this)
        server.pluginManager.registerEvents(PlayerJoinGameListener(), this)
        server.pluginManager.registerEvents(PlayerLeaveGameListener(), this)

    }

    override fun onDisable() {
    }
}
