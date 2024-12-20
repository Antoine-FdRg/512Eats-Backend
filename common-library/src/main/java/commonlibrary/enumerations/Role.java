package commonlibrary.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    STUDENT("STUDENT"), CAMPUS_EMPLOYEE("CAMPUS_EMPLOYEE"), RESTAURANT_MANAGER("RESTAURANT_MANAGER");
    final String name;

    public boolean canOrder() {
        return this == STUDENT || this == CAMPUS_EMPLOYEE;
    }

}
