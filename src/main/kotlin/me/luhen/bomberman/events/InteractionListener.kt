package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.data.Bomb
import me.luhen.bomberman.data.LandMine
import me.luhen.bomberman.enums.BombType
import me.luhen.bomberman.events.custom.PlayerPlaceBombEvent
import me.luhen.bomberman.events.custom.TriggerBombEvent
import me.luhen.bomberman.utils.Utils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InteractionListener: Listener {

    @EventHandler
    fun interactionEvent(event: PlayerInteractEvent){

        val player = event.player

        if(Bomberman.instance.playersPlaying.containsKey(player)){

            val game = Bomberman.instance.playersPlaying[player]

            if(event.action == Action.RIGHT_CLICK_BLOCK){

                game?.let {

                    val clickedBlock = event.clickedBlock

                    val itemInHand = event.item

                    val blockFaceVector = event.blockFace.direction

                    val faceBlock = clickedBlock?.location?.add(blockFaceVector)!!

                    when (itemInHand?.type) {

                        //Placing Bomb
                        (Material.TNT) -> {

                            if (game.canPlaceBomb(player, BombType.BOMB)) {

                                if (faceBlock.y.toInt() == game.corner1.blockY) {

                                    game.bombermans[player]?.let { bomberman ->


                                        val bomb = Bomb(
                                            game.gameFile.getLong("bomb-time") * 20L,
                                            bomberman,
                                            faceBlock
                                        )

                                        Bukkit.getPluginManager().callEvent(PlayerPlaceBombEvent(player, bomb, game))

                                    }

                                }

                            }

                        }

                        (Material.STONE_PRESSURE_PLATE) -> {

                            if (game.canPlaceBomb(player, BombType.LANDMINE)) {

                                if ((clickedBlock.y + 1) == game.corner1.blockY) {

                                    game.bombermans[player]?.let { it.landMines -= 1 }

                                    faceBlock.block.type = Material.STONE_PRESSURE_PLATE

                                    val bomberman = game.bombermans[player]

                                    bomberman?.let {
                                        val landMine = LandMine(bomberman, faceBlock)
                                        game.placedLandMines[Utils.locationInInt(faceBlock)] = landMine
                                        game.fireBlocksToCheck[Utils.locationInInt(faceBlock)] = it
                                    }

                                }

                            }

                        }

                        //Using the golden boots
                        (Material.GOLDEN_BOOTS) -> {

                            if (clickedBlock.type == Material.TNT) {

                                val clickedLoc = Utils.locationInInt(clickedBlock.location)

                                if(game.placedBombs.containsKey(clickedLoc)){
                                    game.placedBombs[clickedLoc]?.let{ game.bootsEffect(it, player) }
                                }

                            }

                        }

                        //Using the Golden Shovel
                        (Material.GOLDEN_SHOVEL) -> {

                            if (clickedBlock.type == Material.TNT) {

                                val clickedLoc = Utils.locationInInt(clickedBlock.location)

                                if(game.placedBombs.containsKey(clickedLoc)){
                                    game.placedBombs[clickedLoc]?.let{ game.shovelEffect(it, player) }
                                }

                            }

                        }

                        //Using the clock
                        (Material.CLOCK) -> {

                            val playerBombs =
                                game.placedBombs.filter { it.value.bomberman.player == event.player }.values.toList()
                            playerBombs.forEach { bomb ->
                                Bukkit.getPluginManager().callEvent(TriggerBombEvent(bomb, game))
                                game.bombermans[player]?.let { it.bombs += 1 }
                            }

                        }

                        else -> {}

                    }

                }

            } else if(event.action == Action.RIGHT_CLICK_AIR){
                if(event.player.inventory.itemInMainHand.type == Material.CLOCK){
                    game?.let {
                        val playerBombs =
                            game.placedBombs.filter { it.value.bomberman.player == event.player }.values.toList()
                        playerBombs.forEach { bomb ->
                            Bukkit.getPluginManager().callEvent(TriggerBombEvent(bomb, game))
                            game.bombermans[player]?.let { it.bombs += 1 }
                        }
                    }
                }
            }

            event.isCancelled = true

        }

    }
}