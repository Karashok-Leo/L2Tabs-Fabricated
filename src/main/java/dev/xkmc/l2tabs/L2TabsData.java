package dev.xkmc.l2tabs;

import dev.xkmc.l2tabs.data.AttributeConfigProvider;
import dev.xkmc.l2tabs.data.AttributeConfigLoader;
import dev.xkmc.l2tabs.data.L2TabsLangData;
import dev.xkmc.l2tabs.lib.config.LayoutConfigLoader;
import dev.xkmc.l2tabs.lib.config.LayoutConfigProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.karashokleo.leobrary.datagen.generator.LanguageGenerator;
import net.minecraft.resource.ResourceType;

public class L2TabsData implements DataGeneratorEntrypoint
{
    public static final LanguageGenerator EN_TEXTS = new LanguageGenerator("en_us");
    public static final LanguageGenerator ZH_TEXTS = new LanguageGenerator("zh_cn");

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
    {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(LayoutConfigProvider::new);
        pack.addProvider(AttributeConfigProvider::new);
        L2TabsLangData.genLang();
        EN_TEXTS.generate(pack);
        ZH_TEXTS.generate(pack);
    }

    public static void register()
    {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new LayoutConfigLoader());
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new AttributeConfigLoader());
    }
}
