package me.luhen.bomberman.tasks

import me.luhen.bomberman.game.Game
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable

class RemovefireTask(val game: Game, private val locations: List<Location>): BukkitRunnable() {

    override fun run() {
        game.removeAllFireBlocks(locations)
    }

}