package com.liem.apigateway.config.auth;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Getter
public enum AuthRole {
    SUPER_ADMIN("admin", 1),
    CLIENT_ADMIN("client", 2),
    INVENTORY_KEEPER("inventory-keeper", 3),
    GUEST("guest", Integer.MAX_VALUE);

    private final String roleName;
    private final Integer rolePriority;

    AuthRole(String roleName, Integer rolePriority) {
        this.roleName = roleName;
        this.rolePriority = rolePriority;
    }

    public AuthRole getAuthRoleByName(final @NotNull String roleName) {
        return Arrays.stream(AuthRole.values())
                .filter(role -> role.getRoleName().equalsIgnoreCase(roleName.trim()))
                .findFirst().orElse(null);
    }
}
