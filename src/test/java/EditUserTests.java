import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EditUserTests {
    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Edit user pass without authorization and check answer")
    public void editUserPassWithoutAuthorizationCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        userClient.create(email, password, username);
        userClient.login(email, password);
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.edit(email, newPassword, username, "");
        assertEquals("StatusCode is incorrect", 401, response.statusCode());
        assertEquals("Isn't unsuccessful", false, response.path("success"));
        assertEquals("Message is incorrect", "You should be authorised", response.path("message"));
    }

    @Test
    @DisplayName("Edit user username without authorization and check answer")
    public void editUserUsernameWithoutAuthorizationCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        userClient.create(email, password, username);
        userClient.login(email, password);
        String newUsername = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.edit(email, password, newUsername, "");
        assertEquals("StatusCode is incorrect", 401, response.statusCode());
        assertEquals("Isn't unsuccessful", false, response.path("success"));
        assertEquals("Message is incorrect", "You should be authorised", response.path("message"));
    }

    @Test
    @DisplayName("Edit user email without authorization and check answer")
    public void editUserEmailWithoutAuthorizationCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        userClient.create(email, password, username);
        userClient.login(email, password);
        String newEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        Response response = userClient.edit(newEmail, password, username, "");
        assertEquals("StatusCode is incorrect", 401, response.statusCode());
        assertEquals("Isn't unsuccessful", false, response.path("success"));
        assertEquals("Message is incorrect", "You should be authorised", response.path("message"));
    }

    @Test
    @DisplayName("Edit user pass with authorization and check answer")
    public void editUserPassWithAuthorizationCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response createResponse = userClient.create(email, password, username);
        String accessToken = createResponse.path("accessToken");
        userClient.login(email, password);
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.edit(email, newPassword, username, accessToken);
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
        assertEquals("Isn't successful", true, response.path("success"));
    }

    @Test
    @DisplayName("Edit user username with authorization and check answer")
    public void editUserUsernameWithAuthorizationCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response createResponse = userClient.create(email, password, username);
        String accessToken = createResponse.path("accessToken");
        userClient.login(email, password);
        String newUsername = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.edit(email, password, newUsername, accessToken);
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
        assertEquals("Isn't successful", true, response.path("success"));
    }

    @Test
    @DisplayName("Edit user email with authorization and check answer")
    public void editUserEmailWithAuthorizationCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response createResponse = userClient.create(email, password, username);
        String accessToken = createResponse.path("accessToken");
        userClient.login(email, password);
        String newEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        Response response = userClient.edit(newEmail, password, username, accessToken);
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
        assertEquals("Isn't successful", true, response.path("success"));
    }

    @Test
    @DisplayName("Edit user already exists email with authorization and check answer")
    public void editUserAlreadyExistsEmailWithAuthorizationCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        String newEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        Response createResponse = userClient.create(email, password, username);
        userClient.create(newEmail, password, username);
        String accessToken = createResponse.path("accessToken");
        userClient.login(email, password);
        Response response = userClient.edit(newEmail, password, username, accessToken);
        assertEquals("StatusCode is incorrect", 403, response.statusCode());
        assertEquals("Isn't unsuccessful", false, response.path("success"));
        assertEquals("Message is incorrect", "User with such email already exists", response.path("message"));
    }
}
