package dev.xkmc.l2tabs.network;

import dev.xkmc.l2serial.network.SerialPacketS2C;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2tabs.L2Tabs;
import dev.xkmc.l2tabs.data.AttributeDisplayConfig;
import net.minecraft.client.network.ClientPlayerEntity;

@SerialClass
public class SyncConfigToClient implements SerialPacketS2C
{
    @Deprecated
    public SyncConfigToClient()
    {
    }

    @SerialClass.SerialField
    public AttributeDisplayConfig config;

    public SyncConfigToClient(AttributeDisplayConfig config)
    {
        this.config = config;
    }

    @Override
    public void handle(ClientPlayerEntity player)
    {
        L2Tabs.ATTRIBUTE_ENTRY.clear();
        L2Tabs.ATTRIBUTE_ENTRY.accept(config);
    }
}
