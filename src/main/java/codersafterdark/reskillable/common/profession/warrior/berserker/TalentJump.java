package codersafterdark.reskillable.common.profession.warrior.berserker;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentJump extends Talent {
    public TalentJump() {
        super(new ResourceLocation(MOD_ID, "jump"), 0, 1, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "profession|reskillable:warrior|26");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
                player.motionY += 0.2F;
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer && event.getSource().damageType.equals(DamageSource.FALL.getDamageType())) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
                double damage = event.getAmount();
                event.setAmount((float) (damage - damage * 0.2F));
            }
        }

    }

}