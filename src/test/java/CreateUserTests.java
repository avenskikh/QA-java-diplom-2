import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
        Map<String,String> data = cred();
        Response response = userClient.create(data.get("email"), data.get("password"), data.get("username"));
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
    }

    @Test
    @DisplayName("Create user without required field and check answer")
    public void createUserWithoutRequiredFieldCheck() {
        Map<String,String> data = cred();
        Response response = userClient.create("", data.get("password"), data.get("username"));
        assertEquals("StatusCode is incorrect", 403, response.statusCode());
        assertEquals("Message is incorrect", "Email, password and name are required fields", response.path("message"));
    }

    @Test
    @DisplayName("Create already exists user and check answer")
    public void createAlreadyExistsUserCheck() {
        Map<String,String> data = cred();
        userClient.create(data.get("email"), data.get("password"), data.get("username"));
        Response response = userClient.create(data.get("email"), data.get("password"), data.get("username"));
        assertEquals("StatusCode is incorrect", 403, response.statusCode());
        assertEquals("Message is incorrect", "User already exists", response.path("message"));
    }

    private Map<String,String> cred(){
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Map<String, String> inputDataMap = new HashMap<>();
        inputDataMap.put("email", email);
        inputDataMap.put("password", password);
        inputDataMap.put("username", username);
        return inputDataMap;
    }
}