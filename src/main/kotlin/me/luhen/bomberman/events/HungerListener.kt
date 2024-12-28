package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent

class HungerListener: Listener {

    @EventHandler
    fun cancelHunger(event: FoodLevelChangeEvent){
        if(event.entityType == EntityType.PLAYER){
            val player = event.entity as Player
            if(Bomberman.instance.playersPlaying.containsKey(player)){
                event.isCancelled = true
            }
        }
    }
}