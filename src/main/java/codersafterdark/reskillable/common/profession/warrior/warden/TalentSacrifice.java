package codersafterdark.reskillable.common.profession.warrior.warden;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentSacrifice extends Talent {

    public TalentSacrifice() {
        super(new ResourceLocation(MOD_ID, "sacrifice"), 2, 2, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
                3, "profession|reskillable:warrior|19");
        setCap(5);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.isCanceled()) return;
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer pl = (EntityPlayer)event.getEntity();
            if (PlayerDataHandler.get(pl).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
                AxisAlignedBB playerRange = new AxisAlignedBB(pl.posX - 16, pl.posY - 16, pl.posZ + 16, pl.posX + 16, pl.posY + 16, pl.posZ - 16);
                List<EntityPlayer> party = pl.world.getEntitiesWithinAABB(EntityPlayer.class, playerRange);
                if (party.size() < 1) {return;}
                for (EntityPlayer entity: party) {
                    float maxHealth = entity.getMaxHealth();
                    float health = entity.getHealth();
                    if (health < maxHealth) {
                        entity.heal(maxHealth - health);
                    }
                    cleanseNegativePotions(entity);
                }
            }
        }
    }

    private void cleanseNegativePotions(EntityLivingBase entity) {
        Collection<PotionEffect> effects = entity.getActivePotionEffects();
        ArrayList<Potion> potionsToRemove = new ArrayList<>();
        Iterator<PotionEffect> it = effects.iterator();

        while (it.hasNext()) {
            PotionEffect effect = (PotionEffect) it.next();
            if (effect.getPotion().isBadEffect()) {
                potionsToRemove.add(effect.getPotion());
            }
        }

        Iterator<Potion> var7 = potionsToRemove.iterator();

        while (var7.hasNext()) {
            Potion potion = (Potion)var7.next();
            entity.removePotionEffect(potion);
        }
    }
}
