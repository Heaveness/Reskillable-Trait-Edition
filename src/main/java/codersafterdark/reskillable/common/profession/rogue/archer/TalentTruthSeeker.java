package codersafterdark.reskillable.common.profession.rogue.archer;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import com.fantasticsource.dynamicstealth.common.ClientData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentTruthSeeker extends Talent {

    public TalentTruthSeeker() {
        super(new ResourceLocation(MOD_ID, "truthseeker"), 1, 1, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "archer"), 3, "profession|reskillable:rogue|13");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBowDraw(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) event.getEntity();
            if (arrow.shootingEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) arrow.shootingEntity;
                if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this) &&
                player.getEntityWorld().canSeeSky(player.getPosition()) && ClientData.lightLevel > 8) {
                    arrow.setDamage(arrow.getDamage() * 1.20);
                }
            }
        }
    }
}
