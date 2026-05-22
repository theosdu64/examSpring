package com.example.examspring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class ExamspringApplicationTests {

	@Test
	void contextLoads() {
        assertFalse(1 == 2);
	}

}
