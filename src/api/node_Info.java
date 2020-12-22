package api;

import gameClient.util.Point3D;

import java.util.HashMap;
/**
 * This class represents the set of operations applicable on a
 * node (vertex) in a (directional) weighted graph.
 */
public class node_Info implements node_data {
    int key;
    int tag;
    double weight;
    String info;
    geo_location loc;
    private final HashMap<Integer, node_data> Nei; //collection of neighbors

    /**
     * This method initialize
     */
    public node_Info(){
        this.tag=0;
        this.weight=0;
        this.info="";
        Nei = new HashMap<>();
    }
    /**
     * This method is a constructor
     * @param node_id - a specific node
     * @param tag - a specific tag
     * @param weight - a specific weight
     * @param info - a specific info
     * @param Loc - a Point3D location of a specofoc node
     */
    public node_Info(int node_id, int tag, double weight, String info, geo_location Loc) {
        this.key = node_id;
        this.tag = tag;
        this.weight = weight;
        this.info = info;
        this.loc = Loc;
        Nei = new HashMap<>();
    }
    /**
     * This method is a constructor
     * @param key - a key of a specific node
     */
    public node_Info(int key){
        this.key=key;
        this.loc = new Point3D(0,0,0);
        this.tag=0;
        this.weight=0;
        this.info="";
        Nei = new HashMap<>();
    }
    /**
     * This method is a constructor
     * @param id - a key of a specific node
     * @param p - a point3D on a graph
     */
    public node_Info(int id, geo_location p) {
        this.key=id;
        this.loc=p;
        Nei = new HashMap<>();
    }

    /**
     * Returns the key (id) associated with this node.
     * @return the key associated with this node.
     */
    @Override
    public int getKey() {
        return this.key;
    }

    /** Returns the location of this node, if
     * none return null.
     *
     * @return the location of this node
     */
    @Override
    public geo_location getLocation() {
        return loc;
    }

    /** Allows changing this node's location.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
    loc = p;
    }

    /**
     * Returns the weight associated with this node.
     * @return the weight associated with this node.
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
    this.weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     * @return the remark associated with this node.
     */
    @Override
    public String getInfo() {
//            return "" + Nei.get(getKey()).getX() + Nei.get(getKey()).getY() + Nei.get(getKey()).getZ();
        return this.info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s - the remark associated with this node.
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
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
    this.tag = t;
    }

    @Override
    public String toString() {
        return "node_Info{" +
                "key=" + key +
                '}';
    }
}
