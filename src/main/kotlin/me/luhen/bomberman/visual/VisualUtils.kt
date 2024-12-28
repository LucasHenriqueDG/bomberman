package me.luhen.bomberman.visual

import me.luhen.bomberman.utils.AdventureUtils
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object VisualUtils {

    fun parseToComponent(string: String): Component {

        return MiniMessage.miniMessage().deserialize(string)

    }

    fun sendComponent(message: String, players: List<Player>) {

        val component = parseToComponent(message)
        val audience = AdventureUtils.createAudience(players)
        audience.sendMessage(component)

    }

    fun sendComponent(message: String, player: Player) {

        val component = parseToComponent(message)
        val audience = AdventureUtils.createAudience(player)
        audience?.sendMessage(component)

    }

    fun sendActionBar(message: String, players: List<Player>){

        val component = parseToComponent(message)
        val audience = AdventureUtils.createAudience(players)
        audience.sendActionBar(component)

    }



}