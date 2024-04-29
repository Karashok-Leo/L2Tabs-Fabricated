package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.client.gui.screen.Screen;

public interface ITabScreen
{
    int getGuiLeft();

    int getGuiTop();

    default int screenWidth()
    {
        return asScreen().width;
    }

    default int screenHeight()
    {
        return asScreen().height;
    }

    int getXSize();

    int getYSize();

    default Screen asScreen()
    {
        return (Screen) this;
    }
}
