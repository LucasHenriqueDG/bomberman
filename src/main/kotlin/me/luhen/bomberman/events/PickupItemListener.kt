package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.visual.VisualUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PickupItemListener: Listener {

    @EventHandler
    fun getItemEvent(event: EntityPickupItemEvent){

        val player = event.entity

        val item = event.item

        if(player is Player){

            val game = Bomberman.instance.playersPlaying[player]

            game?.let {

                if (game.players.contains(player)) {

                    when (item.itemStack.type) {

                        Material.TNT -> {

                            game.bombermans[player]?.let{ it.bombs += 1 }
                            item.remove()
                            event.isCancelled = true

                            VisualUtils.sendComponent(Bomberman.instance.messages["extra-bomb-message"].toString(), player)

                            Bomberman.instance.scoreboards[player]?.update()

                        }

                        Material.STONE_PRESSURE_PLATE -> {

                            if (!player.inventory.contains(item.itemStack)) {

                                VisualUtils.sendComponent(Bomberman.instance.messages["extra-landmine-message"].toString(), player)

                            } else {

                                event.isCancelled = true
                                item.remove()

                            }

                            game.bombermans[player]?.let{ it.landMines += 1 }
                            Bomberman.instance.scoreboards[player]?.update()

                        }

                        Material.BLAZE_POWDER -> {

                            game.bombermans[player]?.let { it.power += 1 }
                            event.isCancelled = true
                            item.remove()

                            VisualUtils.sendComponent(Bomberman.instance.messages["extra-power-message"].toString(), player)
                            Bomberman.instance.scoreboards[player]?.update()

                        }

                        Material.GOLDEN_SHOVEL -> {

                            if (!player.inventory.contains(item.itemStack)) {

                                VisualUtils.sendComponent(Bomberman.instance.messages["shovel-message"].toString(), player)

                            } else {

                                event.isCancelled = true
                                item.remove()

                            }

                        }

                        Material.GOLDEN_BOOTS -> {

                            if (!player.inventory.contains(item.itemStack)) {

                                VisualUtils.sendComponent(Bomberman.instance.messages["boots-message"].toString(), player)

                            } else {

                                event.isCancelled = true
                                item.remove()

                            }

                        }

                        Material.NETHER_STAR -> {

                            game.bombermans[player]?.let { it.lifes += 1 }

                            VisualUtils.sendComponent(Bomberman.instance.messages["extra-life-message"].toString(), player)

                            event.isCancelled = true
                            item.remove()

                        }

                        Material.FEATHER -> {

                            player.removePotionEffect(PotionEffectType.SPEED)
                            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1))

                            VisualUtils.sendComponent(Bomberman.instance.messages["speed-message"].toString(), player)

                            event.isCancelled = true
                            item.remove()

                        }

                        Material.WITHER_SKELETON_SKULL -> {

                            player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, (20 * 7), Int.MAX_VALUE))

                            VisualUtils.sendComponent(Bomberman.instance.messages["blindness-message"].toString(), player)

                            event.isCancelled = true
                            item.remove()

                        }

                        Material.CLOCK -> {

                            game.bombermans[player]?.let { it.hasTimer = true }

                            if (!player.inventory.contains(item.itemStack)) {

                                VisualUtils.sendComponent(Bomberman.instance.messages["clock-message"].toString(), player)

                            } else {

                                event.isCancelled = true
                                item.remove()

                            }
                        }

                        else -> {}

                    }

                }

            }

        }

    }
}