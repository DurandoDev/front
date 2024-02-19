package com.medilabosolutions.front.controllers;

import com.medilabosolutions.front.model.Note;
import com.medilabosolutions.front.model.Patient;
import com.medilabosolutions.front.services.NoteService;
import com.medilabosolutions.front.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class PatientController {

	private final PatientService patientService;

	private final NoteService noteService;

	@Autowired
	public PatientController(PatientService patientService, NoteService noteService) {
		this.patientService = patientService;
		this.noteService = noteService;
	}


	@GetMapping("/patients")
	public String listPatients(Model model) {
		Flux<Patient> patients = patientService.getPatients();
		model.addAttribute("patients", patients.collectList().block());
		return "patients";
	}

	@GetMapping("/patients/search")
	public String searchPatients(@RequestParam("searchType") String searchType,
	                             @RequestParam("value") String value, Model model) {
		Flux<Patient> patients = patientService.searchPatients(searchType, value);
		model.addAttribute("patients", patients.collectList().block());
		return "home";
	}

	@GetMapping("/patients/add")
	public String showAddPatientForm(Model model) {
		model.addAttribute("patient", new Patient());
		return "add";
	}

	@GetMapping("/patients/update/{id}")
	public String showUpdatePatientForm(@PathVariable int id, Model model) {
		Mono<Patient> patientMono = patientService.getPatientById(id);
		Patient patient = patientMono.block();
		model.addAttribute("patient", patient);

		return "update";
	}

	@GetMapping("/patients/details/{id}")
	public String showPatientDetails(@PathVariable int id, Model model) {
		// Récupérer les détails du patient
		Patient patient = patientService.getPatientById(id).block();
		model.addAttribute("patient", patient);

		// Récupérer les notes du patient
		List<Note> notesList = noteService.getNotesByPatientId(id).collectList().block();
		model.addAttribute("notes", notesList);


		return "details";
	}


	@PostMapping("/patients")
	public String addPatient(Patient patient) {
		patientService.addPatient(patient);
		return "redirect:/home";
	}

	@GetMapping("/patients/delete/{id}")
	public String deletePatient(@PathVariable int id) {
		patientService.deletePatient(id).block();
		return "redirect:/home";
	}

	@PostMapping("/patients/update/{id}")
	public Mono<String> updatePatient(@PathVariable int id, @ModelAttribute("patient") Patient patient) {
		return patientService.updatePatient(id, patient)
				.thenReturn("redirect:/home");
	}


}
