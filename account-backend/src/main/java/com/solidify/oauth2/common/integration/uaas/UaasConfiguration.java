package com.solidify.oauth2.common.integration.uaas;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Collections;

/**
 * Created by tomasz on 14.03.17.
 */
@Configuration
public class UaasConfiguration {

    @Bean
    public UaasResourceClient uaasResourceClient(Client client) throws Exception {
        return load(client, UaasResourceClient.class, "http://localhost:8080/uaas-resources/");
    }

    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    public Client client(ObjectMapper objectMapper){

        JacksonJaxbJsonProvider component = new JacksonJaxbJsonProvider();
        component.setMapper(objectMapper);

        Client register = ClientBuilder.newClient()
                .register(JacksonFeature.class)
                .register(component);
        return register;
    }


    public <T> T load(Client client, Class<T> resourceInterface, String url) throws Exception {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

        return WebResourceFactory.newResource(
                resourceInterface,
                client.target(url),
                false,
                headers,
                Collections.<Cookie>emptyList(),
                new Form()
        );
    }
}
