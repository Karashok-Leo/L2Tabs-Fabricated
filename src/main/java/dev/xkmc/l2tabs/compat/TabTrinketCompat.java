package dev.xkmc.l2tabs.compat;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;

public class TabTrinketCompat
{
    public static void onStartup()
    {
        TrinketScreenCompatImpl.get().onStartUp();
    }

    public static void onClientInit()
    {
        TrinketScreenCompatImpl.get().onClientInit();
    }

    public static void openCuriosTab(ServerPlayerEntity player)
    {
        TrinketScreenCompatImpl.get().openScreen(player);
    }

    public static ScreenHandlerType<?> getScreenHandlerType()
    {
        return TrinketScreenCompatImpl.get().ScreenHandlerType;
    }
}
