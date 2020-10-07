
package com.mycom.poc.eksdemo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycom.poc.eksdemo.model.Contact;
import com.mycom.poc.eksdemo.repository.ContactRepository;

@Component
public class ContactService {

	@Autowired
	ContactRepository contactRepository;

	public Iterable<Contact> findAll() {
		return contactRepository.findAll();
	}

	public Contact save(Contact contact) {
		return contactRepository.save(contact);
	}

	public Optional<Contact> findByID(String id) {
		return contactRepository.findById(id);
	}

	public void delete(Integer id) {
		Contact contact = new Contact();
		contact.setId(id);
		contactRepository.delete(contact);
	}

}
