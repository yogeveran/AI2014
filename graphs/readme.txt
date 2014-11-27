Instructions on how to create the world.

1. First Line MUST be of format:
#V n initialSupplies1 initialSupplies2 ... initialSuppliesn

Where n is an integer from 1 to 1000
This will create n of vertices from 1 to n including.
and initialSuppliesi is the amount of supplies in the vertex i

2. To add edges just add the line multiple
#E A B weight


3. To add Agents just put:

#A type [start] [goal] [maxExpansions]

where type is:
1 - Human, 2 - Obama, 3 - greedy Yazidi, 4 - greedy ISIS, 5 - greedy Search Yazidi, 6 - A* Yazidi, 7 - RTA* Yazidi

-------------------------------------------------
If we would like to see which node is expanded each time. We need to uncomment in expand function inside node class
-----------------------------------------------------
the difference in graphs 
3.(type 7) shows a RTA* Yazidi we can see the difference the number of expansions make. With expand 4 we see the Yazidi dies 
since it will always expand the NoOp side of the tree only when we allow 5 expansions each turn will it be able to reach
the traverse side of the tree and win.
3+. this graph shows us the difference in RTA* Yazidi of 6 expansion and 7 expansion where both succeed but 6 start on the wrong path 
and fixes itself why 7 starts straight away on the right path 
4. (type 5) regular greedy search Yazidi with heuristics 
5.(type 3) 2 regular greedy Yazidi without heuristics 
7.(type 1) Here we use a Human agent to check and see different ways to reach the goal (most useful for debugging)
14. Here we made a big graph and use multiple agents . We checked different runs of this graph with all possible agents and
starting positions . As well we tried food in different areas of the graph to see if this changes routes and such.