package dev.xkmc.l2tabs.tabs.content;

import dev.xkmc.l2tabs.L2TabsClient;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class AttributeScreen extends BaseAttributeScreen
{
    protected AttributeScreen(Text title, int page)
    {
        super(title, page);
    }

    @Override
    public void init()
    {
        super.init();
        new TabManager<>(this, new InvTabData()).init(this::addDrawableChild, L2TabsClient.TAB_ATTRIBUTE);
    }

    @Override
    public LivingEntity getEntity()
    {
        return MinecraftClient.getInstance().player;
    }

    protected void click(int nextPage)
    {
        MinecraftClient.getInstance().setScreen(new AttributeScreen(getTitle(), nextPage));
    }
}
