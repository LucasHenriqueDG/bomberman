package me.luhen.bomberman.utils

import me.luhen.bomberman.Bomberman

object DataUtils {

    fun updateMessages(){

        val messages = Bomberman.instance.config.getConfigurationSection("messages")
        messages?.getValues(false)?.forEach { (key, value) ->
            Bomberman.instance.messages[key] = value.toString()
        }

    }

}