package be.tvw.customer.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public interface CustomerRepositoryTest {

    CustomerRepository getCustomerRepository();

    @Test
    @Order(0)
    @DisplayName("Should ensure that a repository is available for testing")
    default void repositoryExists() {
        assertThat(getCustomerRepository()).isNotNull();
    }

    @Test
    @Order(1)
    @DisplayName("When saving customer data an ID should be provided, when searching that ID that customers data should be returned")
    default void saveAndFindById() {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest("customer name", "customer@company.it", 21);
        CustomerData savedCustomer = getCustomerRepository().save(createCustomerRequest);

        Optional<? extends CustomerData> foundCustomer = getCustomerRepository().customerById(savedCustomer.id());

        assertThat(foundCustomer)
                .isPresent()
                .hasValueSatisfying(customer -> {
                    assertThat(customer.id()).isNotNull().isEqualTo(savedCustomer.id());
                    assertThat(customer.name()).isNotNull().isEqualTo(createCustomerRequest.name());
                    assertThat(customer.email()).isNotNull().isEqualTo(createCustomerRequest.email());
                    assertThat(customer.age()).isNotNull().isEqualTo(createCustomerRequest.age());
                });
    }

    @Test
    @DisplayName("When searching without filter, all customers should be returned")
    default void searchAllCustomers() {
        List<DefaultCustomer> customerList = List.of(
                new DefaultCustomer(getCustomerRepository().save(new CreateCustomerRequest("customer one", "one@company.it", 9))),
                new DefaultCustomer(getCustomerRepository().save(new CreateCustomerRequest("second customer", "two@second.com", null))),
                new DefaultCustomer(getCustomerRepository().save(new CreateCustomerRequest("customer three", "three@third.org", 81))),
                new DefaultCustomer(getCustomerRepository().save(new CreateCustomerRequest("last customer", "customer@company.it", 102))));

        List<? extends CustomerData> customersInRepository = getCustomerRepository().allCustomers();

        assertThat(customersInRepository).hasSize(4)
                .map(DefaultCustomer::new)
                .containsExactlyInAnyOrderElementsOf(customerList);
    }

    @Test
    @DisplayName("Searching by unknown ID should not be possible")
    default void findByUnknownId() {
        Optional<? extends CustomerData> foundCustomer = getCustomerRepository().customerById(123L);

        assertThat(foundCustomer)
                .isEmpty();
    }

    @Test
    @DisplayName("Saving a customer with an existing email should not be possible")
    default void savingCustomerWithDuplicateEmail() {
        CustomerData _existingCustomer = getCustomerRepository().save(new CreateCustomerRequest("customer name", "customer@company.it", 21));

        assertThatThrownBy(() -> getCustomerRepository().save(new CreateCustomerRequest("other name", "customer@company.it", null)))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("An existing customer can be updated")
    default void updateCustomer() {
        DefaultCustomer existingCustomer = new DefaultCustomer(getCustomerRepository().save(new CreateCustomerRequest("customer name", "customer@company.it", null)));
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest("be.customer@company.be", 36);
        DefaultCustomer updatedCustomer = new DefaultCustomer(getCustomerRepository().update(existingCustomer.id(), updateCustomerRequest));

        assertThat(updatedCustomer.id()).isNotNull().isEqualTo(existingCustomer.id());
        assertThat(updatedCustomer.name()).isNotNull().isEqualTo(existingCustomer.name());
        assertThat(updatedCustomer.email()).isNotNull().isEqualTo(existingCustomer.email());
        assertThat(updatedCustomer.age()).isNotNull().isEqualTo(updateCustomerRequest.age());

        assertThat(getCustomerRepository().customerById(updatedCustomer.id()))
                .isPresent()
                .hasValueSatisfying(customer -> {
                    assertThat(customer.id()).isNotNull().isEqualTo(updatedCustomer.id());
                    assertThat(customer.name()).isNotNull().isEqualTo(updatedCustomer.name());
                    assertThat(customer.email()).isNotNull().isEqualTo(updatedCustomer.email());
                    assertThat(customer.age()).isNotNull().isEqualTo(updatedCustomer.age());
                });
    }

    @Test
    @DisplayName("An email update has to be unique")
    default void canNotUpdateToEmailInUse() {
        DefaultCustomer customerA = new DefaultCustomer(getCustomerRepository().save(new CreateCustomerRequest("a customer", "a@company.it", null)));
        DefaultCustomer customerB = new DefaultCustomer(getCustomerRepository().save(new CreateCustomerRequest("customer be", "b@company.it", null)));
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest("a@company.it", null);

        assertThatThrownBy(() -> getCustomerRepository().update(customerB.id(), updateCustomerRequest))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Can not update unidentified customer")
    default void updateCustomerName() {
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest("be.customer@company.be", 36);

        assertThatThrownBy(() -> getCustomerRepository().update(123L, updateCustomerRequest))
                .isInstanceOf(Exception.class);
    }

    record DefaultCustomer(Long id, String name, String email, Integer age) implements CustomerData {
        private DefaultCustomer(CustomerData customerData) {
            this(customerData.id(), customerData.name(), customerData.email(), customerData.age());
        }
    }

}