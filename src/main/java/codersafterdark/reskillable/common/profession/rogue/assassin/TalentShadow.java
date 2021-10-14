package codersafterdark.reskillable.common.profession.rogue.assassin;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerTalentInfo;
import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import com.fantasticsource.dynamicstealth.server.Attributes;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentShadow extends Talent {
    IAttribute speed = SharedMonsterAttributes.MOVEMENT_SPEED;
    IAttribute threat = Attributes.THREATGEN_ATTACK;
    IAttribute stealth = Attributes.VISIBILITY_REDUCTION;

    public TalentShadow() {
        super(new ResourceLocation(MOD_ID, "shadow"), 1, 1, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "assassin"), 3, "profession|reskillable:rogue|6");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUnlock(UnlockTalentEvent.Post event) {
        if (event.getTalent() instanceof TalentShadow) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.world.isRemote) {
                PlayerTalentInfo info = PlayerDataHandler.get(player).getTalentInfo(this);
                IAttributeInstance AttributeSpeed = player.getEntityAttribute(speed);
                AttributeModifier modifierSpeed = new AttributeModifier("generic.movementSpeed", 5.0, 2);
                info.addAttributeModifier(AttributeSpeed, modifierSpeed);
                IAttributeInstance AttributeThreat = player.getEntityAttribute(threat);
                AttributeModifier modifierThreat = new AttributeModifier("dynamicstealth.threatGenAttackedBySame", -100.0, 0);
                info.addAttributeModifier(AttributeThreat, modifierThreat);
                IAttributeInstance AttributeStealth = player.getEntityAttribute(stealth);
                AttributeModifier modifierStealth = new AttributeModifier("dynamicstealth.visibilityReduction", 500, 0);
                info.addAttributeModifier(AttributeStealth, modifierStealth);
                PlayerDataHandler.get(player).saveAndSync();
            }
        }
    }

    @SubscribeEvent
    public void onLock(LockTalentEvent.Post event) {
        if (event.getTalent() instanceof TalentShadow) {
            EntityPlayer player = event.getEntityPlayer();
            IAttributeInstance AttributeSpeed = player.getEntityAttribute(speed);
            IAttributeInstance AttributeThreat = player.getEntityAttribute(threat);
            IAttributeInstance AttributeStealth = player.getEntityAttribute(stealth);
            PlayerTalentInfo info = PlayerDataHandler.get(player).getTalentInfo(this);
            info.removeTalentAttribute(AttributeSpeed);
            info.removeTalentAttribute(AttributeThreat);
            info.removeTalentAttribute(AttributeStealth);
            PlayerDataHandler.get(player).saveAndSync();
        }
    }
}
