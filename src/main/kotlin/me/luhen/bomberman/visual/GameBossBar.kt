package me.luhen.bomberman.visual

import me.luhen.bomberman.utils.AdventureUtils
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBar.Color
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class GameBossBar {

    var currentMessage: String = ""
    var progress: Float = 1.0f
    var currentColor = BossBar.Color.BLUE

    val bossBar: BossBar = BossBar.bossBar(
        Component.text(),
        progress,
        currentColor,
        BossBar.Overlay.PROGRESS
    )

    fun updateName(){

        val component = VisualUtils.parseToComponent(currentMessage)

        bossBar.name(component)

    }

    fun updateColor(color: Color){
        currentColor = color
    }

    fun addViewer(player: Player){
        AdventureUtils.createAudience(player)?.let {
            bossBar.addViewer(it)
        }
    }

    fun removeViewer(player: Player){
        AdventureUtils.createAudience(player)?.let {
            bossBar.removeViewer(it)
        }
    }

    fun removeAll(players: List<Player>){
        players.forEach{
            removeViewer(it)
        }
    }


}