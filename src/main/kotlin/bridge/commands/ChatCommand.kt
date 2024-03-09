package bridge.commands


import bridge.features.webber
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class ChatCommand  : CommandBase() {

    override fun getCommandName(): String = "wc"

    override fun getCommandAliases(): List<String> = listOf()

    override fun getCommandUsage(sender: ICommandSender): String = "/$commandName"

    override fun getRequiredPermissionLevel(): Int = 0

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isNotEmpty()) {
            webber.send("{\"action\": \"sendPublic\", \"message\": \"${args.joinToString(" ")}\"}")
        }
    }
}