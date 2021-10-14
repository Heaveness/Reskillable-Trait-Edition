package codersafterdark.reskillable.common.skill.traits.agility;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.event.LockUnlockableEvent;
import codersafterdark.reskillable.api.event.UnlockUnlockableEvent;
import codersafterdark.reskillable.api.event.UpgradeUnlockableEvent;
import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.common.lib.LibMisc;
import dynamicswordskills.entity.DSSPlayerInfo;
import dynamicswordskills.ref.Config;
import dynamicswordskills.skills.Skills;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TraitRisingCut extends Trait {

    public TraitRisingCut() {
        super(new ResourceLocation(LibMisc.MOD_ID, "rising_cut"), 0, 0, new ResourceLocation("reskillable", "agility"), 1, "");
        setCap(5);
        setIcon(new ResourceLocation("dynamicswordskills:textures/items/skill_orb_rising_cut.png"));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUnlock(UnlockUnlockableEvent event) {
        if (Config.isSkillAllowed(Skills.risingCut) && !DSSPlayerInfo.get(event.getEntityPlayer()).hasSkill(Skills.risingCut)
                && event.getUnlockable() == this) {
            DSSPlayerInfo.get(event.getEntityPlayer()).grantSkill(Skills.risingCut);
        }
    }

    @SubscribeEvent
    public void onUpgrade(UpgradeUnlockableEvent.Post event) {
        if (Config.isSkillAllowed(Skills.risingCut) && PlayerDataHandler.get(event.getEntityPlayer()).getSkillInfo(getParentSkill()).isUnlocked(this)
                && event.getUnlockable() == this) {
            DSSPlayerInfo.get(event.getEntityPlayer()).grantSkill(Skills.risingCut);
        }
    }

    @SubscribeEvent
    public void onLock(LockUnlockableEvent event) {
        if (Config.isSkillAllowed(Skills.risingCut) && event.getUnlockable() == this && DSSPlayerInfo.get(event.getEntityPlayer()).hasSkill(Skills.risingCut)) {
            DSSPlayerInfo.get(event.getEntityPlayer()).removeSkill(Skills.risingCut.getRegistryName().toString());
        }
    }

}
