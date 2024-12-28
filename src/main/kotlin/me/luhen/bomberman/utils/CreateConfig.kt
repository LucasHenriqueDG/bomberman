package me.luhen.bomberman.utils

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object CreateConfig {

    fun createConfigYml(outputFile: File, name: String){

        val config: FileConfiguration = YamlConfiguration()

        config.set("arena-name", name)
        config.set("arena-world", "world")

        config.set("corner1.x", 0.0)
        config.set("corner1.y", 100.0)
        config.set("corner1.z", 0.0)

        config.set("corner2.x", 20.0)
        config.set("corner2.y", 100.0)
        config.set("corner2.z", 20.0)

        config.set("exit.x", 0)
        config.set("exit.y", 80)
        config.set("exit.z", 0)
        config.set("exit.yaw", 90)
        config.set("exit.pitch", 0)

        config.set("entrance.x", 5)
        config.set("entrance.y", 104)
        config.set("entrance.z", 5)
        config.set("entrance.yaw", 90)
        config.set("entrance.pitch", 0)

        config.set("spectators.x", 0)
        config.set("spectators.y", 80)
        config.set("spectators.z", 0)
        config.set("spectators.yaw", 90)
        config.set("spectators.pitch", 0)

        config.set("join-message", "[Bomberman] Place bombs to destroy blocks and eliminate other players. Do not bomb yourself!")
        config.set("starting-message", "[Bomberman] Bomb it!")
        config.set("extra-bomb-message", "[Bomberman] You got an extra bomb")
        config.set("extra-landmine-message", "[Bomberman] You have found a landmine. If you find more you can place extra landmines.")
        config.set("extra-power-message", "[Bomberman] Now your bombs and mines are more powerful.")
        config.set("shovel-message", "[Bomberman] Now you can move bombs through blocks.")
        config.set("boots-message", "[Bomberman] Now you can kick bombs until they hit a block.")
        config.set("extra-life-message", "[Bomberman] You have found an extra life. If you lose one life, you gonna be invincible for 5 seconds.")
        config.set("speed-message", "[Bomberman] Gotta go fast.")
        config.set("blind-message", "[Bomberman] Someone have turned the lights off.")
        config.set("life-lost-message", "[Bomberman] You have lost one of your extra lives. You are invincible for 5 seconds...")

        config.set("shovel-name", "&b&lMove Bombs")
        config.set("boots-name", "&a&lKick Bomb")
        config.set("landmine-name", "&9&lPlace Landmine")
        config.set("bomb-name", "&4&lBomb")
        config.set("clock-name", "&6&lTimer")

        config.set("skip-to-warmup-with", 6)
        config.set("start-with", 3)
        config.set("warmup-delay", 5)
        config.set("finishing-time", 3)
        config.set("waiting-delay", 30)
        config.set("blocks-percentage", 40)
        config.set("block", "cobblestone")
        config.set("barrier-block", "iron_block")
        config.set("bomb-time", 2.5)
        config.set("wall-block", "oak_log")

        config.save(outputFile)


    }
}