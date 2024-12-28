package me.luhen.bomberman.tasks

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.enums.GameState
import me.luhen.bomberman.game.Game
import me.luhen.bomberman.utils.PlaceholderUtils
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.scheduler.BukkitRunnable

class BossbarTask(val game: Game): BukkitRunnable() {

    override fun run() {

        val color = if(game.status == GameState.WAITING) BossBar.Color.BLUE else BossBar.Color.GREEN
        val message = PlaceholderUtils.replacePlaceholder(
            Bomberman.instance.messages["bossbar-message"].toString(),
            "%delay%",
            game.currentDelay.toString()
        )

        //Update bossbar to players
        game.bossBar.currentColor = color
        game.bossBar.currentMessage = message
        game.bossBar.updateName()

        game.currentDelay -= 1

    }
}