package codersafterdark.reskillable.common.profession.gatherer.seafarer;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentNeptunium extends Talent {

    private static final double SPEED_MULT = 1.2;
    private static final double MAX_SPEED = 1.3;

    public TalentNeptunium() {
        super(new ResourceLocation(MOD_ID, "neptunium_birthright"), 1, 2, new ResourceLocation(MOD_ID, "gatherer"), new ResourceLocation(MOD_ID, "seafarer"),
                3, "profession|reskillable:gatherer|19");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            if (!PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
                return;
            }

            if (player.isInsideOfMaterial(Material.WATER)) {

                double motionX = player.motionX * SPEED_MULT;
                double motionY = player.motionY * SPEED_MULT;
                double motionZ = player.motionZ * SPEED_MULT;

                boolean flying = player instanceof EntityPlayer && player.capabilities.isFlying;

                if (Math.abs(motionX) < MAX_SPEED && !flying)
                    player.motionX = motionX;
                if (Math.abs(motionY) < MAX_SPEED && !flying)
                    player.motionY = motionY;
                if (Math.abs(motionZ) < MAX_SPEED && !flying)
                    player.motionZ = motionZ;

                PotionEffect effectVision = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
                if (effectVision == null) {
                    PotionEffect newEffectVision = new PotionEffect(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, -42, true, true);
                    player.addPotionEffect(newEffectVision);
                }

                PotionEffect effectBreathing = player.getActivePotionEffect(MobEffects.WATER_BREATHING);
                if (effectBreathing == null) {
                    PotionEffect newEffectBreathing = new PotionEffect(MobEffects.WATER_BREATHING, Integer.MAX_VALUE, -42, true, true);
                    player.addPotionEffect(newEffectBreathing);
                }
            }
        }
    }
}