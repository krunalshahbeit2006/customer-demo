#@parallel=false
Feature: Delete a customer

  Background:
    * url baseUrl
    * call read('app_util.feature')
    * def customerDTO = getCustomerDTO()
    * def customerDTOJson = objectAsJson(customerDTO)
#    * print 'customerDTOJson: ', customerDTOJson


  @Success
  Scenario: Create a customer, then delete the created customer by the 'Unique Id' and verify that the request has been accepted
    Given path '/api/customers/'
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When request customerDTOJson
    And method POST
    Then status 201
#    And def id = $.id

    Given url $._links.delete.href
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When method DELETE
    Then status 200

  @Error
  Scenario: Check the error if deleting the customer 'Unique Id' has space at the end
    Given path '/api/customers/111111111 '
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When method DELETE
    Then status 400
    And match header Content-Type contains 'application/problem+json'
    And match $.title == "Application Generic unknown failure"
    And match $.message contains "Customer object 'Unique Id' field must match pattern"
    And match $.detail == "Application could not handle failure."

  @Error
  Scenario: Check the error if deleting the customer 'Unique Id' does not exist
    Given path '/api/customers/1111111111'
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When method DELETE
    Then status 404
    And match header Content-Type contains 'application/problem+json'
    And match $.title == "Database record not found"
    And match $.detail == "Could not find customer with the 'customerId': 1111111111"
