package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class WGraph_DS implements weighted_graph , Serializable {

	private static final long serialVersionUID = 1L;
    private HashMap<Integer, node_info> weightGraph;
    private HashMap<Integer, HashMap<node_info, Double>> wightList;
    private int vers, edgs, mc;

    public WGraph_DS() {
    	this.weightGraph = new HashMap<>();
    	this.wightList = new HashMap<>();
        this.edgs = 0;
        this.mc = 0;
    }
    
    @Override
    public void addNode(int k) {
        if ((this.weightGraph.containsKey(k)) == false) {
            this.weightGraph.put(k, new Node(k));
            this.wightList.put(k,new HashMap<>());
            vers++;
            mc++;
        }

    }

    @Override
    public node_info getNode(int k) {
        return this.weightGraph.get(k);
    }

    @Override
    public boolean hasEdge(int n1, int n2) {
    	boolean contains1 = this.wightList.get(n1).containsKey(getNode(n2));
    	boolean contains2 = this.wightList.get(n2).containsKey(getNode(n1));
        if(contains1 && contains2)
        	return true;
        else
        	return false;
    }

    @Override
    public double getEdge(int n1, int n2) {
        if (hasEdge(n1, n2) ){
            double w = wightList.get(n1).get(getNode(n2));
            return w;
        }
        if(n1 < n2)
        	return n1 - n2;
        else
        	return n2 - n1;
    }

    @Override
    public void connect(int n1, int n2, double w) {
        if (this.weightGraph.containsKey(n1)) {
        	if(this.weightGraph.containsKey(n2)) {
        		if(n1!=n2){
        			if(hasEdge(n1,n2) == false){
        				HashMap<node_info, Double> hash1 = wightList.get(n1);
        				hash1.put(getNode(n2), w);
        				HashMap<node_info, Double> hash2 = wightList.get(n2);
        				hash2.put(getNode(n1), w);
                        mc++;
                        edgs++;
        			}
        		}       		
        	} 	
        }
    }

    @Override
    public Collection<node_info> getV() {
        return this.weightGraph.values();
    }

    @Override
    public Collection<node_info> getV(int node) {
        return this.wightList.get(node).keySet();
    }

    @Override
    public node_info removeNode(int k) {
        if (this.weightGraph.containsKey(k)) {
            for (node_info n : weightGraph.values()) {
                    removeEdge(n.getKey(), k);
                    vers--;
            }
            return weightGraph.remove(k);
        }
        return null;
    }

    @Override
    public void removeEdge(int n1, int n2) {
        if (hasEdge(n1, n2)) {
        	wightList.get(n1).remove(getNode(n2));
        	wightList.get(n2).remove(getNode(n1));
            edgs--;
            mc++;
        }
    }

    @Override
    public int nodeSize() {
        return this.vers;
    }

    @Override
    public int edgeSize() {
        return this.edgs;
    }

    @Override
    public int getMC() {
        return this.mc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) 
        	return true;
        if (o == null) 
        	return false;
        if(getClass() != o.getClass())
        	return false;
        
        WGraph_DS gDS = (WGraph_DS) o;
        if(vers == gDS.vers && edgs == gDS.edgs)
        	return true;
        else
        	return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vers, edgs, mc, weightGraph, wightList);
    }
    
    private class Node implements node_info, Serializable {

		private static final long serialVersionUID = 1L;
        private String nodeInfo;
        private int key; 
        private double tag;


        public Node(int k) {
        	this.nodeInfo = "";
            this.key = k;
            this.tag = 0;
        }


        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.nodeInfo;
        }

        @Override
        public void setInfo(String s) {
            this.nodeInfo = s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }
    }
}
