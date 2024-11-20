package commonlibrary.enumerations;

public enum Role {
    STUDENT, CAMPUS_EMPLOYEE, RESTAURANT_MANAGER;

    public boolean canOrder() {
        return this == STUDENT || this == CAMPUS_EMPLOYEE;
    }
}
