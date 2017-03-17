package com.solidify.oauth2.integration.uaas;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by tomasz on 13.03.17.
 */
@AllArgsConstructor
@Getter
@Entity
@Table(name = "uaas_identity_mapping")
public class UaasUserMapping implements Serializable {

    @Id
    @Column(name = "source")
    private String source;

    @Id
    @Column(name = "source_id")
    private String sourceId;

    @Id
    @Column(name = "uaas_id")
    private String uaasId;

    protected UaasUserMapping() {
    }


}
