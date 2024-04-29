package dev.xkmc.l2tabs.lib.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SlotLocked extends Slot
{
    public SlotLocked(PlayerInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity)
    {
        return false;
    }

    @Override
    public boolean canInsert(ItemStack stack)
    {
        return false;
    }
}
