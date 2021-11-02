#@parallel=false
Feature: Read the customer(s)

  Background:
    * url baseUrl
    * call read('app_util.feature')
    * def customerDTO = getCustomerDTO()
    * def customerDTOJson = objectAsJson(customerDTO)
#    * print 'customerDTOJson: ', customerDTOJson


  @Success
  Scenario: Create customer(s) and check if all created customer(s) can be retrieved
    Given path '/api/customers/'
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When request customerDTOJson
    And method POST
    Then status 201
#    And def id = $.id

    Given path '/api/customers/'
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When method GET
    Then status 200
#    And print response
    And match header Content-Type contains 'application/json'
    And match $..customers[*] == '##[_ > 1]'
#    And match $..customers[*].id contains id
    And match $..customers[*].uniqueId == '#notnull'
    And match $..customers[*].name contains customerDTO.getName()
    And match $..customers[*].email contains customerDTO.getEmail()
    And match $..customers[*].telephone contains customerDTO.getTelephone()
    And match $..customers[*].createdBy contains customerDTO.getModifiedBy()
    And match $..customers[*].lastModifiedBy contains customerDTO.getModifiedBy()

#  @Success
#  Scenario: Check the response if no customer(s) can be retrieved
#    Given path '/api/customers/'
#    And header Content-Type = 'application/json'
#    And header Accept = 'application/json'
#    When method GET
#    Then status 200
##    And print response
#    And match header Content-Type contains 'application/json'
#    And match $..customers[*] == '##[_ == 0]'

  @Success
  Scenario: Create a customer and check if the created customer can be retrieved by the 'Unique Id'
    Given path '/api/customers/'
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When request customerDTOJson
    And method POST
    Then status 201
#    And def id = $.id

    Given url $._links.self.href
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When method GET
    Then status 200
#    And print response
    And match header Content-Type contains 'application/json'
    And match $.uniqueId == '#notnull'
    And match $.name == customerDTO.getName()
    And match $.email == customerDTO.getEmail()
    And match $.telephone == customerDTO.getTelephone()
    And match $.createdBy == customerDTO.getModifiedBy()
    And match $.lastModifiedBy == customerDTO.getModifiedBy()

  @Error
  Scenario: Check the error if the customer retrieved by the 'Unique Id' has space at the end
    Given path '/api/customers/111111111 '
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When method GET
    Then status 400
#    And print response
    And match header Content-Type contains 'application/problem+json'
    And match $.title == "Application Generic unknown failure"
    And match $.message contains "Customer object 'Unique Id' field must match pattern"
    And match $.detail == "Application could not handle failure."

  @Error
  Scenario: Check the error if the customer retrieved by the 'Unique Id' does not exist
    Given path '/api/customers/1111111111'
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When method GET
    Then status 404
#    And print response
    And match header Content-Type contains 'application/problem+json'
    And match $.title == "Database record not found"
    And match $.detail == "Could not find customer with the 'customerId': 1111111111"
