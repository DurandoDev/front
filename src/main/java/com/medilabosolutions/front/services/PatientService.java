package com.medilabosolutions.front.services;

import com.medilabosolutions.front.model.Patient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class PatientService {

	private final WebClient webClient;

	public PatientService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
	}

	public Flux<Patient> getPatients() {
		return this.webClient.get()
				.uri("/patients")
				.retrieve()
				.bodyToFlux(Patient.class);
	}

	public Mono<Patient> getPatientById(int id) {
		return this.webClient.get()
				.uri("/patients/{id}", id)
				.retrieve()
				.bodyToMono(Patient.class);
	}

	public Flux<Patient> searchPatients(String searchType, String value) {
		return this.webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/patients/search")
						.queryParam("searchType", searchType)
						.queryParam("value", value)
						.build())
				.retrieve()
				.bodyToFlux(Patient.class);
	}

	public void addPatient(Patient patient) {
		webClient.post()
				.uri("/patients")
				.bodyValue(patient)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
	}

	public Mono<Void> deletePatient(int id) {
		return this.webClient.delete()
				.uri("/patients/delete/{id}", id)
				.retrieve()
				.bodyToMono(Void.class);
	}

	public Mono<Patient> updatePatient(int id, Patient patient) {
		return this.webClient.put()
				.uri("/patients/update/{id}", id)
				.bodyValue(patient)
				.retrieve()
				.bodyToMono(Patient.class);
	}


}

