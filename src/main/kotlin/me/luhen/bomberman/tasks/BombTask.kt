package me.luhen.bomberman.tasks

import me.luhen.bomberman.data.Bomb
import me.luhen.bomberman.events.custom.TriggerBombEvent
import me.luhen.bomberman.game.Game
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class BombTask(val game: Game, private val bomb: Bomb): BukkitRunnable() {

    override fun run() {

        if(game.placedBombs.containsValue(bomb)) {

            Bukkit.getPluginManager().callEvent(TriggerBombEvent(bomb, game))
        }

        game.bombermans[bomb.bomberman.player]?.let{ it.bombs += 1 }
    }

}