Create GpsUtilCustom to pass test, because FormatNumberException when Double.parseDouble("123,123") 
    => replace "," by "." 
    
Get First 5 nearest attraction

User cannot have multiple reward for an attraction, even if he changes location.
Only calculate reward on the first visited location

Synchronized on method "hasRewardForAttraction"
When not synchronized, 2 threads are adding reward at the same time for the same attraction

Get Location :
Users	Time (Seconds)	Improved Time
100	    7	            0
1000	75	            0
5000	376	            0
10000	762	            0
50000	3791	        0   
100000	7579	        0


Get Rewards : 
Users	Time (Seconds)	Improved Time   Improved Time (pool Thread)
100	    44	            3               2
1000	472	            4               2
10000	5820	        12              11
100000	64020	        104             111


improved code with thread pool, wait all threads to finish before doing test
