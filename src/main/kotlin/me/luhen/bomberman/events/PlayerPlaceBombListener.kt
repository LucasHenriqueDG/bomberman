package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.events.custom.PlayerPlaceBombEvent
import me.luhen.bomberman.tasks.BombTask
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerPlaceBombListener: Listener {

    @EventHandler
    fun onPlayerPlaceBomb(event: PlayerPlaceBombEvent){

        Bomberman.instance.playersPlaying[event.player]?.let { game ->
            game.bombermans[event.player]?.let { bomberman ->
                if(!bomberman.hasTimer) {
                    BombTask(event.game, event.bomb).apply { runTaskLater(Bomberman.instance, 40L) }
                }

                event.bomb.location.block.type = Material.TNT

                event.game.placedBombs[event.bomb.location] = event.bomb
                event.game.bombermans[event.player]?.let { it.bombs -= 1 }
            }
        }

    }
}