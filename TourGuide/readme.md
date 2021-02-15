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
1000	75	            1               
5000	376	            8               
10000	762	            19              
50000	3791	        351             
100000	7579	        430                            


Get Rewards : 
Users	Time (Seconds)	Improved Time   
100	    44	            2               
1000	472	            4               
10000	5820	        53
100000	64020	        260


improved code with thread pool, wait all threads to finish before doing test

improved perf for get location



org.springframework.web.client.ResourceAccessException: 
    I/O error on GET request for "http://localhost:9092/getVisitedLocation": Address already in use: connect; 
    nested exception is java.net.BindException: Address already in use: connect
at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:748)
at org.springframework.web.client.RestTemplate.execute(RestTemplate.java:674)
at org.springframework.web.client.RestTemplate.getForObject(RestTemplate.java:315)
at tourGuide.service.GpsUtilService.getUserLocation(GpsUtilService.java:52)
at tourGuide.service.TourGuideService.trackUserLocationWithoutThread(TourGuideService.java:117)
at tourGuide.service.TourGuideService$1.run(TourGuideService.java:138)
at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
at java.lang.Thread.run(Thread.java:748)
Caused by: java.net.BindException: Address already in use: connect
at java.net.DualStackPlainSocketImpl.connect0(Native Method)
at java.net.DualStackPlainSocketImpl.socketConnect(DualStackPlainSocketImpl.java:79)
at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350)
at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206)
at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188)
at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:172)
at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392)
at java.net.Socket.connect(Socket.java:589)
at java.net.Socket.connect(Socket.java:538)
at sun.net.NetworkClient.doConnect(NetworkClient.java:180)
at sun.net.www.http.HttpClient.openServer(HttpClient.java:463)
at sun.net.www.http.HttpClient.openServer(HttpClient.java:558)
at sun.net.www.http.HttpClient.<init>(HttpClient.java:242)
at sun.net.www.http.HttpClient.New(HttpClient.java:339)
at sun.net.www.http.HttpClient.New(HttpClient.java:357)
at sun.net.www.protocol.http.HttpURLConnection.getNewHttpClient(HttpURLConnection.java:1202)
at sun.net.www.protocol.http.HttpURLConnection.plainConnect0(HttpURLConnection.java:1138)
at sun.net.www.protocol.http.HttpURLConnection.plainConnect(HttpURLConnection.java:1032)
at sun.net.www.protocol.http.HttpURLConnection.connect(HttpURLConnection.java:966)
at org.springframework.http.client.SimpleBufferingClientHttpRequest.executeInternal(SimpleBufferingClientHttpRequest.java:76)
at org.springframework.http.client.AbstractBufferingClientHttpRequest.executeInternal(AbstractBufferingClientHttpRequest.java:48)
at org.springframework.http.client.AbstractClientHttpRequest.execute(AbstractClientHttpRequest.java:53)
at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:739)
... 8 more