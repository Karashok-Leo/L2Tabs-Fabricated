package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2tabs.compat.base.BaseTrinketListScreen;
import dev.xkmc.l2tabs.tabs.core.ITabScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class TrinketListScreen extends BaseTrinketListScreen<TrinketListScreenHandler> implements ITabScreen
{
    public TrinketListScreen(TrinketListScreenHandler cont, PlayerInventory plInv, Text title) {
        super(cont, plInv, title);
    }

    @Override
    public void init() {
        super.init();
        new TabManager<>(this, new InvTabData()).init(this::addDrawableChild, TabTrinket.tab);
    }

    @Override
    public int getGuiLeft()
    {
        return x;
    }

    @Override
    public int getGuiTop()
    {
        return y;
    }

    @Override
    public int getXSize()
    {
        return width;
    }

    @Override
    public int getYSize()
    {
        return height;
    }
}
