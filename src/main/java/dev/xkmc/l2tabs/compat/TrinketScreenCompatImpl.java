package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2tabs.L2Tabs;
import dev.xkmc.l2tabs.data.L2TabsLangData;
import dev.xkmc.l2tabs.tabs.inventory.TabRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TrinketScreenCompatImpl
{
    private static TrinketScreenCompatImpl INSTANCE;

    public static TrinketScreenCompatImpl get()
    {
        if (INSTANCE == null)
            INSTANCE = new TrinketScreenCompatImpl();
        return INSTANCE;
    }

    ExtendedScreenHandlerType<TrinketListScreenHandler> ScreenHandlerType;

    void onStartUp()
    {
        ScreenHandlerType = Registry.register(Registries.SCREEN_HANDLER, new Identifier(L2Tabs.MOD_ID, "trinket"), new ExtendedScreenHandlerType<>(TrinketListScreenHandler::fromNetwork));
    }

    void onClientInit()
    {
        HandledScreens.register(ScreenHandlerType, TrinketListScreen::new);
        TabTrinket.tab = TabRegistry.GROUP.registerTab(2000, TabTrinket::new, () -> Items.AIR, L2TabsLangData.CURIOS.get());
    }

    void openScreen(ServerPlayerEntity player)
    {
        TrinketEventHandler.openMenuWrapped(player, () -> new TrinketScreenHandlerFactory(ScreenHandlerType).open(player));
    }
}
