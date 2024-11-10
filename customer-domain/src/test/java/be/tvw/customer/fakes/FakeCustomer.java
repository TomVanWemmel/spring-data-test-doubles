package be.tvw.customer.fakes;

import be.tvw.customer.api.CreateCustomerRequest;
import be.tvw.customer.api.CustomerData;
import be.tvw.customer.api.UpdateCustomerRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.util.Objects;

@KeySpace("customers")
public final class FakeCustomer implements CustomerData {
    @Id
    private Long id;
    private String name;
    private String email;
    private Integer age;

    public FakeCustomer(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public FakeCustomer(CreateCustomerRequest createCustomerRequest) {
        this(null, createCustomerRequest.name(), createCustomerRequest.email(), createCustomerRequest.age());
    }

    public FakeCustomer update(UpdateCustomerRequest updateCustomerRequest) {
        return new FakeCustomer(this.id, this.name, this.email, updateCustomerRequest.age());
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FakeCustomer) obj;
        return Objects.equals(this.id, that.id) &&
               Objects.equals(this.name, that.name) &&
               Objects.equals(this.email, that.email) &&
               Objects.equals(this.age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }

    @Override
    public String toString() {
        return "FakeCustomer[" +
               "id=" + id + ", " +
               "name=" + name + ", " +
               "email=" + email + ", " +
               "age=" + age + ']';
    }

}
