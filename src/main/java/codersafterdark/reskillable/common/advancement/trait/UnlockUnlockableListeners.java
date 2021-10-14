package codersafterdark.reskillable.common.advancement.trait;

import codersafterdark.reskillable.common.advancement.CriterionListeners;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.advancements.PlayerAdvancements;

public class UnlockUnlockableListeners extends CriterionListeners<UnlockUnlockableCriterionInstance> {
    public UnlockUnlockableListeners(PlayerAdvancements playerAdvancements) {
        super(playerAdvancements);
    }

    public void trigger(final Unlockable unlockable) {
        trigger(instance -> instance.test(unlockable));
    }
}
