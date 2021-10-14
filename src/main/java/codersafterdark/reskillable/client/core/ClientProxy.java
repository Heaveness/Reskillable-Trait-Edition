package codersafterdark.reskillable.client.core;

import codersafterdark.reskillable.common.core.CommonProxy;
import codersafterdark.reskillable.common.core.handler.ToolTipHandler;
import codersafterdark.reskillable.client.core.handler.ClientTickHandler;
import codersafterdark.reskillable.client.core.handler.HUDHandler;
import codersafterdark.reskillable.client.gui.compat.SeasonsOverlay;
import codersafterdark.reskillable.client.gui.handler.InventoryTabHandler;
import codersafterdark.reskillable.client.gui.handler.KeyBindings;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        KeyBindings.init();
        MinecraftForge.EVENT_BUS.register(ClientTickHandler.class);
        MinecraftForge.EVENT_BUS.register(InventoryTabHandler.class);
        MinecraftForge.EVENT_BUS.register(HUDHandler.class);
        MinecraftForge.EVENT_BUS.register(ToolTipHandler.class);
        MinecraftForge.EVENT_BUS.register(getClass());
        MinecraftForge.EVENT_BUS.register(new KeyBindings());

        if (Loader.isModLoaded("sereneseasons")) {
            MinecraftForge.EVENT_BUS.register(SeasonsOverlay.instance);
        }
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @SubscribeEvent
    public static void connect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        new AdvancementManager(null);
    }

    @Override
    @Nullable
    public AdvancementProgress getPlayerAdvancementProgress(EntityPlayer entityPlayer, Advancement advancement) {
        return Optional.ofNullable(Minecraft.getMinecraft().getConnection())
                .map(NetHandlerPlayClient::getAdvancementManager)
                .map(advancementManager -> advancementManager.advancementToProgress)
                .map(advancementAdvancementProgressMap -> advancementAdvancementProgressMap.get(advancement))
                .orElseGet(AdvancementProgress::new);
    }

    @Override
    public String getLocalizedString(String string) {
        return I18n.format(string);
    }

}