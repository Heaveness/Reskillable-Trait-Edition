package codersafterdark.reskillable.api.event;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class UnlockTalentEvent extends PlayerEvent {
    private Talent talent;

    protected UnlockTalentEvent(EntityPlayer player, Talent talent) {
        super(player);
        this.talent = talent;
    }

    public Talent getTalent() {
        return talent;
    }

    @Cancelable
    public static class Pre extends UnlockTalentEvent {
        public Pre(EntityPlayer player, Talent talent) {
            super(player, talent);
        }
    }

    public static class Post extends UnlockTalentEvent {
        public Post(EntityPlayer player, Talent talent) {
            super(player, talent);
        }
    }

}
