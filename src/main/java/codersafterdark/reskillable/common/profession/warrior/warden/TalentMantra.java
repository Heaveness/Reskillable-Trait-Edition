package codersafterdark.reskillable.common.profession.warrior.warden;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemShield;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Objects;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentMantra extends Talent {
    public TalentMantra() {
        super(new ResourceLocation(MOD_ID, "mantra"), 2, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
                3, "profession|reskillable:warrior|6", "reskillable:defense|8");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTeamHurt(LivingHurtEvent event) {
        if (event.isCanceled()) return;
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer pl = (EntityPlayer)event.getEntity();
            AxisAlignedBB playerRange = new AxisAlignedBB(pl.posX - 6, pl.posY - 6, pl.posZ - 6, pl.posX + 6, pl.posY + 6, pl.posZ + 6);
            List<EntityPlayer> party = pl.world.getEntitiesWithinAABB(EntityPlayer.class, playerRange);
            if (party.size() < 1) {return;}
            for (EntityPlayer entity: party) {
                if (PlayerDataHandler.get(entity).getProfessionInfo(getParentProfession()).isUnlocked(this)
                && entity.getHeldItemMainhand().getItem() instanceof ItemShield
                || entity.getHeldItemOffhand().getItem() instanceof ItemShield) {
                    if (event.getSource().getTrueSource() != null) {
                        DamageSource indirectDamage = DamageSource.causeIndirectDamage(event.getSource().getTrueSource(), entity);
                        float halvedDamage = event.getAmount() * 0.50F;
                        entity.attackEntityFrom(indirectDamage, halvedDamage);
                        event.setAmount(halvedDamage);
                        break;
                    }
                }
            }
        }
    }
}

