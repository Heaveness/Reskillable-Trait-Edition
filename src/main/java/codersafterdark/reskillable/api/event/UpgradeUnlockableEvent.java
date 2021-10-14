package codersafterdark.reskillable.api.event;

import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class UpgradeUnlockableEvent extends PlayerEvent {
    private Unlockable unlockable;
    private int rank;
    private int oldRank;

    protected UpgradeUnlockableEvent(EntityPlayer player, Unlockable unlockable, int rank, int oldRank) {
        super(player);
        this.unlockable = unlockable;
        this.rank = rank;
        this.oldRank = oldRank;
    }

    public Unlockable getUnlockable() {return unlockable;}

    public int getLevel() {
        return rank;
    }

    public int getOldLevel() {
        return oldRank;
    }

    @Cancelable
    public static class Pre extends UpgradeUnlockableEvent {
        public Pre(EntityPlayer player, Unlockable unlockable, int rank) {
            this(player, unlockable, rank, rank - 1);
        }

        public Pre(EntityPlayer player, Unlockable unlockable, int rank, int oldRank) {
            super(player, unlockable, rank, oldRank);
        }
    }

    public static class Post extends UpgradeUnlockableEvent {
        public Post(EntityPlayer player, Unlockable unlockable, int rank) {
            this(player, unlockable, rank, rank - 1);
        }

        public Post(EntityPlayer player, Unlockable unlockable, int rank, int oldRank) {
            super(player, unlockable, rank, oldRank);
        }
    }

}
