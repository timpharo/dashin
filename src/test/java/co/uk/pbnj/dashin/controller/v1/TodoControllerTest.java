package co.uk.pbnj.dashin.controller.v1;

import co.uk.pbnj.dashin.dto.TodoListType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class TodoControllerTest {

    @ParameterizedTest
    @EnumSource(value = TodoListType.class)
    void returnsCorrectTodoPath(TodoListType type) {
        String expectedResult = "/v1/todo/%s".formatted(type);

        String result = TodoController.getV1TodoByTypePath(type);

        assertThat(result).isEqualTo(expectedResult);
    }

}