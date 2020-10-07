
package com.mycom.poc.eksdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycom.poc.eksdemo.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, String> {

}
