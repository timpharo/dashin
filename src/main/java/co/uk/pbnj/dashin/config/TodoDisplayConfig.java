package co.uk.pbnj.dashin.config;

import co.uk.pbnj.dashin.dto.DisplayConfig;
import co.uk.pbnj.dashin.dto.TodoListType;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Optional;

@ConfigurationProperties(prefix = "todo")
public class TodoDisplayConfig {
    private final Map<TodoListType, DisplayConfig> displayConfig;

    public TodoDisplayConfig(Map<TodoListType, DisplayConfig> displayConfig) {
        this.displayConfig = displayConfig;
    }

    public DisplayConfig getDisplayConfig(TodoListType type) {
        return Optional.ofNullable(displayConfig)
                .map(it -> it.get(type))
                .orElse(null);
    }
}
