import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class GetOrdersTests {
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
    @DisplayName("Get order with authorization and check answer")
    public void getOrderWithAuthorizationCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response createResponse = userClient.create(email, password, username);
        String accessToken = createResponse.path("accessToken");
        userClient.login(email, password);
        Response responseIngredients = ingredientsClient.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        orderClient.createOrder(ingredients, accessToken);
        Response response = orderClient.getOrders(accessToken);
        assertEquals("StatusCode is incorrect", 200, response.statusCode());
        assertEquals("Isn't successful", true, response.path("success"));
        assertThat("Orders is null", response.path("orders"), notNullValue());
    }

    @Test
    @DisplayName("Get order without authorization and check answer")
    public void getOrderWithoutAuthorizationCheck() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response createResponse = userClient.create(email, password, username);
        String accessToken = createResponse.path("accessToken");
        userClient.login(email, password);
        Response responseIngredients = ingredientsClient.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        orderClient.createOrder(ingredients, accessToken);
        Response response = orderClient.getOrders("");
        assertEquals("StatusCode is incorrect", 401, response.statusCode());
        assertEquals("Isn't unsuccessful", false, response.path("success"));
        assertEquals("Message is incorrect", "You should be authorised", response.path("message"));
    }
}