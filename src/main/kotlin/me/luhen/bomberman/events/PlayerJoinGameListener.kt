package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.enums.GameState
import me.luhen.bomberman.events.custom.PlayerJoinGameEvent
import me.luhen.bomberman.visual.VisualUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerJoinGameListener: Listener {

    @EventHandler
    fun onPlayerJoinGame(event: PlayerJoinGameEvent){

        println("Attempting to make player join game...")

        event.player.teleport(event.game.entrance)
        event.game.players.add(event.player)
        Bomberman.instance.playersPlaying[event.player] = event.game
        VisualUtils.sendComponent(Bomberman.instance.messages["join-game-message"].toString(), event.player)
        event.game.bossBar.addViewer(event.player)

        println("player successfully joined the game")

        //Check how many players are on the game
        val players = event.game.players.size
        val minimumPlayers = event.game.gameFile.getInt("skip-to-warmup-with")
        if(players >= minimumPlayers && event.game.status == GameState.WAITING){
            event.game.startWarmup()
        }

    }

}