package codersafterdark.reskillable.common.profession.warrior.warden;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.common.potion.ReskillablePotion;
import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentBlocking extends Talent {
    public TalentBlocking() {
        super(new ResourceLocation(MOD_ID, "blocking"), 1, 1, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
                3, "profession|reskillable:warrior|26");
        setCap(5);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBlock(LivingAttackEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        DamageSource damageSourceIn = event.getSource();

        if (entity instanceof EntityPlayer && PlayerDataHandler.get((EntityPlayer)entity).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
            if (!damageSourceIn.isUnblockable() && entity.isHandActive() && !entity.getActiveItemStack().isEmpty()) {
                Item item = entity.getActiveItemStack().getItem();
                if (item.getItemUseAction(entity.getActiveItemStack()) == EnumAction.BLOCK) {
                    EntityPlayer pl = (EntityPlayer)event.getEntityLiving();
                    pl.addPotionEffect(new PotionEffect(ReskillablePotion.POTION_RESIST_EFFECT, 80));
                    if (ConfigHandler.enableDebug) {
                        pl.sendMessage(new TextComponentString("You blocked!"));
                        pl.sendMessage(new TextComponentString("Damage Resistance: " + Math.round(pl.getEntityAttribute(ReskillableAttributes.DAMAGE_RESIST).getAttributeValue()) + "%"));
                    }
                }
            }
        }
    }
}
