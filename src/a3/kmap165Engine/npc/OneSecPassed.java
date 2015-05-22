package a3.kmap165Engine.npc;

import sage.ai.behaviortrees.BTCondition;
import a3.kmap165Engine.network.ghost_avatar.*;
public class OneSecPassed extends BTCondition {

	NPCcontroller npcc;
	GhostNPC npc;
	long lastUpdateTime;

	public OneSecPassed(NPCcontroller c, GhostNPC n, boolean toNegate) {
		super(toNegate);
		npcc = c;
		npc = n;
		lastUpdateTime = System.nanoTime();

	}

	protected boolean check() {
		float elapsedMiliSecs = (System.nanoTime() - lastUpdateTime) / (1000000.0f);
		if (elapsedMiliSecs >= 500.0f) {
			lastUpdateTime = System.nanoTime();
			npcc.setNearFlag(false);
			return true;
		} else
			return false;
	}
}
