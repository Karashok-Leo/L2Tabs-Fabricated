package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2tabs.data.L2TabsLangData;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public record TrinketScreenHandlerFactory(
        ScreenHandlerType<TrinketListScreenHandler> type,
        int page
) implements ExtendedScreenHandlerFactory
{

    public TrinketScreenHandlerFactory(ScreenHandlerType<TrinketListScreenHandler> type)
    {
        this(type, 0);
    }

    @Override
    public Text getDisplayName()
    {
        return L2TabsLangData.CURIOS.get();
    }

    @Override
    public ScreenHandler createMenu(int wid, PlayerInventory plInv, PlayerEntity player)
    {
        return new TrinketListScreenHandler(type, wid, plInv, new TrinketWrapper(player, page));
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf)
    {
        buf.writeInt(page);
    }

    public void open(ServerPlayerEntity player)
    {
        player.openHandledScreen(this);
    }
}