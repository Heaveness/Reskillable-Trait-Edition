package codersafterdark.reskillable.api.event;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class UpgradeTalentEvent extends PlayerEvent {
    private Talent talent;
    private int rank;
    private int oldRank;

    protected UpgradeTalentEvent(EntityPlayer player, Talent talent, int rank, int oldRank) {
        super(player);
        this.talent = talent;
        this.rank = rank;
        this.oldRank = oldRank;
    }

    public Talent getTalent() {return talent;}

    public int getLevel() {
        return rank;
    }

    public int getOldLevel() {
        return oldRank;
    }

    @Cancelable
    public static class Pre extends UpgradeTalentEvent {
        public Pre(EntityPlayer player, Talent talent, int rank) {
            this(player, talent, rank, rank - 1);
        }

        public Pre(EntityPlayer player, Talent talent, int rank, int oldRank) {
            super(player, talent, rank, oldRank);
        }
    }

    public static class Post extends UpgradeTalentEvent {
        public Post(EntityPlayer player, Talent talent, int rank) {
            this(player, talent, rank, rank - 1);
        }

        public Post(EntityPlayer player, Talent talent, int rank, int oldRank) {
            super(player, talent, rank, oldRank);
        }
    }

}
