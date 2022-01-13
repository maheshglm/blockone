package com.blockone.test.studentapi.model;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Student.
 * Model class, mapped to student table in the Database
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "student")
@Entity
public class Student {

    private static final String ID = "id";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String CLASS = "class";
    private static final String NATIONALITY = "nationality";

    @Id
    @NonNull
    @Column(name = ID)
    private Long id;

    @Column(name = FIRSTNAME, nullable = false)
    private String firstName;

    @Column(name = LASTNAME, nullable = false)
    private String lastName;

    @Column(name = CLASS, nullable = true)
    private String clazz;

    @Column(name = NATIONALITY, nullable = false)
    private String nationality;

}
