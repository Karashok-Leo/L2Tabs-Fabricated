package dev.xkmc.l2tabs.compat.base;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import dev.xkmc.l2tabs.compat.TrinketSlotWrapper;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public abstract class BaseTrinketWrapper
{
    public final LivingEntity entity;
    @Nullable
    public final TrinketComponent component;
    public int total, page;

    public BaseTrinketWrapper(LivingEntity entity)
    {
        this.entity = entity;
        this.component = TrinketsApi.getTrinketComponent(entity).orElse(null);
    }

    public abstract int getSize();

    public abstract int getRows();

    @Nullable
    public abstract TrinketSlotWrapper getSlotAtPosition(int i);
}
