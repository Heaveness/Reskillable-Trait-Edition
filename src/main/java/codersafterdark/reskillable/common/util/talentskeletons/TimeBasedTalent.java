package codersafterdark.reskillable.common.util.talentskeletons;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * A Talent that uses a timer to perform actions on a player in time based intervals.
 * @author Yeelp
 *
 */
public abstract class TimeBasedTalent extends Talent {

	protected static final class Timer {
		private long reference;
		public Timer() {
			restart();
		}
		
		public long getTimeDiffInSeconds() {
			return (System.nanoTime() - reference)/1_000_000_000;
		}
		
		public void restart() {
			reference = System.nanoTime();
		}
	}
	
	private final int length;
	private final Map<UUID, Timer> timers = new HashMap<UUID, Timer>();
	
	public TimeBasedTalent(int intervalLengthInSeconds, ResourceLocation name, int x, int y, ResourceLocation professionName, ResourceLocation subProfessionName, int cost, String... defaultRequirements) {
		super(name, x, y, professionName, subProfessionName, cost, defaultRequirements);
		length = intervalLengthInSeconds;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
			if(PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
				Optional<Timer> optTimer = Optional.ofNullable(timers.putIfAbsent(player.getUniqueID(), new Timer()));
				if(optTimer.map((t) -> shouldPeformAction(player, t)).orElse(false)) {
					processPlayer((EntityPlayerMP) event.getEntityLiving());
					optTimer.get().restart();
				}
			}
		}
	}
	
	/**
	 * Should the action provided by {@link #processPlayer(EntityPlayerMP)} be performed for this player?
	 * @param player the player to check
	 * @param t the player's current timer value - how much time has elapsed between that last application and now.
	 * @return true if the action should be peformed, false if not.
	 * @implNote Note the default implementation here already checks if the timer duration has reached the length of this Talent's specified wait interval specified in {@link #getTimerInterval()}.
	 * Extenders need only override this method if they want to check additional conditions on top of the timer length check. 
	 */
	protected boolean shouldPeformAction(EntityPlayerMP player, Timer t) {
		return t.getTimeDiffInSeconds() % length == 0;
	}
	
	
	protected int getTimerInterval() {
		return length;
	}
	/**
	 * Process a player after this Talent's specified time interval
	 * @param player player to process
	 * @return
	 */
	protected abstract void processPlayer(EntityPlayerMP player);
}
