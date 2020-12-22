package test;

import api.*;
import Ex2;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AlgoTest {
    
    @org.junit.jupiter.api.Test
    void init() {
        directed_weighted_graph g=createGraph();
        dw_graph_algorithms myGraph= new DWGraph_Algo();
        dw_graph_algorithms myGraph2= new DWGraph_Algo();
        myGraph2.init(g);
        myGraph.init(g);
        assertSame(g, (myGraph2.getGraph()));
    }

    @org.junit.jupiter.api.Test
    void copy() {
        directed_weighted_graph g1=createGraph();
        dw_graph_algorithms g2 = new DWGraph_Algo();
        directed_weighted_graph g3 = new DWGraph_DS();
        g2.init(g1);
        g3=g2.copy();
        assertEquals(g1,g3);
        g1.removeNode(0);
        assertNotSame(g1, g3);
    }



    @org.junit.jupiter.api.Test
    void isConnected() {
        directed_weighted_graph g=createGraph();
        dw_graph_algorithms myGraph= new DWGraph_Algo();
        myGraph.init(g);
        assertTrue(myGraph.isConnected());
        node_data temp=new node_Info(1000000);

        myGraph.getGraph().addNode(temp);
        myGraph.getGraph().connect(1000000,50,2);
        assertFalse(myGraph.isConnected());
    }

    @org.junit.jupiter.api.Test
    void shortestPathDist() {
       dw_graph_algorithms g =new DWGraph_Algo();
       for(int i =0; i<5; i++) {
           node_data temp = new node_Info(i);
           g.getGraph().addNode(temp);             //MC = 5
       }
       g.getGraph().connect(0, 1, 5);
       g.getGraph().connect(0,2,2);
       g.getGraph().connect(1,3,8);
       g.getGraph().connect(2,3,1);
       g.getGraph().connect(3,2,1);
       g.getGraph().connect(3,4,3);   //MC = 11
       double a=g.shortestPathDist(0,3);
       assertTrue(a==3);
       double b=g.shortestPathDist(4,2);
       assertTrue(b==-1);
       double c=g.shortestPathDist(6,6);
       assertTrue(c==-1);
       double d=g.shortestPathDist(3,3);
       assertTrue(d==0);
        g.getGraph().connect(2,1,2);   //MC = 12
        double e=g.shortestPathDist(0,1);
        assertTrue(e==4);
        g.getGraph().removeNode(2);     //MC = 16
        double f=g.shortestPathDist(0,1);
        assertTrue(f==5);
        g.getGraph().removeEdge(0,2); //MC = 16
        double k=g.shortestPathDist(0,4);
        assertTrue(k==16);
        g.getGraph().removeEdge(0,2);
        assertTrue(g.getGraph().getMC()==16);
    }

    @org.junit.jupiter.api.Test
    void shortestPath() {
        dw_graph_algorithms g =new DWGraph_Algo();
        for(int i =0; i<5; i++) {
            node_data temp = new node_Info(i);
            g.getGraph().addNode(temp);
        }
        g.getGraph().connect(0, 1, 5);
        g.getGraph().connect(0,2,2);
        g.getGraph().connect(1,3,8);
        g.getGraph().connect(2,3,1);
        g.getGraph().connect(3,2,1);
        g.getGraph().connect(3,4,3);

        List<node_data> l = g.shortestPath(3,3);
        assertTrue(l.size()==1 && l.contains(g.getGraph().getNode(3)));
        List<node_data> l2 = g.shortestPath(0,4);
        assertTrue(l2.size() == 4);
        List<node_data> check = new LinkedList<>();
        check.add(g.getGraph().getNode(0));
        check.add(g.getGraph().getNode(2));
        check.add(g.getGraph().getNode(3));
        check.add(g.getGraph().getNode(4));
      //  assertTrue(l2 == check);
        assertTrue(l2.equals(check));

    }

    @org.junit.jupiter.api.Test
    void saveAndLoad() {
        directed_weighted_graph g1= createGraph();
        dw_graph_algorithms g2 = new DWGraph_Algo();
        g2.init(g1);
        String str = "g";
        g2.save(str);
        g2.load(str);
        assertEquals(g1,g2.getGraph());
        g2.getGraph().removeEdge(2,3);
        assertFalse(g1==g2);
    }


    public static directed_weighted_graph createGraph(){
        directed_weighted_graph g0=new DWGraph_DS();
        for(int i=0; i<5; i++) {
            node_data temp = new node_Info(i);
            g0.addNode(temp);
        }
        g0.connect(0,1,2);
        g0.connect(1,2,2);
        g0.connect(2,3,2);
        g0.connect(3,4,2);
        g0.connect(4,0,2);

        return g0;
    }
}