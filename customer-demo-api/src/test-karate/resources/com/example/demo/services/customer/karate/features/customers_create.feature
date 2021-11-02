#@parallel=false
Feature: Create a customer

  Background:
    * url baseUrl
    * call read('app_util.feature')
    * def customerDTO = getCustomerDTO()
    * def customerDTOJson = objectAsJson(customerDTO)
#    * print 'customerDTOJson: ', customerDTOJson


  @Success
  Scenario: Create a customer and check if the customer has been created
    Given path '/api/customers/'
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When request customerDTOJson
    And method POST
    Then status 201
#    And print response
    And match header Content-Type contains 'application/json'
    And match $.uniqueId == '#notnull'
    And match $.name == customerDTO.getName()
    And match $.email == customerDTO.getEmail()
    And match $.telephone == customerDTO.getTelephone()
    And match $.createdBy == customerDTO.getModifiedBy()
    And match $.lastModifiedBy == customerDTO.getModifiedBy()
