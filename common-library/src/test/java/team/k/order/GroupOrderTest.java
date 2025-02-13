package team.k.order;

import commonlibrary.model.order.GroupOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static commonlibrary.model.order.GroupOrder.GROUP_ORDER_CODE_LENGTH;
import static org.junit.jupiter.api.Assertions.assertEquals;

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