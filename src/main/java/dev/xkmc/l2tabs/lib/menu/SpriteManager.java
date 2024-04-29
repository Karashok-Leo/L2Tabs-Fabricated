package dev.xkmc.l2tabs.lib.menu;

import dev.xkmc.l2tabs.lib.config.LayoutConfig;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public record SpriteManager(String modid, String path)
{
    public static final Map<Identifier, LayoutConfig> configs = new HashMap<>();

    public LayoutConfig get()
    {
        return configs.get(new Identifier(modid, path));
    }
}
