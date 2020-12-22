package api;

import com.google.gson.*;
import gameClient.util.Point3D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;
/**
 * This interface represents a Directed (positive) Weighted Graph Theory Algorithms
 */
public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph g;
    /**
     * Constructor for DWGraph_Algo
     */
    public DWGraph_Algo() {
        this.g = new DWGraph_DS();
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g - directed_weighted_graph
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.g = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return graph - directed_weighted_graph
     */
    @Override
    public directed_weighted_graph getGraph() {
        return g;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return graph - directed_weighted_graph
     */
    @Override
    public directed_weighted_graph copy() {
        if (g.getV().isEmpty()) {
            return new DWGraph_DS();
        }
        directed_weighted_graph g2 = new DWGraph_DS();
        for (node_data temp : g.getV()) {
            node_data temp2 = new node_Info(temp.getKey(), temp.getTag(), temp.getWeight(), temp.getInfo(), temp.getLocation());
            g2.addNode(temp2);
        }
        for (node_data temp : g.getV()) {
            if (!g.getE(temp.getKey()).isEmpty()) {
                for (edge_data neighborEdge : g.getE(temp.getKey())) {
                    node_data neighbor = g.getNode(neighborEdge.getDest());
                    g2.connect(temp.getKey(), neighbor.getKey(), neighborEdge.getWeight());
                }
            }
        }
        return g2;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return true if the graph is connected, false if not
     */
    @Override
    public boolean isConnected() {
        if (g.nodeSize() == 0) {
            return false;
        }
        if (g.nodeSize() == 1) {
            return true;
        }
        node_data node = g.getV().iterator().next();
        HashMap<Integer, node_data> visited = new HashMap<>();
        Queue<node_data> neighbors = new LinkedList<>();
        neighbors.add(node);
        while (!neighbors.isEmpty()) {
            if (!g.getE(node.getKey()).isEmpty()) {
                if (g.getE(neighbors.peek().getKey()).isEmpty()) {
                    return false;
                }
                for (edge_data neighborEdge : g.getE(neighbors.poll().getKey())) {
                    if (!visited.containsKey(neighborEdge.getDest())) {
                        visited.put(neighborEdge.getDest(), g.getNode(neighborEdge.getDest()));
                        neighbors.add(g.getNode(neighborEdge.getDest()));
                    }
                }
            } else {
                return false;
            }
        }
        if (visited.size() != g.nodeSize()) {
            return false;
        } else {
            directed_weighted_graph otherDirection = new DWGraph_DS();
            for (node_data temp : g.getV()) {
                otherDirection.addNode(temp);
                if (!g.getE(temp.getKey()).isEmpty()) {
                    for (edge_data tempDest : g.getE(temp.getKey())) {
                        otherDirection.connect(tempDest.getDest(), tempDest.getSrc(), tempDest.getWeight());
                    }
                }
            }
            HashMap<Integer, node_data> visited2 = new HashMap<>();
            Queue<node_data> neighbors2 = new LinkedList<>();
            neighbors2.add(node);
            while (!neighbors2.isEmpty()) {
                if (!g.getE(node.getKey()).isEmpty()) {
                    if (g.getE(neighbors2.peek().getKey()).isEmpty()) {
                        return false;
                    }
                    for (edge_data neighborEdge : g.getE(neighbors2.poll().getKey())) {
                        if (!visited2.containsKey(neighborEdge.getDest())) {
                            visited2.put(neighborEdge.getDest(), g.getNode(neighborEdge.getDest()));
                            neighbors2.add(g.getNode(neighborEdge.getDest()));
                        }
                    }
                } else {
                    return false;
                }
            }
            if (visited2.size() != g.nodeSize()) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return shortest path distance
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (g.getNode(src) == null || g.getNode(dest) == null) {
            return -1;
        }
        if (src == dest) {
            return 0;
        }
        double distance = 0;
        List<node_data> Path = shortestPath(src, dest);
        if (Path == null) {
            return -1;
        }
        for (int i = 0; i < Path.size() - 1; i++) {
            double currEdgeW = g.getEdge(Path.get(i).getKey(), Path.get(i + 1).getKey()).getWeight();
            distance = distance + currEdgeW;
        }
        return distance;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return a list of node presented the shortest path
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        List<node_data> path = new LinkedList<>();
        if (g.getNode(src) != null && g.getNode(dest) != null) {
            if (src == dest) {
                path.add(g.getNode(src));
                return path;
            }
            dw_graph_algorithms workGraph = new DWGraph_Algo();
            workGraph.init(g);
            HashMap<Integer, node_data> visited = new HashMap<>();
            HashMap<node_data, node_data> parent = new HashMap<>();
            Comparator<node_data> comp = Comparator.comparingDouble(node_data::getWeight);
            Queue<node_data> neighbors = new PriorityQueue<>(comp);
            neighbors.add(g.getNode(src));
            for(node_data initTag : workGraph.getGraph().getV()){
                initTag.setWeight(0);
            }
            if (g.getE(src).isEmpty()) {
                return null;
            }
            while (!neighbors.isEmpty()) {
                if (g.getE(neighbors.peek().getKey()) != null) {
                    for (edge_data neighborEdge : g.getE(neighbors.poll().getKey())) {
                        if (!visited.containsKey(neighborEdge.getDest())) {
                            workGraph.getGraph().getNode(neighborEdge.getDest()).setWeight(neighborEdge.getWeight()
                                    +workGraph.getGraph().getNode(neighborEdge.getSrc()).getWeight());
                            parent.put(g.getNode(neighborEdge.getDest()), g.getNode(neighborEdge.getSrc()));
                            visited.put(neighborEdge.getDest(), g.getNode(neighborEdge.getDest()));
                            neighbors.add(g.getNode(neighborEdge.getDest()));
                        } else {
                            if (workGraph.getGraph().getNode(neighborEdge.getSrc()).getWeight() + neighborEdge.getWeight()
                                    < workGraph.getGraph().getNode(neighborEdge.getDest()).getWeight()) {
                                workGraph.getGraph().getNode(neighborEdge.getDest()).setWeight((workGraph.getGraph().getNode(neighborEdge.getSrc()).getWeight()
                                        + neighborEdge.getWeight()));
                                parent.put(g.getNode(neighborEdge.getDest()), g.getNode(neighborEdge.getSrc()));
                            }
                        }
                    }
                }
            }
            if (parent.containsKey(getGraph().getNode(dest))) {
                Stack<node_data> revPath = new Stack<>();
                node_data temp = g.getNode(dest);
                while (!revPath.contains(g.getNode(src))) {
                    if (temp.getKey() == src) {
                        revPath.push(temp);
                        break;
                    } else {
                        revPath.push(temp);
                        temp = parent.get(temp);
                    }
                }
                while (!revPath.empty()) {
                    path.add(revPath.pop());
                }
                return path;
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        directed_weighted_graph graph = g;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(graph);
        System.out.println(json);
        try {
            PrintWriter pw = new PrintWriter(new File(file));
            pw.write(json);
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class, new graphJsonDeserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            directed_weighted_graph graph = gson.fromJson(reader, directed_weighted_graph.class);
            if (graph != null) {
                this.g = graph;
            } else {
                return false;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private class graphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {
        /**
         * This method load specific details from String JSON file in a requested order
         *
         * @param jsonElement - Object from JASON type
         * @param type - original signature of the deserializer
         * @param jsonDeserializationContext - A context of JASON deserializer
         * @return g0 - return a directed_weighted_graph.
         */
        @Override
        public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject nodes = jsonObject.get("myGraph").getAsJsonObject();
            node_data temp;
            directed_weighted_graph g0 = new DWGraph_DS();
            for (Map.Entry<String, JsonElement> node : nodes.entrySet()) {
                JsonObject curr = node.getValue().getAsJsonObject();
                JsonObject currLocation = curr.get("loc").getAsJsonObject();
                geo_location geoLocation = new Point3D(currLocation.get("_x").getAsDouble(), currLocation.get("_y").getAsDouble(), currLocation.get("_z").getAsDouble());
                temp = new node_Info(curr.get("key").getAsInt(), curr.get("tag").getAsInt(), curr.get("weight").getAsDouble(), curr.get("info").getAsString(), geoLocation);
                g0.addNode(temp);
            }
            JsonObject edges = jsonObject.get("connections").getAsJsonObject();
            for (Map.Entry<String, JsonElement> edge : edges.entrySet()) {
                for (Map.Entry<String, JsonElement> currEdge : edge.getValue().getAsJsonObject().entrySet()) {
                    JsonObject edgeData = currEdge.getValue().getAsJsonObject();
                    double weight = edgeData.get("weight").getAsDouble();
                    JsonObject stringSrc = edgeData.get("src").getAsJsonObject();
                    int src = stringSrc.get("key").getAsInt();
                    JsonObject stringDest = edgeData.get("dest").getAsJsonObject();
                    int dest = stringDest.get("key").getAsInt();
                    g0.connect(src, dest, weight);
                }
            }
            return g0;

        }
    }
}

