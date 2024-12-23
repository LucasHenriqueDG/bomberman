package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.enums.EliminationType
import me.luhen.bomberman.enums.GameState
import me.luhen.bomberman.events.custom.PlayerLeaveGameEvent
import me.luhen.bomberman.utils.PlaceholderUtils
import me.luhen.bomberman.visual.VisualUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerLeaveGameListener: Listener {

    @EventHandler
    fun onPlayerLeaveGame(event: PlayerLeaveGameEvent) {

        if (event.eliminationType != EliminationType.REGULAR) {
            event.game.gameFile.getLocation("exit")?.let { entrance ->
                event.player.teleport(entrance)
                VisualUtils.sendComponent(Bomberman.instance.messages["leave-game-message"].toString(), event.game.players)
                VisualUtils.sendComponent(Bomberman.instance.messages["leave-game-message"].toString(), event.game.spectators)
                event.game.players.remove(event.player)
            }
        } else {

            var placeholderMessage2: String? = null

            event.game.eliminationsTemp[event.player]?.let { bomber ->
                val placeholdersMessage = PlaceholderUtils.replacePlaceholder(
                    Bomberman.instance.messages["eliminated-message"].toString(),
                    "%player%",
                    event.player.name
                )
                placeholderMessage2 = PlaceholderUtils.replacePlaceholder(
                    placeholdersMessage,
                    "%bomber%",
                    bomber
                )
            }

            event.game.gameFile.getLocation("spectators")?.let { spectators ->
                event.player.teleport(spectators)
                event.game.players.remove(event.player)
                event.game.spectators.add(event.player)
                placeholderMessage2?.let {
                    VisualUtils.sendComponent(it, event.game.players)
                    VisualUtils.sendComponent(it, event.game.spectators)
                }
            }

        }

        when (event.game.status) {

            GameState.WAITING -> {}

            GameState.WARMUP -> {
                val players = event.game.players.size
                val minimumPlayers = event.game.gameFile.getInt("start-with")
                if (players < minimumPlayers) {
                    //Cancel the warmup and go back to waiting status
                    event.game.cancelWarmup()
                    event.game.startWaiting()
                }
            }

            GameState.RUNNING -> {
                val players = event.game.players.size
                if (players == 1) {
                    //Set the winner
                    event.game.finishGame()
                }
            }
            GameState.FINISHING -> {}

        }


    }

}