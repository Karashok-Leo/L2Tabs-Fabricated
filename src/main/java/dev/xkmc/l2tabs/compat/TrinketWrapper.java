package dev.xkmc.l2tabs.compat;

import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import dev.xkmc.l2tabs.compat.base.BaseTrinketWrapper;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class TrinketWrapper extends BaseTrinketWrapper
{
    private final ArrayList<TrinketSlotWrapper> list = new ArrayList<>();

    public TrinketWrapper(LivingEntity player, int page)
    {
        super(player);
        int max = 6;
        Optional<TrinketComponent> opt = TrinketsApi.getTrinketComponent(player);
        this.page = page;
        if (opt.isEmpty())
        {
            total = 0;
            return;
        }
        var trinketComponent = opt.get();
        int offset = page * max * 9;
        int count = 0;

        trinketComponent.update();
        var groups = trinketComponent.getGroups();
        for (Map.Entry<String, Map<String, TrinketInventory>> entry : trinketComponent.getInventory().entrySet())
        {
            String groupId = entry.getKey();
            SlotGroup group = groups.get(groupId);
            for (Map.Entry<String, TrinketInventory> slot : entry.getValue().entrySet().stream().sorted(Comparator.comparingInt(a -> a.getValue().getSlotType().getOrder())).toList())
            {
                TrinketInventory stacks = slot.getValue();
                if (stacks.size()==0) continue;
                for (int i = 0; i < stacks.size(); i++)
                {
                    count++;
                    if (offset > 0)
                        offset--;
                    else if (list.size() < max * 9)
                        list.add(new TrinketSlotWrapper(player, stacks, i, group));
                }
            }
        }

        this.total = (count - 1) / (max * 9) + 1;
    }

    public int getSize()
    {
        return list.size();
    }

    @Override
    public int getRows()
    {
        return list.isEmpty() ? 0 : (list.size() - 1) / 9 + 1;
    }

    @Nullable
    public TrinketSlotWrapper getSlotAtPosition(int i)
    {
        if (i < 0 || i >= list.size()) return null;
        return list.get(i);
    }
}
