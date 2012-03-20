package edu.cwru.SimpleRTS.agent;

import java.util.*;

import edu.cwru.SimpleRTS.action.*;
import edu.cwru.SimpleRTS.environment.State.StateView;
import edu.cwru.SimpleRTS.model.unit.Unit.UnitView;

public class PEAgent extends Agent {

	private static final long serialVersionUID = 1L;
	static int playernum = 0;
	static String townHall = "TownHall";
	static String peasant = "Peasant";
	private int finalGoldTally = 200;
	private int finalWoodTally = 200;
	private boolean canBuildPeasant = false;

	public PEAgent(int playernum) {
		super(playernum);
	}

	@Override
	public Map<Integer, Action> initialStep(StateView state) {
		return middleStep(state);
	}

	@Override
	public Map<Integer, Action> middleStep(StateView state) 
	{
		ArrayList<STRIP> actions = new ArrayList<STRIP>();
		List<Integer> allUnitIds = state.getAllUnitIds();
		List<Integer> peasantIds = findUnitType(allUnitIds, state, peasant);
		List<Integer> townHallIds = findUnitType(allUnitIds, state, townHall);
		
		//create Planner
		Planner planner = new Planner(state, finalGoldTally, finalWoodTally, canBuildPeasant);
		
		if	(townHallIds.size() > 0) //TownHall Exists. Check if resources available in here too?
		{
			actions = planner.generatePlan(peasantIds.get(0), townHallIds.get(0), state);
		}	
		else
		{
			System.out.println("TownHall.size() <= 0. Where is it?!!");
		}
		
		if(actions == null)
		{
			;//TODO
		}
		return new HashMap<Integer, Action>();
	}

	@Override
	public void terminalStep(StateView state) {

	}
	
	public List<Integer> findUnitType(List<Integer> ids, StateView state, String name)
	{
		List<Integer> unitIds = new ArrayList<Integer>();
		for (int x = 0; x < ids.size(); x++)
		{
			Integer unitId = ids.get(x);
			UnitView unit = state.getUnit(unitId);
			
			if(unit.getTemplateView().getUnitName().equals(name))
			{
				unitIds.add(unitId);
			}
		}
		return unitIds;
	}
}
