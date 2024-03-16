package co.uk.pbnj.dashin.repository;

import co.uk.pbnj.dashin.TodoConfig;
import co.uk.pbnj.dashin.TodoConfigBuilder;
import co.uk.pbnj.dashin.dto.Project;
import co.uk.pbnj.dashin.dto.Todo;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

class TodoRepositoryTest {
    private static final String AUTH_TOKEN = "AUTH_TOKEN1";
    private static final String TASKS_PATH = "/tasks";
    private static final String PROJECTS_PATH = "/projects";
    private static final int PROJECT_ID = 12345;
    private static MockWebServer mockBackEnd;

    private TodoRepository subject;

    @BeforeEach
    void beforeEach() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        String baseUrl = "http://localhost:%s".formatted(mockBackEnd.getPort());

        TodoConfig todoConfig = TodoConfigBuilder.builder()
                .url(baseUrl)
                .authToken(AUTH_TOKEN)
                .tasksPath(TASKS_PATH)
                .projectsPath(PROJECTS_PATH)
                .build();
        subject = new TodoRepository(todoConfig);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void correctlyGetsTasksForProjectId() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
                .setBody("""
                        [
                            {
                                "order": 1,
                                "content": "Task 1",
                                "description": "This is task 1"
                            },
                            {
                                "order": 2,
                                "content": "Task 2",
                                "description": "This is task 2"
                            }
                        ]
                        """)
                .addHeader("Content-Type", "application/json"));

        List<Todo> results = subject.getTasks(PROJECT_ID);

        assertThat(results).containsExactly(
                new Todo(1, "Task 1", "This is task 1"),
                new Todo(2, "Task 2", "This is task 2")
        );
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertThat(recordedRequest.getHeader(AUTHORIZATION)).isEqualTo("Bearer %S".formatted(AUTH_TOKEN));
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        HttpUrl requestUrl = recordedRequest.getRequestUrl();
        assertThat(requestUrl).isNotNull();
        assertThat(requestUrl.encodedPath()).isEqualTo(TASKS_PATH);
        assertThat(requestUrl.querySize()).isEqualTo(1);
        assertThat(requestUrl.queryParameter("project_id")).isEqualTo(
                String.valueOf(PROJECT_ID)
        );
    }

    @Test
    void correctlyGetsAllProjects() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
                .setBody("""
                        [
                            {
                                "id": "1111",
                                "name": "Name 1",
                                "comment_count": 1
                            },
                            {
                                "id": "2222",
                                "name": "Name 2",
                                "comment_count": 2
                            }
                        ]
                        """)
                .addHeader("Content-Type", "application/json"));

        List<Project> results = subject.getLists();

        assertThat(results).containsExactly(
                new Project("1111", "Name 1", 1),
                new Project("2222", "Name 2", 2)
        );
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertThat(recordedRequest.getHeader(AUTHORIZATION)).isEqualTo("Bearer %S".formatted(AUTH_TOKEN));
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        assertThat(recordedRequest.getPath()).isEqualTo(PROJECTS_PATH);
    }

}