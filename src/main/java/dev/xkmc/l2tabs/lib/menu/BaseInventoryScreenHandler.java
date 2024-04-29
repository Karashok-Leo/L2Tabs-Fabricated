package dev.xkmc.l2tabs.lib.menu;

import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.*;

@SuppressWarnings("unused")
public class BaseInventoryScreenHandler<T extends BaseInventoryScreenHandler<T>> extends ScreenHandler
{
    private record SlotKey(String name, int i, int j)
    {
        private static final Comparator<SlotKey> COMPARATOR;

        static
        {
            Comparator<SlotKey> comp = Comparator.comparing(SlotKey::name);
            comp = comp.thenComparingInt(SlotKey::i);
            comp = comp.thenComparingInt(SlotKey::j);
            COMPARATOR = comp;
        }
    }

    /**
     * simple container that prevents looping change
     */
    public static class BaseInventory<T extends BaseInventoryScreenHandler<T>> extends SimpleInventory
    {
        protected final T parent;
        private boolean updating = false;
        private int max = 64;

        public BaseInventory(int size, T menu)
        {
            super(size);
            parent = menu;
        }

        public BaseInventory<T> setMax(int max)
        {
            this.max = max;
            return this;
        }

        @Override
        public int getMaxCountPerStack()
        {
            return Math.min(max, super.getMaxCountPerStack());
        }

        @Override
        public void markDirty()
        {
            super.markDirty();
            if (!updating)
            {
                updating = true;
                parent.onContentChanged(this);
                updating = false;
            }
        }
    }

    /**
     * return items in the slot to player
     */
    public static void clearSlot(PlayerEntity pPlayer, Inventory inventory, int index)
    {
        if (!pPlayer.isAlive() || pPlayer instanceof ServerPlayerEntity && ((ServerPlayerEntity) pPlayer).isDisconnected())
        {
            pPlayer.dropItem(inventory.removeStack(index), false);
        } else
        {
            PlayerInventory plInv = pPlayer.getInventory();
            if (plInv.player instanceof ServerPlayerEntity)
            {
                plInv.offerOrDrop(inventory.removeStack(index));
            }
        }
    }

    public final PlayerInventory playerInventory;
    public final Inventory inventory;
    public final SpriteManager sprite;
    protected int added = 0;
    protected final boolean isVirtual;
    private boolean updating = false;
    private final Map<SlotKey, Slot> slotMap = new TreeMap<>(SlotKey.COMPARATOR);

    /**
     * This contructor will bind player inventory first, so player inventory has lower slot index.
     *
     * @param type      registered menu type
     * @param wid       window id
     * @param plInv     player inventory
     * @param manager   sprite manager used for slot positioning and rendering
     * @param factory   container supplier
     * @param isVirtual true if the slots should be cleared and item returned to player on menu close.
     */
    protected BaseInventoryScreenHandler(ScreenHandlerType<?> type, int wid, PlayerInventory plInv, SpriteManager manager, Function<T, SimpleInventory> factory, boolean isVirtual)
    {
        super(type, wid);
        this.playerInventory = plInv;
        inventory = factory.apply(Wrappers.cast(this));
        sprite = manager;
        int x = manager.get().getPlInvX();
        int y = manager.get().getPlInvY();
        this.bindPlayerInventory(plInv, x, y);
        this.isVirtual = isVirtual;
    }

    /**
     * Binds player inventory. Should not be called by others, but permits override.
     */
    protected void bindPlayerInventory(PlayerInventory plInv, int x, int y)
    {
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                addSlot(createSlot(plInv, j + i * 9 + 9, x + j * 18, y + i * 18));
        for (int k = 0; k < 9; ++k)
            addSlot(createSlot(plInv, k, x + k * 18, y + 58));
    }

    /**
     * Used by bindPlayerInventory only. Create slots as needed. Some slots could be locked.
     */
    protected Slot createSlot(PlayerInventory plInv, int slot, int x, int y)
    {
        return shouldLock(plInv, slot) ? new SlotLocked(plInv, slot, x, y) : new Slot(plInv, slot, x, y);
    }

    /**
     * Lock slots you don't want players modifying, such as the slot player is opening backpack in.
     */
    protected boolean shouldLock(PlayerInventory plInv, int slot)
    {
        return false;
    }

    /**
     * Add new slots, with item input predicate
     */
    protected void addSlot(String name, Predicate<ItemStack> pred)
    {
        sprite.get().getSlot(name, (x, y) -> new PredSlot(inventory, added++, x, y, pred), this::addSlot);
    }

    /**
     * Add new slots, with index-sensitive item input predicate.
     * The index here is relative to the first slot added by this method.
     */
    protected void addSlot(String name, BiPredicate<Integer, ItemStack> pred)
    {
        int current = added;
        sprite.get().getSlot(name, (x, y) ->
        {
            int i = added - current;
            var ans = new PredSlot(inventory, added, x, y, e -> pred.test(i, e));
            added++;
            return ans;
        }, this::addSlot);
    }

    /**
     * Add new slots, with other modifications to the slot.
     */
    protected void addSlot(String name, Predicate<ItemStack> pred, Consumer<PredSlot> modifier)
    {
        sprite.get().getSlot(name, (x, y) ->
        {
            PredSlot s = new PredSlot(inventory, added++, x, y, pred);
            modifier.accept(s);
            return s;
        }, this::addSlot);
    }

    /**
     * Add new slots, with index-sensitive modifications to the slot.
     * The index here is relative to the first slot added by this method.
     */
    protected void addSlot(String name, BiPredicate<Integer, ItemStack> pred, BiConsumer<Integer, PredSlot> modifier)
    {
        int current = added;
        sprite.get().getSlot(name, (x, y) ->
        {
            int i = added - current;
            var ans = new PredSlot(inventory, added, x, y, e -> pred.test(i, e));
            modifier.accept(i, ans);
            added++;
            return ans;
        }, this::addSlot);
    }

    /**
     * internal add slot
     */
    protected void addSlot(String name, int i, int j, Slot slot)
    {
        slotMap.put(new SlotKey(name, i, j), slot);
        this.addSlot(slot);
    }

    /**
     * get the slot by name and id in the grid
     */
    protected Slot getSlot(String name, int i, int j)
    {
        return slotMap.get(new SlotKey(name, i, j));
    }

    /**
     * get a slot as PredSlot, as most slots should be
     */
    public PredSlot getAsPredSlot(String name, int i, int j)
    {
        return (PredSlot) getSlot(name, i, j);
    }

    /**
     * get a slot as PredSlot, as most slots should be
     */
    public PredSlot getAsPredSlot(String name)
    {
        return (PredSlot) getSlot(name, 0, 0);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot)
    {
        ItemStack stack = slots.get(slot).getStack();
        int n = inventory.size();
        boolean moved;
        if (slot >= 36)
            moved = insertItem(stack, 0, 36, true);
        else
            moved = insertItem(stack, 36, 36 + n, false);
        if (moved) slots.get(slot).markDirty();
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player)
    {
        return player.isAlive();
    }

    @Override
    public void onClosed(PlayerEntity player)
    {
        if (isVirtual && !player.getWorld().isClient())
            clearInventoryFiltered(player, inventory);
        super.onClosed(player);
    }

    /**
     * return true (and when isVirtual is true), clear the corresponding slot on menu close.
     */
    protected boolean shouldClear(Inventory inventory, int slot)
    {
        return isVirtual;
    }

    /**
     * clear slots using shouldClear
     */
    protected void clearInventoryFiltered(PlayerEntity player, Inventory inventory)
    {
        if (!player.isAlive() || player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).isDisconnected())
            for (int j = 0; j < inventory.size(); ++j)
                if (shouldClear(inventory, j))
                    player.dropItem(inventory.removeStack(j), false);
                else
                {
                    PlayerInventory plInv = player.getInventory();
                    for (int i = 0; i < inventory.size(); ++i)
                        if (shouldClear(inventory, i))
                            if (plInv.player instanceof ServerPlayerEntity)
                                plInv.offerOrDrop(inventory.removeStack(i));
                }
    }

    @Override
    public void onContentChanged(Inventory inventory)
    {
        if (playerInventory.player.getWorld().isClient())
            super.onContentChanged(inventory);
        else
        {
            if (!updating)
            {
                updating = true;
                securedServerSlotChange(inventory);
                updating = false;
            }
            super.onContentChanged(inventory);
        }
    }

    /**
     * server side only slot change detector that will not be called recursively.
     */
    protected void securedServerSlotChange(Inventory inventory)
    {
    }
}
