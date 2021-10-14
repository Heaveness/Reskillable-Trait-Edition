package codersafterdark.reskillable.common.profession.rogue.archer;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import com.tmtravlr.potioncore.potion.PotionArchery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentTrueShot extends Talent {

     public TalentTrueShot() {
        super(new ResourceLocation(MOD_ID, "trueshot"), 2, 2, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "archer"), 3, "profession|reskillable:rogue|13");
         MinecraftForge.EVENT_BUS.register(this);
     }

    @SubscribeEvent
    public void onDraw(ArrowNockEvent event) {
         EntityPlayer player = event.getEntityPlayer();
         if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this) &&
         player.isPotionActive(new PotionArchery())) {
             event.getEntityPlayer().setActiveHand(event.getHand());
             setItemUseCount(event.getEntityPlayer());
             event.setAction(new ActionResult<>(EnumActionResult.SUCCESS, event.getBow()));
         }
    }

    private void setItemUseCount(EntityPlayer player) {
        player.activeItemStackUseCount -= player.activeItemStackUseCount * 0.25;
    }
}
