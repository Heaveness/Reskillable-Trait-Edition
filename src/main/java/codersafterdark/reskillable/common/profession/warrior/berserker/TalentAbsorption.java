package codersafterdark.reskillable.common.profession.warrior.berserker;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentAbsorption extends Talent {
    public TalentAbsorption() {
        super(new ResourceLocation(MOD_ID, "absorption"), 1, 1, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "profession|reskillable:warrior|26");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKillMob(LivingDeathEvent event) {
        if (event.isCanceled()) return;
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer pl = (EntityPlayer)event.getSource().getTrueSource();
            if (PlayerDataHandler.get(pl).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
                pl.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 60));
            }
        }
    }
}
