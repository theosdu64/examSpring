package com.example.examspring.Service;

import com.example.examspring.Dao.ContactRepository;
import com.example.examspring.Dao.UserRepository;
import com.example.examspring.Entity.Category;
import com.example.examspring.Entity.Contact;
import com.example.examspring.Entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Contact> findByCategory(User user, Category category) {
        return contactRepository.findByUserAndCategory(user, category);
    }

    public List<Contact> findByUser(User user) {
        return contactRepository.findByUser(user);
    }

    public void deleteById(Long id) {
        contactRepository.deleteById(id);
    }
}
