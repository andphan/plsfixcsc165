package a3.kmap165Engine.npc;


import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class GetSmall extends BTAction {
	
	NPC npc;
	public GetSmall(NPC n)
	{
		npc = n;
	}
	
	protected BTStatus update(float elapsedTime)
	{
		npc.mopeAround();
		return BTStatus.BH_SUCCESS;
	}

}