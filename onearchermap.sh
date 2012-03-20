javac -cp 'lib/SimpleRTSv3.1.jar' src/edu/cwru/SimpleRTS/agent/agent1.java

java -cp lib/SimpleRTSv3.1.jar:src/edu/cwru/SimpleRTS/agent:src edu.cwru.SimpleRTS.Main --config data/midasConfig.xml data/rc_3m5t.xml --agent  edu.cwru.SimpleRTS.agent.agent1 0  --agent edu.cwru.SimpleRTS.agent.visual.VisualAgent 0 --agentparam true --agentparam true

