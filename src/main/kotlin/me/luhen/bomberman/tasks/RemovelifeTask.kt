package me.luhen.bomberman.tasks

import me.luhen.bomberman.game.Game
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class RemovelifeTask(val game: Game, val player: Player): BukkitRunnable() {

    override fun run() {
            game.bombermans[player]?.let{ it.isInvincible = false }
    }

}