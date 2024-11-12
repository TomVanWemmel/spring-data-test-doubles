package be.tvw.customer.fakes;

import be.tvw.customer.api.CreateCustomerRequest;
import be.tvw.customer.api.CustomerData;
import be.tvw.customer.api.CustomerRepository;
import be.tvw.customer.api.UpdateCustomerRequest;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactory;

import java.util.List;
import java.util.Optional;

public class FakeCustomerRepository extends FakeRepository<FakeCustomerRepository.SpringKeyValueCustomerRepository> implements CustomerRepository {

    @Override
    protected SpringKeyValueCustomerRepository getRepository(KeyValueRepositoryFactory factory) {
        return factory.getRepository(SpringKeyValueCustomerRepository.class);
    }

    @Override
    public Optional<? extends CustomerData> customerById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<? extends CustomerData> allCustomers() {
        return List.copyOf(repository.findAll());
    }

    @Override
    public CustomerData save(CreateCustomerRequest createCustomerRequest) {
        if (repository.existsByEmail(createCustomerRequest.email())) {
            throw new IllegalArgumentException("Email %s already exists".formatted(createCustomerRequest.email()));
        }
        return repository.save(new FakeCustomer(createCustomerRequest));
    }

    @Override
    public CustomerData update(Long customerId, UpdateCustomerRequest updateCustomerRequest) {
        if (repository.findByEmail(updateCustomerRequest.email()).filter(found -> found.id() != customerId).isPresent()) {
            throw new IllegalArgumentException("Email %s already exists".formatted(updateCustomerRequest.email()));
        }
        return repository.findById(customerId)
                .map(foundEntity -> foundEntity.update(updateCustomerRequest))
                .map(repository::save)
                .orElseThrow(() -> new IllegalArgumentException("Could not find customer with id %d".formatted(customerId)));
    }

    public interface SpringKeyValueCustomerRepository extends KeyValueRepository<FakeCustomer, Long> {
        boolean existsByEmail(String email);

        Optional<FakeCustomer> findByEmail(String email);
    }
}
