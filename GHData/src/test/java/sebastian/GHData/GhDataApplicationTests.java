package sebastian.GHData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GhDataApplicationTests {

	@Autowired
	private GitHubDataApplication gitHubDataApplication;

	//@Test
	//void contextLoads() {
	//}

}
