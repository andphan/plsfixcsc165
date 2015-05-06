package a3.kmap165Engine.network.ghost_avatar;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import a3.kmap165Engine.npc.NPC;

public class GhostNPC {

	NPC body;
	int id;
	Vector3D position;
	public GhostNPC(int id, Vector3D pos)
	{
		this.id = id;
		this.body = new NPC();
		
	}
	
	public void setPosition(Vector3D pos)
	{
		Matrix3D trans = new Matrix3D();
		trans.translate(pos.getX(), pos.getY(), pos.getZ());
		body.setLocalTranslation(trans);
	}
	
}
