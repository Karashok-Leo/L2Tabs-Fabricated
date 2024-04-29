package dev.xkmc.l2tabs.tabs.inventory;

import dev.xkmc.l2tabs.data.L2TabsConfig;
import dev.xkmc.l2tabs.tabs.core.TabGroupData;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.text.TranslatableTextContent;

public class InvTabData extends TabGroupData<InvTabData>
{
    public InvTabData()
    {
        super(TabRegistry.GROUP);
    }

    @Override
    public boolean shouldRender()
    {
        return L2TabsConfig.client().showTabs;
    }

    @Override
    public <X extends TabGroupData<X>> boolean shouldHideTab(TabToken<X, ?> e)
    {
        return e.title.getContent() instanceof TranslatableTextContent tr &&
                L2TabsConfig.client().hiddenTabs.contains(tr.getKey());
    }
}