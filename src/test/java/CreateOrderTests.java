import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateOrderTests {
    OrderClient orderClient;
    UserClient userClient;
    IngredientsClient ingredientsClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        ingredientsClient = new IngredientsClient();
    }

    @Test
    @DisplayName("Create order without ingredients and check answer")
    public void createOrderWithoutIngredientsCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response createResponse = userClient.create(email, password, username);
        String accessToken = createResponse.path("accessToken");
        userClient.login(email, password);
        Response response = orderClient.createOrder(null, accessToken);
        assertEquals("StatusCode is incorrect", 400, response.statusCode());
        assertEquals("Isn't successful", false, response.path("success"));
        assertEquals("Message is incorrect", "Ingredient ids must be provided", response.path("message"));
    }

    @Test
    @DisplayName("Create order with incorrect ingredient and check answer")
    public void createOrderWithIncorrectIngredientCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response createResponse = userClient.create(email, password, username);
        String accessToken = createResponse.path("accessToken");
        userClient.login(email, password);
        Response response = orderClient.createOrder(Arrays.asList("1213"), accessToken);
        assertEquals("StatusCode is incorrect", 500, response.statusCode());
    }

    @Test
    @DisplayName("Create order with authorization and check answer")
    public void createOrderWithAuthorizationCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response createResponse = userClient.create(email, password, username);
        String accessToken = createResponse.path("accessToken");
        userClient.login(email, password);
        Response responseIngredients = ingredientsClient.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        Response response = orderClient.createOrder(ingredients, accessToken);
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
        assertEquals("Isn't successful", true, response.path("success"));
    }

    @Test
    @DisplayName("Create order without authorization and check answer")
    public void createOrderWithoutAuthorizationCheck() {
        Response responseIngredients = ingredientsClient.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        Response response = orderClient.createOrder(ingredients, "");
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
        assertEquals("Isn't successful", true, response.path("success"));
    }
}