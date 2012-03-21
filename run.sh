javac -cp 'lib/SimpleRTSv3.1.jar' src/edu/cwru/SimpleRTS/agent/PEAgent.java src/edu/cwru/SimpleRTS/agent/Planner.java src/edu/cwru/SimpleRTS/agent/AStarPath.java src/edu/cwru/SimpleRTS/agent/ResourceInfo.java src/edu/cwru/SimpleRTS/agent/STRIP.java

java -cp lib/SimpleRTSv3.1.jar:src edu.cwru.SimpleRTS.Main --config data/midasConfig.xml data/rc_3m5t.map --agent  edu.cwru.SimpleRTS.agent.PEAgent 0 --agentparam false --agent edu.cwru.SimpleRTS.agent.visual.VisualAgent 0 --agentparam true --agentparam true