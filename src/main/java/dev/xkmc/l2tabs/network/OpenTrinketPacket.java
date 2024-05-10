package dev.xkmc.l2tabs.network;

import dev.xkmc.l2serial.network.SerialPacketC2S;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.server.network.ServerPlayerEntity;

@SerialClass
public class OpenTrinketPacket implements SerialPacketC2S
{
    @SerialClass.SerialField
    public OpenTrinketHandler event;

    @Deprecated
    public OpenTrinketPacket()
    {
    }

    public OpenTrinketPacket(OpenTrinketHandler event)
    {
        this.event = event;
    }

    @Override
    public void handle(ServerPlayerEntity player)
    {
        event.invoke(player);
    }
}