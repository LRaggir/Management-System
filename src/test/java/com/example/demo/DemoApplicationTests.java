package com.example.demo;

import com.example.demo.model.Review;
import com.example.demo.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	private String getJwtToken() throws Exception {

		String login = "admin@gmail.com";
		String password = "1234";


		String loginRequestJson = "{\"email\": \"" + login + "\", \"password\": \"" + password + "\"}";


		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/authorization")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginRequestJson));


		String responseContent = resultActions.andReturn().getResponse().getContentAsString();
		String token = extractTokenFromResponseBody(responseContent);
		return token;
	}
	public String extractTokenFromResponseBody(String responseContent) {
		int start = responseContent.indexOf("\"body\":\"") + 8;
		int end = responseContent.indexOf("\"", start);


		return responseContent.substring(start, end);
	}

	@Test
	void testCreateTaskEndpoint() throws Exception {
		Task task = new Task();
		task.setTitle("Sample Title");
		task.setDescription("Sample Description");
		task.setAuthor(1);
		task.setStatus("в процессе");
		task.setPriority("высокий");
		String token = getJwtToken();

		mockMvc.perform(MockMvcRequestBuilders.post("/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(task))
						.header("Authorization", "Bearer " + token))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testCreateReviewEndpoint() throws Exception {
		Review review = new Review();
		review.setTaskId(1);
		review.setDescription("Проверка");
		review.setAuthor("Автор");
		String token = getJwtToken();

		mockMvc.perform(MockMvcRequestBuilders.post("/createRe")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(review))
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

	@Test
	void testGetReviewEndpoint() throws Exception {
		Integer taskId = 1;
		String token = getJwtToken();
		mockMvc.perform(MockMvcRequestBuilders.get("/getRe")
						.param("task_id", taskId.toString())
						.header("Authorization", "Bearer " + token))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testEditTaskEndpoint() throws Exception {
		Task task = new Task();
		task.setId(1);
		task.setTitle("Sample Title");
		task.setDescription("Sample Description");
		task.setAuthor(1);
		task.setPerformer(2);
		String token = getJwtToken();

		mockMvc.perform(MockMvcRequestBuilders.post("/edit")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(task))
						.header("Authorization", "Bearer " + token))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetAllTasksEndpoint() throws Exception {
		String token = getJwtToken();

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/get")
				.header("Authorization", "Bearer " + token));

		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.body").isArray());
	}
	@Test
	void testDeleteTaskEndpoint() throws Exception {
		String token = getJwtToken();

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/delete")
				.param("task_id", "1")
				.header("Authorization", "Bearer " + token));
		resultActions.andExpect(status().isOk());
	}
	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

