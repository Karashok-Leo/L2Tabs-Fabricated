package dev.xkmc.l2tabs.tabs.content;

import dev.xkmc.l2tabs.L2TabsClient;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import dev.xkmc.l2tabs.tabs.inventory.ScreenWrapper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.function.Predicate;

public class TabInventory extends TabBase<InvTabData, TabInventory>
{
    public static Predicate<Screen> inventoryTest = e -> e instanceof InventoryScreen;
    public static Runnable openInventory = () ->
    {
        MinecraftClient ins = MinecraftClient.getInstance();
        assert ins.player != null;
        ins.setScreen(new InventoryScreen(MinecraftClient.getInstance().player));
    };

    public static void register()
    {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
        {
            if (inventoryTest.test(screen))
            {
                var manager = new TabManager<>(ScreenWrapper.of(screen), new InvTabData());
                manager.init(screen::addDrawableChild, L2TabsClient.TAB_INVENTORY);
            }
        });
    }

    public static void guiPostRenderBG(Screen screen, DrawContext context)
    {
        for (var e : screen.children())
            if (e instanceof TabBase<?, ?> tab)
                if (tab.manager.selected != tab.token)
                    tab.renderBackground(context);
    }

    public TabInventory(int index, TabToken<InvTabData, TabInventory> token, TabManager<InvTabData> manager, ItemStack stack, Text title)
    {
        super(index, token, manager, stack, title);
    }

    public void onTabClicked()
    {
        openInventory.run();
    }
}
