package sebastian.GHData;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GhDataApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private GitHubDataApplication gitHubDataApplication;

	//@Test
	//void contextLoads() {
	//}

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
	public void testProcessNonexistentUsername() throws Exception {
		String nonExistentUsername = UUID.randomUUID().toString();

		mockMvc.perform(MockMvcRequestBuilders.get("/GitHub/getData/{username}", nonExistentUsername)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

}
