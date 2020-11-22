package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms , Serializable {

	private static final long serialVersionUID = 1L;
	private weighted_graph weightG;
    private nodeDistComp compare;


    public WGraph_Algo() {
        this.weightG = new WGraph_DS();
        compare = new nodeDistComp();
    }

    @Override
    public void init(weighted_graph g) {
        this.weightG = g;
        compare = new nodeDistComp();
    }

    @Override
    public weighted_graph getGraph() {
        return this.weightG;
    }

    @Override
    public weighted_graph copy() {
        if (this.weightG != null) {
            weighted_graph graph = new WGraph_DS();
            for (node_info node : this.weightG.getV()) {
                //copyNode(cGraph, toBeCopied);
            	int key = node.getKey();
            	graph.addNode(key);
            	
            	node_info nodeKey = graph.getNode(key);
            	nodeKey.setInfo(node.getInfo());
            	nodeKey.setTag(node.getTag());
            }

            for (node_info cNode : this.weightG.getV()) {
                for (node_info neighbor : this.weightG.getV(cNode.getKey())) {
                	graph.connect(neighbor.getKey(), cNode.getKey(), this.weightG.getEdge(neighbor.getKey(), cNode.getKey()));

                }

            }
            return graph;
        }
        return null;
    }

    @Override
    public boolean isConnected() {
    	for (node_info node : this.weightG.getV()) 
        	node.setTag(Double.MAX_EXPONENT);
        
        int x = 1;
        
        if (weightG.getV().size() == 1) 
        	return true;

        node_info tempNode = getArbitraryNode();
        if (tempNode == null) 
        	return true;
                
        Queue<node_info> list = new LinkedList<>();
        if(list == null)
        	return false;
        
        list.add(tempNode);
        tempNode.setTag(x);

        if (weightG.getV(tempNode.getKey()).size() == 0)
        	return false;
        
        while (list.isEmpty() == false) {
            node_info inf = list.poll();

            for (node_info i : weightG.getV(inf.getKey())) {
                if (x != i.getTag()) {
                    i.setTag(x);
                    list.add(i);
                }
            }

        }
        for (node_info node : weightG.getV()) {
            if (node.getTag() != x) 
            	return false;
        }
        return true;
    }

    @Override
    public double shortestPathDist(int st, int end) {
        if (weightG.getNode(st) == null || weightG.getNode(end) == null) 
        	return -1;
        if (st == end) 
        	return 0;
        
        List<node_info> path = shortestPath(st, end);
        double ans = path.get(path.size() -1).getTag();

        return ans;
    }

    @Override
    public List<node_info> shortestPath(int st, int end) {
    	for (node_info node : this.weightG.getV()) 
        	node.setTag(Double.MAX_EXPONENT);
        
        PriorityQueue<node_info> que = new PriorityQueue<>(compare);
        if(que == null)
        	return null;
        
        List<node_info> nodes = new ArrayList<>();
        if(nodes == null)
        	return null;
        
        HashMap<Integer, node_info> prev = new HashMap<>();
        if(prev == null)
        	return null;
        
        if (st == end) 
        	return nodes;
        
        if (weightG.getNode(st) == null || weightG.getNode(end) == null) 
        	return null;
        
        weightG.getNode(st).setTag(0);
        que.add(weightG.getNode(st));
        
        while (que.isEmpty() == false) {
            node_info stNode = que.poll();
            
            Collection<node_info> list = weightG.getV(stNode.getKey());
            for (node_info near : list) {
                if (nodes.contains(near) == false) {
                    double edge = weightG.getEdge(stNode.getKey(), near.getKey());
                    double sum  = edge + stNode.getTag();
                    if (near.getTag() - sum > 0) {
                    	near.setTag(sum);
                        prev.put(near.getKey(), stNode);
                        que.add(near);
                    }
                }
            }
            nodes.add(stNode);
        }
        
        return path(prev, st, end);
    }

    @Override
    public boolean save(String f) {
        ObjectOutputStream oOutput;
        try {
            FileOutputStream fOutput = new FileOutputStream(f);
            if(fOutput == null)
            	return false;
            
            oOutput = new ObjectOutputStream(fOutput);
            if(oOutput == null)
            	return false;

            oOutput.writeObject(this.getGraph());
            return true;
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace(); 
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public boolean load(String f) {
        try {
            FileInputStream fInput = new FileInputStream(f);
            if(fInput == null)
            	return false;
            
            ObjectInputStream oInput = new ObjectInputStream(fInput);
            if(oInput == null)
            	return false;
            
            weighted_graph graph = (WGraph_DS) oInput.readObject();
            this.init(graph);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    private node_info getArbitraryNode() {
        for (node_info n : this.weightG.getV()) {
            return n;
        }
        return null;
    }

    private List<node_info> path(HashMap<Integer, node_info> prev, int st, int end) {
        List<node_info> path = new ArrayList<>();
        if(path == null)
        	return null;
        
        node_info node = weightG.getNode(end);
        path.add(node);
        
        while ((path.contains(prev.get(st))) == false) {
        	
        	node = prev.get(node.getKey());
            path.add(node);
            
            if (node.getKey() == st) 
            	break;
        }
        
        Collections.reverse(path);
        return path;
    }
}

 class nodeDistComp implements Comparator<node_info> {

    @Override
    public int compare(node_info obj1, node_info obj2) {
    	if(obj1.getTag() > obj2.getTag())
    		return (int) (obj1.getTag() - obj2.getTag());
    	else
    		return (int) (obj2.getTag() - obj1.getTag());
    }
}
