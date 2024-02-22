package com.medilabosolutions.front.controllers;

import com.medilabosolutions.front.model.Note;
import com.medilabosolutions.front.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

	private final NoteService noteService;

	@Autowired
	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}

	@GetMapping("/notes/add")
	public String showAddNoteForm(Model model) {
		model.addAttribute("note", new Note());
		return "details";
	}

	@PostMapping("/notes")
	public String addNote(Note note, RedirectAttributes redirectAttributes) {
		noteService.addNote(note);
		redirectAttributes.addAttribute("id", note.getPatId());
		return "redirect:/patients/details/{id}";
	}

}
