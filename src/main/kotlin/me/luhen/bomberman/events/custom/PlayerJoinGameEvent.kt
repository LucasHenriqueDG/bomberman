package me.luhen.bomberman.events.custom

import me.luhen.bomberman.game.Game
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerJoinGameEvent(val player: Player, val game: Game) : Event() {

    companion object {
        private val handlers = HandlerList()

        fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    override fun getHandlers(): HandlerList {
        val handler = getHandlerList()
        return handler
    }
}