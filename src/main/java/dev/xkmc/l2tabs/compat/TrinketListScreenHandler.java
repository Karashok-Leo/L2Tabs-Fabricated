package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2tabs.compat.base.BaseTrinketListScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;

public class TrinketListScreenHandler extends BaseTrinketListScreenHandler<TrinketListScreenHandler>
{
    public static TrinketListScreenHandler fromNetwork(int wid, PlayerInventory plInv, PacketByteBuf buf)
    {
        int page = buf.readInt();
        return new TrinketListScreenHandler(TrinketScreenCompatImpl.get().ScreenHandlerType, wid, plInv, new TrinketWrapper(plInv.player, page));
    }

    protected TrinketListScreenHandler(ScreenHandlerType<?> type, int wid, PlayerInventory plInv, TrinketWrapper curios)
    {
        super(type, wid, plInv, curios);
    }

    @Override
    public void switchPage(ServerPlayerEntity sp, int page)
    {
        new TrinketScreenHandlerFactory(TrinketScreenCompatImpl.get().ScreenHandlerType, page).open(sp);
    }
}
