package bridge.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.*
import bridge.features.webber
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

object Config : Vigilant(File("./config/Bridge/config.toml"), "Bridge") {

    @Property(
        name = "Connect to WebSocket",
        type = PropertyType.BUTTON,
        category = "Bridge",
        placeholder = "Connect"
    )
    fun web() {
        webber.connect()
    }

    @Property(
        name = "Close WebSocket",
        type = PropertyType.BUTTON,
        category = "Bridge",
        placeholder = "Close"
    )
    fun close() {
        webber.close()
    }

    @Property(
        name = "WebSocket URL",
        type = PropertyType.TEXT,
        category = "Bridge",
    )
    var url = ""

    init {
        initialize()
    }
}
