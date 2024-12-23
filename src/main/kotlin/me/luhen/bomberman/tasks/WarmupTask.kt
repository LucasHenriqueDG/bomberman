package me.luhen.bomberman.tasks

import me.luhen.bomberman.game.Game
import org.bukkit.scheduler.BukkitRunnable

class WarmupTask(val game: Game): BukkitRunnable() {

    override fun run() {

        //Try starting the game
        if(game.canStartGame()) game.start() else game.startWaiting()

    }
}