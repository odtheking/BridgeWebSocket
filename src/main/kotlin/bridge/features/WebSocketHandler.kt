package bridge.features


import bridge.Bridge.Companion.mc
import bridge.config.Config
import bridge.utils.addColor
import bridge.utils.modMessage
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import okhttp3.*
import java.util.concurrent.TimeUnit

var webber = Webber()
var unloaded = false
var reconnected = false

class Webber {
    private var webSocket: WebSocket? = null

    fun connect() {
        runBlocking {
            val client = OkHttpClient.Builder().readTimeout(0, TimeUnit.SECONDS).build()

            if (Config.url == "") return@runBlocking println("Please set the WebSocket URL in the config!")
            val request = Request.Builder().url(url = Config.url).build()

            val webSocketListener = EchoWebSocketListener()
            client.newWebSocket(request, webSocketListener)

            while (webSocketListener.connectionState != EchoWebSocketListener.ConnectionState.OPEN) {
                delay(100)
            }
            webSocket = webSocketListener.getWebSocket()
        }
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun close() {
        webSocket?.close(1000, "Goodbye!")
        EchoWebSocketListener.ConnectionState.CLOSED
        webSocket = null
    }
}

class EchoWebSocketListener : WebSocketListener() {
    var connectionState: ConnectionState = ConnectionState.CONNECTING
        private set

    private var webSocket: WebSocket? = null

    override fun onOpen(webSocket: WebSocket, response: Response) {
        modMessage("§aConnected to server.")
        this.webSocket = webSocket
        connectionState = ConnectionState.OPEN

        webSocket.send("{\"action\": \"setName\", \"name\": \"${Config.rank} ${mc.session?.username}\", \"token\": \"${Config.guildToken}\"}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val messageJson = JsonParser().parse(text) as JsonObject
        if (messageJson["systemMessage"] != null && Config.toggleJoinedMessage) modMessage(addColor(messageJson["systemMessage"].toString().replace("\"", "")))
        if (messageJson["publicMessage"] != null && Config.guildChatMessages) {
            val message = messageJson["publicMessage"].toString().replace("\"", "")
            val coloredMessage = addColor("${Config.prefix} ${message}${Config.suffix}")
            modMessage(coloredMessage)
        }
        reconnected = false
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        modMessage("§cWebSocket Connection Closed.")
        connectionState = ConnectionState.CLOSED

        if (unloaded) return

        if (!reconnected) modMessage("§8Attempting reconnect in 5s!")
        else return modMessage("§cReconnect failed!")

        reconnected = true
        GlobalScope.launch {
            delay(5000)
            webber.connect()
        }
    }

    fun getWebSocket(): WebSocket? {
        return webSocket
    }
    enum class ConnectionState {
        CONNECTING,
        OPEN,
        CLOSED
    }
}

