package be.tvw.customer.entity;

import be.tvw.customer.api.CreateCustomerRequest;
import be.tvw.customer.api.CustomerData;
import be.tvw.customer.api.UpdateCustomerRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "customer_entity")
public class CustomerEntity implements CustomerData, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column
    private Integer age;

    public CustomerEntity() {
        //For JPA
    }

    public CustomerEntity(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public CustomerEntity(CustomerData customerData) {
        this(customerData.name(), customerData.email(), customerData.age());
    }

    public CustomerEntity(CreateCustomerRequest createCustomerRequest) {
        this(createCustomerRequest.name(), createCustomerRequest.email(), createCustomerRequest.age());
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public Integer age() {
        return age;
    }

    public CustomerEntity update(UpdateCustomerRequest updateCustomerRequest) {
        this.age = updateCustomerRequest.age();
        return this;
    }
}
