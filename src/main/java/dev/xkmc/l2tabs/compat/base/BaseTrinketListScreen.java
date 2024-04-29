package dev.xkmc.l2tabs.compat.base;

import dev.emi.trinkets.TrinketSlot;
import dev.xkmc.l2tabs.lib.menu.BaseInventoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class BaseTrinketListScreen<T extends BaseTrinketListScreenHandler<T>> extends BaseInventoryScreen<T>
{
    public BaseTrinketListScreen(T cont, PlayerInventory plInv, Text title)
    {
        super(cont, plInv, title);
    }

    @Override
    protected void init()
    {
        super.init();
        if (this.y < 28) this.y = 28;
        int w = 10;
        int h = 11;
        int x = this.x + this.titleX + textRenderer.getWidth(getTitle()) + 14;
        int y = this.y + 4;
        if (handler.trinkets.page > 0)
            addDrawableChild(ButtonWidget.builder(Text.literal("<"), e -> click(1)).position(x - w - 1, y).size(w, h).build());
        if (handler.trinkets.page < handler.trinkets.total - 1)
            addDrawableChild(ButtonWidget.builder(Text.literal(">"), e -> click(2)).position(x, y).size(w, h).build());
    }


    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY)
    {
        var sr = handler.sprite.get().getRenderer(this);
        sr.start(context);
        for (int i = 0; i < handler.trinkets.getRows() * 9; i++)
            if (handler.trinkets.getSlotAtPosition(i) != null)
                sr.draw(context, "grid", "slot", i % 9 * 18 - 1, i / 9 * 18 - 1);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int x, int y)
    {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        Slot slot = this.focusedSlot;
        if (clientPlayer != null && clientPlayer.playerScreenHandler
                .getCursorStack().isEmpty() && slot != null)
            if (slot instanceof TrinketSlot trinketSlot && !slot.hasStack())
                context.drawTooltip(textRenderer, trinketSlot.getType().getTranslation(), x, y);
        super.drawMouseoverTooltip(context, x, y);
    }
}
