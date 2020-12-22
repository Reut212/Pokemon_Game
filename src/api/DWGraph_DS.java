package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.io.Serializable;
/**
 * This class represents a directional weighted graph.
 * The interface has a road-system or communication network in mind -
 * and supports a large number of nodes (over 100,000).
 */
public class DWGraph_DS implements directed_weighted_graph ,Serializable{

    private HashMap<Integer,node_data> myGraph;
    private HashMap<node_data,HashMap<node_data,edge_data>> connections;
    private int numOfEdges;
    private int changes;

    /**
     *constructor of DWGraph_DS
     */
    public DWGraph_DS(){
    myGraph = new HashMap<>();
    connections = new HashMap<>();
    numOfEdges = 0;
    changes = 0;
    }
    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if(myGraph.containsKey(key))
            return myGraph.get(key);
        return null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     * @param src - source node
     * @param dest - destination node
     * @return a specific edge
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if (myGraph.containsKey(src)&&myGraph.containsKey(dest)&& src!=dest)
            if(connections.containsKey(getNode(src)))
                if(connections.get(getNode(src)).containsKey(getNode(dest)))
                    return connections.get(getNode(src)).get(getNode(dest));
        return null;
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     * @param n - a specific node
     */
    @Override
    public void addNode(node_data n) {
        if(!myGraph.containsKey(n.getKey()))
        myGraph.put(n.getKey(),n);
        connections.put(n,new HashMap<>());
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (myGraph.containsKey(src)&&myGraph.containsKey(dest)&& w>0 && src!=dest){
                if(!connections.get(getNode(src)).containsKey(getNode(dest))){
                    connections.get(getNode(src)).put(getNode(dest), new edge_info(getNode(src), getNode(dest), w));
                    changes++;
                    numOfEdges++;
                }
            }
        }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     * @return collections of nodes
     */
    @Override
    public Collection<node_data> getV() {
        return myGraph.values();
    }

    /**
     * This method returns a neighbors collection of a specific vertex.
     * @param node_id - a specific node
     * @return neighbors collection of a specific vertex
     */
    public Collection<node_data> getNei (node_data node_id){
        return connections.get(node_id).keySet();
    }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     * @param node_id - a specific node ID
     * @return collections of edges
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if(myGraph.containsKey(node_id))
        return connections.get(getNode(node_id)).values();
        return null;
    }
    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     * @param key - a specific key
     */
    @Override
    public node_data removeNode(int key) {
        if(myGraph.containsKey(key)){
            for ( node_data temp : myGraph.values() ) {
                if(removeEdge(temp.getKey(),key)!=null){
                    removeEdge(temp.getKey(),key);
                    numOfEdges--;
                    changes++;
                }
                if(removeEdge(key,temp.getKey())!=null){
                    removeEdge(key,temp.getKey());
                    numOfEdges--;
                    changes++;
                }
            }
            node_data removedNode = getNode(key);
            myGraph.remove(key);
            changes++;
            return removedNode;
        }
        return null;
    }
    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     * @param src - source node
     * @param dest - destination node
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if(myGraph.containsKey(src)&&myGraph.containsKey(dest)&&src!=dest){
            if(connections.get(getNode(src)).containsKey(getNode(dest))){
                numOfEdges--;
                changes++;
                return connections.get(getNode(src)).remove(getNode(dest));
            }
            return null;
        }
        return null;
    }
    /** Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return node size
     */
    @Override
    public int nodeSize() {
        return myGraph.size();
    }
    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     * @return edge size
     */
    @Override
    public int edgeSize() {
        return this.numOfEdges;
    }
    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return get MC
     */
    @Override
    public int getMC() {
        return this.changes;
    }
    /**
     * This method compare 2 Objects to check if they are from the same type.
     * @return boolean flag
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWGraph_DS that = (DWGraph_DS) o;
        boolean flag = false;
        if (myGraph.size() == that.nodeSize() &&
                numOfEdges == that.numOfEdges) {
            flag = true;
            for (int src : myGraph.keySet())
                for (node_data dest : connections.get(getNode(src)).keySet())
                    if (((DWGraph_DS) o).getEdge(src, dest.getKey()).getSrc() != src || ((DWGraph_DS) o).getEdge(src, dest.getKey()).getDest() != dest.getKey())
                        flag = false;
        }

        return flag;

    }
}
