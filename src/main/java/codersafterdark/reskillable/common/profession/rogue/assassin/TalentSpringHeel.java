package codersafterdark.reskillable.common.profession.rogue.assassin;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.potion.ReskillablePotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentSpringHeel extends Talent {

    public TalentSpringHeel() {
        super(new ResourceLocation(MOD_ID, "spring_heel"), 0, 4, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "assassin"), 3, "profession|reskillable:rogue|6");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onMobKill(LivingDeathEvent event) {
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
            player.addPotionEffect(new PotionEffect(ReskillablePotion.POTION_SPRING_HEEL_EFFECT, 60, 0, true, false));
        }
    }

}
