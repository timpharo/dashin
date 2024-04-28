package co.uk.pbnj.dashin.service;

import co.uk.pbnj.dashin.config.TodoDisplayConfig;
import co.uk.pbnj.dashin.dto.*;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.uk.pbnj.dashin.controller.v1.CountdownController.getV1CountdownByNamePath;
import static co.uk.pbnj.dashin.controller.v1.StockController.getV1StockEquityPath;
import static co.uk.pbnj.dashin.controller.v1.TodoController.getV1TodoByTypePath;
import static java.lang.String.format;

@Service
public class DisplayItemService {

    private final Clock clock;
    private final TodoDisplayConfig todoDisplayConfig;
    private final CountdownService countdownService;

    public DisplayItemService(Clock clock, TodoDisplayConfig todoDisplayConfig, CountdownService countdownService) {
        this.clock = clock;
        this.todoDisplayConfig = todoDisplayConfig;
        this.countdownService = countdownService;
    }

    public List<DisplayItem> getDisplayItems(){
        List<DisplayItem> displayItems = new ArrayList<>();
        displayItems.add(displayItem("STOCK EQUITY", StockEquityCalculation.class, getV1StockEquityPath()));

        List<DisplayItem> countdownItems = countdownService.getCountdownItems()
                .stream()
                .map(name -> displayItem(name, Countdown.class, getV1CountdownByNamePath(name)))
                .toList();

        displayItems.addAll(countdownItems);

        for (TodoListType type : TodoListType.values()) {
            Optional<DisplayConfig> displayConfigOpt = Optional.ofNullable(todoDisplayConfig.getDisplayConfig(type));

            if(displayConfigOpt.isEmpty()){
                displayItems.add(displayItem(type.name(), Todo.class, todoList(type)));
                continue;
            }

            boolean shouldDisplay = displayConfigOpt.get().shouldDisplay(LocalDateTime.now(clock));

            if(shouldDisplay){
                displayItems.add(displayItem(type.name(), Todo.class, todoList(type)));
            }
        }

        return displayItems;
    }



    private static String todoList(TodoListType type) {
        return format(getV1TodoByTypePath(type));
    }

    private static DisplayItem displayItem(String name, Class type, String location) {
        return new DisplayItem(
                name,
                type.getSimpleName(),
                location
        );
    }
}
