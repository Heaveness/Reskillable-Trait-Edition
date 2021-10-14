package codersafterdark.reskillable.api.event;

import codersafterdark.reskillable.api.profession.Profession;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class LevelUpProfessionEvent extends PlayerEvent {
    private Profession profession;
    private int level;
    private int oldLevel;

    protected LevelUpProfessionEvent(EntityPlayer player, Profession profession, int level, int oldLevel) {
        super(player);
        this.profession = profession;
        this.level = level;
        this.oldLevel = oldLevel;
    }

    public Profession getProfession() {return profession;}

    public int getLevel() {
        return level;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    @Cancelable
    public static class Pre extends LevelUpProfessionEvent {
        public Pre(EntityPlayer player, Profession profession, int level) {
            this(player, profession, level, level - 1);
        }

        public Pre(EntityPlayer player, Profession profession, int level, int oldLevel) {
            super(player, profession, level, oldLevel);
        }
    }

    public static class Post extends LevelUpProfessionEvent {
        public Post(EntityPlayer player, Profession profession, int level) {
            this(player, profession, level, level - 1);
        }

        public Post(EntityPlayer player, Profession profession, int level, int oldLevel) {
            super(player, profession, level, oldLevel);
        }
    }

}
