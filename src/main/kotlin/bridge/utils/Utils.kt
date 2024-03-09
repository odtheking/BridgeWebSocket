package bridge.utils

import bridge.Bridge.Companion.mc
import bridge.features.webber
import net.minecraft.util.ChatComponentText

fun modMessage(message: Any) {
    if (mc.thePlayer == null) return
    mc.thePlayer?.addChatMessage(ChatComponentText(message.toString()))
}

fun sendGuildMessage(message: String) {
    webber.send("{\"action\": \"sendPublic\", \"message\": \"$message\"}")
}

fun addColor(message: String?): String {
    return message.toString().replace("(?<!\\\\)&(?![^0-9a-fk-or]|$)".toRegex(), "\u00a7")
}