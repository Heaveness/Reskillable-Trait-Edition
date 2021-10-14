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

public class TraitSunder extends Trait {

    public TraitSunder() {
        super(new ResourceLocation(LibMisc.MOD_ID, "sunder"), 1, 0, new ResourceLocation("reskillable", "attack"), 1, "");
        setCap(5);
        MinecraftForge.EVENT_BUS.register(this);
        setIcon(new ResourceLocation("dynamicswordskills:textures/items/skill_orb_armor_break.png"));
    }

    @SubscribeEvent
    public void onUnlock(UnlockUnlockableEvent event) {
        if (Config.isSkillAllowed(Skills.armorBreak) && !DSSPlayerInfo.get(event.getEntityPlayer()).hasSkill(Skills.armorBreak)
        && event.getUnlockable() == this) {
            DSSPlayerInfo.get(event.getEntityPlayer()).grantSkill(Skills.armorBreak);
        }
    }

    @SubscribeEvent
    public void onUpgrade(UpgradeUnlockableEvent.Post event) {
        if (Config.isSkillAllowed(Skills.armorBreak) && PlayerDataHandler.get(event.getEntityPlayer()).getSkillInfo(getParentSkill()).isUnlocked(this)
        && event.getUnlockable() == this) {
            DSSPlayerInfo.get(event.getEntityPlayer()).grantSkill(Skills.armorBreak);
        }
    }

    @SubscribeEvent
    public void onLock(LockUnlockableEvent event) {
        if (Config.isSkillAllowed(Skills.armorBreak) && event.getUnlockable() == this && DSSPlayerInfo.get(event.getEntityPlayer()).hasSkill(Skills.armorBreak)) {
            DSSPlayerInfo.get(event.getEntityPlayer()).removeSkill(Skills.armorBreak.getRegistryName().toString());
        }
    }

}
