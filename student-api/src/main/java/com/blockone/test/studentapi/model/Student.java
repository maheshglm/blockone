package com.blockone.test.studentapi.model;

import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

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

    @Column(name = FIRSTNAME)
    private String firstName;

    @Column(name = LASTNAME)
    private String lastName;

    @Column(name = CLASS)
    private String clazz;

    @Column(name = NATIONALITY)
    private String nationality;

    public Student(Long id, String firstName, String lastName, String clazz, String nationality) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clazz = clazz;
        this.nationality = nationality;
    }

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String section) {
        this.clazz = section;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return id.equals(student.id)
                && firstName.equals(student.firstName)
                && lastName.equals(student.lastName)
                && clazz.equals(student.clazz)
                && nationality.equals(student.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, clazz, nationality);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", class='" + clazz + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
