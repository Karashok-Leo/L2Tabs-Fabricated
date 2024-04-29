package dev.xkmc.l2tabs.tabs.content;

import net.minecraft.entity.attribute.EntityAttribute;

public record AttributeEntry(EntityAttribute attr, boolean usePercent, int order, double intrinsic)
{
}
