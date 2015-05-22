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


public class GhostNPC extends Cube {

	private Cube body;
	private int id, heading;
	private Vector3D position, playerPosition;
   private Matrix3D npcM;
	public GhostNPC(Vector3D pos, Vector3D playerPos)
	{
		body = new Cube();
		Matrix3D theMeshS = body.getLocalScale();
		theMeshS.scale(2.0, 2.0, 2.0);
		setLocalScale(theMeshS);
      npcM = this.getLocalTranslation();
      
		//this.id = id;
		//this.body = new Cube(Integer.toString(id));
		position = pos;
		setPosition(pos);
      setHeading(270);
		this.updateWorldBound();
	}
	
	public void setPosition(Vector3D pos)
	{
		npcM.translate(pos.getX(), pos.getY(), pos.getZ());
		setLocalTranslation(npcM);
      //this.translate((float) pos.getX(), (float) pos.getY(), (float) pos.getZ());
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
      //System.out.println("npc loc is at: "  + position);
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
      //setHeading(180 + (int) Math.toDegrees(Math.atan2(getZ() - playerPosition.getZ() , getX() - playerPosition.getX())));
        //System.out.println("playerPos: " +  playerPosition +  " thisloc: " +  this.returnPosition());
        //npcM.translate((Math.cos(Math.toRadians((double) getHeading()*5))), 0, (Math.sin(Math.toRadians((double) getHeading()*5))));
		   //npcM.translate(-0.0000000000000000000001,0.0,-0.0000000000000000000001);
         this.setLocalTranslation(npcM);
      //setPosition(new Vector3D(-1.0,-1.0,0.0));
      //setPosition(npcM.getCol(3));
      //setPosition(new Vector3D(-1.0,-1.0,0.0));
      //System.out.println(getLocalTranslation());
      //updateLocation();
      
      //this.rotate(90, new Vector3D(1, 1, 1));
	}
	public void throwPowerUps()
	{
		System.out.println("powerups throwing");
	}
   public void attackAvatar()  // test area thing here
   { 
		System.out.println("attacking avatar");
	this.scale(5, 5, 5);
		
	}
   public void setPlayerPosition(Vector3D kd){
      playerPosition = kd;
   }
   public void approachAvatar() {
		System.out.println("approaching avatar");
/*		setHeading(180 + (int) Math.toDegrees(Math.atan2(getZ() - playerPosition.getZ(), getX() - playerPosition.getX())));
      Matrix3D npcM = this.getLocalTranslation();
      npcM.translate((Math.cos(Math.toRadians((double) getHeading()))), 0, (Math.sin(Math.toRadians((double) getHeading()))));
	   this.setLocalTranslation(npcM);
	   */
		//this.scale(1, 1, 1);
         Matrix3D npcM = this.getLocalTranslation();
        //npcM.translate((Math.cos(Math.toRadians((double) getHeading()))), 0, (Math.sin(Math.toRadians((double) getHeading()))));
		   npcM.translate(-0.5,0.0,-0.5);
         this.setLocalTranslation(npcM);
   }
}
