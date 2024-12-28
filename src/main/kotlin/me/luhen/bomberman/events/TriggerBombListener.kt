package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.events.custom.DropPerkEvent
import me.luhen.bomberman.events.custom.TriggerBombEvent
import me.luhen.bomberman.tasks.RemovefireTask
import me.luhen.bomberman.utils.Utils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.util.Vector
import java.util.*

class TriggerBombListener: Listener {

    @EventHandler
    fun triggerBomb(event: TriggerBombEvent) {

        val location = event.bomb.location

        val fireBlocks = mutableListOf<Location>()

        val locationsToCheck = Stack<Location>()
        locationsToCheck.push(location)

        while (locationsToCheck.isNotEmpty()) {

            val tempLocation = locationsToCheck.pop()

            val dirN = Vector(0.0, 0.0, -1.0)
            val dirS = Vector(0.0, 0.0, 1.0)
            val dirE = Vector(1.0, 0.0, 0.0)
            val dirW = Vector(-1.0, 0.0, 0.0)


            // Directions: North, South, East, West
            val directions = listOf(
                tempLocation.clone().add(dirN), // North
                tempLocation.clone().add(dirS),  // South
                tempLocation.clone().add(dirE),  // East
                tempLocation.clone().add(dirW)  // West
            )


            // Check each direction

            var step = 0

            for (direction in directions) {
                step += 1

                for (i in 1..event.bomb.bomberman.power) {

                    val blockTemp: Block = direction.block

                    if (blockTemp.type == Material.valueOf(event.game.barrierBlocks)) {

                        break // Stop if we hit a wall

                    } else if (blockTemp.type == Material.valueOf(event.game.blockToBreak)) {

                        blockTemp.type = Material.FIRE // Destroy the block

                        fireBlocks.add(Utils.locationInInt(blockTemp.location))

                        Bukkit.getPluginManager().callEvent(DropPerkEvent(direction, event.game))

                        break // Stop after destroying the first oak leaves

                    } else if (blockTemp.type == Material.TNT) {

                        for (bombs in event.game.placedBombs) {

                            val loc = bombs.value.location
                            val tempBomb = bombs.key

                            if (Utils.areLocationsEqual(loc, blockTemp.location)) {

                                event.game.placedBombs.remove(tempBomb)

                                locationsToCheck.push(loc)

                                break

                            }

                        }

                        break

                    } else if (blockTemp.type == Material.STONE_PRESSURE_PLATE) {

                        break

                    } else if(blockTemp.type == Material.AIR) {

                        // Place fire at the current block location
                        blockTemp.type = Material.FIRE

                        fireBlocks.add(Utils.locationInInt(blockTemp.location))
                        event.game.fireBlocksToCheck[Utils.locationInInt(blockTemp.location)] = event.bomb.bomberman

                    }

                    when (step) {

                        1 -> {
                            direction.add(dirN)
                        }

                        2 -> {
                            direction.add(dirS)
                        }

                        3 -> {
                            direction.add(dirE)
                        }

                        4 -> {
                            direction.add(dirW)
                        }

                    }

                }

            }

            tempLocation.block.type = Material.FIRE
            fireBlocks.add(Utils.locationInInt(tempLocation))
            event.game.fireBlocksToCheck[Utils.locationInInt(tempLocation)] = event.bomb.bomberman


        }

        location.block.type = Material.FIRE
        fireBlocks.add(location)

        RemovefireTask(event.game, fireBlocks).apply { runTaskLater(Bomberman.instance, 20L) }

        if (event.game.placedBombs.containsValue(event.bomb)) {

            event.game.placedBombs.remove(event.bomb.location)

        }

    }

}