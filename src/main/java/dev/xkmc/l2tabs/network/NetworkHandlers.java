package dev.xkmc.l2tabs.network;

import dev.xkmc.l2tabs.L2Tabs;

public class NetworkHandlers
{
    public static void registerMain()
    {
        L2Tabs.HANDLER.configure(SyncAttributeToClient.class);
    }

    public static void registerClient()
    {
        L2Tabs.HANDLER.configureS2C(SyncAttributeToClient.class);
    }
}
