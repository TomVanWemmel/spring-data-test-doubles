package be.tvw.customer.app;

import be.tvw.customer.api.CreateCustomerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import be.tvw.customer.api.CustomerData;
import be.tvw.customer.api.CustomerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    private ObjectMapper objectMapper = JsonMapper.builder()
            .build();

    @Test
    void requestIsSentToRepository() throws Exception {
        mockMvc.perform(post("/v1/customers/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CreateCustomerRequest("my.name", "my.email@slothmail.com", null))))
                .andExpect(status().isOk());

        assertThat(customerRepository.allCustomers())
                .hasSize(1)
                .map(CustomerData::name)
                .containsExactly("my.name");
    }

}