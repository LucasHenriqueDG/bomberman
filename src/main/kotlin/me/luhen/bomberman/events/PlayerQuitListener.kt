package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.enums.EliminationType
import me.luhen.bomberman.events.custom.PlayerLeaveGameEvent
import me.luhen.bomberman.mechanics.PlayerManagement
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener: Listener {

    @EventHandler
    fun playerQuitEvent(event: PlayerQuitEvent){

        if(Bomberman.instance.playersPlaying.containsKey(event.player)) {
            Bomberman.instance.playersPlaying[event.player]?.let { game ->
                if (game.players.contains(event.player)) {
                    Bukkit.getPluginManager().callEvent(PlayerLeaveGameEvent(event.player, game, EliminationType.DISCONNECTION))
                } else {
                    PlayerManagement.removeFromArena(game, event.player)
                }
            }
        } else if(Bomberman.instance.playersOnQueue.contains(event.player)){
            Bomberman.instance.playersOnQueue.remove(event.player)
        }

    }

}