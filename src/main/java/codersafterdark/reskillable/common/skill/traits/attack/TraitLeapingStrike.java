package codersafterdark.reskillable.common.skill.traits.attack;

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

public class TraitLeapingStrike extends Trait {

    public TraitLeapingStrike() {
        super(new ResourceLocation(LibMisc.MOD_ID, "leaping_strike"), 4, 0, new ResourceLocation("reskillable", "attack"), 1, "");
        setCap(5);
        MinecraftForge.EVENT_BUS.register(this);
        setIcon(new ResourceLocation("dynamicswordskills:textures/items/skill_orb_leaping_blow.png"));
    }

    @SubscribeEvent
    public void onUnlock(UnlockUnlockableEvent event) {
        if (Config.isSkillAllowed(Skills.leapingBlow) && !DSSPlayerInfo.get(event.getEntityPlayer()).hasSkill(Skills.leapingBlow)
        && event.getUnlockable() == this) {
            DSSPlayerInfo.get(event.getEntityPlayer()).grantSkill(Skills.leapingBlow);
        }
    }

    @SubscribeEvent
    public void onUpgrade(UpgradeUnlockableEvent.Post event) {
        if (Config.isSkillAllowed(Skills.leapingBlow) && PlayerDataHandler.get(event.getEntityPlayer()).getSkillInfo(getParentSkill()).isUnlocked(this)
        && event.getUnlockable() == this) {
            DSSPlayerInfo.get(event.getEntityPlayer()).grantSkill(Skills.leapingBlow);
        }
    }

    @SubscribeEvent
    public void onLock(LockUnlockableEvent event) {
        if (Config.isSkillAllowed(Skills.leapingBlow) && event.getUnlockable() == this && DSSPlayerInfo.get(event.getEntityPlayer()).hasSkill(Skills.leapingBlow)) {
            DSSPlayerInfo.get(event.getEntityPlayer()).removeSkill(Skills.leapingBlow.getRegistryName().toString());
        }
    }

}
