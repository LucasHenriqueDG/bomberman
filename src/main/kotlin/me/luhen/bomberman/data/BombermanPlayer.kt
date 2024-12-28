package me.luhen.bomberman.data

import org.bukkit.entity.Player

class BombermanPlayer(
    val player: Player,
    var bombs: Int = 1,
    var power: Int = 2,
    var delay: Long = 0L,
    var landMines: Int = 0,
    var lifes: Int = 1,
    var eliminations: Int = 0,
    var isInvincible: Boolean = false,
    var hasTimer: Boolean = false,
    var kills: Int = 0
)