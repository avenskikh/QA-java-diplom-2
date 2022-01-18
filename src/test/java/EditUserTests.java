import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.edit(data.get("email"), newPassword, data.get("name"), "");
        assertEquals("StatusCode is incorrect", 401, response.statusCode());
        assertEquals("Isn't unsuccessful", false, response.path("success"));
        assertEquals("Message is incorrect", "You should be authorised", response.path("message"));
    }

    @Test
    @DisplayName("Edit user username without authorization and check answer")
    public void editUserUsernameWithoutAuthorizationCheck() {
        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newUsername = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.edit(data.get("email"), data.get("password"), newUsername, "");
        assertEquals("StatusCode is incorrect", 401, response.statusCode());
        assertEquals("Isn't unsuccessful", false, response.path("success"));
        assertEquals("Message is incorrect", "You should be authorised", response.path("message"));
    }

    @Test
    @DisplayName("Edit user email without authorization and check answer")
    public void editUserEmailWithoutAuthorizationCheck() {
        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        Response response = userClient.edit(newEmail, data.get("password"), data.get("name"), "");
        assertEquals("StatusCode is incorrect", 401, response.statusCode());
        assertEquals("Isn't unsuccessful", false, response.path("success"));
        assertEquals("Message is incorrect", "You should be authorised", response.path("message"));
    }

    @Test
    @DisplayName("Edit user pass with authorization and check answer")
    public void editUserPassWithAuthorizationCheck() {
        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.edit(data.get("email"), newPassword, data.get("name"), data.get("accessToken"));
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
        assertEquals("Isn't successful", true, response.path("success"));
    }

    @Test
    @DisplayName("Edit user username with authorization and check answer")
    public void editUserUsernameWithAuthorizationCheck() {
        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newUsername = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.edit(data.get("email"), data.get("password"), newUsername, data.get("accessToken"));
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
        assertEquals("Isn't successful", true, response.path("success"));
    }

    @Test
    @DisplayName("Edit user email with authorization and check answer")
    public void editUserEmailWithAuthorizationCheck() {
        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        Response response = userClient.edit(newEmail, data.get("password"), data.get("name"), data.get("accessToken"));
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
        assertEquals("Isn't successful", true, response.path("success"));
    }

    @Test
    @DisplayName("Edit user already exists email with authorization and check answer")
    public void editUserAlreadyExistsEmailWithAuthorizationCheck() {
        Map<String, String> data = create();
        String newEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        userClient.create(newEmail, data.get("password"), data.get("name"));
        login(data.get("email"), data.get("password"));
        Response response = userClient.edit(newEmail, data.get("password"), data.get("name"), data.get("accessToken"));
        assertEquals("StatusCode is incorrect", 403, response.statusCode());
        assertEquals("Isn't unsuccessful", false, response.path("success"));
        assertEquals("Message is incorrect", "User with such email already exists", response.path("message"));
    }

    private Map<String, String> create(){
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.create(email, password, username);
        String accessToken = response.path("accessToken");
        Map<String, String> inputDataMap = new HashMap<>();
        inputDataMap.put("email", email);
        inputDataMap.put("password", password);
        inputDataMap.put("name", username);
        inputDataMap.put("accessToken", accessToken);
        return inputDataMap;
    }

    private void login(String email, String password){
        userClient.login(email, password);
    }
}
