@student-api @fetch-student
Feature: Fetch Students /fetchStudents end point

  As a Student-api user,
  I expect GET request with valid id on /fetchStudents endpoint should result in getting student details from database

  Scenario: Fetch student by id
  application should allow fetching student by id

    * assign a random integer to variable named "random_id"
    * I execute below query to "insert a student record"
    """
    INSERT INTO student (id, firstName, lastName, class, nationality)
    VALUES (${random_id}, 'Fetch', 'User', '1 A', 'SG');
    """

    Given end point configured as "/fetchStudents"
    And query params configured as
      | id | ${random_id} |

    And header params configured as
      | content-type | application/json |

    When I send GET request

    Then I expect 200 status code

    And I expect response including following json key values
      | id          | ${random_id} |
      | firstName   | Fetch        |
      | lastName    | User         |
      | clazz       | 1 A          |
      | nationality | SG           |

  Scenario: Fetch student by Clazz
  application should allow fetching all students by clazz

    * assign a random integer to variable named "random_id"

    * I execute below query to "insert a student1 record"
    """
    INSERT INTO student (id, firstName, lastName, class, nationality)
    VALUES (1${random_id}, 'Fetch', 'User', 'T C', 'SG');
    """

    * I execute below query to "insert a student2 record"
    """
    INSERT INTO student (id, firstName, lastName, class, nationality)
    VALUES (2${random_id}, 'Fetch', 'User', 'T C', 'SG');
    """

    * I execute below query to "insert a student3 record"
    """
    INSERT INTO student (id, firstName, lastName, class, nationality)
    VALUES (3${random_id}, 'Fetch', 'User', 'T C', 'SG');
    """

    Given end point configured as "/fetchStudents"
    And query params configured as
      | clazz | T C |

    And header params configured as
      | content-type | application/json |

    When I send GET request

    Then I expect 200 status code

    And I expect response including following json key values
      | [0].clazz | T C |
      | [1].clazz | T C |
      | [2].clazz | T C |


  Scenario: Cleanup

    * I execute below query to "delete students created above"
    """
    DELETE FROM student WHERE id IN (${random_id},1${random_id},2${random_id},3${random_id});
    """
