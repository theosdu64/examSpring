package com.example.examspring.Service;

import com.example.examspring.Dao.ContactRepository;
import com.example.examspring.Entity.Category;
import com.example.examspring.Entity.Contact;
import com.example.examspring.Entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactService {
    private ContactRepository contactRepository;

    public Contact findById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    public List<Contact> findByUserAndCategory(User user, Optional<Category> category) {
        return contactRepository.findByUserAndCategory(user, category);
    }

    public List<Contact> findByUser(User user) {
        return contactRepository.findByUser(user);
    }

    public void deleteById(Long id) {
        contactRepository.deleteById(id);
    }
}
