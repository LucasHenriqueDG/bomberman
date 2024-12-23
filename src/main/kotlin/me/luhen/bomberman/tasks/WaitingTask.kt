package me.luhen.bomberman.tasks

import me.luhen.bomberman.game.Game
import org.bukkit.scheduler.BukkitRunnable

class WaitingTask(val game: Game): BukkitRunnable() {

    override fun run() {

        //Check if the game can enter on warmup
        if(game.canStartWarmup()) game.startWarmup() else game.startWaiting()
    }
}