package me.luhen.bomberman.data

import org.bukkit.entity.Player

class BombermanPlayer(val player: Player,
                      var bombs: Int = 1,
                      var power: Int = 2,
                      var delay: Long = 0L,
                      var landMines: Int = 0,
                      var lifes: Int = 1,
                      var isInvincible: Boolean = false
)