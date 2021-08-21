@student-api @update-student
Feature: Fetch Students /updateStudent end point

  As a Student-api user,
  I expect PUT request with valid id on /updateStudent endpoint should result in updating student details in database

  Scenario: Update student
  application should allow updating student details by id

    * assign a random integer to variable named "random_id"

    * I execute below query to "insert a student record"
    """
    INSERT INTO student (id, firstName, lastName, class, nationality)
    VALUES (${random_id}, 'Bob', 'UpdateMe', 'UpdateMe', 'USA');
    """

    Given end point configured as "/updateStudent"

    And header params configured as
      | content-type | application/json |

    And body parameter configured as
    """
    {
      "id":${random_id},
      "lastName":"Perryman",
      "clazz":"N C"
    }
    """

    When I send PUT request

    Then I expect 200 status code

    And I expect response including following json key values
      | id          | ${random_id} |
      | firstName   | Bob          |
      | lastName    | Perryman     |
      | clazz       | N C          |
      | nationality | USA          |


  Scenario: Cleanup

    * I execute below query to "delete students created above"
    """
    DELETE FROM student WHERE id IN (${random_id});
    """