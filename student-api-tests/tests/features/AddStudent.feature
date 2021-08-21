@student-api @add-student
Feature: Add Student with /addStudent endpoint

  As a Student-api user,
  I expect POST request with valid json on /addStudent endpoint should result in adding student into database

  Scenario: Add New student
  application should allow adding a new student with valid json

    * assign a random integer to variable named "random_id1"

    Given end point configured as "/addStudent"
    And header params configured as
      | content-type | application/json |

    And body parameter configured as
    """
    {
      "id":${random_id1},
      "firstName": "Mahesh",
      "lastName": "G",
      "clazz":"3 A",
      "nationality": "India"
    }
    """

    When I send POST request

    Then I expect 201 status code
    And I expect a response as "Student with id=${random_id1} added successfully"

    And I expect value of column "RECORD_COUNT" in the below query equals to "1"
    """
    SELECT count(*) AS RECORD_COUNT FROM student
    WHERE id=${random_id1}
    AND firstName='Mahesh'
    AND lastName='G'
    AND class='3 A'
    AND nationality='India';
    """

  Scenario: Add existing student
  application should not add existing student again

    * assign a random integer to variable named "random_id2"
    * I execute below query to "insert a student record"
    """
    INSERT INTO student (id, firstName, lastName, class, nationality)
    VALUES (${random_id2}, 'Bob', 'Bob', '1 C', 'UK');
    """

    Given end point configured as "/addStudent"
    And header params configured as
      | content-type | application/json |

    And body parameter configured as
    """
    {
      "id":${random_id2},
      "firstName": "Bob",
      "lastName": "Bob",
      "clazz":"1 C",
      "nationality": "UK"
    }
    """

    When I send POST request

    Then I expect 200 status code
    And I expect a response as "Student with id=${random_id2} already available"

    #expectation is, the record count should still be one
    And I expect value of column "RECORD_COUNT" in the below query equals to "1"
    """
    SELECT count(*) AS RECORD_COUNT FROM student
    WHERE id=${random_id2}
    AND firstName='Bob'
    AND lastName='Bob'
    AND class='1 C'
    AND nationality='UK';
    """


  Scenario: Cleanup

    * I execute below query to "delete students created above"
    """
    DELETE FROM student WHERE id IN (${random_id2}, ${random_id1});
    """

