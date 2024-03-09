package bridge.utils

import bridge.Bridge
import net.minecraft.util.ChatComponentText
import net.minecraft.util.StringUtils


fun modMessage(message: Any) {
    if (Bridge.mc.thePlayer == null) return
    Bridge.mc.thePlayer?.addChatMessage(ChatComponentText(message.toString()))
}