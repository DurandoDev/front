package com.medilabosolutions.front.controllers;

import com.medilabosolutions.front.model.Note;
import com.medilabosolutions.front.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
