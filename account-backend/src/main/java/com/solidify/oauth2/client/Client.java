package com.solidify.oauth2.client;

import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "clients")
@Access(AccessType.FIELD)
public class Client implements ClientDetails {

    @Id
    private String id;

    @Column(name = "client_secret")
    private String clientSecret;

    @ElementCollection()
    @CollectionTable(name = "auto_approve_scopes", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "scope")
    private Set<String> autoApproveScopes;

    @ElementCollection()
    @CollectionTable(name = "resource_ids", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "resource_id")
    private Set<String> resourceIds;

    @ElementCollection()
    @CollectionTable(name = "scope", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "scope")
    private Set<String> scope;

    @ElementCollection()
    @CollectionTable(name = "authorized_grant_types", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "grant_type")
    private Set<String> authorizedGrantTypes;

    @ElementCollection()
    @CollectionTable(name = "redirect_uris", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "redirect_uri")
    private Set<String> registeredRedirectUri;


    public String getClientId() {
        return id;
    }

    public Set<String> getResourceIds() {
        return resourceIds;
    }

    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public boolean isScoped() {
        return this.scope != null && !this.scope.isEmpty();
    }

    public Set<String> getScope() {
        return scope;
    }

    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    public Set<String> getAutoApproveScopes() {
        return autoApproveScopes;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>();
    }

    public Integer getAccessTokenValiditySeconds() {
        return null;
    }

    public Integer getRefreshTokenValiditySeconds() {
        return null;
    }

    public boolean isAutoApprove(String scope) {
        if (this.autoApproveScopes == null) {
            return false;
        } else {
            Iterator var2 = this.autoApproveScopes.iterator();

            String auto;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                auto = (String) var2.next();
            } while (!"true".equals(auto) && !scope.matches(auto));

            return true;
        }
    }

    public Map<String, Object> getAdditionalInformation() {
        return CollectionHelper.newHashMap();
    }
}