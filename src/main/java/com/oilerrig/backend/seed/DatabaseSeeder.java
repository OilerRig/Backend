package com.oilerrig.backend.seed;

import com.github.javafaker.Faker;
import com.oilerrig.backend.data.entity.*;
import com.oilerrig.backend.data.repository.*;
import com.oilerrig.backend.domain.Order;
import com.oilerrig.backend.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
//@Profile("!prod") TODO FOR NOW SEED PROD
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public DatabaseSeeder(UserRepository userRepository,
                          VendorRepository vendorRepository,
                          ProductRepository productRepository,
                          OrderRepository orderRepository,
                          OrderItemRepository orderItemRepository) {
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new Locale("en-CA"));

        // Clear existing data
        orderRepository.deleteAll();
        productRepository.deleteAll();
        vendorRepository.deleteAll();
        userRepository.deleteAll();

        // Seed Users
        List<UserEntity> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserEntity user = new UserEntity();
            user.setAuth0Id(faker.regexify("[a-z0-9]{32}"));
            user.setEmail(faker.internet().emailAddress());
            user.setName(faker.name().fullName());
            user.setCreatedAt(OffsetDateTime.now().minusDays(faker.number().numberBetween(1, 365)));
            user.setRole(
                    switch(i) {
                        case 0 -> User.UserRole.ADMIN;
                        case 1, 2 -> User.UserRole.GUEST;
                        default -> User.UserRole.CLIENT;
                    }
            );
            users.add(user);
        }
        userRepository.saveAll(users);

        // Seed Vendors
        List<VendorEntity> vendors = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            VendorEntity vendor = new VendorEntity();
            vendor.setName(faker.company().name());
            vendor.setBaseurl(faker.internet().url());
            vendors.add(vendor);
        }
        vendorRepository.saveAll(vendors);

        vendors = vendorRepository.findAll(); // refresh list of vendors
        // Seed Products
        List<ProductEntity> products = new ArrayList<>();
        for (VendorEntity vendor : vendors) {
            for (int i = 0; i < faker.number().numberBetween(5, 15); i++) {
                ProductEntity product = new ProductEntity();
                product.setVendor(vendor);
                product.setProductId(i); // Vendor's internal product ID
                product.setName(faker.commerce().productName());
                product.setPrice(faker.number().randomDouble(2, 5, 500));
                product.setStock(faker.number().numberBetween(0, 200));
                product.setLastUpdated(OffsetDateTime.now().minusHours(faker.number().numberBetween(1, 720)));
                products.add(product);
            }
        }
        productRepository.saveAll(products);

        // Seed Orders and OrderItems
        List<OrderEntity> orders = new ArrayList<>();
        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (UserEntity user : users) {
            int numberOfOrders = faker.number().numberBetween(0, 10);
            for (int i = 0; i < numberOfOrders; i++) {
                OrderEntity order = new OrderEntity();
                order.setUser(user);
                order.setCreatedAt(OffsetDateTime.now().minusDays(faker.number().numberBetween(1, 365)));
                order.setStatus(faker.options().option(Order.OrderStatus.class));
                orders.add(order);

                int numberOfItems = faker.number().numberBetween(1, 5);
                for (int j = 0; j < numberOfItems; j++) {
                    OrderItemEntity orderItem = new OrderItemEntity();
                    orderItem.setOrder(order);
                    // Ensure product exists and has stock
                    ProductEntity randomProduct = products.get(faker.number().numberBetween(0, products.size() - 1));
                    while(randomProduct.getStock() == 0){ // re-pick if out of stock
                        randomProduct = products.get(faker.number().numberBetween(0, products.size() - 1));
                    }
                    orderItem.setProduct(randomProduct);
                    orderItem.setQuantity(faker.number().numberBetween(1, Math.min(5, randomProduct.getStock())));
                    orderItems.add(orderItem);
                }
            }
        }
        orderRepository.saveAll(orders);
        orderItemRepository.saveAll(orderItems);


        // TODO SEED SOME SAGAS?

        System.out.println("Database seeded successfully with test data!");
    }
}