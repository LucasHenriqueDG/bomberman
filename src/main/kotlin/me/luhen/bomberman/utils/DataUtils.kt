package me.luhen.bomberman.utils

import me.luhen.bomberman.Bomberman
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object DataUtils {

    fun updateMessages(){

        val messages = Bomberman.instance.config.getConfigurationSection("messages")
        messages?.getValues(false)?.forEach { (key, value) ->
            Bomberman.instance.messages[key] = value.toString()
        }

    }

    fun updateGameFiles(){
        val directory = File(Bomberman.instance.dataFolder, "games")
        if(!directory.exists()){
            directory.mkdir()
        }
        directory.listFiles()?.let{ dir ->
            Bomberman.instance.gameFiles.clear()
            dir.forEach { file ->
                if(file.extension == "yml"){
                    Bomberman.instance.gameFiles[file.name.removeSuffix(".yml")] = false
                }
            }
        }

        Bomberman.instance.gameFiles.forEach {
            println(it.key)
        }

        Bomberman.instance.allowedCommands.clear()
        Bomberman.instance.config.getList("allowed-commands")?.forEach {
            if(it is String){
                Bomberman.instance.allowedCommands.add(it)
            }
        }
    }

    fun getLocationFromFile(file: YamlConfiguration, location: String): Location {
        println(file.getString("arena-world"))
        return Location(
            Bukkit.getWorld(file.getString("arena-world").toString()),
            file.getString("${location}.x")?.toDouble() ?: 0.0,
            file.getString("${location}.y")?.toDouble() ?: 0.0,
            file.getString("${location}.z")?.toDouble() ?: 0.0,
            file.getString("${location}.yaw")?.toFloat() ?: 0.0f,
            file.getString("${location}.pitch")?.toFloat() ?: 0.0f
        )
    }

}