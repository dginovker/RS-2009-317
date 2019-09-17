package com.runescape.scene.map.object;
import com.runescape.collection.Linkable;

public final class SpawnedObject extends Linkable {

	public SpawnedObject()
	{
		getLongetivity = -1;
	}

	public int id;
	public int orientation;
	public int type;
	public int getLongetivity;
	public int plane;
	public int group;
	public int tileX;
	public int tileY;
	public int getPreviousId;
	public int previousOrientation;
	public int previousType;
	public int delay;
}
