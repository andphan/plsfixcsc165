package a3.kmap165Engine.npc;

import graphicslib3D.Point3D;

public class NPC {
	
	Point3D location;
	
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
	public void randomizeLocation(int nextInt, int nextInt2) {
		this.location.setX(nextInt);
		this.location.setZ(nextInt2);
		
	}

}
