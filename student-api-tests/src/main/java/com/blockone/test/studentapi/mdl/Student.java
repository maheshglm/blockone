package com.blockone.test.studentapi.mdl;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Student {

    private Long id;
    private String firstName;
    private String lastName;
    private String clazz;
    private String nationality;

    //As default StudentBuilder is not accessible outside the package,
    //this is the workaround is used
    //https://stackoverflow.com/questions/48318097/is-it-possible-to-make-lomboks-builder-public/48318222
    public static StudentBuilder builder() {
        return new StudentBuilder();
    }
}
