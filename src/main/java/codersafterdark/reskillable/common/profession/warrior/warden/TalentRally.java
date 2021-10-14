package codersafterdark.reskillable.common.profession.warrior.warden;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentRally extends Talent {

   public TalentRally() {
       super(new ResourceLocation(MOD_ID, "rally"), 1, 3, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
               3, "profession|reskillable:warrior|13");
       setCap(5);
       MinecraftForge.EVENT_BUS.register(this);
   }

    @SubscribeEvent
    public void onKillMob(LivingDeathEvent event) {
        if (event.isCanceled()) return;
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer pl = (EntityPlayer)event.getSource().getTrueSource();
            if (PlayerDataHandler.get(pl).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
                AxisAlignedBB playerRange = new AxisAlignedBB(pl.posX - 8, pl.posY - 8, pl.posZ - 8, pl.posX + 8, pl.posY + 8, pl.posZ + 8);
                List<EntityPlayer> party = pl.world.getEntitiesWithinAABB(EntityPlayer.class, playerRange);
                for (EntityPlayer entity: party) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100));
                    entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100));
                }
            }
        }
    }
}
