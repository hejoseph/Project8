package tourGuide;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import tourGuide.util.HttpUtils;
import tourGuide.util.LoggingRequestInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TestRestTemplate{

    public static RestTemplate createRestTemplate(int connectionTimeoutMs, int readTimeoutMs, ObjectMapper objectMapper) {

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
        httpRequestFactory.setConnectTimeout(connectionTimeoutMs);
        httpRequestFactory.setReadTimeout(readTimeoutMs);

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(httpRequestFactory));
        MappingJackson2HttpMessageConverter messageConverter = restTemplate.getMessageConverters().stream().filter(MappingJackson2HttpMessageConverter.class::isInstance)
                .map(MappingJackson2HttpMessageConverter.class::cast).findFirst().orElseThrow(() -> new RuntimeException("MappingJackson2HttpMessageConverter not found"));
        messageConverter.setObjectMapper(objectMapper);

        restTemplate.getMessageConverters().stream().filter(StringHttpMessageConverter.class::isInstance).map(StringHttpMessageConverter.class::cast).forEach(a -> {
            a.setWriteAcceptCharset(false);
            a.setDefaultCharset(StandardCharsets.UTF_8);
        });

        return restTemplate;
    }


//    @Test
//    public void testRestTemplate(){
//        RestTemplate restTemplate = HttpUtils.createRestTemplate(60 * 1000, 3 * 60 * 60 * 1000, objectMapper);
//    }

}
