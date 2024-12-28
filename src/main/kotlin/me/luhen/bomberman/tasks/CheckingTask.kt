package me.luhen.bomberman.tasks

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.enums.EliminationType
import me.luhen.bomberman.enums.GameState
import me.luhen.bomberman.events.custom.PlayerLeaveGameEvent
import me.luhen.bomberman.game.Game
import me.luhen.bomberman.utils.Utils
import me.luhen.bomberman.visual.VisualUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.scheduler.BukkitRunnable

class CheckingTask(val game: Game): BukkitRunnable() {

    override fun run() {

        //Checking players
        val copyList = game.bombermans

        for (player in copyList) {

            val blockBelow = player.key.location.block

            val bomberman = player.value

            if(game.status == GameState.RUNNING) {

                if (blockBelow.type == Material.FIRE) {
                    // Player is standing above fire

                    player.key.fireTicks = 0

                    if (!bomberman.isInvincible) {

                        if (bomberman.lifes == 1) {

                            Bukkit.getPluginManager()
                                .callEvent(PlayerLeaveGameEvent(player.key, game, EliminationType.REGULAR))

                        } else {

                            game.bombermans[player.key]?.let { it.isInvincible = true }
                            game.bombermans[player.key]?.let { it.lifes -= 1 }
                            VisualUtils.sendComponent(
                                Bomberman.instance.messages["life-lost-message"].toString(),
                                player.key
                            )

                            val task = RemovelifeTask(game, player.key)
                            task.apply { runTaskLater(Bomberman.instance, 100L) }

                        }

                    }

                } else if (blockBelow.type == Material.STONE_PRESSURE_PLATE) {

                    player.key.fireTicks = 0

                    if (!bomberman.isInvincible) {

                        if (bomberman.lifes == 1) {

                            Bukkit.getPluginManager()
                                .callEvent(PlayerLeaveGameEvent(player.key, game, EliminationType.REGULAR))

                        } else {

                            val task = RemovelifeTask(game, player.key)
                            task.apply { runTaskLater(Bomberman.instance, 100L) }

                        }

                    }

                    //Execute the landmine
                    game.checkAndDestroyLandmine(Utils.locationInInt(player.key.location))

                }

            }
        }
    }
}