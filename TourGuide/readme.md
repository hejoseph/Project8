Create GpsUtilCustom to pass test, because FormatNumberException when Double.parseDouble("123,123") 
    => replace "," by "." 
    
Get First 5 nearest attraction

User cannot have multiple reward for an attraction, even if he changes location.
Only calculate reward on the first visited location

Synchronized on method "hasRewardForAttraction"
When not synchronized, 2 threads are adding reward at the same time for the same attraction

Get Location :
Users	Time (Seconds)	Improved Time   Improved Time (26 attractions)
100	    7	            0 -> 6              14
1000	75	            1               16
5000	376	            8               24
10000	762	            19              34
50000	3791	        351             387
100000	7579	        875                            


Get Rewards : 
Users	Time (Seconds)	Improved Time   Improved Time (pool Thread)
100	    44	            2               2
1000	472	            4               2
10000	5820	        22 -> 28 -> 53              11
100000	64020	        496 -> 252             111


improved code with thread pool, wait all threads to finish before doing test

improved perf for get location