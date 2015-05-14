package a3.kmap165Engine.npc;

import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;
import a3.kmap165Engine.network.ghost_avatar.*;

public class AttackAvatar extends BTAction {

	GhostNPC npc;

	public AttackAvatar(GhostNPC n) {
		npc = n;
	}

	protected BTStatus update(float time) {
		npc.approachAvatar();
		return BTStatus.BH_SUCCESS;
	}

}