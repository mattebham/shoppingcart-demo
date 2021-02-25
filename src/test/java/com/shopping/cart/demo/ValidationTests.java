package com.shopping.cart.demo;

import com.shopping.cart.demo.model.OrderInfo;
import com.shopping.cart.demo.model.OrderLine;
import com.shopping.cart.demo.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@ContextConfiguration(classes = ShoppingCartDemoApplication.class, loader = SpringBootContextLoader.class)
public class ValidationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldFailValidationWhenCreatingOrUpdatingOrder() {
        // Given
        OrderInfo orderData = new OrderInfo("1",
                newArrayList(new OrderLine(new Product(null, "product1"), 2)));
        // Product id can't be null, validation error!!

        // When
        ResponseEntity<String> actual = restTemplate.postForEntity("/api/orders", orderData, String.class);

        // Then
        assertThat(actual.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertTrue(actual.getBody().contains("Validation Error occurred for message"));
    }
}
