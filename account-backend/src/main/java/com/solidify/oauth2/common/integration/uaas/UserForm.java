package com.solidify.oauth2.common.integration.uaas;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Created by tomasz on 9/14/2015.
 */
@Data
@AllArgsConstructor
public class UserForm {

    private String name;

    private String source;

    private String sourceId;

    private Map<String, String> attributes = Maps.newHashMap();

}
