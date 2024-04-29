package dev.xkmc.l2tabs.compat;

import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.screen.slot.Slot;

public record TrinketSlotWrapper(LivingEntity player, TrinketInventory trinketInventory, int index, SlotGroup group)
{
    public Slot toSlot(int x, int y)
    {
        return new SurvivalTrinketSlot(trinketInventory, index, x, y, group, trinketInventory.getSlotType(), index, true);
    }
}