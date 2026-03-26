Feature: Test

  Scenario: Test API
    Given I M Sending A GET Request To "/actuator/health"
    Then RESPONSE STATUS 200