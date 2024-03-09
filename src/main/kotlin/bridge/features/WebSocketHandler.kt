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
            val client = OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.SECONDS)
                .build()

            val request = Request.Builder()
                .url(url = Config.url)
                .build()
            val webSocketListener = EchoWebSocketListener()
            client.newWebSocket(request, webSocketListener)

            // Ensure the connection is established
            while (webSocketListener.connectionState != EchoWebSocketListener.ConnectionState.OPEN) {
                delay(100) // Check every 100 milliseconds
            }
            webSocket = webSocketListener.getWebSocket()
        }
    }

    fun send(message: String) {
        println("Sending message: $message")
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
        println("Connected to server")
        modMessage("§aConnected to server.")
        this.webSocket = webSocket
        connectionState = ConnectionState.OPEN

        webSocket.send("{\"action\": \"setName\", \"name\": \"${Config.rank}${mc.session?.username}\", \"token\": \"${Config.guildToken}\"}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println(text)
        val json1 = JsonParser().parse(text) as JsonObject
        if (json1["systemMessage"] != null && Config.toggleJoinedMessage) modMessage(addColor(json1["systemMessage"].toString().replace("\"", "")))
        if (json1["publicMessage"] != null) {
            val message = json1["publicMessage"].toString().replace("\"", "").split(":")
            val coloredMessage = addColor("${Config.prefix}${message[0]}§f:${message[1]}${Config.suffix}")
            modMessage(coloredMessage)
        }
        reconnected = false
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        println("Closing: $code / $reason")
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

