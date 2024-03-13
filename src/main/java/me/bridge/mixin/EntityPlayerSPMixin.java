package me.bridge.mixin;

import bridge.config.Config;
import bridge.utils.UtilsKt;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPMixin {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        if (Config.INSTANCE.getToggleGuildChat() && !message.startsWith("/")) {
            UtilsKt.sendGuildMessage(message);
            ci.cancel();
        }
    }
}
