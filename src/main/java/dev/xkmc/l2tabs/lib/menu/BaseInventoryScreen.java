package dev.xkmc.l2tabs.lib.menu;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

@SuppressWarnings("unused")
public abstract class BaseInventoryScreen<T extends BaseInventoryScreenHandler<T>> extends HandledScreen<T>
{
    public BaseInventoryScreen(T cont, PlayerInventory plInv, Text title)
    {
        super(cont, plInv, title);
        this.backgroundHeight = handler.sprite.get().getHeight();
        this.playerInventoryTitleY = handler.sprite.get().getPlInvY() - 11;
    }

    @Override
    public void render(DrawContext g, int mx, int my, float partial)
    {
        super.render(g, mx, my, partial);
        drawMouseoverTooltip(g, mx, my);
    }

    protected boolean click(int btn)
    {
        MinecraftClient instance = MinecraftClient.getInstance();
        if (handler.onButtonClick(instance.player, btn) &&
                instance.interactionManager != null)
        {
            instance.interactionManager.clickButton(this.handler.syncId, btn);
            return true;
        }
        return false;
    }
}
