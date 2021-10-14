package codersafterdark.reskillable.common.skill.attributes;

import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AttributeDamageResist {

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (event.isCanceled() || !(event.getEntityLiving() instanceof EntityPlayer)){
            return;
        }

        float baseDamage = event.getAmount();
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        IAttributeInstance AttributeDamageReduction = player.getEntityAttribute(ReskillableAttributes.DAMAGE_RESIST);
        float reduction = (float) (AttributeDamageReduction.getAttributeValue() / 100F) * baseDamage;
        baseDamage -= reduction;
        event.setAmount(baseDamage);
    }
}
