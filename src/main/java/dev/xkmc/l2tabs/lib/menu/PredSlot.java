package dev.xkmc.l2tabs.lib.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * Slot added by BaseInventoryScreenHandler. Contains multiple helpers
 */
@SuppressWarnings("unused")
public class PredSlot extends Slot
{
    private final Predicate<ItemStack> pred;
    private final int slotCache;
    @Nullable
    private BooleanSupplier pickup;
    @Nullable
    private BooleanSupplier inputLockPred;
    private int max = 64;
    private boolean changed = false;
    private boolean lockInput = false;
    private boolean lockOutput = false;

    /**
     * Should be called by BaseContainerScreenHandler::addSlot only.
     * Predicate supplied from subclasses pf BaseContainerScreenHandler.
     */
    public PredSlot(Inventory inventory, int index, int x, int y, Predicate<ItemStack> pred)
    {
        super(inventory, index, x, y);
        this.pred = pred;
        this.slotCache = index;
    }

    /**
     * Set the condition to unlock a slot for input.
     * Parallel with manual lock and item predicate.
     */
    public PredSlot setInputLockPred(BooleanSupplier pred)
    {
        this.inputLockPred = pred;
        return this;
    }

    /**
     * Set restriction for pickup.
     */
    public PredSlot setPickup(BooleanSupplier pickup)
    {
        this.pickup = pickup;
        return this;
    }

    public PredSlot setMax(int max)
    {
        this.max = max;
        return this;
    }

    @Override
    public int getMaxItemCount()
    {
        return Math.min(max, super.getMaxItemCount());
    }

    @Override
    public boolean canInsert(ItemStack stack)
    {
        if (isInputLocked()) return false;
        return pred.test(stack);
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity)
    {
        if (isOutputLocked()) return false;
        return pickup == null || pickup.getAsBoolean();
    }

    @Override
    public void markDirty()
    {
        changed = true;
        super.markDirty();
    }

    /**
     * run only if the content of this slot is changed
     */
    public boolean clearDirty(Runnable r)
    {
        if (changed)
        {
            r.run();
            changed = false;
            return true;
        }
        return false;
    }

    public boolean clearDirty()
    {
        if (changed)
        {
            changed = false;
            return true;
        }
        return false;
    }

    /**
     * eject the content of this slot if the item is no longer allowed
     */
    public void updateEject(PlayerEntity player)
    {
        if (!canInsert(getStack())) clearSlot(player);
    }

    /**
     * Lock the input of this slot.
     * Parallel with lock conditions and item predicate
     */
    public void setLockInput(boolean lock)
    {
        lockInput = lock;
    }

    /**
     * See if the input is locked manually or locked by lock conditions
     */
    public boolean isInputLocked()
    {
        return lockInput || (inputLockPred != null && inputLockPred.getAsBoolean());
    }

    /**
     * Lock the output of this slot.
     * Parallel with pickup restrictions.
     */
    public void setLockOutput(boolean lock)
    {
        lockOutput = lock;
    }

    /**
     * See if the output is locked manually.
     */
    public boolean isOutputLocked()
    {
        return lockOutput;
    }

    /**
     * eject the content of this slot.
     */
    public void clearSlot(PlayerEntity player)
    {
        BaseInventoryScreenHandler.clearSlot(player, inventory, slotCache);
    }
}
