package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public abstract class TabBase<G extends TabGroupData<G>, T extends TabBase<G, T>> extends ButtonWidget
{
    public final int index;
    public final ItemStack stack;
    public final TabToken<G, T> token;
    public final TabManager<G> manager;

    public int page;

    @SuppressWarnings("unchecked")
    public TabBase(int index, TabToken<G, T> token, TabManager<G> manager, ItemStack stack, Text title)
    {
        super(0, 0, token.getType().width, token.getType().height, title, b -> ((T) b).onTabClicked(), Supplier::get);
        this.index = index;
        this.stack = stack;
        this.token = token;
        this.manager = manager;
    }

    public abstract void onTabClicked();

    public void onTooltip(DrawContext context, int x, int y)
    {
        context.drawTooltip(MinecraftClient.getInstance().textRenderer, getMessage(), x, y);
    }

    public void renderBackground(DrawContext context)
    {
        if (!this.visible) return;
        token.getType().draw(context, getX(), getY(), manager.selected == token, index);
        renderIcon(context);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta)
    {
        if (manager.selected == token)
            renderBackground(context);
        if (this == manager.list.get(manager.list.size() - 1)) // draw on last
            manager.onToolTipRender(context, mouseX, mouseY);
    }

    protected void renderIcon(DrawContext context)
    {
        if (this.stack.isEmpty()) return;
        token.getType().drawIcon(context, getX(), getY(), this.stack);
    }
}