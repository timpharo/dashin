package co.uk.pbnj.dashin.controller.v1;

import co.uk.pbnj.dashin.dto.DisplayConfigResponse;
import co.uk.pbnj.dashin.dto.DisplayItem;
import co.uk.pbnj.dashin.service.DisplayItemService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class DisplayConfigController {

    private final DisplayItemService displayItemService;

    public DisplayConfigController(DisplayItemService displayItemService) {
        this.displayItemService = displayItemService;
    }

    @GetMapping("/v1/display-config")
    public DisplayConfigResponse displayConfig() {
        return new DisplayConfigResponse(
                displayItemService.getDisplayItems()
        );
    }
}
