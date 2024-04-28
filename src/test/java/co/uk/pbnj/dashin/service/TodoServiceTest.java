package co.uk.pbnj.dashin.service;

import co.uk.pbnj.dashin.TodoConfig;
import co.uk.pbnj.dashin.dto.Todo;
import co.uk.pbnj.dashin.dto.TodoListType;
import co.uk.pbnj.dashin.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static co.uk.pbnj.dashin.dto.TodoListType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    private static final int SHOPPING_LIST_ID = 1111;
    private static final int PERSONAL_LIST_ID = 2222;
    private static final int WORK_LIST_ID = 3333;
    private static final int LEARN_LIST_ID = 4444;
    private static final int INBOX_LIST_ID = 5555;

    private Todo todo1;
    private Todo todo2;

    @Mock
    private TodoRepository repository;

    private TodoService subject;

    @BeforeEach
    void beforeEach() {
        TodoConfig todoConfig = TodoConfig.builder()
                .shoppingListId(SHOPPING_LIST_ID)
                .personalListId(PERSONAL_LIST_ID)
                .workListId(WORK_LIST_ID)
                .learnListId(LEARN_LIST_ID)
                .inboxListId(INBOX_LIST_ID)
                .build();
        subject = new TodoService(repository, todoConfig);
        todo1 = new Todo(1, "Task 1", "This is task 1");
        todo2 = new Todo(2, "Task 2", "This is task 2");
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void getListReturnsExpectedResults(TodoListType type, int listId) {
        given(repository.getTasks(listId)).willReturn(List.of(todo1, todo2));

        List<Todo> results = subject.getList(type);

        assertThat(results).containsExactly(todo1, todo2);
    }

    private static Stream<Arguments> arguments(){
        return Stream.of(
            Arguments.of(SHOPPING, SHOPPING_LIST_ID),
            Arguments.of(PERSONAL, PERSONAL_LIST_ID),
            Arguments.of(WORK, WORK_LIST_ID),
            Arguments.of(LEARN, LEARN_LIST_ID),
            Arguments.of(INBOX, INBOX_LIST_ID)
        );
    }
}