package team.k.order;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import team.k.common.model.order.GroupOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static team.k.common.model.order.GroupOrder.GROUP_ORDER_CODE_LENGTH;

class GroupOrderTest {

    GroupOrder.Builder builder;

    @ParameterizedTest
    @ValueSource(ints = {7, 23, 512, -3, -953, 0})
    void builderGenerateId(int seed){
        int code = GroupOrder.Builder.generateId(seed);
        int codeLength = String.valueOf(code).length();
        assertEquals(GROUP_ORDER_CODE_LENGTH, codeLength);
    }
}