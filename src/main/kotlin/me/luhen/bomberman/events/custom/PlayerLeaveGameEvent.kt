package me.luhen.bomberman.events.custom

import me.luhen.bomberman.enums.EliminationType
import me.luhen.bomberman.game.Game
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerLeaveGameEvent(val player: Player, val game: Game, val eliminationType: EliminationType) : Event() {

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