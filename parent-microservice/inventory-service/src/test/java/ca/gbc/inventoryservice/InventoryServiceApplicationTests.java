package ca.gbc.inventoryservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.hamcrest.Matchers.equalTo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("inventory-service")
            .withUsername("admin")
            .withPassword("password");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    
        // Insert test data into the correct table (t_inventory)
        String insertSQL = "INSERT INTO t_inventory (sku_code, quantity) VALUES ('SKU_001', 10)";
    
        try (Connection conn = DriverManager.getConnection(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
             Statement stmt = conn.createStatement()) {
            // Clean up any existing data for SKU_001
            stmt.executeUpdate("DELETE FROM t_inventory WHERE sku_code = 'SKU_001'");
            // Insert the new test data
            stmt.executeUpdate(insertSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    


    static {
        postgresContainer.start();
    }


    @Test
    void isInStockTest() {
        // Assuming the 'isInStock' method is mapped to a GET request with query params
        String skuCode = "SKU_001";
        int quantity = 5;

        // Send GET request to check if the product is in stock
        RestAssured.given()
                .contentType("application/json")
                .param("skuCode", skuCode)
                .param("quantity", quantity)
                .when()
                .get("/api/inventory")
                .then()
                .log().all()
                .statusCode(200)  // Check if the response status is OK
                .body(equalTo("true"));  // Assuming the response has a field 'inStock' indicating availability
    }
}
