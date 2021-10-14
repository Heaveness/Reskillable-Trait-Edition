package codersafterdark.reskillable.common.profession.rogue.assassin;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.network.JumpPacket;
import codersafterdark.reskillable.common.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentPlungingSilence extends Talent {

    public TalentPlungingSilence() {
        super(new ResourceLocation(MOD_ID, "plunging_silence"), 1, 3, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "assassin"), 3, "profession|reskillable:rogue|6");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getEntity();

        if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this) &&
                event.getSource().damageType.equals(DamageSource.FALL.damageType)) {
            event.setAmount(event.getAmount() - event.getAmount()*0.2F);
        }
    }

    @SubscribeEvent
	public void onFall(LivingFallEvent event) {
		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();
		if (world.isRemote) {
			return;
		}

		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer player = (EntityPlayer) event.getEntity();
		if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
            BlockPos fallpos = entity.getPosition().down();
            Block block = world.getBlockState(fallpos).getBlock();
            if (block.equals(Blocks.HAY_BLOCK)) {
                event.setCanceled(true);
            }
        }
	}

    @SideOnly(Side.CLIENT)
    private static boolean canDoubleJump;

    @SideOnly(Side.CLIENT)
    private static boolean hasReleasedJumpKey;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (player != null) {
                if ((player.onGround || player.isOnLadder()) && !player.isInWater()) {
                    hasReleasedJumpKey = false;
                    canDoubleJump = true;
                } else {
                    if (!player.movementInput.jump) {
                        hasReleasedJumpKey = true;
                    } else {
                        if (!player.capabilities.isFlying && canDoubleJump && hasReleasedJumpKey) {
                            canDoubleJump = false;
                            if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
                                PacketHandler.INSTANCE.sendToServer(new JumpPacket());
                                player.jump();
                                player.fallDistance = 0;
                            }
                        }
                    }
                }
            }
        }
     }
}
