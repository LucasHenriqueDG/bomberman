package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.events.custom.DropPerkEvent
import me.luhen.bomberman.tasks.DropitemTask
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import java.util.*

class DropPerkListener: Listener {

    @EventHandler
    fun dropPerk(event: DropPerkEvent) {

        var item: ItemStack? = null

        val chance = Random().nextInt(0, 1000)

        if (chance <= 5) {

            val netherStar = ItemStack(Material.NETHER_STAR)

            val clock = event.game.items.clock

            val items = arrayOf(netherStar, clock)

            item = items.random()

        } else if (chance <= 25) {

            val shovel = event.game.items.shovel

            val boots = event.game.items.boots

            val pressurePlate = event.game.items.landMine

            val items = arrayOf(shovel, boots, pressurePlate)

            item = items.random()

        } else if (chance <= 55) {

            val feather = ItemStack(Material.FEATHER)
            val witherSkull = ItemStack(Material.WITHER_SKELETON_SKULL)

            val items = arrayOf(feather, witherSkull)

            item = items.random()

        } else if (chance <= 85) {

            val blaze = ItemStack(Material.BLAZE_POWDER)

            val tnt = event.game.items.bomb

            val items = arrayOf(blaze, tnt)

            item = items.random()

        }

        if (item != null) {
            val task = DropitemTask(event.game, event.location, item)
            task.apply { runTaskLater(Bomberman.instance, 22L) }
        }

    }

}