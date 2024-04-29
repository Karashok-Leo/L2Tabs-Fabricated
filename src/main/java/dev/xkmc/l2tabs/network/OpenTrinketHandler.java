package dev.xkmc.l2tabs.network;

import dev.xkmc.l2tabs.compat.TabTrinketCompat;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Consumer;

public enum OpenTrinketHandler
{
    CURIO_OPEN(TabTrinketCompat::openCuriosTab);

    private final Consumer<ServerPlayerEntity> task;

    OpenTrinketHandler(Consumer<ServerPlayerEntity> run) {
        this.task = run;
    }

    public void invoke(ServerPlayerEntity player) {
        task.accept(player);
    }
}
