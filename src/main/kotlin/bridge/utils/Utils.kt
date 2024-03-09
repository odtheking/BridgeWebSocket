package bridge.utils

import bridge.Bridge.Companion.mc
import net.minecraft.util.ChatComponentText

fun modMessage(message: Any) {
    if (mc.thePlayer == null) return
    mc.thePlayer?.addChatMessage(ChatComponentText(message.toString()))
}