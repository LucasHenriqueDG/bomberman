package me.luhen.bomberman.game

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.data.Bomb
import me.luhen.bomberman.data.BombermanPlayer
import me.luhen.bomberman.data.LandMine
import me.luhen.bomberman.enums.BombType
import me.luhen.bomberman.enums.GameState
import me.luhen.bomberman.items.GameItems
import me.luhen.bomberman.mechanics.GameFunctions
import me.luhen.bomberman.mechanics.PlayerManagement
import me.luhen.bomberman.tasks.*
import me.luhen.bomberman.utils.PlaceholderUtils
import me.luhen.bomberman.utils.Utils
import me.luhen.bomberman.visual.VisualUtils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

class Game(val gameFile: YamlConfiguration, val status: GameState) {

    var currentDelay = gameFile.getInt("waiting-delay")
    val players = mutableListOf<Player>()
    val spectators = mutableListOf<Player>()
    private var warmupTask: WarmupTask? = null
    private var waitingTask: WaitingTask? = null
    private var checkingTask: CheckingTask? = null
    private var bossbarTask: BossbarTask? = null
    private var finishingTask: FinishingTask? = null
    val eliminationsTemp = mutableMapOf<Player, String>()
    val teleportLocations = mutableListOf<Location>()
    val bombermans = mutableMapOf<Player, BombermanPlayer>()
    val items = GameItems(this)
    val corner1 = gameFile.getLocation("corner1")
    private val corner2 = gameFile.getLocation("corner2")
    val placedBombs = mutableMapOf<Location, Bomb>()
    val placedLandMines = mutableMapOf<Location, LandMine>()
    private val blockToBreak = gameFile.getString("block").toString().lowercase()
    private val barrierBlocks = gameFile.getString("barrier-block").toString().lowercase()
    private val wallBlock = gameFile.getString("wall-block").toString().lowercase()

    init {

        corner1?.let {
            corner2?.let {
                GameFunctions.setupArena(this, corner1, corner2)
                //Start checking for players with a task
                startWaiting()
            } ?: errorStarting()
        } ?: errorStarting()

    }

    private fun errorStarting(){
        Bomberman.instance.logger.warning("[Bomberman] Error trying to initialize game. Check your game file.")
        cancel()
    }

    fun start(){

        //Spread the players
        GameFunctions.spreadPlayers(this)

        //Start the game
        startRunning()

    }

    private fun cancel(){
        //Remove all the players
        players.forEach{
            PlayerManagement.removeFromArena(this, it)
        }
        spectators.forEach{
            PlayerManagement.removeFromArena(this, it)
        }
        //Remove Game
        Bomberman.instance.gameFiles[gameFile] = false
        Bomberman.instance.currentGames.remove(this)
    }

    fun finishGame(){

        //Enter finishing mode
        val winner = players.first()

        val winnerMessage = PlaceholderUtils.replacePlaceholder(
            Bomberman.instance.messages["winner-message"].toString(),
            "%winner%",
            winner.name
        )

        VisualUtils.sendComponent(winnerMessage, spectators)
        VisualUtils.sendComponent(winnerMessage, winner)

        startFinishing()


    }

    fun canStartWarmup(): Boolean{

        val players = players.size
        val minimumPlayers = gameFile.getInt("start-with")
        return players >= minimumPlayers

    }

    fun canStartGame(): Boolean{

        val players = players.size
        val minimumPlayers = gameFile.getInt("start-with")
        return players >= minimumPlayers
    }

    fun startWarmup() {
        cancelWaiting()
        VisualUtils.sendComponent(Bomberman.instance.messages["warmup-message"].toString(), players)

        //Start Warmup Task
        val task = WarmupTask(this)
        warmupTask = task.apply { runTaskLater(Bomberman.instance, gameFile.getInt("warmup-delay") * 20L) }

    }

    fun cancelWarmup(){
        warmupTask?.cancel()
    }

     fun startWaiting() {

         currentDelay = gameFile.getInt("waiting-delay")

        //Start Waiting Task
        val task = BossbarTask(this)
        bossbarTask = task.apply { runTaskTimer(Bomberman.instance, 20L, 20L) }

         val task2 = WaitingTask(this)
         waitingTask = task2.apply { runTaskLater(Bomberman.instance, currentDelay * 20L) }


    }

    private fun cancelWaiting(){
        waitingTask?.cancel()
    }

    private fun startRunning() {

        //Starts Running Task
        val task = CheckingTask(this)
        checkingTask = task.apply { runTaskTimer(Bomberman.instance, 2L, 2L) }

    }

    private fun cancelChecking(){
        checkingTask?.cancel()
    }

    private fun startFinishing(){

        cancelChecking()

        val task = FinishingTask(this)
        finishingTask = task.apply { runTaskLater(Bomberman.instance, gameFile.getInt("finishing-time")*20L) }

    }

    fun canPlaceBomb(player: Player, bombType: BombType): Boolean {

        val currentTime = System.currentTimeMillis()
        val lastTime = bombermans[player]?.delay ?: 0

        when(bombType){

            BombType.BOMB -> {

                val bombs = bombermans[player]?.bombs

                // Check if 100ms have passed since the last bomb placement
                return if ((currentTime - lastTime >= 100) && (bombs!! >= 1)) {

                    bombermans[player]?.delay = currentTime

                    true

                } else {

                    false

                }

            }

            BombType.LANDMINE -> {

                val landMines = bombermans[player]?.landMines

                // Check if 100ms have passed since the last bomb placement
                return if ((currentTime - lastTime >= 100) && (landMines!! >= 1)) {

                    bombermans[player]?.delay = currentTime

                    true

                } else {

                    false

                }

            }

        }

    }

    fun checkAndDestroyBomb(bomb: Bomb) {

        val location = bomb.location

        val fireBlocks = mutableListOf<Location>()

        val locationsToCheck = Stack<Location>()
        locationsToCheck.push(location)

        while(locationsToCheck.isNotEmpty()) {

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

                for (i in 1..bomb.bomberman.power) {

                    val blockTemp: Block = direction.block

                    if (blockTemp.type != Material.AIR || blockTemp.type == Material.valueOf(barrierBlocks)) {

                        break // Stop if we hit a wall

                    } else if (blockTemp.type == Material.valueOf(blockToBreak)) {

                        blockTemp.type = Material.AIR // Destroy the oak leaves

                        itemDrop(direction)

                        break // Stop after destroying the first oak leaves

                    } else if (blockTemp.type == Material.TNT) {

                        for(bombs in placedBombs){

                            val loc = bombs.value.location
                            val tempBomb = bombs.key

                            if (Utils.areLocationsEqual(loc, blockTemp.location)) {

                                placedBombs.remove(tempBomb)

                                locationsToCheck.push(loc)

                                break

                            }

                        }

                        break

                    } else if(blockTemp.type == Material.STONE_PRESSURE_PLATE){

                        break

                    } else {

                        // Place fire at the current block location
                        blockTemp.type = Material.FIRE

                        fireBlocks.add(blockTemp.location)

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
            fireBlocks.add(tempLocation)

        }

        RemovefireTask(this, fireBlocks).apply { runTaskLater(Bomberman.instance, 20L) }

        if(placedBombs.containsValue(bomb)){

            placedBombs.remove(bomb.location)

        }

    }

    fun removeAllFireBlocks(fireBlocks: List<Location>) {

        for (location in fireBlocks) {

            val block = location.block

            if (block.type == Material.FIRE) {

                block.type = Material.AIR // Remove the fire block

            }

        }

    }

    private fun itemDrop(location: Location){

        var item: ItemStack? = null

        val chance = Random().nextInt(0,1000)

        if(chance <= 5){

            val netherStar = ItemStack(Material.NETHER_STAR)

            item = netherStar

        } else if(chance <= 25){

            val shovel = items.shovel

            val boots = items.boots

            val pressurePlate = items.landMine

            val items = arrayOf(shovel, boots, pressurePlate)

            item = items.random()

        } else if(chance <= 55) {

            val feather = ItemStack(Material.FEATHER)
            val witherSkull = ItemStack(Material.WITHER_SKELETON_SKULL)

            val items = arrayOf(feather, witherSkull)

            item = items.random()

        } else if(chance <= 85){

            val blaze = ItemStack(Material.BLAZE_POWDER)

            val tnt = items.bomb

            val items = arrayOf(blaze, tnt)

            item = items.random()

        }

        if (item != null) {
            val task = DropitemTask(this, location, item)
            task.apply { runTaskLater(Bomberman.instance, 22L) }
        }

    }

     fun bootsEffect(bomb: Bomb, player: Player) {

        val initialBombLocation = placedBombs[bomb.location]?.location

        var currentLocation = initialBombLocation?.clone()
        val block = currentLocation?.block
        val blockMaterial = block?.type

        // Set the original block to air
        block?.type = Material.AIR

        val direction = player.facing

        while (true) {

            // Move to the next location in the given direction
            currentLocation = currentLocation?.add(direction.direction)

            // Check if the next block is solid
            val nextBlock: Block = currentLocation!!.block

            if (nextBlock.type != Material.AIR) {

                // If it's solid, move the block to the previous location
                currentLocation.subtract(direction.direction)
                currentLocation.block.type = blockMaterial!!
                break

            }
        }

        if(!Utils.areLocationsEqual(initialBombLocation!!, currentLocation!!)){

            placedBombs[bomb.location]?.location = currentLocation

        }

    }

     fun shovelEffect(bomb: Bomb, player: Player) {

        val bombInitialLocation = bomb.location

        var currentLocation = bombInitialLocation.clone()
        val block = currentLocation.block
        val blockMaterial = block.type

        val direction = player.facing

        // Set the original block to air
        block.type = Material.AIR

        while (true) {
            // Move to the next location in the given direction
            currentLocation = currentLocation.add(direction.direction)

            // Get the next block
            val nextBlock: Block = currentLocation.block

            // If we find a wall block, stop and place the block in the past position
            if (nextBlock.type == Material.valueOf(wallBlock)) {

                currentLocation.subtract(direction.direction)
                currentLocation.block.type = blockMaterial
                break

            }

            if(nextBlock.type == Material.valueOf(barrierBlocks)){

                val twoBlocksAhead = currentLocation.clone().add(direction.direction)

                if (twoBlocksAhead.block.type == Material.AIR) {

                    // If both the next two blocks are air, move the block two spaces forward
                    currentLocation = twoBlocksAhead

                    currentLocation.block.type = blockMaterial

                    break
                }

            }

            // If we find an air block, place the block here
            if (nextBlock.type == Material.AIR) {

                val twoBlocksAhead = currentLocation.clone().add(direction.direction)

                if (twoBlocksAhead.block.type == Material.AIR) {

                    // If both the next two blocks are air, move the block two spaces forward
                    currentLocation = twoBlocksAhead

                }

                currentLocation.block.type = blockMaterial

                break

            }

            // If we find cobblestone, skip over it
            if (nextBlock.type == Material.COBBLESTONE) {

                continue // Move to the next block in the direction

            }

        }

        if(!Utils.areLocationsEqual(bombInitialLocation, currentLocation)){

            val bombData = placedBombs[bomb.location]

            bombData?.location = currentLocation

            placedBombs.remove(bomb.location)

            bombData?.let { placedBombs[currentLocation] = bombData }

        }

    }

    fun checkAndDestroyLandmine(location: Location){

        placedLandMines[location]?.let{ mine ->

            val bomb = Bomb(0L, mine.bomberman, location)

            placedLandMines.remove(location)
            location.block.type = Material.AIR

            placedBombs[location] = bomb
            checkAndDestroyBomb(bomb)

        }


    }

}