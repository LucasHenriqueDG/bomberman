package me.luhen.bomberman.mechanics

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.data.BombermanPlayer
import me.luhen.bomberman.enums.GameState
import me.luhen.bomberman.events.custom.PlayerJoinGameEvent
import me.luhen.bomberman.game.Game
import me.luhen.bomberman.utils.Utils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import kotlin.random.Random

object GameFunctions {

    fun createNewGame(config: YamlConfiguration, players: List<Player>? = null){
        val newGame = Game(config, GameState.WAITING)
        Bomberman.instance.gameFiles[config] = true
        Bomberman.instance.currentGames.add(newGame)

        players?.let { playerList ->
            playerList.forEach {
                Bukkit.getPluginManager().callEvent(PlayerJoinGameEvent(it, newGame))
            }
        }
        PlayerManagement.movePlayersFromQueue(newGame)
    }

    private fun clearArena(loc1: Location, loc2: Location, blockMaterial: String) {

        val locs = Utils.getMinAndMaxLocs(loc1, loc2)

        val x1: Int = locs[0]
        val x2: Int = locs[1]
        val z1: Int = locs[2]
        val z2: Int = locs[3]


        for(x in x1..x2){
            for(z in z1..z2){
                val block = loc1.world?.getBlockAt(x,loc1.y.toInt(),z)
                if(block?.type == Material.valueOf(blockMaterial)||
                    block?.type == Material.TNT ||
                    block?.type == Material.STONE_PRESSURE_PLATE){
                    loc1.world!!.getBlockAt(x,loc1.y.toInt(),z).type = Material.AIR
                }
            }
        }

        //Clear all items from arena

    }

     fun setupArena(game: Game, loc1: Location, loc2: Location){

         game.gameFile.getString("block")?.let { blockMaterial ->
             clearArena(loc1, loc2, blockMaterial)
             val locations = Utils.getMinAndMaxLocs(loc1, loc2)

             for (x in locations[0]..locations[1]) {

                 for (z in locations[2]..locations[3]) {

                     val loc = Location(loc1.world, x.toDouble(), loc1.y, z.toDouble())

                     if (loc.block.type == Material.AIR) {

                         if (Random.nextInt(100) <= game.gameFile.getInt("blocks-percentage")) {

                             loc.block.type = Material.valueOf(blockMaterial)

                         } else {

                             loc.block.type = Material.AIR

                             game.teleportLocations.add(Utils.adjustToBlockCenterPlayer(loc))

                         }

                     }

                 }

             }
         }

    }

    fun spreadPlayers(game: Game){
        game.players.forEach{ bomberman ->
            bomberman.teleport(game.teleportLocations.random())
            game.bombermans[bomberman] = BombermanPlayer(bomberman)

            //Give the items
            bomberman.inventory.setItem(0, game.items.bomb)
        }
    }



}