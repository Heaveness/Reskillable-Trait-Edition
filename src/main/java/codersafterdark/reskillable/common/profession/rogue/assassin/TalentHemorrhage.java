package codersafterdark.reskillable.common.profession.rogue.assassin;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.potion.ReskillablePotion;
import com.oblivioussp.spartanweaponry.item.ItemDagger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentHemorrhage extends Talent {
    Random rand = new Random();

    public TalentHemorrhage() {
        super(new ResourceLocation(MOD_ID, "hemorrhage"), 2, 2, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "assassin"), 3, "profession|reskillable:rogue|6");
        MinecraftForge.EVENT_BUS.register(this);
        setCap(3);
    }

    @SubscribeEvent
    public void onAttack(LivingDamageEvent event) {
        if ((event.getSource().getTrueSource() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this) && player.getHeldEquipment().iterator().next().getItem() instanceof ItemDagger) {
                float bleedChance = 15.0F * PlayerDataHandler.get(player).getTalentInfo(this).getRank();
                bleedChance /= 100.0F;
                if (!player.onGround && player.fallDistance > 0.0 && bleedChance >= rand.nextFloat()) {
                    event.getEntityLiving().addPotionEffect(new PotionEffect(ReskillablePotion.POTION_BLEED_EFFECT, 120));
                    player.sendMessage(new TextComponentString("Bleed Inflicted"));
                }
            }
        }
    }
}
