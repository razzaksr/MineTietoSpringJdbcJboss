package elements.spring.explore;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BankersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankersService bankersService;

    @InjectMocks
    private BankersController bankersController;

    @Test
    public void testStoredEndpoint() throws Exception {
        // Mock the service method call
        when(bankersService.storedOne(anyInt())).thenReturn(new ProcedureResponse());

        // Perform the request and assert the status code
        mockMvc.perform(get("/template/stored/123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCallDeleteEndpoint() throws Exception {
        // Mock the service method call
        when(bankersService.deleteOne(anyInt())).thenReturn("Record deleted");

        // Perform the request and assert the status code
        mockMvc.perform(delete("/template/123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCallUpdateEndpoint() throws Exception {
        // Mock the service method call
        when(bankersService.updateOne(any())).thenReturn("Record updated");

        // Perform the request and assert the status code
        mockMvc.perform(put("/template/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bankerId\": 123, \"bankerName\": \"John\", \"bankerPasscode\": \"pass123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCallReadByNameEndpoint() throws Exception {
        // Mock the service method call
        List<Bankers> bankersList = Arrays.asList(new Bankers(1, "John", "pass123"), new Bankers(2, "Jane", "pass456"));
        when(bankersService.readNames(anyString())).thenReturn(bankersList);

        // Perform the request and assert the status code and response content
        mockMvc.perform(get("/template/byname/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bankerId").value(1))
                .andExpect(jsonPath("$[0].bankerName").value("John"))
                .andExpect(jsonPath("$[0].bankerPasscode").value("pass123"))
                .andExpect(jsonPath("$[1].bankerId").value(2))
                .andExpect(jsonPath("$[1].bankerName").value("Jane"))
                .andExpect(jsonPath("$[1].bankerPasscode").value("pass456"));
    }

    @Test
    public void testAddingEndpoint() throws Exception {
        // Mock the service method call
        when(bankersService.insertion(any())).thenReturn("Record inserted");

        // Perform the request and assert the status code
        mockMvc.perform(post("/template/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bankerName\": \"John\", \"bankerPasscode\": \"pass123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCallOneIdEndpoint() throws Exception {
        // Mock the service method call
        Bankers banker = new Bankers(123, "John", "pass123");
        when(bankersService.listOne(anyInt())).thenReturn(Optional.of(banker));

        // Perform the request and assert the status code and response content
        mockMvc.perform(get("/template/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bankerId").value(123))
                .andExpect(jsonPath("$.bankerName").value("John"))
                .andExpect(jsonPath("$.bankerPasscode").value("pass123"));
    }

    @Test
    public void testCallListEndpoint() throws Exception {
        // Mock the service method call
        List<Bankers> bankersList = Arrays.asList(new Bankers(1, "John", "pass123"), new Bankers(2, "Jane", "pass456"));
        when(bankersService.listAll()).thenReturn(bankersList);

        // Perform the request and assert the status code and response content
        mockMvc.perform(get("/template/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bankerId").value(1))
                .andExpect(jsonPath("$[0].bankerName").value("John"))
                .andExpect(jsonPath("$[0].bankerPasscode").value("pass123"))
                .andExpect(jsonPath("$[1].bankerId").value(2))
                .andExpect(jsonPath("$[1].bankerName").value("Jane"))
                .andExpect(jsonPath("$[1].bankerPasscode").value("pass456"));
    }
}