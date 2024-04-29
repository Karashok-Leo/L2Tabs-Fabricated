package dev.xkmc.l2tabs.data;

import dev.xkmc.l2tabs.L2Tabs;
import net.karashokleo.leobrary.data.AbstractDataLoader;
import net.minecraft.util.Identifier;

public class AttributeConfigLoader extends AbstractDataLoader<AttributeDisplayConfig>
{
    public AttributeConfigLoader()
    {
        super(L2Tabs.ATTRIBUTE_CONFIG_PATH, AttributeDisplayConfig.class);
    }

    @Override
    protected void clear()
    {
        L2Tabs.ATTRIBUTE_ENTRY.clear();
    }

    @Override
    protected void load(Identifier id, AttributeDisplayConfig config)
    {
        L2Tabs.ATTRIBUTE_ENTRY.accept(config);
    }

    @Override
    public Identifier getFabricId()
    {
        return new Identifier(L2Tabs.MOD_ID, "attribute_entry");
    }
}
