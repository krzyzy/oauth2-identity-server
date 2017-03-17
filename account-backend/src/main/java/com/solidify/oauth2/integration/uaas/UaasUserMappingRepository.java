package com.solidify.oauth2.integration.uaas;

import java.util.Optional;

/**
 * Created by tomasz on 13.03.17.
 */
public interface UaasUserMappingRepository {

    Optional<UaasUserMapping> getBySource(String source, String sourceId);

    void save(UaasUserMapping uaasUserMapping);

}
