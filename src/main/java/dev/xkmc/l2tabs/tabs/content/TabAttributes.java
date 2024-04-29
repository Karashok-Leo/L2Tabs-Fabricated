package dev.xkmc.l2tabs.tabs.content;

import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class TabAttributes extends TabBase<InvTabData, TabAttributes>
{
    public TabAttributes(int index, TabToken<InvTabData, TabAttributes> token, TabManager<InvTabData> manager, ItemStack stack, Text title)
    {
        super(index, token, manager, stack, title);
    }

    @Override
    public void onTabClicked()
    {
        MinecraftClient.getInstance().setScreen(new AttributeScreen(this.getMessage(), 0));
    }
}