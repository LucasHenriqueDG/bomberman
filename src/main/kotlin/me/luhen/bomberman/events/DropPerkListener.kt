package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.events.custom.DropPerkEvent
import me.luhen.bomberman.tasks.DropitemTask
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import java.util.*

class DropPerkListener: Listener {

    @EventHandler
    fun dropPerk(event: DropPerkEvent) {

        val items = mutableListOf<ItemStack>()

        val chance = Random().nextInt(0, 1000)

        event.game.items.chances.forEach {
            if(it.value >= chance){
                items.add(it.key)
            }
        }

        if (items.isNotEmpty()) {
            val task = DropitemTask(event.game, event.location, items.random())
            task.apply { runTaskLater(Bomberman.instance, 22L) }
        }

    }

}