package codersafterdark.reskillable.common.skill.attributes;

import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.common.core.handler.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class AttributeCritChance {

    Random rand = new Random();

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onArrowCt(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) event.getEntity();
            if (arrow.shootingEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) arrow.shootingEntity;
                float critChance = (float) player.getEntityAttribute(ReskillableAttributes.CRIT_CHANCE).getAttributeValue();
                critChance /= 100.0F;
                if (critChance >= rand.nextFloat()) {
                    arrow.setIsCritical(true);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onCrit(CriticalHitEvent event) {
        if (event.isVanillaCritical() || event.getResult() == Event.Result.ALLOW) {
            event.setResult(Event.Result.DENY);
        }
        EntityPlayer player = event.getEntityPlayer();
        float critChance = (float) player.getEntityAttribute(ReskillableAttributes.CRIT_CHANCE).getAttributeValue();
        critChance /= 100.0F;
        if (critChance >= rand.nextFloat()) {
            event.setResult(Event.Result.ALLOW);
            if (ConfigHandler.enableDebug && !player.world.isRemote) {
                player.sendMessage(new TextComponentString("Critical Chance: " + MathHelper.round(critChance * 100, 2) + "%"));
            }
        }
    }
}
