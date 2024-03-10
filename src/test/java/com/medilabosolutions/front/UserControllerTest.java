package com.medilabosolutions.front;

import com.medilabosolutions.front.model.Role;
import com.medilabosolutions.front.model.User;
import com.medilabosolutions.front.repositories.RoleRepository;
import com.medilabosolutions.front.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder encoder;

	private List<Long> createdUserIds = new ArrayList<>();

	@BeforeEach
	public void init() {
		Role role = roleRepository.findByName("USER").orElseGet(() -> {
			Role newRole = new Role("USER");
			roleRepository.save(newRole);
			return newRole;
		});

		User user = new User();
		user.setFullname("testName");
		user.setUsername("test");
		user.setPassword(encoder.encode("testPassword"));
		user.setRole(role);
		User savedUser = userRepository.save(user);
		createdUserIds.add(savedUser.getId());
	}

	@AfterEach
	public void clean() {
		for (Long userId : createdUserIds) {
			userRepository.deleteById(userId);
		}
		createdUserIds.clear();
	}


	@Test
	public void testShowSignupForm() throws Exception {
		mockMvc.perform(get("/signup"))
				.andExpect(status().isOk())
				.andExpect(view().name("signup"))
				.andExpect(model().attributeExists("user"));
	}

	@Test
	@WithMockUser(username = "test@email.com", password = "testPassword", roles = "USER")
	public void testShowUserList() throws Exception {
		mockMvc.perform(get("/listUser"))
				.andExpect(status().isOk());
	}

	@Test
	public void testAddUser() throws Exception {
		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("username", "testUsername")
						.param("fullname", "testFullName")
						.param("password", "testPassword")
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("login"));
		userRepository.findByUsername("testUsername").ifPresent(user -> userRepository.delete(user));
	}



	@Test
	public void testLogin() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"));
	}

	@Test
	@WithMockUser(username = "test@email.com", password = "testPassword", roles = "USER")
	public void testLogout() throws Exception {
		mockMvc.perform(get("/logout"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?logout"));
	}

	@Test
	@WithMockUser(username = "test@email.com", password = "testPassword", roles = "USER")
	public void testDeleteUser() throws Exception {
		Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role USER not found"));

		User user = new User();
		user.setUsername("delete");
		user.setFullname("user");
		user.setPassword(encoder.encode("deletePassword"));
		user.setRole(role); // Utiliser le rôle existant
		User savedUser = userRepository.save(user);

		mockMvc.perform(get("/delete/" + savedUser.getId()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("user/list"));
	}


}

