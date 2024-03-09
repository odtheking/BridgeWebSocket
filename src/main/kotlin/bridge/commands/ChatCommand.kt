package bridge.commands


import bridge.config.Config
import bridge.utils.modMessage
import bridge.utils.sendGuildMessage
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class ChatCommand  : CommandBase() {

    override fun getCommandName(): String = "wc"

    override fun getCommandAliases(): List<String> = listOf()

    override fun getCommandUsage(sender: ICommandSender): String = "/$commandName"

    override fun getRequiredPermissionLevel(): Int = 0

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isNotEmpty())
            sendGuildMessage(args.joinToString(" "))
        else {
            Config.toggleGuildChat = !Config.toggleGuildChat
            modMessage("§2Toggled guild chat to: ${if (Config.toggleGuildChat) "§aON" else "§cOFF"}")
        }
    }
}