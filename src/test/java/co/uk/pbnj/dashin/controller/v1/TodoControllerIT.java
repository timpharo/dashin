package co.uk.pbnj.dashin.controller.v1;

import co.uk.pbnj.dashin.dto.Todo;
import co.uk.pbnj.dashin.dto.TodoBuilder;
import co.uk.pbnj.dashin.dto.TodoListType;
import co.uk.pbnj.dashin.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class TodoControllerIT {

    @MockBean
    public TodoService todoService;

    @Autowired
    public TodoController controller;

    private WebTestClient client;

    @BeforeEach
    public void setup() {
        client = WebTestClient.bindToController(controller).build();
    }

    @ParameterizedTest
    @EnumSource(TodoListType.class)
    void getTodos(TodoListType type) {
        Todo todo1 = TodoBuilder.builder().order(1).content("Task 1").description("Task 1 desc").build();
        Todo todo2 = TodoBuilder.builder().order(2).content("Task 2").description("Task 2 desc").build();
        given(todoService.getList(type)).willReturn(List.of(todo1, todo2));

        client.get()
                .uri("/v1/todo/%s".formatted(type.name()))
                .exchange()
                .expectAll(
                        spec -> spec.expectStatus().isOk(),
                        spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spec -> spec.expectBodyList(Todo.class)
                                    .hasSize(2)
                                    .contains(todo1, todo2)
                );
    }

}