package api;
/**
 * This interface represents the set of operations applicable on a
 * directional edge(src,dest) in a (directional) weighted graph.
 *
 */
public class edge_info implements edge_data {

    private double weight;
    private node_data src;
    private node_data dest;
    private String info;
    private int tag;

    /**
     * constructor for edge_info
     */
    public edge_info() {
        this.src = null;
        this.dest = null;
        this.weight = 0;
        this.info = "";
        this.tag = 0;
    }
    /**
     * constructor for edge_info
     * @param src - source node
     * @param dest - destination node
     * @param weight - weight of edge
     */
    public edge_info(node_data src, node_data dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
    /**
     * The id of the source node of this edge.
     * @return source node key
     */
    @Override
    public int getSrc() {
        return this.src.getKey();
    }
    /**
     * The id of the destination node of this edge
     * @return destination node key
     */
    @Override
    public int getDest() {
        return this.dest.getKey();
    }
    /**
     * returns the weight of this edge (positive value).
     * @return the weight of edge
     */
    @Override
    public double getWeight() {
        return this.weight;
    }
    /**
     * Returns the remark (meta data) associated with this edge.
     * @param n - new weight
     */
    public void setWeight(double n){
        this.weight = n;
    }
    /**
     *remark (meta data) associated with this edge.
     * @return the remark (meta data) associated with this edge.
     *
     */
    @Override
    public String getInfo() {
        return this.info;
    }
    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s - new string
     */
    @Override
    public void setInfo(String s) {
    this.info = s;
    }
    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return tag
     */
    @Override
    public int getTag() {
        return this.tag;
    }
    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
    this.tag = t;
    }
}
