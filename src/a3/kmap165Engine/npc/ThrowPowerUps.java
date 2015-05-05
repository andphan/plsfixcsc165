package a3.kmap165Engine.npc;

import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class ThrowPowerUps extends BTAction {
	
	NPC npc;
	public ThrowPowerUps(NPC n)
	{
		npc = n;
	}
	
	protected BTStatus update(float elapsedTime)
	{
		npc.throwPowerUps();
		return BTStatus.BH_SUCCESS;
	}

}
