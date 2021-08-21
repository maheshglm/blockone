package com.blockone.test.studentapi.repository;

import com.blockone.test.studentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * JPA Student repo interface.
 */
public interface StudentRepo extends JpaRepository<Student, Long> {

    /**
     * Gets students by clazz.
     *
     * @param clazz the clazz
     * @return the students by clazz
     */
    @Query(value = "select * from student where class=?1", nativeQuery = true)
    List<Student> getStudentsByClazz(String clazz);
}
