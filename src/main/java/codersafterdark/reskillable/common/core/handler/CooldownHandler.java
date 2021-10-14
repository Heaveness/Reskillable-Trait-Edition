package codersafterdark.reskillable.common.core.handler;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.TalentActive;
import codersafterdark.reskillable.common.lib.LibMisc;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class CooldownHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityTickCooldown(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntityLiving().getEntityWorld().isRemote) {
            EntityLivingBase entity = event.getEntityLiving();
            if (!(entity instanceof EntityPlayer)) return;

            EntityPlayer player = (EntityPlayer) entity;
            if (PlayerDataHandler.get(player).hasActiveTalents()) {
                for (TalentActive talent : PlayerDataHandler.get(player).getAllActiveTalents()) {
                    if (talent.getCooldown() > 0) {
                        talent.setCooldown(talent.getCooldown() - 1);
                    }
                }
            }
        }
    }
}
