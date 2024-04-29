package dev.xkmc.l2tabs.mixin;

import dev.xkmc.l2tabs.compat.TrinketEventHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
{
    @Inject(method = "tick", at = @At("TAIL"))
    private void inject_tick(CallbackInfo ci)
    {
        TrinketEventHandler.onPlayerTick((ServerPlayerEntity) (Object) this);
    }
}
