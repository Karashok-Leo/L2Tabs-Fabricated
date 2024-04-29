package dev.xkmc.l2tabs.tabs.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public enum TabType
{
    ABOVE(0, 0, 26, 32, 8),
    BELOW(84, 0, 26, 32, 8),
    LEFT(0, 64, 32, 26, 5),
    RIGHT(96, 64, 32, 26, 5);

    private static final Identifier TEXTURE = new Identifier("textures/gui/container/creative_inventory/tabs.png");
    public static final int MAX_TABS = 7;
    private final int textureX;
    private final int textureY;
    public final int width;
    public final int height;
    public final int max;

    TabType(int tx, int ty, int w, int h, int max)
    {
        this.textureX = tx;
        this.textureY = ty;
        this.width = w;
        this.height = h;
        this.max = max;
    }

    public void draw(DrawContext context, int x, int y, boolean selected, int index)
    {
        index = index % max;
        int tx = this.textureX;
        if (index > 0)
            tx += this.width;

        if (index == max - 1)
        {
            //tx += this.width;
        }

        int ty = selected ? this.textureY + this.height : this.textureY;
        context.drawTexture(TEXTURE, x, y, tx, ty, this.width, this.height);
    }

    public void drawIcon(DrawContext context, int x, int y, ItemStack stack)
    {
        int i = x;
        int j = y;
        switch (this)
        {
            case ABOVE ->
            {
                i += 6;
                j += 9;
            }
            case BELOW ->
            {
                i += 6;
                j += 6;
            }
            case LEFT ->
            {
                i += 10;
                j += 5;
            }
            case RIGHT ->
            {
                i += 6;
                j += 5;
            }
        }
        context.drawItemWithoutEntity(stack, i, j);
        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, stack, i, j);
    }

    public int getX(int pIndex)
    {
        return switch (this)
        {
            case ABOVE, BELOW -> this.width * pIndex;
            case LEFT -> -this.width + 4;
            case RIGHT -> -4;
        };
    }

    public int getY(int pIndex)
    {
        return switch (this)
        {
            case ABOVE -> -this.height + 4;
            case BELOW -> -4;
            case LEFT, RIGHT -> this.height * pIndex;
        };
    }

    public boolean isMouseOver(int x, int y, int i, double u, double v)
    {
        int left = x + this.getX(i);
        int bottom = y + this.getY(i);
        return u > (double) left && u < (double) (left + this.width) &&
                v > (double) bottom && v < (double) (bottom + this.height);
    }

    public int getTabX(int imgWidth, int index)
    {
        return (this == RIGHT ? imgWidth : 0) + getX(index);
    }

    public int getTabY(int imgHeight, int index)
    {
        return (this == BELOW ? imgHeight : 0) + getY(index);
    }

    public ButtonWidget getLeftButton(ITabScreen screen, ButtonWidget.PressAction o)
    {
        int radius = 3;
        int guiLeft = screen.getGuiLeft();
        int guiTop = screen.getGuiTop();

        return ButtonWidget
                .builder(Text.literal("<"), o)
                .dimensions(guiLeft + radius, guiTop - 26 + radius, 26 - radius * 2, 26 - radius * 2)
                .build();
    }

    public ButtonWidget getRightButton(ITabScreen screen, ButtonWidget.PressAction o)
    {
        int radius = 3;
        int guiLeft = screen.getGuiLeft();
        int guiTop = screen.getGuiTop();

        return ButtonWidget
                .builder(Text.literal(">"), o)
                .dimensions(guiLeft + (TabType.MAX_TABS - 1) * 26 + radius, guiTop - 26 + radius, 26 - radius * 2, 26 - radius * 2)
                .build();
    }
}