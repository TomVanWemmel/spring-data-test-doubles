package be.tvw.customer.web.controller;

import be.tvw.customer.api.CreateCustomerRequest;
import be.tvw.customer.api.CustomerData;
import be.tvw.customer.fakes.FakeCustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class ExampleControllerTest {

    private FakeCustomerRepository fakeCustomerRepository = new FakeCustomerRepository();
    private MockMvc mockMvc = standaloneSetup(new ExampleController(fakeCustomerRepository))
            .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
            .alwaysExpect(status().isOk())
            .alwaysExpect(content().contentType("application/json"))
            .build();

    private ObjectMapper objectMapper = JsonMapper.builder()
            .build();

    @Test
    void requestIsSentToRepository() throws Exception {
        mockMvc.perform(post("/v1/customers/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CreateCustomerRequest("my.name", "my.email@slothmail.com", null))))
                .andExpect(status().isOk());

        assertThat(fakeCustomerRepository.allCustomers())
                .hasSize(1)
                .map(CustomerData::name)
                .containsExactly("my.name");
    }
}