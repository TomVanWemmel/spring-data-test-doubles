package be.tvw.customer.fakes;

import be.tvw.customer.api.CustomerRepository;
import be.tvw.customer.api.CustomerRepositoryTest;

public class FakeCustomerRepositoryTest implements CustomerRepositoryTest {

    private final FakeCustomerRepository fakeCustomerRepository;

    public FakeCustomerRepositoryTest() {
        fakeCustomerRepository = new FakeCustomerRepository();
    }

    @Override
    public CustomerRepository getCustomerRepository() {
        return fakeCustomerRepository;
    }
}
