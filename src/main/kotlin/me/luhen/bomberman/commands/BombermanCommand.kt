package me.luhen.bomberman.commands

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.enums.EliminationType
import me.luhen.bomberman.enums.GameState
import me.luhen.bomberman.events.custom.PlayerJoinGameEvent
import me.luhen.bomberman.events.custom.PlayerLeaveGameEvent
import me.luhen.bomberman.mechanics.GameFunctions
import me.luhen.bomberman.mechanics.PlayerManagement
import me.luhen.bomberman.utils.CreateConfig
import me.luhen.bomberman.utils.DataUtils
import me.luhen.bomberman.visual.VisualUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

object BombermanCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, str: String, args: Array<out String>): Boolean {

        if(sender is Player){
            println(sender.location.world?.name)
            if(args.isNotEmpty()){
                val args1 = args[0]
                when(args1) {
                    "join" -> {
                        //Join game if possible
                        //Check if there is any available game, if not create a new one
                        if(Bomberman.instance.currentGames.size > 0) {
                            val iterator = Bomberman.instance.currentGames.iterator()
                            if (iterator.hasNext()) {
                                while (iterator.hasNext()) {
                                    val game = iterator.next()
                                    if (game.status == GameState.WAITING || game.status == GameState.WARMUP) {
                                        //Player can join
                                        if (Bomberman.instance.isRunning) {
                                            Bukkit.getPluginManager().callEvent(PlayerJoinGameEvent(sender, game))
                                            println("player joined an existing game")
                                            println("1")
                                            break
                                        } else {
                                            VisualUtils.sendComponent(
                                                Bomberman.instance.messages["no-game-available"].toString(),
                                                sender
                                            )
                                            println("2")
                                        }
                                    }

                                    if (!iterator.hasNext()) {
                                        if (Bomberman.instance.isRunning) {
                                            //No games available, try creating a new one
                                            if(Bomberman.instance.gameFiles.isNotEmpty()) {
                                                val iterator2 = Bomberman.instance.gameFiles.iterator()
                                                while (iterator2.hasNext()) {
                                                    val gameName = iterator2.next()
                                                    if (!gameName.value) {
                                                        //Create a new game and break the iterator
                                                        val configFile =
                                                            File(Bomberman.instance.dataFolder, "games/${gameName.key}.yml")
                                                        GameFunctions.createNewGame(
                                                            YamlConfiguration.loadConfiguration(configFile),
                                                            listOf(sender)
                                                        )
                                                        println("new game created")

                                                        break
                                                    }

                                                    if (!iterator2.hasNext()) {
                                                        //No games available, player will have to wait or be put into a queue
                                                        PlayerManagement.addPlayerToQueue(sender)
                                                        println("player added to queue")
                                                        println("3")
                                                    }
                                                }
                                            } else {
                                                PlayerManagement.addPlayerToQueue(sender)
                                                println("player added to queue")
                                                println("4")
                                            }

                                        } else {
                                            VisualUtils.sendComponent(
                                                Bomberman.instance.messages["no-game-available"].toString(),
                                                sender
                                            )
                                            println("5")
                                        }
                                    }
                                }
                            }
                        } else {
                            //No games running
                            if (Bomberman.instance.isRunning) {
                                //No games available, try creating a new one
                                if(Bomberman.instance.gameFiles.isNotEmpty()) {
                                    val iterator3 = Bomberman.instance.gameFiles.iterator()
                                    while (iterator3.hasNext()) {
                                        val gameName = iterator3.next()
                                        if (!gameName.value) {
                                            //Create a new game and break the iterator
                                            val configFile =
                                                File(Bomberman.instance.dataFolder, "games/${gameName.key}.yml")
                                            if (configFile.exists()) {
                                                GameFunctions.createNewGame(
                                                    YamlConfiguration.loadConfiguration(configFile), listOf(sender)
                                                )
                                                println("new game created")
                                                println("6")

                                                break
                                            } else {
                                                println("this file was not found")
                                            }
                                        }

                                        if (!iterator3.hasNext()) {
                                            VisualUtils.sendComponent(
                                                Bomberman.instance.messages["no-game-available"].toString(),
                                                sender
                                            )
                                            println("7")
                                        }
                                    }
                                } else {
                                    VisualUtils.sendComponent(
                                        Bomberman.instance.messages["no-game-available"].toString(),
                                        sender
                                    )
                                    println("8")
                                }
                            }
                        }
                    }
                    "leave" -> {
                        //Leave game if possible
                        if(Bomberman.instance.playersPlaying.containsKey(sender)) {
                            val game = Bomberman.instance.playersPlaying[sender]
                            game?.let {
                                if (game.players.contains(sender)) {
                                    Bukkit.getPluginManager().callEvent(PlayerLeaveGameEvent(sender, game, EliminationType.COMMAND))
                                } else {
                                    PlayerManagement.removeFromArena(game, sender)
                                }
                            }
                        } else {
                            //Player is not playing any game
                            VisualUtils.sendComponent(Bomberman.instance.messages["not-playing-message"].toString(), sender)
                        }
                    }
                    "reload" -> {
                        //Update messages and game files
                        DataUtils.updateMessages()
                        DataUtils.updateGameFiles()
                    }
                    "stats" -> {
                        //Access your stats
                        VisualUtils.sendComponent(Bomberman.instance.messages["not-done-yet"].toString(), sender)
                    }
                    "toggle" -> {
                        //Next players won't be able to join
                        if(sender.hasPermission("bomberman.admin")){
                            when (Bomberman.instance.isRunning){
                                true ->{
                                    Bomberman.instance.isRunning = false
                                    VisualUtils.sendComponent(Bomberman.instance.messages["bomberman-off"].toString(), sender)
                                }
                                false ->{
                                    Bomberman.instance.isRunning = true
                                    VisualUtils.sendComponent(Bomberman.instance.messages["bomberman-on"].toString(), sender)
                                }
                            }
                        } else {
                            VisualUtils.sendComponent(Bomberman.instance.messages["no-permission-message"].toString(), sender)
                        }
                    }
                    "cancel" -> {
                        //Cancel every game running
                        if(sender.hasPermission("bomberman.admin")){
                            val gamesRunning = Bomberman.instance.currentGames.toList().iterator()
                            while(gamesRunning.hasNext()){
                                val game = gamesRunning.next()
                                game.cancel()
                           }
                        } else {
                            VisualUtils.sendComponent(Bomberman.instance.messages["no-permission-message"].toString(), sender)
                        }
                    }
                    "create" -> {
                        if(sender.hasPermission("bomberman.admin")) {
                            if (args.size > 1) {
                                val name = args[1]
                                CreateConfig.createConfigYml(File(Bomberman.instance.dataFolder, "games/${name}.yml"), name)
                            }
                        } else {
                            VisualUtils.sendComponent(Bomberman.instance.messages["no-permission-message"].toString(), sender)
                        }
                    }
                    else -> {
                        //Wrong argument
                        Bomberman.instance.messages["command-usage-message"]?.let { VisualUtils.sendComponent(it, sender) }
                    }
                }
            } else {
                Bomberman.instance.messages["command-usage-message"]?.let { VisualUtils.sendComponent(it, sender) }
            }
        }

        return true

    }

}