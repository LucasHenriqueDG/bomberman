package me.luhen.bomberman.commands

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.enums.EliminationType
import me.luhen.bomberman.enums.GameState
import me.luhen.bomberman.events.custom.PlayerJoinGameEvent
import me.luhen.bomberman.events.custom.PlayerLeaveGameEvent
import me.luhen.bomberman.mechanics.GameFunctions
import me.luhen.bomberman.mechanics.PlayerManagement
import me.luhen.bomberman.visual.VisualUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object BombermanCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, str: String, args: Array<out String>): Boolean {

        if(sender is Player){
            if(args.isNotEmpty()){
                val args1 = args[0]
                when(args1) {
                    "join" -> {
                        //Join game if possible
                        //Check if there is any available game, if not create a new one
                        val iterator = Bomberman.instance.currentGames.iterator()
                        while(iterator.hasNext()) {
                            val game = iterator.next()
                            if(game.status == GameState.WAITING || game.status == GameState.WARMUP){
                                //Player can join
                                Bukkit.getPluginManager().callEvent(PlayerJoinGameEvent(sender, game))
                            }

                            if(!iterator.hasNext()){
                                //No games available, try creating a new one
                                val iterator2 = Bomberman.instance.gameFiles.iterator()
                                while(iterator2.hasNext()){
                                    val gameFile = iterator2.next()
                                    if(!gameFile.value){
                                        //Create a new game and break the iterator
                                        GameFunctions.createNewGame(gameFile.key, listOf(sender))

                                        break
                                    }

                                    if(!iterator2.hasNext()){
                                        //No games available, player will have to wait or be put into a queue
                                        PlayerManagement.addPlayerToQueue(sender)
                                    }
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
                    "stats" -> {
                        //Access your stats
                    }
                    "close" -> {
                        //Next players won't be able to join
                    }
                    "cancel" -> {
                        //Cancel every game running
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