package com.medilabosolutions.front.services;

import com.medilabosolutions.front.model.Note;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class NoteService {

	private final WebClient webClient;

	public NoteService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://gateway:8080").build();
	}

	public Flux<Note> getNotesByPatientId(int patientId) {
		return this.webClient.get()
				.uri("/notes/{patId}", patientId)
				.retrieve()
				.bodyToFlux(Note.class);
	}

	public void addNote(Note note) {
		webClient.post()
				.uri("/notes")
				.bodyValue(note)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
	}

}
