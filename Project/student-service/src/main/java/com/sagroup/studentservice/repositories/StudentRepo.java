package com.sagroup.studentservice.repositories;

import com.sagroup.studentservice.domains.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {
    List<Student> findByClassName(String className);
    Optional<Student> findByEmail(String email);
}
