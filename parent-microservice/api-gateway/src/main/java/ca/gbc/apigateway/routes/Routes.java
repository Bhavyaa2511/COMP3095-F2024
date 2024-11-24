package ca.gbc.apigateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;


@Configuration
@Slf4j
public class Routes {

    @Value("${services.product-url}")
    private String productServiceUrl;


    @Value("${services.order-url}")
    private String orderServiceUrl;

    @Value("${services.inventory-url}")
    private String  inventoryServiceUrl;


    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {

        log.info("initializing product service route with url: {}", productServiceUrl);

        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("/api/product"), request -> {
                    log.info("received request for product-service: {}", request.uri());

                    try {
                        ServerResponse response = HandlerFunctions.http(productServiceUrl).handle(request);
                        log.info("Response Status: {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("error occurred while routing to {}", productServiceUrl, e);
                        return ServerResponse.status(500).body("error occurred while routing to product-service");
                    }
                })
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {

        log.info("initializing  order-service route with url: {}", orderServiceUrl);

        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("/api/order"), request -> {
                    log.info("received request for order-service: {}", request.uri());

                    try {
                        ServerResponse response = HandlerFunctions.http(orderServiceUrl).handle(request);
                        log.info("Response Status: {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("error occurred while routing to {}", orderServiceUrl, e);
                        return ServerResponse.status(500).body("error occurred while routing to order-service");
                    }
                })
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute() {

        log.info("initializing inventory service route with url: {}", inventoryServiceUrl);

        return GatewayRouterFunctions.route("inventory_service")
                .route(RequestPredicates.path("/api/inventory"), request -> {
                    log.info("received request for inventory-service: {}", request.uri());

                    try {
                        ServerResponse response = HandlerFunctions.http(inventoryServiceUrl).handle(request);
                        log.info("Response Status: {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("error occurred while routing to {}", inventoryServiceUrl, e);
                        return ServerResponse.status(500).body("error occurred while routing to inventory-service");
                    }
                })
                .build();

    }


    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
                        HandlerFunctions.http(productServiceUrl))
                .filter(setPath("/api-docs"))
                .build();

    }


    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                        HandlerFunctions.http(orderServiceUrl))
                .filter(setPath("/api-docs"))
                .build();

    }


    @Bean
    public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
                        HandlerFunctions.http(inventoryServiceUrl))
                .filter(setPath("/api-docs"))
                .build();

    }
}
