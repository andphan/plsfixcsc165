package a3.kmap165Engine.network.ghost_avatar;

import java.util.UUID;

import sage.model.loader.OBJLoader;
import sage.scene.TriMesh;
import sage.scene.shape.Cube;
import sage.scene.shape.Rectangle;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import a3.kmap165Engine.network.MyClient;
//import a3.kmap165Engine.npc.NPC;
import java.lang.Math;


public class GhostNPC extends TriMesh {

	private Cube body;
	private int id, heading;
	private Vector3D position, playerPosition;
	private Rectangle rect;
	private OBJLoader objectLoader = new OBJLoader();
   private TriMesh theMesh;
   
	public GhostNPC(/*int id,*/ Vector3D pos, Vector3D playerPos)
	{
      theMesh = objectLoader
				.loadModel("./a3/kmap165Engine/external_models/albertTestMesh.obj");
		this.setVertexBuffer(theMesh.getVertexBuffer());
		this.setIndexBuffer(theMesh.getIndexBuffer());
      playerPosition = playerPos;
		Matrix3D theMeshS = theMesh.getLocalScale();
		theMeshS.scale(.75, 0.75, .75);
		setLocalScale(theMeshS);
      
		//this.id = id;
		//this.body = new Cube(Integer.toString(id));
      position = pos;
		setPosition(pos);
      setHeading(270);
		this.updateWorldBound();
	}
	
	public void setPosition(Vector3D pos)
	{
		Matrix3D trans = new Matrix3D();
		trans.translate(pos.getX(), pos.getY(), pos.getZ());
		this.setLocalTranslation(trans);
      
      updateLocalBound();
		updateWorldBound();
      //System.out.println("FF");
      
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
      
      setPosition(position);
      
	}
   /*public boolean isClose(){
      if(Math.abs(position.getX() - playerPosition.getX()) <= 5 &&
         Math.abs(position.getZ() - playerPosition.getZ()) <= 5){
            System.out.println("X " + (position.getX() - playerPosition.getX()));
            System.out.println("Z " + (position.getZ() - playerPosition.getZ()));
            System.out.println();
            return true;
         }
         
      else return false;
   }*/
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
   public int getHeading() {
		return heading;
	}
	public void setHeading(int heading) {
		this.heading = heading;
	}
	/*public int getNPCID() {
		return id;
	}

	public void setNPCid(int d) {
		this.id = d;
	}*/

	public void mopeAround()
	{
		System.out.println("moping around");
      setHeading(180 + (int) Math.toDegrees(Math.atan2(getZ() - playerPosition.getZ() , getX() - playerPosition.getX())));
  //    System.out.println( playerPosition.getZ());
      Matrix3D npcM = this.getLocalTranslation();
      npcM.translate((Math.cos(Math.toRadians((double) getHeading()))), 0, (Math.sin(Math.toRadians((double) getHeading()))));
      this.setLocalTranslation(npcM);
      //setPosition(new Vector3D(-1.0,-1.0,0.0));
      //setPosition(npcM.getCol(3));
      //setPosition(new Vector3D(-1.0,-1.0,0.0));
   //   System.out.println(getLocalTranslation());
      updateLocation();
	}
	public void throwPowerUps()
	{
		System.out.println("powerups throwing");
	}
   public void attackAvatar() {
		System.out.println("attacking avatar");
		
	}
   public void setPlayerPosition(Vector3D kd){
      playerPosition = kd;
   }
   public void approachAvatar() {
		System.out.println("approaching avatar");
		setHeading(180 + (int) Math.toDegrees(Math.atan2(getZ() - playerPosition.getZ(), getX() - playerPosition.getX())));
      Matrix3D npcM = this.getLocalTranslation();
      npcM.translate((Math.cos(Math.toRadians((double) getHeading()))), 0, (Math.sin(Math.toRadians((double) getHeading()))));
	   this.setLocalTranslation(npcM);
   }
}
