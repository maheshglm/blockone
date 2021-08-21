@student-api @delete-student
Feature: Delete student with /deleteStudent endpoint

  As a Student-api user,
  I expect DELETE request with valid id on /deleteStudent endpoint should result in deleting student from database

  Scenario: Delete student
  application should allow deleting a valid student

    * assign a random integer to variable named "random_id"
    * I execute below query to "insert a student record"
    """
    INSERT INTO student (id, firstName, lastName, class, nationality)
    VALUES (${random_id}, 'Delete', 'User', '1 C', 'SG');
    """

    Given end point configured as "/deleteStudent"
    And query params configured as
      | id | ${random_id} |

    And header params configured as
      | content-type | application/json |

    When I send DELETE request

    Then I expect 200 status code

    And I expect value of column "RECORD_COUNT" in the below query equals to "0"
    """
    SELECT count(*) AS RECORD_COUNT FROM student
    WHERE id=${random_id}
    AND firstName='Delete'
    AND lastName='User'
    AND class='1 C'
    AND nationality='UK';
    """

  Scenario: Cleanup

    * I execute below query to "delete students created above"
    """
    DELETE FROM student WHERE id IN (${random_id});
    """