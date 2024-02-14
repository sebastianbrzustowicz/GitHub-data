package sebastian.GHData;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sebastian.GHData.controller.DataController;
import sebastian.GHData.resource.RepoBranchesRequester;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GhDataApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testProcessUsernameNotFound() throws Exception {
		String nonExistentUsername = UUID.randomUUID().toString();

		mockMvc.perform(MockMvcRequestBuilders.get("/GitHub/getData/{username}", nonExistentUsername)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));
	}

	@Test
	public void testProcessNonexistentUsernameStatusCode() throws Exception {
		String nonExistentUsername = UUID.randomUUID().toString();

		mockMvc.perform(MockMvcRequestBuilders.get("/GitHub/getData/{username}", nonExistentUsername)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testProcessCorrectUserStatusCode() throws Exception {
		String username = "dummy011";

		//String jsonResponse = "{\"name\":\"main\",\"commit\":{\"sha\":\"55c12e78eb6d5c267c0263e244a6e45ccf4507b9\",\"url\":\"https\"},\"protected\":false}";

		//Map<String, Integer> mockResponse = Map.of(jsonResponse, 200);

		//Mockito.when(RepoBranchesRequester.sendGetRequest(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(mockResponse);

		mockMvc.perform(MockMvcRequestBuilders.get("/GitHub/getData/{username}", username)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
