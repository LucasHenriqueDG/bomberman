package me.luhen.bomberman.mechanics

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.events.custom.PlayerJoinGameEvent
import me.luhen.bomberman.game.Game
import me.luhen.bomberman.visual.VisualUtils
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player

object PlayerManagement {

    fun addPlayerToQueue(player: Player){
        Bomberman.instance.playersOnQueue.add(player)
        VisualUtils.sendComponent(Bomberman.instance.messages["player-join-queue-message"].toString(), player)
    }

    fun removeFromArena(game: Game, player: Player){
        game.gameFile.getLocation("exit")?.let { exit ->
            game.bombermans.remove(player)
            player.inventory.clear()
            player.teleport(exit)
            player.gameMode = GameMode.SURVIVAL
            Bomberman.instance.playersPlaying.remove(player)
        }
    }

    fun movePlayersFromQueue(game: Game){
        Bomberman.instance.playersOnQueue.forEach{ queuePlayer ->
            Bukkit.getPluginManager().callEvent(PlayerJoinGameEvent(queuePlayer, game))
        }
    }

}