package dev.xkmc.l2tabs.network;

import dev.xkmc.l2serial.network.SerialPacketS2C;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;

import java.util.ArrayList;
import java.util.UUID;

@SerialClass
public class SyncAttributeToClient implements SerialPacketS2C
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

    @Override
    public void handle(ClientPlayerEntity player)
    {
        var level = MinecraftClient.getInstance().world;
        if (level == null) return;
        var entity = level.getEntityById(id);
        if (!(entity instanceof LivingEntity le)) return;
        for (var attr : list)
        {
            var ins = le.getAttributeInstance(attr.attr());
            if (ins == null) continue;
            for (var ent : attr.list())
            {
                var mod = ins.getModifier(ent.id());
                if (mod == null) continue;
                mod.nameGetter = ent::name;
            }
        }
    }

    public record ModifierEntry(UUID id, String name)
    {
    }

    public record AttributeEntry(EntityAttribute attr, ArrayList<ModifierEntry> list)
    {
    }
}