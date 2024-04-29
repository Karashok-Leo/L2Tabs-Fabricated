package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class TabToken<G extends TabGroupData<G>, T extends TabBase<G, T>>
{
    private final TabGroup<G> group;
    private final TabFactory<G, T> factory;
    private final Supplier<Item> item;
    public final Text title;

    TabToken(TabGroup<G> group, TabFactory<G, T> factory, Supplier<Item> item, Text title)
    {
        this.group = group;
        this.factory = factory;
        this.item = item;
        this.title = title;
    }

    public TabType getType()
    {
        return group.type;
    }

    public T create(int index, TabManager<G> manager)
    {
        return factory.create(index, this, manager, item.get().getDefaultStack(), title);
    }

    public interface TabFactory<G extends TabGroupData<G>, T extends TabBase<G, T>>
    {
        T create(int index, TabToken<G, T> token, TabManager<G> manager, ItemStack stack, Text component);
    }
}