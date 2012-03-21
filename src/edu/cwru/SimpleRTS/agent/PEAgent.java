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
	static String move = "move";
	static String gather = "gather";
	static String deposit = "deposit";
	private int finalGoldTally = 200;
	private int finalWoodTally = 200;
	private boolean canBuildPeasant = false;
	private ArrayList<STRIP> actionsList = new ArrayList<STRIP>();
	private int index = 0;
	private int peasantID;
	private AStarPath astar = new AStarPath(playernum);
	
	public PEAgent(int playernum, String[] args) {
		super(playernum);
		if(args[0].equals("true")) //check to see if we can build peasants
		{
			canBuildPeasant = true;
		}
	}

	@Override
	public Map<Integer, Action> initialStep(StateView state) 
	{
		List<Integer> allUnitIds = state.getAllUnitIds();
		List<Integer> peasantIds = findUnitType(allUnitIds, state, peasant);
		List<Integer> townHallIds = findUnitType(allUnitIds, state, townHall);
		
		//create Planner
		Planner planner = new Planner(state, finalGoldTally, finalWoodTally, canBuildPeasant);
		if	(townHallIds.size() > 0) //TownHall Exists. Check if resources available in here too?
		{
			actionsList = planner.generatePlan(peasantIds.get(0), townHallIds.get(0), state);
			peasantID = actionsList.get(0).unit.getID();
			index = 0;
		}	
		else
		{
			System.out.println("TownHall.size() <= 0. Where is it?!!");
		}
		return middleStep(state);
	}

	@Override
	public Map<Integer, Action> middleStep(StateView state) 
	{
		Map<Integer, Action> actions = new HashMap<Integer, Action>();
		actions = convertToMap(actionsList.get(index), peasantID);
		System.out.println(actions);
		if(actions == null)
		{
			actions = new HashMap<Integer, Action>();
		}
		
		if(index < actionsList.size()-1)
		{
			index++;
		}
		return actions;
	}

	@Override
	public void terminalStep(StateView state) {

	}
	
	public Map<Integer, Action> convertToMap(STRIP actionsIn, int pID)
	{
		Map<Integer, Action> actionsOut = new HashMap<Integer, Action>();
		if(actionsIn.unit.getTemplateView().getUnitName() == move)
		{
			actionsOut.put(pID, Action.createCompoundMove(pID, actionsIn.unit.getXPosition(), actionsIn.unit.getYPosition()));
		}
		else if(actionsIn.unit.getTemplateView().getUnitName() == deposit)
		{
			actionsOut.put(pID, Action.createCompoundDeposit(pID, actionsIn.unit.getID()));
		}
		else if(actionsIn.unit.getTemplateView().getUnitName() == gather)
		{
			actionsOut.put(pID, Action.createCompoundGather(pID, actionsIn.unit.getID()));
		}
		return actionsOut;
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
