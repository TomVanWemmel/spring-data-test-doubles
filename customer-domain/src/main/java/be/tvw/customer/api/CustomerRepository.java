package be.tvw.customer.api;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Optional<? extends CustomerData> customerById(long id);

    List<? extends CustomerData> allCustomers();

    CustomerData save(CreateCustomerRequest createCustomerRequest);

    CustomerData update(Long customerId, UpdateCustomerRequest updateCustomerRequest);
}
