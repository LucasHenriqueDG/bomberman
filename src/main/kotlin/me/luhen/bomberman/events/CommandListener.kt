package me.luhen.bomberman.events

import me.luhen.bomberman.Bomberman
import me.luhen.bomberman.visual.VisualUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandListener: Listener {

    @EventHandler
    fun playerUseCommand(event: PlayerCommandPreprocessEvent){
        if(Bomberman.instance.playersPlaying.containsKey(event.player)){
            val cmd = event.message
            val args = cmd.split(" ")
            if(!Bomberman.instance.allowedCommands.contains(args[0].removePrefix("/"))){
                event.isCancelled = true
                VisualUtils.sendComponent(Bomberman.instance.messages["command-blocked-message"].toString(), event.player)
            }
        }
    }
}