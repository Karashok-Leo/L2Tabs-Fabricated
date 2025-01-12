package dev.xkmc.l2tabs;

import dev.xkmc.l2serial.network.PacketHandler;
import dev.xkmc.l2tabs.data.AttributeDisplayConfig;
import dev.xkmc.l2tabs.data.L2TabsConfig;
import dev.xkmc.l2tabs.network.NetworkHandlers;
import dev.xkmc.l2tabs.network.SyncAttributeToClient;
import dev.xkmc.l2tabs.network.SyncConfigToClient;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class L2Tabs implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger("l2tabs");

    @Override
    public void onInitialize()
    {
        NetworkHandlers.registerMain();
        L2TabsConfig.register();
        L2TabsData.register();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> sender.sendPacket(HANDLER.getPacket(new SyncConfigToClient(ATTRIBUTE_ENTRY))));
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> ServerPlayNetworking.send(player, HANDLER.getPacket(new SyncConfigToClient(ATTRIBUTE_ENTRY))));
    }

    public static final String MOD_ID = "l2tabs";
    public static final String ATTRIBUTE_CONFIG_PATH = "l2tabs_config/attribute_entry";
    public static final String LAYOUT_CONFIG_PATH = "l2library_config/menu_layout";

    public static final PacketHandler HANDLER = new PacketHandler(MOD_ID);

    public static final AttributeDisplayConfig ATTRIBUTE_ENTRY = new AttributeDisplayConfig();

    public static void onAttributeUpdate(LivingEntity le)
    {
        var f = new SyncAttributeToClient(le);
        if (f.list.isEmpty()) return;
        if (le instanceof ServerPlayerEntity sp)
            if (L2TabsConfig.common().syncPlayerAttributeName)
                ServerPlayNetworking.send(sp, HANDLER.getPacket(f));
        if (L2TabsConfig.common().syncAllEntityAttributeName)
            PlayerLookup.tracking(le).forEach(player -> ServerPlayNetworking.send(player, HANDLER.getPacket(f)));
    }
}