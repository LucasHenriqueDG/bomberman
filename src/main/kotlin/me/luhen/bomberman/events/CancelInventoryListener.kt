package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class CancelInventoryListener: Listener {

    @EventHandler
    fun cancelInventoryMove(event: InventoryClickEvent){
        if(event.whoClicked is Player){
            val player = event.whoClicked as Player
            if(Bomberman.instance.playersPlaying.containsKey(player)){
                event.isCancelled = true
            }
        }
    }
}