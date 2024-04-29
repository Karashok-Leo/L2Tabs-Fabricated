package dev.xkmc.l2tabs.data;

import dev.xkmc.l2tabs.L2Tabs;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

public class L2TabsConfig
{
    private static Global CONFIG;

    public static ClientConfig client()
    {
        return CONFIG.client;
    }

    public static CommonConfig common()
    {
        return CONFIG.common;
    }

    public static void register()
    {
        AutoConfig.register(
                Global.class,
                PartitioningSerializer.wrap(JanksonConfigSerializer::new)
        );
        CONFIG = AutoConfig.getConfigHolder(Global.class).getConfig();
    }

    @Config(name = L2Tabs.MOD_ID)
    static class Global extends PartitioningSerializer.GlobalData
    {
        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.TransitiveObject
        ClientConfig client;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.TransitiveObject
        CommonConfig common;
    }

    @Config(name = "client")
    public static class ClientConfig implements ConfigData
    {
        @Comment("Show inventory tabs")
        public final boolean showTabs = true;
        @Comment("Show inventory tabs only in trinket page. Only works when showTabs is true and trinket is installed.")
        public final boolean showTabsOnlyTrinket = false;
        @Comment("Number of attribure lines per page")
        public final int attributeLinePerPage = 15;
        @Comment(
                "List of tabs to hide. Use title translation key for tab id." +
                        "Example: menu.tabs.attribute for attribute tab" +
                        "Example: menu.tabs.trinkets for trinkets tab" +
                        "Example: pandora.menu.title for pandora tab"
        )
        public final List<String> hiddenTabs = new ArrayList<>();
    }

    @Config(name = "common")
    public static class CommonConfig implements ConfigData
    {
        @Comment("Sync player attribute names to client")
        public final boolean syncPlayerAttributeName = true;
        @Comment("Sync all entity attribute name to client")
        public final boolean syncAllEntityAttributeName = false;
    }
}
