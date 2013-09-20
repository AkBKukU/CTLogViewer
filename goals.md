Goals
=====

I intend to make the program have all of these features.

##General##

 - Show average temperatures for each core and of all cores
 - Show highs and lows of each core and the averages
 - Show temperature averages after 30 minutes of logging to account for cold startups
 - Graphs with useful data interpretation

###Charts###

 - Bar graph that shows how many instances of a temperature were logged
 - Bar graph to show show the average temperatures during the day
 - Line graph to show the change in temperature during a set time
 - Pie chart to show average load on each core

 
###Error Handling###
It needs to be able to compensate for these potential cases
 
 - Corrupt logs
 - Multiple CPUs in different logs
 - Overlapping time spans between logs