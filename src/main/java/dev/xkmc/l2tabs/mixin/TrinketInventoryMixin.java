package dev.xkmc.l2tabs.mixin;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.xkmc.l2tabs.compat.TrinketEventHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TrinketInventory.class)
public abstract class TrinketInventoryMixin
{
    @Final
    @Shadow(remap = false)
    private TrinketComponent component;

    @Inject(method = "update", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/collection/DefaultedList;ofSize(ILjava/lang/Object;)Lnet/minecraft/util/collection/DefaultedList;"))
    private void inject_update(CallbackInfo ci)
    {
        TrinketEventHandler.onSlotModifierUpdate(this.component.getEntity());
    }
}
