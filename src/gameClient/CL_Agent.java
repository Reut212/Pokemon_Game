package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;
/**
 * This class represents the agents on the game using
 * directional edge(src,dest) in a (directional) weighted graph.
 *
 */
public class CL_Agent {
	/**
	 * This method represents an Epsilon
	 */
		public static final double EPS = 0.0001;
		private static int _count = 0;
		private static int _seed = 3331;
		private int _id;
	//	private long _key;
		private geo_location _pos;
		private double _speed;
		private edge_data _curr_edge;
		private node_data _curr_node;
		private directed_weighted_graph _gg;

		private CL_Pokemon _curr_fruit;

		private long _sg_dt;

		private double nearPokDist = 1000;
		
		private double _value;

	/**
	 * This method represents the agents on the game.
	 * @param g - directed_weighted_graph
	 * @param start_node - node ID
	 */
		public CL_Agent(directed_weighted_graph g, int start_node) {

			_gg = g;
			setMoney(0);
			this._curr_node = _gg.getNode(start_node);
			_pos = _curr_node.getLocation();
			_id = -1;
			setSpeed(0);

		}
	/**
	 * This method updatethe game.
	 * @param json - JSON string
	 */
		public void update(String json) {
			JSONObject line;
			try {
				// "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
				line = new JSONObject(json);
				JSONObject ttt = line.getJSONObject("Agent");
				int id = ttt.getInt("id");
				if(id==this.getID() || this.getID() == -1) {
					if(this.getID() == -1) {_id = id;}
					double speed = ttt.getDouble("speed");
					String p = ttt.getString("pos");
					Point3D pp = new Point3D(p);
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					double value = ttt.getDouble("value");
					this._pos = pp;
					this.setCurrNode(src);
					this.setSpeed(speed);
					this.setNextNode(dest);
					this.setMoney(value);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

	/**
	 * This method get source node.
	 * @return source node.
	 */
		public int getSrcNode() {return this._curr_node.getKey();}
	/**
	 * This method make a JSON
	 * @return JSON string
	 */
		public String toJSON() {
			int d = this.getNextNode();
			String ans = "{\"Agent\":{"
					+ "\"id\":"+this._id+","
					+ "\"value\":"+this._value+","
					+ "\"src\":"+this._curr_node.getKey()+","
					+ "\"dest\":"+d+","
					+ "\"speed\":"+this.getSpeed()+","
					+ "\"pos\":\""+_pos.toString()+"\""
					+ "}"
					+ "}";
			return ans;	
		}
	/**
	 * This method set money.
	 */
		private void setMoney(double v) {_value = v;}
	/**
	 * This method set next node.
	 * @param dest - destination key
	 * @return if there is another node return true, else return false
	 */
		public boolean setNextNode(int dest) {
			boolean ans = false;
			int src = this._curr_node.getKey();
			this._curr_edge = _gg.getEdge(src, dest);
			if(_curr_edge!=null) {
				ans=true;
			}
			else {_curr_edge = null;}
			return ans;
		}
	/**
	 * This method set current node.
	 * @param src - source key
	 */
		public void setCurrNode(int src) {
			this._curr_node = _gg.getNode(src);
		}
	/**
	 * This method set curr edge not null
	 * @return if edge id not null
	 */
		public boolean isMoving() {
			return this._curr_edge!=null;
		}
	/**
	 * This method is to string
	 * @return string
	 */
		public String toString() {
			return toJSON();
		}
		public String toString1() {
			String ans=""+this.getID()+","+_pos+", "+isMoving()+","+this.getValue();	
			return ans;
		}
	/**
	 * This method get ID
	 * @return get id
	 */
		public int getID() {
			// TODO Auto-generated method stub
			return this._id;
		}
	/**
	 * This method is to get a location
	 * @return 3D location
	 */
		public geo_location getLocation() {
			// TODO Auto-generated method stub
			return _pos;
		}
		public double getNearPokDist(){
			return nearPokDist;
		}
		
		public double getValue() {
			// TODO Auto-generated method stub
			return this._value;
		}



		public int getNextNode() {
			int ans = -2;
			if(this._curr_edge==null) {
				ans = -1;}
			else {
				ans = this._curr_edge.getDest();
			}
			return ans;
		}

		public double getSpeed() {
			return this._speed;
		}

		public void setSpeed(double v) {
			this._speed = v;
		}
		public CL_Pokemon get_curr_fruit() {
			return _curr_fruit;
		}
		public void set_curr_fruit(CL_Pokemon curr_fruit) {
			this._curr_fruit = curr_fruit;
		}

		public void set_SDT(long ddtt) {

			long ddt = ddtt;

			if(this._curr_edge!=null) {
				double w = get_curr_edge().getWeight();

				geo_location dest = _gg.getNode(get_curr_edge().getDest()).getLocation();
				geo_location src = _gg.getNode(get_curr_edge().getSrc()).getLocation();

				double de = src.distance(dest); // distance from src -> dest

				double dist = _pos.distance(dest);

				if(this.get_curr_fruit().get_edge()==this.get_curr_edge()) {
					 dist = _curr_fruit.getLocation().distance(this._pos);
				}

				double norm = dist/de;
				double dt = w*norm / this.getSpeed(); 
				ddt = (long)(1000.0*dt);

			}

			this.set_sg_dt(ddt);
		}
		
		public edge_data get_curr_edge() {
			return this._curr_edge;
		}
		public long get_sg_dt() {
			return _sg_dt;
		}
		public void set_sg_dt(long _sg_dt) {
			this._sg_dt = _sg_dt;
		}
		public void setNearPokDist(double d){
			this.nearPokDist = d;
		}
	}
