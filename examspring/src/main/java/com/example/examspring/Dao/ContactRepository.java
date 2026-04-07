package com.example.examspring.Dao;

import com.example.examspring.Entity.Category;
import com.example.examspring.Entity.Contact;
import com.example.examspring.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUser(User user);
    List<Contact> findByUserAndCategory(User user , Category category);
}
