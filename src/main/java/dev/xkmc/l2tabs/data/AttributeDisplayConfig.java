package dev.xkmc.l2tabs.data;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2tabs.L2Tabs;
import dev.xkmc.l2tabs.tabs.content.AttributeEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SerialClass
public class AttributeDisplayConfig
{
    public static List<AttributeEntry> get()
    {
        AttributeDisplayConfig config = L2Tabs.ATTRIBUTE_ENTRY;
        if (config.cache.isEmpty())
            config.merge();
        return config.cache;
    }

    public static List<AttributeEntry> get(LivingEntity le)
    {
        return get().stream().filter(e -> le.getAttributeInstance(e.attr()) != null).toList();
    }

    @SerialClass.SerialField
    protected final ArrayList<AttributeDataEntry> list = new ArrayList<>();

    private final ArrayList<AttributeEntry> cache = new ArrayList<>();

    public void accept(AttributeDisplayConfig config)
    {
        this.list.addAll(config.list);
    }

    public void clear()
    {
        list.clear();
        cache.clear();
    }

    protected void merge()
    {
        for (var e : list)
        {
            if (!Registries.ATTRIBUTE.containsId(e.id()))
                continue;
            EntityAttribute attr = Registries.ATTRIBUTE.get(e.id());
            if (attr == null)
                continue;
            cache.add(new AttributeEntry(attr.setTracked(true), e.usePercent(), e.order(), e.intrinsic()));
        }
        cache.sort(Comparator.comparingInt(AttributeEntry::order));
    }

    public AttributeDisplayConfig add(EntityAttribute attr, boolean usePercent, int order, double intrinsic)
    {
        list.add(new AttributeDataEntry(Registries.ATTRIBUTE.getId(attr), usePercent, order, intrinsic));
        return this;
    }

    public AttributeDisplayConfig add(EntityAttribute attr, int order)
    {
        return add(attr, false, order, 0);
    }

    public record AttributeDataEntry(Identifier id, boolean usePercent, int order, double intrinsic)
    {
    }
}