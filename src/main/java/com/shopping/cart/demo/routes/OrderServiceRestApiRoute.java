package com.shopping.cart.demo.routes;

import com.shopping.cart.demo.exception.OrderNotFoundException;
import com.shopping.cart.demo.model.*;
import org.apache.camel.Exchange;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

@Component
public class OrderServiceRestApiRoute extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceRestApiRoute.class);

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.context.path}")
    private String contextPath;

    @Override
    public void configure() {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .contextPath(contextPath)
                .port(serverPort)
                .enableCORS(true)
                .apiContextPath("/swagger") //swagger endpoint path
                .apiContextRouteId("swagger") //id of route providing the swagger endpoint
                .contextPath("/api") //base.path swagger property; use the mapping path set for CamelServlet
                .apiProperty("api.title", "Shopping cart rest API")
                .apiProperty("api.version", "1.0")
                .apiProperty("api.contact.url", "raju mattebam git");


        rest("/orders").produces("application/json").consumes("application/json")
                .get()
                //swagger
                .description("Get Orders")
                .responseMessage().code(200).responseModel(OrderInfoList.class).endResponseMessage() //OK
                //route
                .to("direct:orders-get")
                .outType(OrderInfoList.class)

                .get("/{id}/products")
                //swagger
                .description("Get Products")
                .responseMessage().code(200).endResponseMessage()//OK
                //route
                .to("direct:order-products-get")
                .outType(ProductList.class);

        rest("/orders").produces("application/json").consumes("application/json")
                .post()
                .type(OrderInfo.class)
                //swagger
                .description("Create Orders")
                .responseMessage().code(201).endResponseMessage()// Created
                //route
                .route()
                .to("direct:fe-validate-order-json")
                .to("direct:order-create");

        onException(ValidationException.class)
                .handled(true)
                .setHeader(HTTP_RESPONSE_CODE, constant(400))
                .log("occurredAt: ${date:now} ,\nmessage: ${exception.message},\nexception: ${exception.stacktrace} ")
                .setBody(simple("Validation Error occurred for message with id= ${id}"));

        onException(OrderNotFoundException.class)
                .handled(true)
                .setHeader(HTTP_RESPONSE_CODE, constant(404))
                .log("occurredAt: ${date:now} ,\nmessage: ${exception.message},\nexception: ${exception.stacktrace} ")
                .process(exchange -> {
                    Exception exception = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
                    logger.error(exception.getMessage());
                    exchange.getIn().setBody("Order can't be found");
                });

        onException(RuntimeException.class)
                .handled(true)
                .setHeader(HTTP_RESPONSE_CODE, constant(500))
                .log("occurredAt: ${date:now} ,\nmessage: ${exception.message},\nexception: ${exception.stacktrace} ")
                .setBody(simple("Internal Server Error occurred for message with id= ${id}"));

        from("direct:fe-validate-order-json")
                .log("validation of order input has started")
                .marshal().json(JsonLibrary.Jackson, true)
                .to("json-validator:fe-order-schema.json")
                .log("validation of order input is successful");


    }
}
