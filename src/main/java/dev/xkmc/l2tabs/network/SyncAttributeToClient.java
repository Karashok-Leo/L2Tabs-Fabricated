package dev.xkmc.l2tabs.network;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;

import java.util.ArrayList;
import java.util.UUID;

@SerialClass
public class SyncAttributeToClient implements SerialPacketBase
{
    @Deprecated
    public SyncAttributeToClient()
    {
    }

    @SerialClass.SerialField
    public int id;

    @SerialClass.SerialField
    public ArrayList<AttributeEntry> list = new ArrayList<>();

    public SyncAttributeToClient(LivingEntity le)
    {
        id = le.getId();
        for (var attr : le.getAttributes().getTracked())
        {
            ArrayList<ModifierEntry> modifiers = new ArrayList<>();
            for (var mod : attr.getModifiers())
                modifiers.add(new ModifierEntry(mod.getId(), mod.getName()));
            if (modifiers.isEmpty()) continue;
            list.add(new AttributeEntry(attr.getAttribute(), modifiers));
        }
    }

    public record ModifierEntry(UUID id, String name)
    {
    }

    public record AttributeEntry(EntityAttribute attr, ArrayList<ModifierEntry> list)
    {
    }
}