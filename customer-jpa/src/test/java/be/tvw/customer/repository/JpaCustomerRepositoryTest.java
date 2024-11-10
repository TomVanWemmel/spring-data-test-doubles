package be.tvw.customer.repository;

import be.tvw.customer.api.CustomerRepository;
import be.tvw.customer.api.CustomerRepositoryTest;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

public class JpaCustomerRepositoryTest extends JpaRepositoryTest<JpaCustomerRepository> implements CustomerRepositoryTest {

    @Override
    protected JpaCustomerRepository getRepository(JpaRepositoryFactory factory) {
        return new JpaCustomerRepository(factory.getRepository(JpaCustomerRepository.SpringCustomerJpaRepository.class));
    }

    @Override
    public CustomerRepository getCustomerRepository() {
        return repository;
    }
}