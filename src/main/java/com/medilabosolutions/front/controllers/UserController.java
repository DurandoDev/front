package com.medilabosolutions.front.controllers;

import com.medilabosolutions.front.model.Patient;
import com.medilabosolutions.front.model.Role;
import com.medilabosolutions.front.model.User;
import com.medilabosolutions.front.repositories.RoleRepository;
import com.medilabosolutions.front.repositories.UserRepository;
import com.medilabosolutions.front.services.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;


import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

	@Autowired
	PatientService patientService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder encoder;

	@GetMapping("/listUser")
	public List<User> userList(Model model) {
		List<User> users = userRepository.findAll();
		return users;
	}

	@PostMapping("/users")
	public String addUser(@Valid User user, BindingResult result) {
		if (result.hasErrors()) {
			return "signup";
		}

		user.setPassword(encoder.encode(user.getPassword()));
		Role role = roleRepository.findByName("USER").orElse(new Role("USER"));
		user.setRole(role);
		userRepository.save(user);
		return "redirect:login";
	}

	@GetMapping("/signup")
	public String showUserForm(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}


	@GetMapping("/login")
	public String login() {
		return "login";
	}


	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:login?logout";
	}

	@GetMapping("/update/{id}")
	public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		user.setPassword("");
		model.addAttribute("user", user);
		return "update";
	}

	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") Long id, @Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "update";
		}

		user.setId(id);
		userRepository.save(user);
		model.addAttribute("users", userRepository.findAll());
		return "redirect:user/list";
	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id, Model model) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userRepository.delete(user);
		model.addAttribute("users", userRepository.findAll());
		return "redirect:user/list";
	}


	@GetMapping("/home")
	public String home(Model model, Principal principal){
		String userName = principal.getName();
		model.addAttribute("userName", userName);
		Flux<Patient> patients = patientService.getPatients();
		model.addAttribute("patients", patients.collectList().block());
		return "home";
	}
}

