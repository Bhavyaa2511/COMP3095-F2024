package ca.gbc.orderservice;

import ca.gbc.orderservice.stub.InventoryClientStub;
import io.restassured.RestAssured;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("order-service")
            .withUsername("admin")
            .withPassword("password");

    @LocalServerPort
    private Integer port;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void startWireMock() {
       wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8080));
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        postgresContainer.start();
    }

    /*@Test
    void placeOrderTest() {
        String requestBody = """
                {
                    "skuCode": "SKU123",
                    "price": 199.99,
                    "quantity": 1
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201);
    }*/

    @Test
    void shouldSubmitOrder(){
        String submitOrderJson = """
               {
               "skuCode": "SKU001",
               "price": 5000,
               "quantity": 10,
               
               }""";

        //week10
        //mock a call to inventory service
        InventoryClientStub.stubInventoryCall("SKU001",10);


        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(submitOrderJson)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().asString();

        assertThat(responseBodyString,Matchers.is("Order Placed Successfully") );

    }
}
