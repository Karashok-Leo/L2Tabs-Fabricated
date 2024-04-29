package dev.xkmc.l2tabs.compat.base;

import dev.xkmc.l2tabs.L2Tabs;
import dev.xkmc.l2tabs.compat.TrinketEventHandler;
import dev.xkmc.l2tabs.lib.menu.BaseInventoryScreenHandler;
import dev.xkmc.l2tabs.lib.menu.SpriteManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class BaseTrinketListScreenHandler<T extends BaseInventoryScreenHandler<T>> extends BaseInventoryScreenHandler<T>
{
    public static final SpriteManager[] MANAGER;

    private static SpriteManager getManager(int size)
    {
        return MANAGER[Math.min(Math.max(size - 3, 0), 3)];
    }

    static
    {
        MANAGER = new SpriteManager[4];
        for (int i = 0; i < 4; i++)
        {
            MANAGER[i] = new SpriteManager(L2Tabs.MOD_ID, "trinkets_" + (i + 3));
        }
    }

    public final BaseTrinketWrapper trinkets;

    protected BaseTrinketListScreenHandler(ScreenHandlerType<?> type, int wid, PlayerInventory plInv, BaseTrinketWrapper trinkets)
    {
        super(type, wid, plInv, getManager(trinkets.getRows()), e -> new BaseInventory<>(trinkets.getSize(), e), false);
        addTrinketSlot("grid", trinkets);
        this.trinkets = trinkets;
    }

    protected void addTrinketSlot(String name, BaseTrinketWrapper trinkets)
    {
        int current = added;
        int[] removed = new int[]{0};
        sprite.get().getSlot(name, (x, y) ->
        {
            int i = added - current;
            var slot = trinkets.getSlotAtPosition(i + removed[0]);
            if (slot == null)
            {
                removed[0]++;
                return null;
            }
            var ans = slot.toSlot(x, y);
            added++;
            return ans;
        }, this::addSlot);
    }

    private boolean checkSwitch(PlayerEntity player, int page)
    {
        if (page >= 0 && page < trinkets.total)
        {
            if (player instanceof ServerPlayerEntity sp)
                TrinketEventHandler.openMenuWrapped(sp, () -> switchPage(sp, page));
            else
                slots.clear();
            return true;
        }
        return false;
    }

    public abstract void switchPage(ServerPlayerEntity sp, int page);

    @Override
    public boolean onButtonClick(PlayerEntity player, int id)
    {
        if (id == 1) return checkSwitch(player, trinkets.page - 1);
        else if (id == 2) return checkSwitch(player, trinkets.page + 1);
        else return super.onButtonClick(player, id);
    }
}
