package co.uk.pbnj.dashin.controller.v1;

import co.uk.pbnj.dashin.dto.Todo;
import co.uk.pbnj.dashin.dto.TodoListType;
import co.uk.pbnj.dashin.service.TodoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class TodoController {
    private static final String V_1_TODO_BY_TYPE = "/v1/todo/{type}";
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(V_1_TODO_BY_TYPE)
    public List<Todo> getTodos(@PathVariable("type") TodoListType type) {
        return todoService.getList(type);
    }

    public static String getV1TodoByTypePath(TodoListType type) {
        return V_1_TODO_BY_TYPE.replaceAll("\\{type}", type.name());
    }
}
