import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LoginUserTests {

    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Login user and check answer")
    public void loginUserCheck() {
        Map<String, String> data = create();
        Response response = userClient.login(data.get("email"), data.get("password"));
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
        assertEquals("Isn't successful", true, response.path("success"));
    }

    @Test
    @DisplayName("Login user with incorrect password and check answer")
    public void loginUserWithIncorrectPasswordCheck() {
        Map<String, String> data = create();
        Response response = userClient.login(data.get("email"), "test");
        assertEquals("StatusCode is incorrect", 401, response.statusCode());
        assertEquals("Isn't unsuccessful", false, response.path("success"));
        assertEquals("Message is incorrect", "email or password are incorrect", response.path("message"));
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
}