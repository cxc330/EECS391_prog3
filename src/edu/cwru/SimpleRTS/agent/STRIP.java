package edu.cwru.SimpleRTS.agent;

import java.util.ArrayList;

import edu.cwru.SimpleRTS.model.unit.Unit.UnitView;

public class STRIP {

	public UnitView unit; //used for x,y position of peasant
	public ArrayList<ResourceInfo> lumber = new ArrayList<ResourceInfo>();
	public ArrayList<ResourceInfo> gold = new ArrayList<ResourceInfo>();
	public boolean hasGold = false;
	public boolean hasWood = false;
}
