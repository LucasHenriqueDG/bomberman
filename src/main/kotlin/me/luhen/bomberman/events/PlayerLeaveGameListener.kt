package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.enums.EliminationType
import me.luhen.bomberman.enums.GameState
import me.luhen.bomberman.events.custom.PlayerLeaveGameEvent
import me.luhen.bomberman.game.Game
import me.luhen.bomberman.mechanics.PlayerManagement
import me.luhen.bomberman.utils.DataUtils
import me.luhen.bomberman.utils.PlaceholderUtils
import me.luhen.bomberman.utils.Utils
import me.luhen.bomberman.visual.VisualUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerLeaveGameListener: Listener {

    @EventHandler
    fun onPlayerLeaveGame(event: PlayerLeaveGameEvent) {

        Bomberman.instance.scoreboards[event.player]?.remove()
        event.player.activePotionEffects.forEach {
            event.player.removePotionEffect(it.type)
        }

        if (event.eliminationType != EliminationType.REGULAR) {
            PlayerManagement.removeFromArena(event.game, event.player)
            val placeholderMessage = PlaceholderUtils.replacePlaceholder(
                Bomberman.instance.messages["leave-game-message"].toString(),
                "%player%",
                event.player.name
            )
            VisualUtils.sendComponent(placeholderMessage, event.game.players)
            VisualUtils.sendComponent(placeholderMessage, event.game.spectators)
        } else {

            event.game.bossBar.removeViewer(event.player)

            var placeholderMessage2: String? = null

            event.game.fireBlocksToCheck[Utils.locationInInt(event.player.location)]?.let { bomber ->
                val placeholdersMessage = PlaceholderUtils.replacePlaceholder(
                    Bomberman.instance.messages["eliminated-message"].toString(),
                    "%player%",
                    event.player.name
                )
                placeholderMessage2 = if (bomber.player.name != event.player.name) PlaceholderUtils.replacePlaceholder(
                    placeholdersMessage,
                    "%bomber%",
                    bomber.player.name
                ) else {
                    null
                }

                if(bomber.player != event.player) event.game.bombermans[bomber.player]?.let { it.eliminations += 1 }
                Bomberman.instance.scoreboards[bomber.player]?.update()

            }

            DataUtils.getLocationFromFile(event.game.gameFile, "spectators").let { spectators ->
                println("p1")
                event.player.teleport(spectators)
                event.game.players.remove(event.player)
                event.game.spectators.add(event.player)
                placeholderMessage2?.let {
                    VisualUtils.sendComponent(it, event.game.players)
                    VisualUtils.sendComponent(it, event.game.spectators)
                } ?: temp1(event.player.name, event.game)
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
                println("p2")
                val players = event.game.players.size
                if (players == 1) {
                    println("finishing")
                    //Set the winner
                    event.game.finishGame()
                }
            }
            GameState.FINISHING -> {}

        }

    }

    fun temp1(name: String, game: Game) {
            val placeholderMessage3 = PlaceholderUtils.replacePlaceholder(
                Bomberman.instance.messages["eliminated-itself"].toString(),
                "%player%",
                name
            )
            VisualUtils.sendComponent(placeholderMessage3, game.players)
            VisualUtils.sendComponent(placeholderMessage3, game.spectators)

    }

}