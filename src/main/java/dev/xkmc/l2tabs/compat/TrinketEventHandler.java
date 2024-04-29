package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2tabs.compat.base.BaseTrinketListScreenHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.LinkedHashMap;
import java.util.Map;

public class TrinketEventHandler
{
    public static void onSlotModifierUpdate(LivingEntity entity)
    {
        if (entity.getWorld() instanceof ServerWorld sl)
            for (var player : sl.getPlayers())
                if (player.currentScreenHandler instanceof BaseTrinketListScreenHandler<?> menu)
                    if (menu.trinkets.entity == entity)
                        MAP.put(player, () -> openMenuWrapped(player,
                                () -> menu.switchPage(player, menu.trinkets.page)));
    }

    private static final Map<PlayerEntity, Runnable> MAP = new LinkedHashMap<>();

    public static void onPlayerTick(ServerPlayerEntity player)
    {
        if (MAP.isEmpty()) return;
        var run = MAP.get(player);
        if (run == null) return;
        if (player.isAlive() && !player.isDisconnected())
            run.run();
        MAP.remove(player);
    }

    public static void openMenuWrapped(ServerPlayerEntity player, Runnable run)
    {
        var menu = player.currentScreenHandler;
        ItemStack stack = menu.getCursorStack();
        menu.setCursorStack(ItemStack.EMPTY);
        run.run();
        menu = player.currentScreenHandler;
        menu.setCursorStack(stack);
    }
}
