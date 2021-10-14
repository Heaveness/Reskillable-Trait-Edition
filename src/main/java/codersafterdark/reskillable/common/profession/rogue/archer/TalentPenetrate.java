package codersafterdark.reskillable.common.profession.rogue.archer;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentPenetrate extends Talent {

    public TalentPenetrate() {
        super(new ResourceLocation(MOD_ID, "penetrate"), 0, 4, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "archer"), 3, "profession|reskillable:rogue|13");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (event.getSource().getImmediateSource() instanceof EntityArrow && event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            DamageSource arrowDamage = event.getSource();

            if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
                arrowDamage.setDamageBypassesArmor();
            }
        }
    }

}
