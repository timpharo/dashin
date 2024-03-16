package co.uk.pbnj.dashin.service;

import co.uk.pbnj.dashin.TodoConfig;
import co.uk.pbnj.dashin.dto.Todo;
import co.uk.pbnj.dashin.dto.TodoListType;
import co.uk.pbnj.dashin.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository repository;
    private final int shoppingProjectId;
    private final int personalProjectId;
    private final int workProjectId;
    private final int learnProjectId;

    private final int inboxProjectId;

    public TodoService(TodoRepository repository, TodoConfig todoConfig) {
        this.repository = repository;
        this.shoppingProjectId = todoConfig.shoppingListId();
        this.personalProjectId = todoConfig.personalListId();
        this.workProjectId = todoConfig.workListId();
        this.learnProjectId = todoConfig.learnListId();
        this.inboxProjectId = todoConfig.inboxListId();
    }

    public List<Todo> getList(TodoListType type) {
        return switch (type) {
            case SHOPPING -> tasksFor(shoppingProjectId);
            case PERSONAL -> tasksFor(personalProjectId);
            case WORK -> tasksFor(workProjectId);
            case LEARN -> tasksFor(learnProjectId);
            case INBOX -> tasksFor(inboxProjectId);
        };
    }

    private List<Todo> tasksFor(int listId) {
        return repository.getTasks(listId);
    }
}
