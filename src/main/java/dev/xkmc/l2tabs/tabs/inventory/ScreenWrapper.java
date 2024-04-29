package dev.xkmc.l2tabs.tabs.inventory;

import dev.xkmc.l2tabs.mixin.HandledScreenAccessor;
import dev.xkmc.l2tabs.tabs.core.ITabScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

public record ScreenWrapper(int x, int y, int w, int h, Screen screen) implements ITabScreen
{
    public static ITabScreen of(Screen screen)
    {
        int x, y;
        if (screen instanceof HandledScreen<?> tx)
            return new ScreenWrapper(((HandledScreenAccessor) tx).getX(), ((HandledScreenAccessor) tx).getY(), tx.width, tx.height, screen);
        else
        {
            x = (screen.width - 176) / 2;
            y = (screen.height - 166) / 2;
        }
        return new ScreenWrapper(x, y, 176, 166, screen);
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
        return w;
    }

    @Override
    public int getYSize()
    {
        return h;
    }

    @Override
    public Screen asScreen()
    {
        return screen;
    }
}
