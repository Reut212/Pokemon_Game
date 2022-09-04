# Weighted & directed Graph. 

This project represents a Pokemon game.
In this game our goal is to get as much Pokemons as we can.
In order to catch a Pokemon we need to send our Agent towards 
this Pokemon so he can catch him.
When we catch a Pokemon , A new one shows up in the game randomly.

This game in based on Classes that we created such as:

```
node_info - represents the Vertex in the graph.
edge_info - represents the Edges in the graph.
directed_weighted_graph - our graph that contains all vertexes and edges.
dw_graph_algorithms - Operation of our algorithms on the graph.
```

Each class offers us multiple functions to get information about the object.
we have several algorithms that we can operate on our graph such as:

```
isConnected() - return true if and only if there is a path from every Vertex to every other Vertex in the graph.
shortestPath(Vertex 1 , Vertex 2) - return the *shortest* path from Vertex 1 to Vertex 2.
shortestPathDist(Vertex 1 , Vertex 2) - return the *shortest* distance from Vertex 1 to Vertex 2.
```

those 3 methods use the same algorithm (BFS).

our game algorithm is working this way:
- We set all the agents near the most values Pokemons.
- We collect the distance between an agent to all the Pokemons.
- Now we calculate which Pokemon is the ideal to go for based on the distance , 
- And the value of the Pokemon. (we pick the highets ratio value/distance)
- The Agents speed is based on the distance between them and the pokemons.
- The Agents steps are big until there is A Pokemon nearby,then their steps are small so that,
- The agent will catch the Pokemon and will not pass over him.

#For more information about the project you welcome to watch Wiki title 😎 .
