package a3.kmap165Engine.npc;

import graphicslib3D.Point3D;
import sage.ai.behaviortrees.BTCondition;
import a3.kmap165Engine.network.GameServerTCP;

public class AvatarNear extends BTCondition {
	
	private GameServerTCP server;
	private NPCcontroller npcc;
	private NPC npc;
	public AvatarNear(GameServerTCP s, NPCcontroller c, NPC n, boolean toNegate)
	{
		super(toNegate);
		server = s;
		npcc = c;
		npc = n;
	}

	protected boolean check()
	{
		Point3D npcP = new Point3D(npc.getX(), npc.getY(), npc.getZ());
		server.sendCheckForAvatarNear();
		return npcc.getNearFlag();
	}
}
