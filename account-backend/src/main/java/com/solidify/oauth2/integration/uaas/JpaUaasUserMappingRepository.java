package com.solidify.oauth2.integration.uaas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Created by tomasz on 13.03.17.
 */
@Repository
@Transactional
public class JpaUaasUserMappingRepository implements UaasUserMappingRepository {

    private EntityManager em;

    @Autowired
    public JpaUaasUserMappingRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<UaasUserMapping> getBySource(String source, String sourceId) {
        List<UaasUserMapping> resultList = em
                .createQuery("select o from " + UaasUserMapping.class.getCanonicalName() + " o where o.source=:source and o.sourceId=:sourceId", UaasUserMapping.class)
                .setParameter("source", source)
                .setParameter("sourceId", sourceId)
                .getResultList();

        if (resultList.isEmpty())
            return Optional.empty();

        return Optional.of(resultList.get(0));
    }

    @Override
    public void save(UaasUserMapping uaasUserMapping) {
        em.persist(uaasUserMapping);
    }
}
