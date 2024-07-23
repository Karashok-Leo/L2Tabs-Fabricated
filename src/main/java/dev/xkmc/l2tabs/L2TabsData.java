package dev.xkmc.l2tabs;

import dev.xkmc.l2tabs.data.AttributeConfigLoader;
import dev.xkmc.l2tabs.data.AttributeConfigProvider;
import dev.xkmc.l2tabs.data.L2TabsLangData;
import dev.xkmc.l2tabs.lib.config.LayoutConfigLoader;
import dev.xkmc.l2tabs.lib.config.LayoutConfigProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class L2TabsData implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
    {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(AttributeConfigProvider::new);
        pack.addProvider(LayoutConfigProvider::new);
        pack.addProvider((FabricDataOutput output) -> new FabricLanguageProvider(output, "en_us")
        {
            @Override
            public void generateTranslations(TranslationBuilder builder)
            {
                for (L2TabsLangData lang : L2TabsLangData.values())
                    builder.add(lang.key, lang.defEn);
            }
        });
        pack.addProvider((FabricDataOutput output) -> new FabricLanguageProvider(output, "zh_cn")
        {
            @Override
            public void generateTranslations(TranslationBuilder builder)
            {
                for (L2TabsLangData lang : L2TabsLangData.values())
                    builder.add(lang.key, lang.defZh);
            }
        });
    }

    public static void register()
    {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new LayoutConfigLoader());
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new AttributeConfigLoader());
    }
}
