package me.luhen.bomberman.items

import me.luhen.bomberman.game.Game
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


class GameItems(game: Game){

    init {
        createItems()
    }

    lateinit var bomb: ItemStack
    lateinit var boots: ItemStack
    lateinit var shovel: ItemStack
    lateinit var landMine: ItemStack

    private val bootsName = game.gameFile.getString("boots-name")
    private val shovelName = game.gameFile.getString("shovel-name")
    private val mineName = game.gameFile.getString("landmine-name")
    private val bombName = game.gameFile.getString("bomb-name")

    private fun createItems(){

        val bombTemp = ItemStack(Material.TNT)
        val bombMeta = bombTemp.itemMeta

        val bootsTemp = ItemStack(Material.GOLDEN_BOOTS)
        val bootsMeta = bootsTemp.itemMeta

        val shovelTemp = ItemStack(Material.GOLDEN_SHOVEL)
        val shovelMeta = shovelTemp.itemMeta

        val landMineTemp = ItemStack(Material.STONE_PRESSURE_PLATE)
        val landMineMeta = landMineTemp.itemMeta

        bombMeta?.setDisplayName(bombName)
        bombTemp.itemMeta = bombMeta

        bootsMeta?.setDisplayName(bootsName)
        bootsTemp.itemMeta = bootsMeta

        shovelMeta?.setDisplayName(shovelName)
        shovelTemp.itemMeta = shovelMeta

        landMineMeta?.setDisplayName(mineName)
        landMineTemp.itemMeta = landMineMeta

        bomb = bombTemp
        boots = bootsTemp
        shovel = shovelTemp
        landMine = landMineTemp

    }

}