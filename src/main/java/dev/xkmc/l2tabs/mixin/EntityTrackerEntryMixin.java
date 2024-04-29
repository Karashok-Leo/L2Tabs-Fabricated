package dev.xkmc.l2tabs.mixin;

import dev.xkmc.l2tabs.L2Tabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerEntryMixin
{
	@Shadow
	@Final
	private Entity entity;

	@Inject(at = @At("HEAD"), method = "syncEntityData")
	public void l2tabs$sendDirtyData(CallbackInfo ci) {
		if (this.entity instanceof LivingEntity le) {
			L2Tabs.onAttributeUpdate(le);
		}
	}
}