@student-api @outline-examples
Feature: Add Student with /addStudent endpoint

  As a Student-api user,
  I expect POST request with valid json on /addStudent endpoint should result in adding student into database

  Scenario Outline: Add New student
  application should allow adding a new student with valid json

    When I add a student with below attributes
      | id   | firstName   | lastName   | class   | nationality   |
      | <id> | <firstName> | <lastName> | <class> | <nationality> |

    Then I expect <status_code> status code

    And I expect there is a student entry in the database
      | id   | firstName   | lastName   | class   | nationality   |
      | <id> | <firstName> | <lastName> | <class> | <nationality> |

    Examples:
      | id     | firstName | lastName | class | nationality | status_code |
      | 232323 | Mahesh    | G        | 1 A   | IND         | 201         |
      | 232324 | hhsgdshg  | hjshdjs  | 2 A   | US          | 201         |


  Scenario Outline: Cleanup student with id <id>

    * delete student with id <id>

    Examples:
      | id     |
      | 232323 |
      | 232324 |