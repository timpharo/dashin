package co.uk.pbnj.dashin.controller.v1;

import co.uk.pbnj.dashin.dto.DisplayConfigResponse;
import co.uk.pbnj.dashin.dto.DisplayItem;
import co.uk.pbnj.dashin.service.DisplayItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Nested
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class DisplayConfigurationControllerIT {

    @MockBean
    private DisplayItemService displayItemService;

    @Autowired
    private DisplayConfigController controller;

    private WebTestClient client;

    @BeforeEach
    public void setup() {
        client = WebTestClient.bindToController(controller).build();
    }

    @Test
    void returnsExpectedDisplayItems() {
        List<DisplayItem> expectedDisplayItems = List.of(
                DisplayItem.builder()
                        .name("item1")
                        .type("type1")
                        .location("/item1/location")
                        .build()
        );
        given(displayItemService.getDisplayItems()).willReturn(expectedDisplayItems);

        client.get()
                .uri("/v1/display-config")
                .exchange()
                .expectAll(
                        spec -> spec.expectStatus().isOk(),
                        spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spec -> spec.expectBody(DisplayConfigResponse.class)
                                .consumeWith( it ->
                                        {
                                            DisplayConfigResponse body = it.getResponseBody();
                                            assertThat(body).isNotNull();
                                            assertThat(body.displayItems()).containsExactlyInAnyOrderElementsOf(expectedDisplayItems);
                                        }
                                )
                );
    }

}