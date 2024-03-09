package bridge.commands

import bridge.Bridge.Companion.config
import bridge.Bridge.Companion.display
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class BridgeCommands : CommandBase() {

    override fun getCommandName(): String = "bridge"

    override fun getCommandAliases(): List<String> = listOf()

    override fun getCommandUsage(sender: ICommandSender): String = "/$commandName"

    override fun getRequiredPermissionLevel(): Int = 0

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            display = config.gui()
            return
        }
    }
}
