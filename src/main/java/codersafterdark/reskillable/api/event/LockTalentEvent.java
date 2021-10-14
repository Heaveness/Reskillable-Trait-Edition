package codersafterdark.reskillable.api.event;

import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class LockTalentEvent extends PlayerEvent {
    private Talent talent;

    protected LockTalentEvent(EntityPlayer player, Talent talent) {
        super(player);
        this.talent = talent;
    }

    public Talent getTalent() {
        return talent;
    }

    @Cancelable
    public static class Pre extends LockTalentEvent {
        public Pre(EntityPlayer player, Talent talent) {
            super(player, talent);
        }
    }

    public static class Post extends LockTalentEvent {
        public Post(EntityPlayer player, Talent talent) {
            super(player, talent);
        }
    }

}
