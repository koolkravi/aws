package com.mycom.poc.eksdemo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycom.poc.eksdemo.model.Contact;
import com.mycom.poc.eksdemo.service.ContactService;

@Controller
@RequestMapping("/")
public class ContactController {
	private static final Logger log = LoggerFactory.getLogger(ContactController.class);

	@Autowired
	ContactService contactService;

	@GetMapping
	public ModelAndView contacts() {
		List<Contact> contacts = (List<Contact>) contactService.findAll();
		return new ModelAndView("index", "value", contacts.isEmpty() ? null : contacts.get(0).getName());
	}

	@PostMapping("save")

	public ModelAndView saveContact(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("phone") String phone, RedirectAttributes redirect) {
		log.info("Contact data:: ");

		List<Contact> values = (List<Contact>) contactService.findAll();
		if (!values.isEmpty()) {
			contactService.delete(values.get(0).getId());
		}
		contactService.save(new Contact(name, email, phone));
		return new ModelAndView("redirect:/");
	}

}