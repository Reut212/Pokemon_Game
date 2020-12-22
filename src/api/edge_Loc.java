package api;

import java.util.HashMap;

/**
 * This class represents a position on the graph (a relative position
 * on an edge - between two consecutive nodes).
 */
public class edge_Loc implements edge_location {

    /**
     * Returns the edge on which the location is.
     * @return edge data
     */
    @Override
    public edge_data getEdge() {

        return null;
    }

    /**
     * Returns the relative ration [0,1] of the location between src and dest.
     * @return ratio
     */
    @Override
    public double getRatio() {
        return 0;
    }
}
