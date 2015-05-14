package a3.kmap165Engine.npc;

import graphicslib3D.Point3D;
import a3.kmap165Engine.network.GameServerTCP;
import a3.kmap165Engine.network.ghost_avatar.*;
import sage.ai.behaviortrees.BTCondition;

public class AvatarFar extends BTCondition {

	GameServerTCP server;
	NPCcontroller npcc;
	GhostNPC npc;

	public AvatarFar(GameServerTCP s, NPCcontroller c, GhostNPC n, boolean toNegate) {
		super(toNegate);
		server = s;
		npcc = c;
		npc = n;
	}

	protected boolean check() {
		// Point3D npcP = new Point3D(npc.getX(), npc.getY(), npc.getZ());
		// server.send

		return npcc.getNearFlag();
	}
}
