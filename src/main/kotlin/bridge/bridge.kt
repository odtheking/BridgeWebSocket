package bridge

import bridge.commands.BridgeCommands
import bridge.commands.ChatCommand
import bridge.config.Config
import bridge.features.unloaded
import bridge.features.webber
import bridge.utils.modMessage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import java.io.File

@Mod(
    modid = Bridge.MOD_ID, name = Bridge.MOD_NAME, version = Bridge.MOD_VERSION
)
class Bridge {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        File(event.modConfigurationDirectory, "bridge").mkdirs()
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ClientCommandHandler.instance.registerCommand((BridgeCommands()))
        ClientCommandHandler.instance.registerCommand((ChatCommand()))

        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || display == null) return
        mc.displayGuiScreen(display)
        display = null
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SubscribeEvent
    fun onConnect(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
        GlobalScope.launch {
            delay(300)
            webber.connect()
            unloaded = false

            while (!unloaded) {
                delay(80000)
                modMessage("sending keep alive message")
                webber.send("{\"action\": \"sendPrivate\", \"message\": \"&6[MVP&0++&6] &2Guild &r&6has connected to the server.\"}")
            }
        }
    }

    @SubscribeEvent
    fun onDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        webber.close()
        unloaded = true
    }

    companion object {
        const val MOD_ID = "bridge"
        const val MOD_NAME = "Bridge"
        const val MOD_VERSION = "1.0"

        val mc: Minecraft = Minecraft.getMinecraft()
        val config = Config
        var display: GuiScreen? = null
    }
}

