package co.uk.pbnj.dashin.controller.v1;

import co.uk.pbnj.dashin.dto.Countdown;
import co.uk.pbnj.dashin.service.CountdownService;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@CrossOrigin
@RestController
public class CountdownController {
    private static final String V_1_COUNTDOWN_BY_NAME = "/v1/countdown/{name}";

    private final CountdownService countdownService;

    public CountdownController(CountdownService countdownService) {
        this.countdownService = countdownService;
    }

    @GetMapping(V_1_COUNTDOWN_BY_NAME)
    public Countdown getCountdown(@PathVariable("name") String name) {
        Optional<Countdown> opt = countdownService.getCountdownRepresentation(name);

        if(opt.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404));
        }
        return opt.get();
    }

    public static String getV1CountdownByNamePath(String name) {
        return V_1_COUNTDOWN_BY_NAME.replaceAll("\\{name}", name);
    }
}
