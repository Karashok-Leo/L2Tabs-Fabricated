package dev.xkmc.l2tabs.network;


import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;

@SerialClass
public class OpenTrinketPacket implements SerialPacketBase
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
}