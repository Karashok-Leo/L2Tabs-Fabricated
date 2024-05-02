package dev.xkmc.l2tabs;

import dev.xkmc.l2tabs.compat.TabTrinketCompat;
import dev.xkmc.l2tabs.data.L2TabsLangData;
import dev.xkmc.l2tabs.network.NetworkHandlers;
import dev.xkmc.l2tabs.tabs.content.TabAttributes;
import dev.xkmc.l2tabs.tabs.content.TabInventory;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import dev.xkmc.l2tabs.tabs.inventory.TabRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.Items;

public class L2TabsClient implements ClientModInitializer
{
    public static TabToken<InvTabData, TabInventory> TAB_INVENTORY;
    public static TabToken<InvTabData, TabAttributes> TAB_ATTRIBUTE;

    @Override
    public void onInitializeClient()
    {
        NetworkHandlers.registerClient();
        TabInventory.register();
        TAB_INVENTORY = TabRegistry.GROUP.registerTab(0, TabInventory::new, () -> Items.CRAFTING_TABLE, L2TabsLangData.INVENTORY.get());
        TAB_ATTRIBUTE = TabRegistry.GROUP.registerTab(1000, TabAttributes::new, () -> Items.IRON_SWORD, L2TabsLangData.ATTRIBUTE.get());

        // NYI
//        TabTrinketCompat.onClientInit();
    }
}
