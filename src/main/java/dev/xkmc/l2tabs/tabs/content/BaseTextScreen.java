package dev.xkmc.l2tabs.tabs.content;

import dev.xkmc.l2tabs.tabs.core.ITabScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class BaseTextScreen extends Screen implements ITabScreen
{
    private final Identifier texture;
    public int imageWidth, imageHeight, leftPos, topPos;

    protected BaseTextScreen(Text title, Identifier texture)
    {
        super(title);
        this.texture = texture;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    public void init()
    {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(DrawContext g, int mx, int my, float ptick)
    {
        renderBackground(g);
        int i = this.leftPos;
        int j = this.topPos;
        g.drawTexture(texture, i, j, 0, 0, this.imageWidth, this.imageHeight);
        super.render(g, mx, my, ptick);
    }

    public boolean keyPressed(int a, int b, int c)
    {
        if (MinecraftClient.getInstance().options.inventoryKey.matchesKey(a, b))
        {
            this.close();
            return true;
        }
        return super.keyPressed(a, b, c);
    }

    @Override
    public boolean shouldPause()
    {
        return false;
    }

    @Override
    public int getGuiLeft()
    {
        return leftPos;
    }

    @Override
    public int getGuiTop()
    {
        return topPos;
    }

    @Override
    public int getXSize()
    {
        return imageWidth;
    }

    @Override
    public int getYSize()
    {
        return imageHeight;
    }
}
