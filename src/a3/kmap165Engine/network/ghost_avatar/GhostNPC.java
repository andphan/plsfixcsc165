package a3.kmap165Engine.network.ghost_avatar;

import java.util.UUID;

import sage.model.loader.OBJLoader;
import sage.scene.TriMesh;
import sage.scene.shape.Cube;
import sage.scene.shape.Rectangle;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import a3.kmap165Engine.network.MyClient;
import a3.kmap165Engine.npc.NPC;

public class GhostNPC extends TriMesh {

	
	private Rectangle body;
	int id;
	private Vector3D position;
	public GhostNPC(int id, Vector3D pos)
	{
		this.id = id;
		this.body = new Rectangle(Integer.toString(id));
		setPosition(pos);
		body.updateWorldBound();
		System.out.println(this.body);
		
	}
	
	public void setPosition(Vector3D pos)
	{
		Matrix3D trans = new Matrix3D();
		trans.translate(pos.getX(), pos.getY(), pos.getZ());
		body.setLocalTranslation(trans);
	}
	public Vector3D returnPosition()
	{
		return position;
	}
   public void updateLocation()
	{
		position.setX(this.getX());
		position.setY(this.getY());
		position.setZ(this.getZ());
		
	}
   public double getX()
	{
		return position.getX();
	}
	public double getY()
	{
		return position.getY();
	}
	public double getZ()
	{
		return position.getZ();
	}

	public int getNPCID() {
		return id;
	}

	public void setNPCid(int d) {
		this.id = d;
	}
	
}
