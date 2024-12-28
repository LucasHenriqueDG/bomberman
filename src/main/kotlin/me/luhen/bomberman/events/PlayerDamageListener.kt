package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class PlayerDamageListener: Listener {

    @EventHandler
    fun cancelDamageListener(event: EntityDamageEvent){
        if(event.entity is Player){
            val player = event.entity as Player
            if(Bomberman.instance.playersPlaying.containsKey(player)){
                event.isCancelled = true
            }
        }
    }
}