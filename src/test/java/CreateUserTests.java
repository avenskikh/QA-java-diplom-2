import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateUserTests {
    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Create user and check answer")
    public void createUserCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.create(email, password, username);
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
    }

    @Test
    @DisplayName("Create user without required field and check answer")
    public void createUserWithoutRequiredFieldCheck() {
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.create("",password, username);
        assertEquals("StatusCode is incorrect", 403, response.statusCode());
        assertEquals("Message is incorrect", "Email, password and name are required fields", response.path("message"));
    }

    @Test
    @DisplayName("Create already exists user and check answer")
    public void createAlreadyExistsUserCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        userClient.create(email, password, username);
        Response response = userClient.create(email, password, username);
        assertEquals("StatusCode is incorrect", 403, response.statusCode());
        assertEquals("Message is incorrect", "User already exists", response.path("message"));
    }
}
