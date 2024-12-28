package me.luhen.bomberman.visual

import me.luhen.bomberman.Bomberman
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot

class GameScoreboard(val player: Player) {

    private val title = Bomberman.instance.messages["title-sidebar"].toString()
    val bomberman = Bomberman.instance.playersPlaying[player]?.bombermans?.get(player)
    val scoreboard = Bukkit.getScoreboardManager()?.newScoreboard
    private val bombermanObjective = scoreboard?.registerNewObjective("bomberman", "dummy", title)

    init {
        bombermanObjective?.displaySlot = DisplaySlot.SIDEBAR
        update()
    }

     fun update(){

        bomberman?.let {
            val bombs = bomberman.bombs
            val power = bomberman.power
            val landmines = bomberman.landMines
            val kills = bomberman.kills

            bombermanObjective?.scoreboard?.resetScores(bombermanObjective.criteria)

            val bombsScore = bombermanObjective?.getScore(Bomberman.instance.messages["bombs-sidebar"].toString())
            bombsScore?.score = bombs
            val powerScore = bombermanObjective?.getScore(Bomberman.instance.messages["power-sidebar"].toString())
            powerScore?.score = power
            val landmineScore = bombermanObjective?.getScore(Bomberman.instance.messages["landmines-sidebar"].toString())
            landmineScore?.score = landmines
            val killsScore = bombermanObjective?.getScore(Bomberman.instance.messages["kills-sidebar"].toString())
            killsScore?.score = kills

            Bomberman.instance.scoreboards[player] = this
        }

    }

    fun remove(){

        Bukkit.getScoreboardManager()?.let { player.scoreboard = it.mainScoreboard }
        Bomberman.instance.scoreboards.remove(player)

    }

}