package a3.kmap165Engine.npc;

import sage.scene.shape.Cube;
// import sage.scene.shape.Rectangle;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;

public class NPC extends Cube{
	
	Point3D location;
	private Matrix3D localTranslation;
	private String str;
	public NPC()
	{
      location = new Point3D();
		this.localTranslation = new Matrix3D();
		//herihiehirVREFVSVV
	}
	
	
	public NPC(String string) {
		str = string;
	}


	public double getX()
	{
		return location.getX();
	}
	public double getY()
	{
		return location.getY();
	}
	public double getZ()
	{
		return location.getZ();
	}
	
	public void updateLocation()
	{
		location.setX(this.getX());
		location.setY(this.getY());
		location.setZ(this.getZ());
		
	}
	public void mopeAround()
	{
		System.out.println("moping around");
	}
	public void throwPowerUps()
	{
		System.out.println("powerups throwing");
	}
	public void randomizeLocation(double d, double e) {
		this.location.setX(d);
		this.location.setZ(e);
		
	}
	public void setLocalTranslation(Matrix3D trans) {
		localTranslation = trans;
	}


	public void attackAvatar() {
		System.out.println("attacking avatar");
		
	}
}
