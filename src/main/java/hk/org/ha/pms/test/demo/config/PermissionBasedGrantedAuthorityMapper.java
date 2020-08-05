package hk.org.ha.pms.test.demo.config;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.keycloak.AuthorizationContext;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.idm.authorization.Permission;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class PermissionBasedGrantedAuthorityMapper implements GrantedAuthoritiesMapper {

    private static final Logger LOG = getLogger(PermissionBasedGrantedAuthorityMapper.class);

    @Autowired
    PermissionBasedGrantedAuthorityMapper() {
        // avoid instantiation
    }

    @Override
    public Collection<GrantedAuthority> mapAuthorities(
            final Collection<? extends GrantedAuthority> authorities) {

        final Collection<GrantedAuthority> allPermissions = new HashSet<>();
        mapRoles(allPermissions, authorities);
        mapPermissions(allPermissions);

        return allPermissions;
    }

    private void mapPermissions(final Collection<GrantedAuthority> allPermissions) {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();

        if (isNull(requestAttributes)) {
            return;
        }

        final HttpServletRequest request = requestAttributes.getRequest();
        final KeycloakPrincipal<?> userPrincipal = (KeycloakPrincipal<?>) request.getUserPrincipal();
        final AuthorizationContext authorizationContext = userPrincipal
                .getKeycloakSecurityContext()
                .getAuthorizationContext();

        checkState(!isNull(authorizationContext),
                "Keycloak authorization context is null. Is the policy enforcer active?");

        final List<Permission> permissions = authorizationContext.getPermissions();

        if (isNull(permissions)) {
            LOG.trace("No permissions found within authorization context");
            return;
        }

        LOG.info("!!!!@!!!!!permissions size: "+permissions.size());

        for (final Permission p : permissions) {
            p.getScopes().forEach(scope -> {
                final String resourceName = p.getResourceName();
                allPermissions.add(asPermission("p", resourceName, scope));
                LOG.info("!!!!!!!!resourceName:"+resourceName+",scope:"+scope);
            });
        }

        LOG.debug("==> Flat permissions (authorities): {}", allPermissions);
    }

    private void mapRoles(final Collection<GrantedAuthority> all,
                          final Collection<? extends GrantedAuthority> authorities) {
        if (isNull(authorities) || authorities.isEmpty()) {
            return;
        }

        authorities.forEach(r -> all.add(asPermission(r)));
    }

    private GrantedAuthority asPermission(final GrantedAuthority roleName) {
        return asPermission("r", roleName.getAuthority(), null);
    }

    private GrantedAuthority asPermission(final String prefix, final String resource,
                                          final String scope) {
        if (isNull(scope)) {
            return new SimpleGrantedAuthority(format("%s:%s", prefix, resource).toLowerCase());
        }

        return new SimpleGrantedAuthority(format("%s:%s:%s", prefix, resource, scope).toLowerCase());
    }
}


