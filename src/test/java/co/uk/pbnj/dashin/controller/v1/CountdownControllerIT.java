package co.uk.pbnj.dashin.controller.v1;

import co.uk.pbnj.dashin.ActivateIntegrationProfile;
import co.uk.pbnj.dashin.dto.Countdown;
import co.uk.pbnj.dashin.service.CountdownService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActivateIntegrationProfile
class CountdownControllerIT {
    private static final String ITEM_1_NAME = "item1";
    private static final String PATH_FORMAT = "/v1/countdown/%s";

    @MockBean
    public CountdownService countdownService;

    @Autowired
    public CountdownController controller;

    private WebTestClient client;

    @BeforeEach
    public void setup() {
        client = WebTestClient.bindToController(controller).build();
    }

    @Test
    void countdownByName() {
        Optional<Countdown> expectedResultOpt = Optional.of(
                Countdown.builder()
                        .name("name1")
                        .description("name1 description")
                        .daysUntil(1).hoursUntil(2).minutesUntil(3).secondsUntil(4)
                        .build()
        );
        given(countdownService.getCountdownRepresentation(ITEM_1_NAME)).willReturn(expectedResultOpt);

        client.get()
                .uri(PATH_FORMAT.formatted(ITEM_1_NAME))
                .exchange()
                .expectAll(
                        spec -> spec.expectStatus().isOk(),
                        spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spec -> spec.expectBody(Countdown.class)
                                .isEqualTo(expectedResultOpt.get())
                );
    }

    @Test
    void notFound() {
        given(countdownService.getCountdownRepresentation(ITEM_1_NAME)).willReturn(Optional.empty());

        client.get()
                .uri(PATH_FORMAT.formatted(ITEM_1_NAME))
                .exchange()
                .expectStatus().isNotFound();
    }

}