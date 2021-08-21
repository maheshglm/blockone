package com.blockone.test.studentapi.repository;

import com.blockone.test.studentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, Long> {

    @Query(value = "select * from student where class=?1", nativeQuery = true)
    List<Student> getStudentsByClazz(String clazz);
}
