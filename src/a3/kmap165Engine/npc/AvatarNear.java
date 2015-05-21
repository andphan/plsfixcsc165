package a3.kmap165Engine.npc;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import a3.kmap165Engine.network.GameServerTCP;
import a3.kmap165Engine.network.ghost_avatar.*;
import sage.ai.behaviortrees.BTCondition;

public class AvatarNear extends BTCondition {

	GameServerTCP server;
	NPCcontroller npcc;
	GhostNPC npc;

	public AvatarNear(GameServerTCP s, NPCcontroller c, GhostNPC n, boolean toNegate) {
		super(toNegate);
		server = s;
		npcc = c;
		npc = n;
	}

	protected boolean check() {
		Vector3D npcV = new Vector3D(npc.getX(), npc.getY(), npc.getZ());
		System.out.println("avatar korra is the avatar near: " + npcV);
		server.sendCheckForAvatarNear(npcV);
		return npcc.getNearFlag();
	}
}
