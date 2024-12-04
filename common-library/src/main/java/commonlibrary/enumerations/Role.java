package commonlibrary.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    STUDENT("Student"), CAMPUS_EMPLOYEE("Campus Employee"), RESTAURANT_MANAGER("Restaurant Manager");
    final String name;

    public boolean canOrder() {
        return this == STUDENT || this == CAMPUS_EMPLOYEE;
    }

}
