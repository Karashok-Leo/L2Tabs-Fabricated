package dev.xkmc.l2tabs.tabs.inventory;

import dev.xkmc.l2tabs.tabs.core.TabGroup;
import dev.xkmc.l2tabs.tabs.core.TabType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TabRegistry
{
    public static final TabGroup<InvTabData> GROUP = new TabGroup<>(TabType.ABOVE);
}