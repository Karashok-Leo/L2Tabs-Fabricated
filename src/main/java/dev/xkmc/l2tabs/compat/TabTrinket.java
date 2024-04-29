package dev.xkmc.l2tabs.compat;

import dev.emi.trinkets.TrinketsMain;
import dev.xkmc.l2tabs.L2Tabs;
import dev.xkmc.l2tabs.network.OpenTrinketHandler;
import dev.xkmc.l2tabs.network.OpenTrinketPacket;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TabTrinket extends TabBase<InvTabData, TabTrinket>
{
    public static TabToken<InvTabData, TabTrinket> tab;

    public TabTrinket(int index, TabToken<InvTabData, TabTrinket> token, TabManager<InvTabData> manager, ItemStack stack, Text title)
    {
        super(index, token, manager, stack, title);
    }

    @Override
    public void onTabClicked()
    {
        ClientPlayNetworking.send(L2Tabs.HANDLER.getPacket(new OpenTrinketPacket(OpenTrinketHandler.CURIO_OPEN)));
    }

    @Override
    public void renderBackground(DrawContext context)
    {
        if (!this.visible) return;
        token.getType().draw(context, getX(), getY(), manager.selected == token, index);
        context.drawTexture(new Identifier(TrinketsMain.MOD_ID, "textures/gui/slots/ring.png"), getX() + 6, getY() + 10, 50, 14, 14, 14);
    }
}
