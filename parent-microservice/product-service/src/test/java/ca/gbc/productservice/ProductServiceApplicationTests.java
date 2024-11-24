package ca.gbc.productservice;

import io.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

//tells springboot to look for a main configuration class (@springbootapplication)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class ProductServiceApplicationTests {

	//this annotation is used in combination with test containers to automatically configure the
	// connection to the mongoDb container
	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

	@LocalServerPort
	private Integer port;


	@BeforeEach
	void setup(){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mongoDBContainer.start();
	}
	@Test
	void createProductsTest() {

		String requestBody = """
				{
				"name" : "samsungTv",
				"description" : "tv model 2024",
				"price" : "2000"
				}
				""";


		//BDD - 0 behavioural driven development (given,then,when)
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("api/product")
				.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("samsungTv"))
				.body("description", Matchers.equalTo("tv model 2024"))
				.body("price", Matchers.equalTo(2000));



	}

	@Test
	void getAllProductsTest(){

		String requestBody = """
				{
				"name" : "samsungTv",
				"description" : "tv model 2024",
				"price" : "2000"
				}
				""";


		//BDD - 0 behaviroural driven development (given,then,when)
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("api/product")
				.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("samsungTv"))
				.body("description", Matchers.equalTo("tv model 2024"))
				.body("price", Matchers.equalTo(2000));


		//restassured is used to perform http requests  and verify responses.
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("api/product")
				.then()
				.log().all()
				.statusCode(200)
				.body("size()", Matchers.greaterThan(0))
				.body("[0].name", Matchers.equalTo("samsungTv"))
				.body("[0].description", Matchers.equalTo("tv model 2024"))
				.body("[0].price", Matchers.equalTo(2000));
	}

}
