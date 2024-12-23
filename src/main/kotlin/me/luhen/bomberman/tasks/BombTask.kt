package me.luhen.bomberman.tasks

import me.luhen.bomberman.data.Bomb
import me.luhen.bomberman.game.Game
import org.bukkit.scheduler.BukkitRunnable

class BombTask(val game: Game, private val bomb: Bomb): BukkitRunnable() {

    override fun run() {

        if(game.placedBombs.containsValue(bomb)) {

            game.checkAndDestroyBomb(bomb)

        }

        game.bombermans[bomb.bomberman.player]?.let{ it.bombs += 1 }
    }

}