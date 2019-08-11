package org.wb.auth.utils;

import org.wb.auth.entity.Permission;
import org.wb.auth.entity.Role;

import java.util.ArrayList;
import java.util.List;

public interface RoleUtils {


    public static  Role adminRole() {
        Role role = new Role("ROLE_ADMIN");
        List<Permission> permissions = new ArrayList<>();
        permissions.add(new Permission(org.wb.auth.constant.Permission.READ.name()));
        permissions.add(new Permission(org.wb.auth.constant.Permission.WRITE.name()));
        permissions.add(new Permission(org.wb.auth.constant.Permission.UPDATE.name()));
        permissions.add(new Permission(org.wb.auth.constant.Permission.DELETE.name()));
        role.setPermissions(permissions);
        return role;
    }

}
