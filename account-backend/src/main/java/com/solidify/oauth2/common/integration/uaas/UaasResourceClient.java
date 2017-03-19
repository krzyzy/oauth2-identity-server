package com.solidify.oauth2.common.integration.uaas;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by tomasz on 13.03.17.
 */
@Path("/user")
public interface UaasResourceClient {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    UserDto createUser(UserForm form);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<UserDto> getUser(@QueryParam("source") String source, @QueryParam("sourceId") String sourceId);
}
