package me.luhen.bomberman.events.custom

import me.luhen.bomberman.data.Bomb
import me.luhen.bomberman.game.Game
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerPlaceBombEvent(val player: Player, val bomb: Bomb, val game: Game): Event() {

    companion object {

        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList{
            return HANDLERS
        }

    }


    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

}