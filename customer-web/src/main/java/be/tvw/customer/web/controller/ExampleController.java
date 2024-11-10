package be.tvw.customer.web.controller;

import be.tvw.customer.api.CreateCustomerRequest;
import be.tvw.customer.api.CustomerData;
import be.tvw.customer.api.CustomerRepository;
import be.tvw.customer.api.UpdateCustomerRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/customers")
public class ExampleController {

    //TODO Map Data
    private final CustomerRepository customerRepository;

    public ExampleController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/")
    public List<CustomerData> getAllExamples() {
        return List.copyOf(customerRepository.allCustomers());
    }

    @PostMapping("/")
    public CustomerData addExample(@RequestBody CreateCustomerRequest request) {
        return customerRepository.save(request);
    }

    @GetMapping("/{id}")
    public CustomerData getExample(@PathVariable Long id) {
        return customerRepository.customerById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public CustomerData getExample(@PathVariable Long id, @RequestBody UpdateCustomerRequest request) {
        return customerRepository.update(id, request);
    }
}
