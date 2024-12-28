package me.luhen.bomberman.items

import me.luhen.bomberman.game.Game
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


class GameItems(game: Game){

    init {
        createItems()
    }

    lateinit var bomb: ItemStack
    private lateinit var boots: ItemStack
    private lateinit var shovel: ItemStack
    private lateinit var landMine: ItemStack
    private lateinit var clock: ItemStack

    private val bombChance = game.gameFile.getInt("bomb-chance")
    private val bootsChance = game.gameFile.getInt("boots-chance")
    private val shovelChance = game.gameFile.getInt("shovel-chance")
    private val landmineChance = game.gameFile.getInt("landmine-chance")
    private val clockChance = game.gameFile.getInt("clock-chance")
    private val speedChance = game.gameFile.getInt("speed-chance")
    private val blindChance = game.gameFile.getInt("blindness-chance")
    private val lifeChance = game.gameFile.getInt("life-chance")

    lateinit var chances: Map<ItemStack, Int>

    private val bootsName = game.gameFile.getString("boots-name")
    private val shovelName = game.gameFile.getString("shovel-name")
    private val mineName = game.gameFile.getString("landmine-name")
    private val bombName = game.gameFile.getString("bomb-name")
    private val clockName = game.gameFile.getString("clock-name")

    private fun createItems(){

        val bombTemp = ItemStack(Material.TNT)
        val bombMeta = bombTemp.itemMeta

        val bootsTemp = ItemStack(Material.GOLDEN_BOOTS)
        val bootsMeta = bootsTemp.itemMeta

        val shovelTemp = ItemStack(Material.GOLDEN_SHOVEL)
        val shovelMeta = shovelTemp.itemMeta

        val landMineTemp = ItemStack(Material.STONE_PRESSURE_PLATE)
        val landMineMeta = landMineTemp.itemMeta

        val clockTemp = ItemStack(Material.CLOCK)
        val clockMeta = clockTemp.itemMeta

        bombMeta?.setDisplayName(bombName)
        bombTemp.itemMeta = bombMeta

        bootsMeta?.setDisplayName(bootsName)
        bootsTemp.itemMeta = bootsMeta

        shovelMeta?.setDisplayName(shovelName)
        shovelTemp.itemMeta = shovelMeta

        landMineMeta?.setDisplayName(mineName)
        landMineTemp.itemMeta = landMineMeta

        clockMeta?.setDisplayName(clockName)
        clockTemp.itemMeta = clockMeta

        bomb = bombTemp
        boots = bootsTemp
        shovel = shovelTemp
        landMine = landMineTemp
        clock = clockTemp

        val chancesTemp = mapOf(
            Pair(bomb, bombChance),
            Pair(boots, bootsChance),
            Pair(shovel, shovelChance),
            Pair(landMine, landmineChance),
            Pair(clock, clockChance),
            Pair(ItemStack(Material.FEATHER), speedChance),
            Pair(ItemStack(Material.WITHER_SKELETON_SKULL), blindChance),
            Pair(ItemStack(Material.NETHER_STAR), lifeChance)
        )

        chances = chancesTemp

    }

}