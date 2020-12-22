package gameClient;

import api.*;
import Server.Game_Server_Ex2;
import com.google.gson.*;
import gameClient.util.Point3D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
/**
this class implements the login display
 */
public class Ex2 implements Runnable {

    private static myLogin _login;
    private static MyFrame _win;
    private static Arena _ar;
    private static long dt;
    private static HashMap<Integer, Long> agent_times; // <agent_id, dt>

    private static long id;
    private static int level;

    public static void main(String[] a) {

        if (a.length > 0) { // have arguments

            id = Long.parseLong(a[0]);
            level = Integer.parseInt(a[1]);
            Thread client = new Thread(new gameClient.Ex2());
            client.start();

        }
        else {
            _login = new myLogin("Welcome to the game, have fun!");
            boolean play = _login.getStatus();
            while (!play) {
                play = _login.getStatus();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            level = _login.getLEVEL();
            id = _login.getID();



            Thread client = new Thread(new gameClient.Ex2());

            client.start();
        }
    }
    /**
     * This method runs the game based on given information from the user, such as:
     * ID, Level.
     */
    @Override
    public void run() {

        game_service game = Game_Server_Ex2.getServer(level); // you have [0,23] games

          game.login(305170219);

        String g = game.getGraph();
        directed_weighted_graph gg = loadG(g);
        System.out.println(game);
        init(game);

        game.startGame();

        int ind = 0;

        dt = 100;

        List<CL_Agent> l = Arena.getAgents(game.getAgents(), gg);
        _ar.setAgents(l); //this is the first SET.

        for (CL_Agent a : l) {
            agent_times.put(a.getID(), dt);
        }


        while (game.isRunning()) {
            long time = game.timeToEnd()/1000;
            _win.setTitle("GameEx2: Level: " +level+ "  TimeToEnd: " + time + "  Moves: " + JSONmoves(game) + "  Grade: " + JSONgrade(game));
            _win.update(_ar);
            _win.paint(_win.getGraphics());
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
                synchronized (Thread.currentThread()) {
                    moveAgants(game, gg);
                }
//                    Comparator<CL_Agent> comp = Comparator.comparingDouble(CL_Agent::getNearPokDist);
//                    Queue<CL_Agent> Agents = new PriorityQueue<>(comp);

                List<String> stam = new ArrayList<>();

                stam.add("time to finish: " + game.timeToEnd()/1000);

                for (CL_Agent agent : _ar.getAgents()) {
                    stam.add("agent: " + agent.toString());
                }

                _ar.set_info(stam);

                PriorityQueue<Long> Agents = new PriorityQueue<>(agent_times.values());

//                System.out.println("map: " + agent_times.values());

                dt = Agents.poll();
                if(dt == 0){
                    dt=60;
                }
                if(dt<70){
                    dt=70;
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(!_win.isActive()){
                System.exit(0);
            }
        }
        String res = game.toString();
        System.out.println(res);

        System.exit(0);
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     *
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        String agentStatus = game.move();


        // before move

//        HashMap<CL_Agent, boolean> lock = new HashMap<CL_Agent, boolean>();

//        agent.locked();

        List<CL_Agent> agentsList = Arena.getAgents(agentStatus, gg);

        //after move

        _ar.setAgents(agentsList);
        String gamePokemons = game.getPokemons();


        List<CL_Pokemon> pokemonsList = Arena.json2Pokemons(gamePokemons);


        _ar.setPokemons(pokemonsList);

        for (int i = 0; i < agentsList.size(); i++) {
            CL_Agent currAgent = agentsList.get(i);
            int id = currAgent.getID();
            int dest = currAgent.getNextNode();
            int src = currAgent.getSrcNode();
            double v = currAgent.getValue();
            if (dest == -1) {
            dest = nextNode(game, gg, currAgent);
            synchronized (Thread.currentThread()) {
            game.chooseNextEdge(currAgent.getID(), dest);
            }
            }
        }
    }

    /**
     * a very simple random walk implementation!
     *
     * @param
     * @return
     * @paramg
     */
    private static int nextNode(game_service game, directed_weighted_graph g, CL_Agent currAgent) {
        String gamePokemons = game.getPokemons();
        List<CL_Pokemon> pokemonsList = Arena.json2Pokemons(gamePokemons);
        for (int i = 0; i < pokemonsList.size(); i++) {
            Arena.updateEdge(pokemonsList.get(i), g);
        }
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(g);
        int src = currAgent.getSrcNode();

        int pokeSrc = 0;
        int pokeDest = 0;
        geo_location pokeLoc = null;

        double Dist = 0;
        double value = 0;
        for (CL_Pokemon currPokemon : pokemonsList) {
            value = currPokemon.getValue();
            double weight = g1.shortestPathDist(src, currPokemon.get_edge().getSrc());
            if (weight != 0) {
                if (value / weight > Dist) {
                    currAgent.set_curr_fruit(currPokemon);
                    Dist = value / weight;

                    pokeSrc = currPokemon.get_edge().getSrc();
                    pokeDest = currPokemon.get_edge().getDest();
                    pokeLoc = currPokemon.getLocation();
                }
            } else {
                currAgent.set_curr_fruit(currPokemon);
                pokeSrc = currPokemon.get_edge().getSrc();
                pokeDest = currPokemon.get_edge().getDest();
                pokeLoc = currPokemon.getLocation();
                break;
            }
        }
        List<node_data> path = g1.shortestPath(src, pokeSrc);
        agent_times.put(currAgent.getID(),(long) 1000.0);
        if (path.size() > 1) {

            return path.get(1).getKey();
        }
        else {
            set_SDT(dt, currAgent, pokeDest, pokeSrc, pokeLoc, g);
            return currAgent.get_curr_fruit().get_edge().getDest();
        }
    }
    /**
     * this method initializing the game based on a requested level.
     * @param game
     * @return
     */
    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();

//        List<CL_Agent> l = Arena.getAgents(game.getAgents()/game.move(), _ar.getGraph())/

        agent_times = new HashMap<>();

        directed_weighted_graph gg = loadG(g);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));

        _win = new MyFrame("test Ex2");

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        _win.setSize(dimension.width - 100, dimension.height - 100);
        _win.update(_ar);
        _win.setVisible(true);
        String gr = game.toString();
        JSONObject agents;
        try {
            agents = new JSONObject(gr);
            JSONObject gameInformation = agents.getJSONObject("GameServer");
            int numAgents = gameInformation.getInt("agents");
            ArrayList<CL_Pokemon> pokemonsList = Arena.json2Pokemons(game.getPokemons());
            Comparator<CL_Pokemon> comp = Comparator.comparingDouble(CL_Pokemon::getValue);
            pokemonsList.sort(comp);
            for (int i = pokemonsList.size(); i > 0; i--) {
                Arena.updateEdge(pokemonsList.get(i - 1), gg);
            }

            for (int i = 0; i < numAgents; i++) {

                if (i < pokemonsList.size()) {
                    int pref = pokemonsList.size() - i - 1;
                    CL_Pokemon c = pokemonsList.get(pref);
                    int pokemonEdge;
                    if (c.getType() < 0) {
                        pokemonEdge = getMax(c.get_edge().getSrc(), c.get_edge().getDest());
                    } else {
                        pokemonEdge = getMin(c.get_edge().getSrc(), c.get_edge().getDest());
                    }
                    game.addAgent(pokemonEdge);
                } else {
                    CL_Pokemon c2 = pokemonsList.get(0);
                    int pokemonEdge2 = c2.get_edge().getDest();
                    game.addAgent(pokemonEdge2);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * this method choose the maximal number between two numbers.
     * @param a - Integer
     * @param b - Integer
     * @return
     */
    public static int getMax(int a, int b) {
        return (a > b ? a : b);
    }
    /**
     * this method choose the minimal number between two numbers.
     * @param a - Integer
     * @param b - Integer
     * @return
     */
    public static int getMin(int a, int b) {
        return (a < b ? a : b);
    }
    /**
     * this method load a graph based on string file.
     * @param file - String
     * @return graph - directed_weighted_graph.
     */
    public directed_weighted_graph loadG(String file) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(directed_weighted_graph.class, new graphJsonDeserGame());
        Gson gson = builder.create();
        directed_weighted_graph graph = gson.fromJson(file, directed_weighted_graph.class);
        if (graph != null) {
            return graph;
        } else {
            return null;
        }
    }

    private static class graphJsonDeserGame implements JsonDeserializer<directed_weighted_graph> {
        /**
         * This method load specific details from String JSON file in a requested order
         *
         * @param jsonElement - Object from JASON type
         * @param type - original signature of the deserializer
         * @param jsonDeserializationContext - A context of JASON deserializer
         * @return g1 - return a directed_weighted_graph.
         */
        @Override
        public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray nodes = jsonObject.get("Nodes").getAsJsonArray();
            node_data temp;
            directed_weighted_graph g1 = new DWGraph_DS();
            for (JsonElement curr : nodes) {
                temp = new node_Info(curr.getAsJsonObject().get("id").getAsInt(), posToDouble(curr.getAsJsonObject().get("pos").getAsString()));
                g1.addNode(temp);
            }
            JsonArray edges = jsonObject.get("Edges").getAsJsonArray();
            for (JsonElement curr : edges) {
                int src = curr.getAsJsonObject().get("src").getAsInt();
                int dest = curr.getAsJsonObject().get("dest").getAsInt();
                double w = curr.getAsJsonObject().get("w").getAsDouble();
                g1.connect(src, dest, w);
            }
            return g1;
        }
        /**
         * this method get a position with String value and convert it to geo_Location value - Point3D.
         * @param pos - String
         * @return Point3D - geo_location.
         */
        private geo_location posToDouble(String pos) {
            String[] loc = pos.split(",");
            return new Point3D(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]));
        }
    }
    /**
     * this method get the numbers of moves from a specific game.
     * @param game - String
     * @return int - moves.
     */
    public int JSONmoves(game_service game) {
        try {
            JSONObject jsonObject = new JSONObject(game.toString());
            JSONObject jsonArray = jsonObject.getJSONObject("GameServer");
            return jsonArray.getInt("moves");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * this method get the numbers of grade from a specific game.
     * @param game - String
     * @return int - grade.
     */
    public int JSONgrade(game_service game) {
        try {
            JSONObject jsonObject = new JSONObject(game.toString());
            JSONObject object = jsonObject.getJSONObject("GameServer");
            return object.getInt("grade");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * this method set the serial display time of the game.
     * @param ddtt - long
     * @param currAgent - current agent
     * @param pokeDest - Dest of Pokemon
     * @param pokeSrc - Source of Pokemon
     * @param pokeLoc - A location of Pokemon
     * @param g - graph
     */
    public static void set_SDT(long ddtt, CL_Agent currAgent, int pokeDest, int pokeSrc, geo_location pokeLoc, directed_weighted_graph g) {

        geo_location pok_dest_loc = g.getNode(pokeDest).getLocation();
        geo_location pok_src_loc = g.getNode(pokeSrc).getLocation();

        double de = pok_src_loc.distance(pok_dest_loc); // distance from src -> dest



        double dist = currAgent.getLocation().distance(pok_dest_loc);
//
        if(pokeDest == currAgent.getSrcNode()) {
            assert pokeLoc != null;
            dist = pokeLoc.distance(currAgent.getLocation());
//            dist = 1;
        }
        else {
            if(pokeSrc == currAgent.getSrcNode()) {
                assert pokeLoc != null;
                dist = pokeLoc.distance(currAgent.getLocation());
            }
        }

        double norm = dist/de;


        double dt = g.getEdge(pokeSrc, pokeDest).getWeight() * norm / currAgent.getSpeed();
        ddtt = (long)(100.0*dt);

        agent_times.put(currAgent.getID(), (long) ddtt);
    }


}
