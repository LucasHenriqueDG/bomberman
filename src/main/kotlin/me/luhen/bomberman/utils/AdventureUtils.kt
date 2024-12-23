package me.luhen.bomberman.utils

import me.luhen.bomberman.Bomberman
import net.kyori.adventure.audience.Audience
import org.bukkit.entity.Player

object AdventureUtils {

    fun createAudience(players: List<Player>): Audience{

        return Audience.audience(players.map { Bomberman.instance.audience?.player(it) })

    }

    fun createAudience(player: Player): Audience?{

        return Bomberman.instance.audience?.player(player)

    }

}