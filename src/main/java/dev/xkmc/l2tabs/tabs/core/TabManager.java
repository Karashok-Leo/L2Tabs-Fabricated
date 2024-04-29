package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TabManager<G extends TabGroupData<G>>
{
    protected final List<TabBase<G, ?>> list = new ArrayList<>();
    public final ITabScreen screen;
    public final G token;
    private ButtonWidget left, right;
    public int tabPage;
    public TabToken<G, ?> selected;
    private int maxPages = 0;

    public TabManager(ITabScreen screen, G token)
    {
        this.screen = screen;
        this.token = token;
    }

    public void init(Consumer<ClickableWidget> adder, TabToken<G, ?> selected)
    {
        if (!token.shouldRender()) return;
        TabGroup<G> group = token.getGroup();
        List<TabToken<G, ?>> token_list = new ArrayList<>(token.getTabs());
        token_list.removeIf(token::shouldHideTab);
        list.clear();
        this.selected = selected;
        int guiLeft = screen.getGuiLeft();
        int guiTop = screen.getGuiTop();
        int imgWidth = screen.getXSize();
        int imgHeight = screen.getYSize();
        int index = 0, order = 0, page = 0;

        for (TabToken<G, ?> token : token_list)
        {
            if (index > 0 && order == 0)
                order++;
            if (token == selected)
                tabPage = page;
            TabBase<G, ?> tab = token.create(order, this);
            tab.page = page;
            tab.setX(guiLeft + group.type.getTabX(imgWidth, order));
            tab.setY(guiTop + group.type.getTabY(imgHeight, order));
            list.add(tab);
            adder.accept(tab);

            order++;
            if (order == TabType.MAX_TABS - 1)
            {
                order = 0;
                page++;
            }
            index++;
        }

        maxPages = order == 0 ? page : page + 1;

        adder.accept(left = group.type.getLeftButton(screen, b ->
        {
            tabPage = Math.max(tabPage - 1, 0);
            updateVisibility();
        }));
        adder.accept(right = group.type.getRightButton(screen, b ->
        {
            tabPage = Math.min(tabPage + 1, maxPages);
            updateVisibility();
        }));

        updateVisibility();
    }

    private void updateVisibility()
    {
        left.visible = left.active = tabPage > 0;
        right.visible = right.active = tabPage < maxPages - 1;
        for (TabBase<G, ?> tab : list)
        {
            tab.visible = tab.page == tabPage;
            tab.active = tab.visible;
        }
    }

    public Screen getScreen()
    {
        return screen.asScreen();
    }

    public void onToolTipRender(DrawContext stack, int mouseX, int mouseY)
    {
        for (TabBase<G, ?> tab : list)
            if (tab.visible && tab.isSelected())
                tab.onTooltip(stack, mouseX, mouseY);
    }
}
