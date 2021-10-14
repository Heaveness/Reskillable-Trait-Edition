package codersafterdark.reskillable.common.advancement.professionlevel;

import codersafterdark.reskillable.common.advancement.CriterionTrigger;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.common.lib.LibMisc;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class ProfessionLevelTrigger extends CriterionTrigger<ProfessionLevelListeners, ProfessionLevelCriterionInstance> {
    public ProfessionLevelTrigger() {super(new ResourceLocation(LibMisc.MOD_ID, "profession_level"), ProfessionLevelListeners::new);}

    public void trigger(EntityPlayerMP player, Profession profession, int level) {
        ProfessionLevelListeners listeners = this.getListeners(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger(profession, level);
        }
    }

    @Override
    public ProfessionLevelCriterionInstance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return null;
    }
}
