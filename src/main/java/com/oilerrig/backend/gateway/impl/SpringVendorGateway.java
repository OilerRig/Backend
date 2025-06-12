package com.oilerrig.backend.gateway.impl;

import com.oilerrig.backend.exception.VendorApiException;
import com.oilerrig.backend.gateway.VendorGateway;
import com.oilerrig.backend.gateway.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;

public class SpringVendorGateway implements VendorGateway {

    private static final Logger log = LoggerFactory.getLogger(SpringVendorGateway.class);

    private final WebClient webClient;
    private final String apiKey;

    private final String NAME = "springVendorGateway";

    public SpringVendorGateway(WebClient.Builder webClientBuilder,
                               String baseUrl, String apiKey) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        log.info("SpringVendorGateway initialized with base URL: {}", baseUrl);
    }

    @CircuitBreaker(name = NAME, fallbackMethod = "getProductFallback")
    @RateLimiter(name = NAME)
    @Override
    public Optional<VendorProductDto> getProduct(Integer vendorId, Integer vendorProductId) {
        log.debug("Attempting to get product with ID: {} from SpringVendor (vendorId: {})", vendorProductId, vendorId);
        try {
            VendorProductDto productResponse = webClient.get()
                    .uri("/products/{id}", vendorProductId)
                    .header("X-API-KEY", apiKey)
                    .retrieve()
                    .bodyToMono(VendorProductDto.class)
                    .block(); // Blocking call

            log.info("Successfully retrieved basic product: {}", productResponse != null ? productResponse.getId() : "null");
            return Optional.ofNullable(productResponse);
        } catch (WebClientResponseException e) {
            log.error("WebClient error getting product {}: Status {} - Body: {}", vendorProductId, e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // For 404 Not Found, return an empty Optional as per method signature
                return Optional.empty();
            }
            // For other HTTP errors, re-throw as VendorApiException
            throw new VendorApiException("Failed to retrieve product: " + e.getMessage(), e.getStatusCode().value(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Unexpected error getting product {}: {}", vendorProductId, e.getMessage(), e);
            // Catch any other unexpected exceptions and wrap them
            throw new VendorApiException("Unexpected error getting product", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e);
        }
    }

    @CircuitBreaker(name = NAME, fallbackMethod = "getProductFallback")
    @RateLimiter(name = NAME)
    @Override
    public Optional<VendorProductWithDetailsDto> getProductDetails(Integer vendorId, Integer vendorProductId) {
        log.debug("Attempting to get product details with ID: {} from SpringVendor (vendorId: {})", vendorProductId, vendorId);
        try {
            VendorProductWithDetailsDto productDetailResponse = webClient.get()
                    .uri("/products/{id}/details", vendorProductId)
                    .header("X-API-KEY", apiKey)
                    .retrieve()
                    .bodyToMono(VendorProductWithDetailsDto.class)
                    .block(); // Blocking call

            log.info("Successfully retrieved detailed product: {}", productDetailResponse != null ? productDetailResponse: "null");
            return Optional.ofNullable(productDetailResponse);
        } catch (WebClientResponseException e) {
            log.error("WebClient error getting product details {}: Status {} - Body: {}", vendorProductId, e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // For 404 Not Found, return an empty Optional
                return Optional.empty();
            }
            // For other HTTP errors, re-throw as VendorApiException
            throw new VendorApiException("Failed to retrieve product details: " + e.getMessage(), e.getStatusCode().value(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Unexpected error getting product details {}: {}", vendorProductId, e.getMessage(), e);
            // Catch any other unexpected exceptions and wrap them
            throw new VendorApiException("Unexpected error getting product details", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e);
        }
    }

    @CircuitBreaker(name = NAME, fallbackMethod = "getAllProductsFallback")
    @RateLimiter(name = NAME)
    @Override
    public List<VendorProductDto> getAllProducts(Integer vendorId) {
        log.debug("Attempting to get all products from SpringVendor (vendorId: {})", vendorId);
        try {
            List<VendorProductDto> allProducts = webClient.get()
                    .uri("/products")
                    .header("X-API-KEY", apiKey)
                    .retrieve()
                    .bodyToFlux(VendorProductDto.class) // Use bodyToFlux to expect a list of items
                    .collectList()                     // Collect all items into a List
                    .block();                          // Block to get the result

            if (allProducts == null) {
                log.warn("Received null list when getting all products from vendor.");
                return List.of(); // Return empty list instead of null
            }

            log.info("Successfully retrieved {} products from SpringVendor.", allProducts.size());
            return allProducts;
        } catch (WebClientResponseException e) {
            log.error("WebClient error getting all products: Status {} - Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new VendorApiException("Failed to retrieve all products: " + e.getMessage(), e.getStatusCode().value(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Unexpected error getting all products: {}", e.getMessage(), e);
            throw new VendorApiException("Unexpected error getting all products", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e);
        }
    }


    @CircuitBreaker(name = NAME, fallbackMethod = "placeOrderFallback")
    @RateLimiter(name = NAME)
    @Override
    public VendorOrderDto placeOrder(Integer vendorId, VendorPlaceOrderDto command) throws VendorApiException {
        log.debug("Attempting to place order for product: {} quantity: {} with SpringVendor (vendorId: {})", command.getProductId(), command.getQuantity(), vendorId);
        try {
            VendorOrderDto orderResponse = webClient.post()
                    .uri("/orders")
                    .header("X-API-KEY", apiKey)
                    .bodyValue(command)
                    .retrieve()
                    .bodyToMono(VendorOrderDto.class)
                    .block(); // Blocking call

            if (orderResponse == null) {
                throw new VendorApiException("Received null response when placing order", HttpStatus.INTERNAL_SERVER_ERROR.value(), "No response body");
            }
            log.info("Successfully placed order with vendor, vendorOrderId: {}", orderResponse.getId());
            return orderResponse;
        } catch (WebClientResponseException e) {
            log.error("WebClient error placing order for product {}: Status {} - Body: {}", command.getProductId(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new VendorApiException("WebClient error placing order: " + e.getMessage(), e.getStatusCode().value(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Unexpected error placing order for product {}: {}", command.getProductId(), e.getMessage(), e);
            throw new VendorApiException("Unexpected error placing order", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e);
        }
    }

    @CircuitBreaker(name = NAME, fallbackMethod = "cancelOrderFallback")
    @RateLimiter(name = NAME)
    @Override
    public VendorOrderDto cancelOrder(Integer vendorId, VendorCancelOrderDto command) throws VendorApiException {
        log.debug("Attempting to cancel order with ID: {} from SpringVendor (vendorId: {})", command.getOrderId(), vendorId);
        try {
            VendorOrderDto cancelResponse = webClient.delete()
                    .uri("/orders/{id}", command.getOrderId())
                    .header("X-API-KEY", apiKey)
                    .retrieve()
                    .bodyToMono(VendorOrderDto.class)
                    .block(); // Blocking call

            if (cancelResponse == null) {
                throw new VendorApiException("Received null response when cancelling order", HttpStatus.INTERNAL_SERVER_ERROR.value(), "No response body");
            }
            log.info("Successfully received cancel confirmation for order: {}", cancelResponse.getId());
            return cancelResponse;
        } catch (WebClientResponseException e) {
            log.error("WebClient error cancelling order {}: Status {} - Body: {}", command.getOrderId(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new VendorApiException("WebClient error cancelling order: " + e.getMessage(), e.getStatusCode().value(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Unexpected error cancelling order {}: {}", command.getOrderId(), e.getMessage(), e);
            throw new VendorApiException("Unexpected error cancelling order", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e);
        }
    }

    // fallbacks
    private VendorProductDto getProductsFallback(Integer vendorId, Integer productId, Throwable t) throws VendorApiException {
        log.warn("SpringVendorGateway: Fallback triggered for getProduct(Details) with vendor {}. Product: {}. Cause: {}",
                vendorId, productId, t.getMessage());

        // re throw based on original exception
        throw new VendorApiException("Vendor getting product failed due to " + t.getClass().getSimpleName() + ": " + t.getMessage(),
                t instanceof WebClientResponseException ? ((WebClientResponseException) t).getStatusCode().value() : 503,
                t.getMessage(), t);
    }

    private List<VendorProductDto> getAllProductsFallback(Integer vendorId, Throwable t) throws VendorApiException {
        log.warn("SpringVendorGateway: Fallback triggered for getAllProducts with vendor {}. Cause: {}",
                vendorId, t.getMessage());

        // re throw based on original exception
        throw new VendorApiException("Vendor getting all products failed due to " + t.getClass().getSimpleName() + ": " + t.getMessage(),
                t instanceof WebClientResponseException ? ((WebClientResponseException) t).getStatusCode().value() : 503,
                t.getMessage(), t);
    }

    private VendorOrderDto placeOrderFallback(int vendorId, VendorPlaceOrderDto command, Throwable t) throws VendorApiException {
        log.warn("SpringVendorGateway: Fallback triggered for placeOrder with vendor {}. Product: {}. Cause: {}",
                vendorId, command.getProductId(), t.getMessage());

        // re throw based on original exception
        throw new VendorApiException("Vendor order placement failed due to " + t.getClass().getSimpleName() + ": " + t.getMessage(),
                t instanceof WebClientResponseException ? ((WebClientResponseException) t).getStatusCode().value() : 503,
                t.getMessage(), t);
    }

    private VendorOrderDto cancelOrderFallback(int vendorId, VendorCancelOrderDto command, Throwable t) throws VendorApiException {
        log.warn("SpringVendorGateway: Fallback triggered for cancelOrder with vendor {}. Order ID: {}. Cause: {}",
                vendorId, command.getOrderId(), t.getMessage());
        // rethrow compensation failure
        throw new VendorApiException("Vendor order cancellation failed due to " + t.getClass().getSimpleName() + ": " + t.getMessage(),
                t instanceof WebClientResponseException ? ((WebClientResponseException) t).getStatusCode().value() : 503,
                t.getMessage(), t);
    }
}
