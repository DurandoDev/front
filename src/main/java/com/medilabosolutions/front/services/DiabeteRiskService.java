package com.medilabosolutions.front.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DiabeteRiskService {

	private final WebClient webClient;

	public DiabeteRiskService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://localhost:8084").build();
	}

	public Mono<String> determineRiskLevel(int patientId) {
		return webClient.get()
				.uri("/diabetes/risk/{patientId}", patientId)
				.retrieve()
				.bodyToMono(String.class)
				.onErrorReturn("Unknown");
	}
}
