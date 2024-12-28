package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class PlayerDropItemListener: Listener {

    @EventHandler
    fun playerDropItem(event: PlayerDropItemEvent){
        if(Bomberman.instance.playersPlaying.containsKey(event.player)){
            event.isCancelled = true
        }
    }
}