package dev.xkmc.l2tabs.lib.config;

import dev.xkmc.l2tabs.L2Tabs;
import dev.xkmc.l2tabs.lib.menu.SpriteManager;
import karashokleo.leobrary.data.AbstractDataLoader;
import net.minecraft.util.Identifier;

public class LayoutConfigLoader extends AbstractDataLoader<LayoutConfig>
{
    public LayoutConfigLoader()
    {
        super(L2Tabs.LAYOUT_CONFIG_PATH, LayoutConfig.class);
    }

    @Override
    protected void clear()
    {
        SpriteManager.configs.clear();
    }

    @Override
    protected void load(Identifier id, LayoutConfig config)
    {
        config.id = id;
        SpriteManager.configs.put(id, config);
    }

    @Override
    public Identifier getFabricId()
    {
        return new Identifier(L2Tabs.MOD_ID, "layout");
    }
}
