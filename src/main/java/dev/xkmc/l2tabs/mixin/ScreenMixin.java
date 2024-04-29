package dev.xkmc.l2tabs.mixin;

import dev.xkmc.l2tabs.tabs.content.TabInventory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin
{
    @Inject(method = "renderBackground", at = @At("TAIL"))
    private void inject_renderBackground(DrawContext context, CallbackInfo ci)
    {
        TabInventory.guiPostRenderBG((Screen) (Object) this, context);
    }
}
