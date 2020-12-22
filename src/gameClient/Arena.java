package gameClient;

import api.*;
import com.google.gson.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 * @author boaz.benmoshe
 *
 */
public class Arena {
	/**
	 * represents epsilons
	 */
	public static final double EPS1 = 0.001, EPS2 = EPS1 * EPS1, EPS = EPS2;
	private directed_weighted_graph _gg;
	private List<CL_Agent> _agents;
	private List<CL_Pokemon> _pokemons;
	private List<String> _info;
	private static Point3D MIN = new Point3D(0, 100, 0);
	private static Point3D MAX = new Point3D(0, 100, 0);
	/**
	 * This method represents a new arena
	 */
	public Arena() {
		_info = new ArrayList<String>();
	}
	/**
	 * This method represents a constructor
	 * @param g - directed weighted graph
	 * @param r - a list of agents
	 * @param p - a list of pokemon
	 */
	private Arena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p) {
		_gg = g;
		this.setAgents(r);
		this.setPokemons(p);
	}
	/**
	 * This method represents a set of Pokemons
	 * @param f - a list of Pokemon
	 */
	public void setPokemons(List<CL_Pokemon> f) {
		this._pokemons = f;
	}
	/**
	 * This method represents a set of agents
	 * @param f - a list of agents
	 */
	public void setAgents(List<CL_Agent> f) {
		this._agents = f;
	}

	/**
	 * This method set a graph
	 * @param g - a directed_weighted_graph
	 */
	public void setGraph(directed_weighted_graph g) {
		this._gg = g;
	}//init();}
	/**
	 * This method initialize
	 */
	private void init() {
		MIN = null;
		MAX = null;
		double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
		Iterator<node_data> iter = _gg.getV().iterator();
		while (iter.hasNext()) {
			geo_location c = iter.next().getLocation();
			if (MIN == null) {
				x0 = c.x();
				y0 = c.y();
				x1 = x0;
				y1 = y0;
				MIN = new Point3D(x0, y0);
			}
			if (c.x() < x0) {
				x0 = c.x();
			}
			if (c.y() < y0) {
				y0 = c.y();
			}
			if (c.x() > x1) {
				x1 = c.x();
			}
			if (c.y() > y1) {
				y1 = c.y();
			}
		}
		double dx = x1 - x0, dy = y1 - y0;
		MIN = new Point3D(x0 - dx / 10, y0 - dy / 10);
		MAX = new Point3D(x1 + dx / 10, y1 + dy / 10);

	}
	/**
	 * This method get a set of agents
	 * @return a list of agents
	 */
	public List<CL_Agent> getAgents() {
		return _agents;
	}
	/**
	 * This method get a set of pokemons
	 * @return a list of pokemons
	 */
	public List<CL_Pokemon> getPokemons() {
		return _pokemons;
	}

	/**
	 * This method returns a directed_weighted_graph
	 * @return returns a directed_weighted_graph
	 */
	public directed_weighted_graph getGraph() {
		return _gg;
	}
	/**
	 * This method returns a meta data
	 * @return returns a list of metadata
	 */
	public List<String> get_info() {
		return _info;
	}
	/**
	 * This method updated meta data
	 * @param _info - a list of metadata
	 */
	public void set_info(List<String> _info) {
		this._info = _info;
	}

	////////////////////////////////////////////////////
	/**
	 * This method returns a list of agents
	 * @param aa - string of a file
	 * @param gg - directed_weighted_graph
	 * @return returns a list of agents
	 */
	public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
		ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();

		try {

			JSONObject ttt = new JSONObject(aa);
			JSONArray ags = ttt.getJSONArray("Agents");

			for (int i = 0; i < ags.length(); i++) {

				CL_Agent c = new CL_Agent(gg, 0);

//				c.updateDT();

				c.update(ags.get(i).toString());
				ans.add(c);
			}
			//= getJSONArray("Agents");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return ans;
	}

	/**
	 * This method returns a list of pokemon from JSON
	 * @param fs - string of a file
	 * @return returns a list of pokemon
	 */
	public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
		ArrayList<CL_Pokemon> ans = new ArrayList<CL_Pokemon>();
		try {
			JSONObject ttt = new JSONObject(fs);
			JSONArray ags = ttt.getJSONArray("Pokemons");
			for (int i = 0; i < ags.length(); i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				//double s = 0;//pk.getDouble("speed");
				String p = pk.getString("pos");

				CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, null);

				ans.add(f);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}
	/**
	 * This method update edge
	 * @param fr - a specific pokemon
	 * @param g -    directed_weighted_graph
	 */
	public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
		//	oop_edge_data ans = null;
		Iterator<node_data> itr = g.getV().iterator();
		while (itr.hasNext()) {
			node_data v = itr.next();
			Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
			while (iter.hasNext()) {
				edge_data e = iter.next();
				boolean f = isOnEdge(fr.getLocation(), e, fr.getType(), g);
				if (f) {
					fr.set_edge(e);
				}
			}
		}
	}
	/**
	 * This method check if the pokemon is on the edge
	 * @param p - a Point3D location
	 * @param src - source - a Point3D location
	 * @param dest - destination - a Point3D location
	 * @return returns a list of pokemon
	 */
	private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {

		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if (dist > d1 - EPS2*EPS2) {
			ans = true;
		}
		return ans;
	}
	/**
	 * This method check if the pokemon is on the edge
	 * @param p - a Point3D location
	 * @param s - source key
	 * @param d - destination key
	 * @param g - directed_weighted_graph
	 * @return returns a list of pokemon
	 */
	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p, src, dest);
	}
	/**
	 * This method check if the pokemon is on the edge
	 * @param p - a Point3D location
	 * @param e - edge data
	 * @param type - type of pokemon
	 * @param g - directed_weighted_graph
	 * @return returns a list of pokemon
	 */
	private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if (type < 0 && dest > src) {
			return false;
		}
		if (type > 0 && src > dest) {
			return false;
		}
		return isOnEdge(p, src, dest, g);
	}
	/**
	 * This method check the Graph range
	 * @param g - directed_weighted_graph
	 * @return returns the range of the graph
	 */
	private static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
		boolean first = true;
		while (itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if (first) {
				x0 = p.x();
				x1 = x0;
				y0 = p.y();
				y1 = y0;
				first = false;
			} else {
				if (p.x() < x0) {
					x0 = p.x();
				}
				if (p.x() > x1) {
					x1 = p.x();
				}
				if (p.y() < y0) {
					y0 = p.y();
				}
				if (p.y() > y1) {
					y1 = p.y();
				}
			}
		}
		Range xr = new Range(x0, x1);
		Range yr = new Range(y0, y1);
		return new Range2D(xr, yr);
	}
	/**
	 * This method check the Graph range
	 * @param g - directed_weighted_graph
	 * @param frame - 2 dimensions frame
	 * @return range of graph
	 */
	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}
}
