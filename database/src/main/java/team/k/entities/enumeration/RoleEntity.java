package team.k.entities.enumeration;

import io.ebean.annotation.DbEnumType;
import io.ebean.annotation.DbEnumValue;

public enum RoleEntity {
    STUDENT("student"), CAMPUS_EMPLOYEE("employee"), RESTAURANT_MANAGER("manager");

    final String role;

    RoleEntity(String role) {
        this.role = role;
    }

    public boolean canOrder() {
        return this == STUDENT || this == CAMPUS_EMPLOYEE;
    }

    @DbEnumValue(storage = DbEnumType.VARCHAR)
    public String getValue() {
        return role;
    }
}
