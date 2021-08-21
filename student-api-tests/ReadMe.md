# Student api Cucumber Integration Tests for API and Database

It is a simple test framework to perform API & Database validations primarily focussed on REST and MYSQL integrations of
student-api.

### Features & Configuration

Features, reports and env Configurations are isolated from source code with the intention of update in any of the
features
(example test data) and env configuration does not require to compile the source code.

Features are placed in `/tests/features` and config is in `/config` relative to base directory

`Bootstrap.java` class will load `dev` environment properties i.e. `env_dev.properties` by default, but it can be
overridden by setting `curr.env.name` system property while running the tests.

### How to run tests

As a prerequisite, ensure student-api is running on `localhost:8080` (Refer: student-api readme.md to run api using
docker-compose). If application url is different, please update `api.base.url` value in `env_dev.properties`

* Clone this repository
* run `mvn clean install` to run the features which are tagged as `@student-api`
* To run a specific tests (tag) ` mvn test -Dcucumber.options="--tags <tagname>"`
* Reports will be generated at `/testout/reports`
* For simplicity, default cucumber reporting format is used, but it can be extended with NetmasterThought or Extent
  reports of choice by adding required dependencies and configuration.

### Acceptance Criteria

* 4 features are written covering 4 basic endpoint operations i.e. GET, POST, PUT and DELETE
* Tests more focuses on the API acceptance criteria and integration with the database (mysql).
* Used SQL statements (to set up & clear down data) in integration with API functionality.


