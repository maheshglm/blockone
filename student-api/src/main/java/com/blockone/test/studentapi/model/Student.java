package com.blockone.test.studentapi.model;

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

    /**
     * Instantiates a new Student.
     *
     * @param id          the id
     * @param firstName   the first name
     * @param lastName    the last name
     * @param clazz       the clazz
     * @param nationality the nationality
     */
    public Student(Long id, String firstName, String lastName, String clazz, String nationality) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clazz = clazz;
        this.nationality = nationality;
    }

    /**
     * Instantiates a new Student.
     */
    public Student() {
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets clazz.
     *
     * @return the clazz
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * Sets clazz.
     *
     * @param section the section
     */
    public void setClazz(String section) {
        this.clazz = section;
    }

    /**
     * Gets nationality.
     *
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Sets nationality.
     *
     * @param nationality the nationality
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    /**
     * Equals boolean.
     *
     * @param o the o
     * @return the boolean
     */
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

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, clazz, nationality);
    }

    /**
     * To string string.
     *
     * @return the string
     */
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
