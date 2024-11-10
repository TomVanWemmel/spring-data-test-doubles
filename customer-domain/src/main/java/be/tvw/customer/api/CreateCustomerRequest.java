package be.tvw.customer.api;

public record CreateCustomerRequest(
        String name,
        String email,
        Integer age) {
}
