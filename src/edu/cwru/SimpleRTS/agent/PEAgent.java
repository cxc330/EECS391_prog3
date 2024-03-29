package edu.cwru.SimpleRTS.agent;

import java.util.*;

import edu.cwru.SimpleRTS.action.*;
import edu.cwru.SimpleRTS.environment.State.StateView;
import edu.cwru.SimpleRTS.model.Template.TemplateView;
import edu.cwru.SimpleRTS.model.resource.ResourceType;
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
	private ArrayList<Integer> peasantID = new ArrayList<Integer>();
	private String fileName = "pln.txt";
	public STRIP currentAction = null;
	private List<Integer> townHallIds;
	private int stepCount = 0;
	private boolean alreadyEnd = false;
	private int planCount = 0;
	
	public PEAgent(int playernum, String[] args) 
	{
		super(playernum);
		finalGoldTally = Integer.parseInt(args[0]);
		finalWoodTally = Integer.parseInt(args[1]);
		if(args[2].equals("true")) //check to see if we can build peasants
		{
			canBuildPeasant = true;
		}
		fileName = args[3];
	}

	@Override
	public Map<Integer, Action> initialStep(StateView state) 
	{
		stepCount++;
		List<Integer> allUnitIds = state.getAllUnitIds();
		List<Integer> peasantIds = findUnitType(allUnitIds, state, peasant);
		townHallIds = findUnitType(allUnitIds, state, townHall);
		
		//create Planner
		Planner planner = new Planner(state, finalGoldTally, finalWoodTally, canBuildPeasant, fileName);
		if	(townHallIds.size() > 0) //TownHall Exists. Check if resources available in here too?
		{
			actionsList = planner.generatePlan(peasantIds.get(0), townHallIds.get(0), state);
			if (actionsList != null)
				peasantID.add(actionsList.get(0).unit.getID());
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
		stepCount++;
		Map<Integer, Action> actions = new HashMap<Integer, Action>();

		List<Integer> allUnitIds = state.getAllUnitIds();
		List<Integer> peasantIds = findUnitType(allUnitIds, state, peasant);
		if (actionsList != null)
		actions = convertToMap(actionsList, peasantID, state);
		
		if(actions == null)
		{
			actions = new HashMap<Integer, Action>();
		}
		
		if(state.getResourceAmount(playernum, ResourceType.GOLD) >= finalGoldTally && state.getResourceAmount(playernum, ResourceType.WOOD) >= finalWoodTally)
		{
			if(!alreadyEnd)
			{
				alreadyEnd = true;
				System.out.println("==========================================");
				System.out.println("We have reached the end of this episode.");
				System.out.println("We have gone through " + planCount + " steps (" + stepCount + " SimpleRTS steps). [This includes movement from one adjacent square to another, gathering, and depositing resources].");
				System.out.println("The plan file have also been generated");
				System.out.println("Please quit SimpleRTS or use Ctrl + C to Exit this eipsode through the commandline.");
			}
			return new HashMap<Integer, Action>();
		}
		else
		{
			System.out.println("Gold: " + state.getResourceAmount(playernum, ResourceType.GOLD) + " Wood: " + state.getResourceAmount(playernum, ResourceType.WOOD) + "");
			
			for(int x = 0; x < peasantIds.size(); x++)
			{
				if(state.getUnit(peasantIds.get(x)).getCargoType() == ResourceType.GOLD)
					System.out.println("Peasant is carrying " + state.getUnit(peasantIds.get(x)).getCargoAmount() + " gold.");
				else if(state.getUnit(peasantIds.get(x)).getCargoType() == ResourceType.WOOD)
					System.out.println("Peasant is carrying " + state.getUnit(peasantIds.get(x)).getCargoAmount() + " wood.");
				else if(state.getUnit(peasantIds.get(x)).getCargoAmount() == 0)
					System.out.println("Peasant is carrying nothing.");
				else
					System.out.println("Peasant is carrying " + state.getUnit(peasantIds.get(x)).getCargoAmount() + " of something.");
			}
			System.out.println();
			
			return actions;
		}
	}

	@Override
	public void terminalStep(StateView state) {
	}
	
	public Map<Integer, Action> convertToMap(ArrayList<STRIP> actionsIn, ArrayList<Integer> pID, StateView state)
	{
		Map<Integer, Action> actionsOut = new HashMap<Integer, Action>();
		List<Integer> allUnitIds = state.getAllUnitIds();
		List<Integer> peasantIds = findUnitType(allUnitIds, state, peasant);
		
		pID = (ArrayList<Integer>) peasantIds;
		
		if (currentAction == null) //grab the first move
		{
			actionsIn.remove(0);
			currentAction = actionsIn.get(0);
			planCount++;
		}
		
		if(currentAction.unit.getTemplateView().getUnitName().equals(move))
		{
			if (actionsIn.size() > 0)
			{
				if(currentAction.buildPeasant)
				{
					System.out.println(state.getUnit(pID.get(0)).getTemplateView().getGoldCost());
					TemplateView peasantTemplate = state.getTemplate(playernum, peasant);
					actionsOut.put(townHallIds.get(0), Action.createPrimitiveProduction(townHallIds.get(0), peasantTemplate.getID()));
					//pID.add(peasantTemplate.getID());
				}
				actionsIn.remove(0);
				if (actionsIn.size() > 0)
				{
					currentAction = actionsIn.get(0); //grab the next move
					planCount++;
				}

				return actionsOut;
			}
		}
		
		if(currentAction.buildPeasant)
		{
			TemplateView peasantTemplate = state.getTemplate(playernum, peasant);
			actionsOut.put(townHallIds.get(0), Action.createCompoundProduction(townHallIds.get(0), peasantTemplate.getID()));
			//pID.add(peasantTemplate.getID());
		}
		
			if(currentAction.unit.getTemplateView().getUnitName().equals(deposit) || (state.getUnit(pID.get(0)).getCargoType() != null && actionsIn.size() <= 0))
			{	
				boolean contains = false;
				for (Integer id: pID)
				{
					if (state.getUnit(id) != null && state.getUnit(id).getCargoType() != null)
					{
						actionsOut.put(id, Action.createCompoundDeposit(id, 
								state.unitAt(currentAction.unit.getXPosition(), currentAction.unit.getYPosition())));
						contains = true;
					}
				}
				if (!contains && actionsIn.size() > 0)
				{
					if(currentAction.buildPeasant)
					{
						TemplateView peasantTemplate = state.getTemplate(playernum, peasant);
						actionsOut.put(townHallIds.get(0), Action.createCompoundProduction(townHallIds.get(0), peasantTemplate.getID()));
					}
					actionsIn.remove(0);
					if (actionsIn.size() > 0)
						currentAction = actionsIn.get(0);
					return actionsOut;
				}
			}
			else if(currentAction.unit.getTemplateView().getUnitName().equals(gather))
			{
				boolean contains = false;
				for (Integer id: pID)
				{
					if (state.getUnit(id).getCargoType() == null)
					{
						actionsOut.put(id, Action.createCompoundGather(id, 
								state.resourceAt(currentAction.unit.getXPosition(), currentAction.unit.getYPosition())));
						contains = true;
					}
				}
				if (!contains && actionsIn.size() > 0)
				{
					if(currentAction.buildPeasant)
					{
						TemplateView peasantTemplate = state.getTemplate(playernum, peasant);
						actionsOut.put(townHallIds.get(0), Action.createCompoundProduction(townHallIds.get(0), peasantTemplate.getID()));
						//pID.add(peasantTemplate.getID());
					}
					actionsIn.remove(0);
					if (actionsIn.size() > 0)
						currentAction = actionsIn.get(0);
				}
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
