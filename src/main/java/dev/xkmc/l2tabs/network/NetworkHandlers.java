package dev.xkmc.l2tabs.network;

import dev.xkmc.l2tabs.L2Tabs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;

public class NetworkHandlers
{
    public static void registerMain()
    {
        ServerPlayNetworking.registerGlobalReceiver(L2Tabs.HANDLER.getPacketType(OpenTrinketPacket.class), (packet, player, responseSender) ->
                packet.packet().event.invoke(player));
    }

    public static void registerClient()
    {
        ClientPlayNetworking.registerGlobalReceiver(L2Tabs.HANDLER.getPacketType(SyncAttributeToClient.class), (packet, player, responseSender) ->
                handleSyncAttribute(packet.packet().id, packet.packet().list));
    }

    public static void handleSyncAttribute(int id, ArrayList<SyncAttributeToClient.AttributeEntry> list)
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
}
