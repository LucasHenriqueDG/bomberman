package me.luhen.bomberman.utils

import org.bukkit.Location

object Utils {

    fun getMinAndMaxLocs(loc1: Location, loc2: Location): List<Int> {

        val minX = loc1.blockX.coerceAtMost(loc2.blockX)
        val minZ = loc1.blockZ.coerceAtMost(loc2.blockZ)

        val maxX = loc1.blockX.coerceAtLeast(loc2.blockX)
        val maxZ = loc1.blockZ.coerceAtLeast(loc2.blockZ)

        return listOf(minX, maxX, minZ, maxZ)

    }

    fun adjustToBlockCenterPlayer(location: Location): Location {

        // Adjust the X and Z coordinates to be the center of the block
        val centeredX = (location.x.toInt() + 0.5)
        val centeredZ = (location.z.toInt() + 0.5)

        // Create a new Location object with the adjusted X and Z

        return Location(
            location.world,  // Keep the same world
            centeredX,  // Adjusted X
            location.y,      // Keep the same Y
            centeredZ   // Adjusted Z
        )

    }

    fun areLocationsEqual(loc1: Location, loc2: Location): Boolean {
        return loc1.world == loc2.world &&
                loc1.blockX == loc2.blockX &&
                loc1.blockY == loc2.blockY &&
                loc1.blockZ == loc2.blockZ
    }

    fun locationInInt(location: Location): Location{

        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ

        return Location(
            location.world,
            x.toDouble(),
            y.toDouble(),
            z.toDouble()
        )

    }

}