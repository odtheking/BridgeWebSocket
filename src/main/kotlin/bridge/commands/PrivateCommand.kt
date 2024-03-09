package bridge.commands


import bridge.utils.sendPrivateMessage
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class PrivateCommand : CommandBase() {

    override fun getCommandName(): String = "dm"

    override fun getCommandAliases(): List<String> = listOf()

    override fun getCommandUsage(sender: ICommandSender): String = "/$commandName"

    override fun getRequiredPermissionLevel(): Int = 0

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.size >= 2)
            sendPrivateMessage(args[0], args.sliceArray(1..args.size).joinToString(" "))
    }
}