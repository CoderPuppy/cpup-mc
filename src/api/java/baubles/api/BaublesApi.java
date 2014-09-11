package baubles.api;

import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import cpw.mods.fml.common.FMLLog;

/**
 * @author Azanor
 */
public class BaublesApi {

	private static boolean ringEnabled = true;
	
	static Method getBaubles;
	/**
	 * Retrieves the baubles inventory for the supplied player
	 */
	public static IInventory getBaubles(EntityPlayer player) {
		IInventory ot = null;
		try {
			if(getBaubles == null) {
				Class fake = Class.forName("baubles.common.lib.PlayerHandler");
				getBaubles = fake.getMethod("getPlayerBaubles", EntityPlayer.class);
			}
			ot = (IInventory) getBaubles.invoke(null, player);
		} catch(Exception ex) {
			FMLLog.warning("[Baubles API] Could not invoke baubles.common.lib.PlayerHandler method getPlayerBaubles");
		}
		return ot;
	}
	
	/**
	 * Call this on PreInit to disable the recipe for the miner's ring.
	 */
	public static void disableMinerRing() {
		ringEnabled = false;
	}
	
	/**
	 * Returns whether the recipe for the miner's ring is enabled.
	 */
	public static boolean isMinerRingEnabled() {
		return ringEnabled;
	}

}
