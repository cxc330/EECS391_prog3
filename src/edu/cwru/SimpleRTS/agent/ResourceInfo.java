package edu.cwru.SimpleRTS.agent;

import edu.cwru.SimpleRTS.model.resource.ResourceNode.Type;

public class ResourceInfo {
	public int collected = 0;
	public int totalAvailable = 0;
	public Integer x;
	public Integer y;
	public Type type;
	
	public ResourceInfo (int resourceValue)
	{
		totalAvailable = resourceValue;
	}

	public ResourceInfo() {
		// TODO Auto-generated constructor stub
	}
}
