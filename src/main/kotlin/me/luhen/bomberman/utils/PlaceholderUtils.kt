package me.luhen.bomberman.utils

object PlaceholderUtils {

    fun replacePlaceholder(message: String, placeholder: String, replacement: String): String{

        return message.replace(placeholder, replacement)

    }
}