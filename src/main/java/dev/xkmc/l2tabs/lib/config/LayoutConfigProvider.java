package dev.xkmc.l2tabs.lib.config;

import dev.xkmc.l2tabs.L2Tabs;
import karashokleo.leobrary.data.AbstractDataProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.util.Identifier;

public class LayoutConfigProvider extends AbstractDataProvider
{
    public LayoutConfigProvider(FabricDataOutput output)
    {
        super(output, L2Tabs.LAYOUT_CONFIG_PATH);
    }

    @Override
    public void addAll()
    {
        for (int i = 0; i < 4; i++)
        {
            Identifier id = new Identifier(L2Tabs.MOD_ID, "trinkets_" + (i + 3));
            LayoutConfig config = LayoutConfig.of(id, 166 + 18 * i);
            config.comp.put("grid", LayoutConfig.rect(8, 17, 18, 18, 9, i + 3));
            config.side.put("slot", LayoutConfig.rect(176, 0, 18, 18, 1, 1));
            add(id, config);
        }
    }

    @Override
    public String getName()
    {
        return "L2 Lib Menu Layout Config Provider";
    }
}
