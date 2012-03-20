package edu.cwru.SimpleRTS.agent;

import java.util.ArrayList;

import edu.cwru.SimpleRTS.model.unit.Unit.UnitView;

public class STRIP {

	public UnitView unit; //used for x,y position of peasant
	public ArrayList<ResourceInfo> lumber;
	public ArrayList<ResourceInfo> gold;
	public boolean hasGold;
	public boolean hasWood;
}
