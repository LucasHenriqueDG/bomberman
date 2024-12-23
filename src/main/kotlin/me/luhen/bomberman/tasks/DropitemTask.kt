package me.luhen.bomberman.tasks

import me.luhen.bomberman.game.Game
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class DropitemTask(val game: Game, private val location: Location, private val item: ItemStack): BukkitRunnable() {

    override fun run() {
        location.world?.let { Bukkit.getWorld(it.name)?.dropItemNaturally(location, item) }
    }
}