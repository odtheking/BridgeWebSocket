package bridge.config

import bridge.features.webber
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File

object Config : Vigilant(File("./config/Bridge/config.toml"), "Bridge") {

    @Property(
        name = "Prefix",
        description = "The prefix for the chat command",
        category = "Chat",
        type = PropertyType.TEXT,
    )
    var prefix = "&2Guild > &6"

    @Property(
        name = "Suffix",
        description = "The suffix for the chat command",
        category = "Chat",
        type = PropertyType.TEXT,
    )
    var suffix = "&r"

    @Property(
        name = "Rank",
        description = "Your Hypixel Rank",
        category = "Chat",
        type = PropertyType.TEXT,
    )
    var rank = "&6[MVP&0++&6] "

    @Property(
        name = "Guild Token",
        description = "Your Hypixel Guild Token",
        category = "Chat",
        type = PropertyType.TEXT,
    )
    var guildToken = ""

    @Property(
        name = "Toggle Joined Message",
        description = "Toggles the joined message",
        category = "Chat",
        type = PropertyType.SWITCH,
    )
    var toggleJoinedMessage = true

    @Property(
        name = "Toggle Guild Chat",
        description = "Toggles the guild chat",
        category = "Chat",
        type = PropertyType.SWITCH,
    )
    var toggleGuildChat = true

    @Property(
        name = "Connect to WebSocket",
        type = PropertyType.BUTTON,
        category = "WebSocket",
        placeholder = "Connect"
    )
    fun web() {
        webber.connect()
    }

    @Property(
        name = "Close WebSocket",
        type = PropertyType.BUTTON,
        category = "WebSocket",
        placeholder = "Close"
    )
    fun close() {
        webber.close()
    }

    @Property(
        name = "WebSocket URL",
        type = PropertyType.TEXT,
        category = "WebSocket",
    )
    var url = ""

    init {
        initialize()
    }
}
