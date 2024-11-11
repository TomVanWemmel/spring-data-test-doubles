package be.tvw.customer.repository;

import be.tvw.customer.api.CreateCustomerRequest;
import be.tvw.customer.api.CustomerData;
import be.tvw.customer.api.CustomerRepository;
import be.tvw.customer.api.UpdateCustomerRequest;
import be.tvw.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCustomerRepository implements CustomerRepository {

    public SpringCustomerJpaRepository springCustomerJpaRepository;

    public JpaCustomerRepository(SpringCustomerJpaRepository springCustomerJpaRepository) {
        this.springCustomerJpaRepository = springCustomerJpaRepository;
    }

    @Override
    public Optional<CustomerEntity> customerById(long id) {
        return springCustomerJpaRepository.findById(id);
    }

    @Override
    public List<CustomerEntity> allCustomers() {
        return List.copyOf(springCustomerJpaRepository.findAll());
    }

    public CustomerData save(CreateCustomerRequest createCustomerRequest) {
        return springCustomerJpaRepository.saveAndFlush(new CustomerEntity(createCustomerRequest));
    }

    @Override
    public CustomerData update(Long customerId, UpdateCustomerRequest updateCustomerRequest) {
        if (springCustomerJpaRepository.existsByEmailAndIdNot(updateCustomerRequest.email(), customerId)) {
            throw new IllegalArgumentException("Customer with email " + updateCustomerRequest.email() + " already exists");
        }
        return springCustomerJpaRepository.findById(customerId)
                .map(foundEntity -> foundEntity.update(updateCustomerRequest))
                .map(springCustomerJpaRepository::saveAndFlush)
                .orElseThrow();
    }

    public interface SpringCustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
        boolean existsByEmailAndIdNot(String email, Long id);
    }
}
