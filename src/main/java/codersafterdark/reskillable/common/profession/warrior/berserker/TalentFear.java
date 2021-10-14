package codersafterdark.reskillable.common.profession.warrior.berserker;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerProfessionInfo;
import codersafterdark.reskillable.api.data.PlayerTalentInfo;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import electroblob.wizardry.potion.PotionMagicEffect;
import electroblob.wizardry.registry.WizardryPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentFear extends Talent {
    Random rand = new Random();

    public TalentFear() {
        super(new ResourceLocation(MOD_ID, "fear"), 0, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "profession|reskillable:warrior|6", "reskillable:attack|8");
        setCap(3);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAttack(LivingDamageEvent event) {
        if ((event.getSource().getTrueSource() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this) && player.getHealth() <= player.getMaxHealth() * 0.5) {
                float fearChance = 5.0F * PlayerDataHandler.get(player).getTalentInfo(this).getRank();
                fearChance /= 100.0F;
                if (fearChance >= rand.nextFloat()) {
                    event.getEntityLiving().addPotionEffect(new PotionEffect(WizardryPotions.fear, 120));
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getDescription() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        PlayerProfessionInfo professionInfo = PlayerDataHandler.get(player).getProfessionInfo(this.getParentProfession());
        PlayerTalentInfo talentInfo = PlayerDataHandler.get(player).getTalentInfo(this);
        float fearChance = talentInfo.getRank() * 5.0F;
        TextFormatting formatting = TextFormatting.GRAY;
        if (professionInfo.isUnlocked(this)) {
            formatting = TextFormatting.GREEN;
        }
        if (talentInfo.isCapped()) {
            formatting = TextFormatting.GOLD;
        }
        return new TextComponentTranslation("reskillable.talent." + getKey() + ".desc", formatting.toString(), fearChance).getUnformattedComponentText();
    }
}
