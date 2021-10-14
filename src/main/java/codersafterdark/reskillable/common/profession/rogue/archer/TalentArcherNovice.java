package codersafterdark.reskillable.common.profession.rogue.archer;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerTalentInfo;
import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import com.tmtravlr.potioncore.PotionCoreAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentArcherNovice extends Talent {
    IAttribute projectileDamage = PotionCoreAttributes.PROJECTILE_DAMAGE;

    public TalentArcherNovice() {
        super(new ResourceLocation(MOD_ID, "archer_novice"), 1, 4, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "archer"),
                3, "profession|reskillable:rogue|6", "reskillable:agility|8");
    }

    @SubscribeEvent
    public void onUnlock(UnlockTalentEvent event) {
        if (event.getTalent() instanceof TalentArcherNovice) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.world.isRemote) {
                PlayerTalentInfo info = PlayerDataHandler.get(player).getTalentInfo(this);
                IAttributeInstance AttributeProjectile = player.getEntityAttribute(projectileDamage);
                AttributeModifier modifier = new AttributeModifier("potioncore.projectileDamage", 15.0D, 0);
                info.addAttributeModifier(AttributeProjectile, modifier);
                PlayerDataHandler.get(player).saveAndSync();
            }
        }
    }

    @SubscribeEvent
    public void onLock(LockTalentEvent.Post event) {
        if (event.getTalent() instanceof TalentArcherNovice) {
            EntityPlayer player = event.getEntityPlayer();
            IAttributeInstance AttributeProjectile = player.getEntityAttribute(projectileDamage);
            PlayerTalentInfo info = PlayerDataHandler.get(player).getTalentInfo(this);
            info.removeTalentAttribute(AttributeProjectile);
            PlayerDataHandler.get(player).saveAndSync();
        }
    }


}
