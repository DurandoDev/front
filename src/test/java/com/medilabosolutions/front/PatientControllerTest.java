package com.medilabosolutions.front;

import com.medilabosolutions.front.model.Note;
import com.medilabosolutions.front.model.Patient;
import com.medilabosolutions.front.services.DiabeteRiskService;
import com.medilabosolutions.front.services.NoteService;
import com.medilabosolutions.front.services.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class PatientControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PatientService patientService;

	@MockBean
	private NoteService noteService;

	@MockBean
	private DiabeteRiskService diabeteRiskService;


	@Test
	@WithMockUser(username = "test@email.com", password = "testPassword", roles = "USER")
	void testListPatients() throws Exception {
		when(patientService.getPatients()).thenReturn(Flux.just(new Patient(), new Patient()));

		mockMvc.perform(get("/patients"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test@email.com", password = "testPassword", roles = "USER")
	void testShowAddPatientForm() throws Exception {
		mockMvc.perform(get("/patients/add"))
				.andExpect(status().isOk())
				.andExpect(view().name("add"));
	}

	@Test
	@WithMockUser(username = "test@email.com", password = "testPassword", roles = "USER")
	void testShowPatientDetails() throws Exception {
		int patientId = 1;
		when(patientService.getPatientById(eq(patientId))).thenReturn(Mono.just(new Patient()));
		when(noteService.getNotesByPatientId(eq(patientId))).thenReturn(Flux.just(new Note(), new Note()));
		when(diabeteRiskService.determineRiskLevel(eq(patientId))).thenReturn(Mono.just("Low Risk"));

		mockMvc.perform(get("/patients/details/{id}", patientId))
				.andExpect(status().isOk())
				.andExpect(view().name("details"));
	}

}
