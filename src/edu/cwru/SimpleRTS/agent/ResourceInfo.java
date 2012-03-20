package edu.cwru.SimpleRTS.agent;

import edu.cwru.SimpleRTS.model.unit.Unit.UnitView;

public class ResourceInfo {
	public int collected = 0;
	public int totalAvailable = 0;
	public UnitView position;
	
	public ResourceInfo (int resourceValue)
	{
		totalAvailable = resourceValue;
	}
}
