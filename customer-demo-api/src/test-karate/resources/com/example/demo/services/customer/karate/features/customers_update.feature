#@parallel=false
Feature: Update a customer

  Background:
    * url baseUrl
    * call read('app_util.feature')
    * def customerDTO = getCustomerDTO()
    * def customerDTOJson = objectAsJson(customerDTO)
#    * print 'customerDTOJson: ', customerDTOJson


#  @ignore
#  @Success
#  Scenario: Create a customer, then update the created customer by the 'Unique Id' and check if the customer is updated
#    * def customerDTOWithUpdatedInfo = getCustomerDTOWithUpdatedInfo()
#    * def customerDTOWithUpdatedInfoJson = objectAsJson(customerDTOWithUpdatedInfo)
##    * print 'customerDTOWithUpdatedInfoJson: ', customerDTOWithUpdatedInfoJson
#
#    Given path '/api/customers/'
#    And header Content-Type = 'application/json'
#    And header Accept = 'application/json'
#    When request customerDTOJson
#    And method POST
#    Then status 201
#    And def id = $.id
#
#    Given path '/api/customers/'+id
#    And header Content-Type = 'application/json'
#    And header Accept = 'application/json'
#    When request customerDTOWithUpdatedInfoJson
#    And method PUT
#    Then status 201
##    And print response
#    And match header Content-Type contains 'application/json'
#    And match $.uniqueId == '#notnull'
#    And match $.name == customerDTOWithUpdatedInfo.getName()
#    And match $.email == customerDTOWithUpdatedInfo.getEmail()
#    And match $.telephone == customerDTOWithUpdatedInfo.getTelephone()
#    And match $.createdBy == customerDTOWithUpdatedInfo.getModifiedBy()
#    And match $.lastModifiedBy == customerDTO.getModifiedBy()

  @Success
  Scenario: Update the customer by the 'Unique Id' does not exist and check if the customer is created with the provided 'Unique Id'
    * def customerDTONew = getCustomerDTO()
    * def customerDTONewJson = objectAsJson(customerDTONew)
    * def customerDTOWithUpdatedInfo = getCustomerDTOWithUpdatedInfo()
    * def customerDTOWithUpdatedInfoJson = objectAsJson(customerDTOWithUpdatedInfo)
#    * print 'customerDTOWithUpdatedInfoJson: ', customerDTOWithUpdatedInfoJson

    Given path '/api/customers/1111111111'
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When request customerDTOWithUpdatedInfoJson
    And method PUT
    Then status 201
#    And print response
    And match header Content-Type contains 'application/json'
    And match $.uniqueId == '#notnull'
    And match $.name == customerDTOWithUpdatedInfo.getName()
    And match $.email == customerDTOWithUpdatedInfo.getEmail()
    And match $.telephone == customerDTOWithUpdatedInfo.getTelephone()
    And match $.createdBy == customerDTO.getModifiedBy()
    And match $.lastModifiedBy == customerDTO.getModifiedBy()

  @Error
  Scenario: Check the error if updating the customer 'Unique Id' has space at the end
    * def customerDTONew = getCustomerDTO()
    * def customerDTONewJson = objectAsJson(customerDTONew)
    * def customerDTOWithUpdatedInfo = getCustomerDTOWithUpdatedInfo()
    * def customerDTOWithUpdatedInfoJson = objectAsJson(customerDTOWithUpdatedInfo)
#    * print 'customerDTOWithUpdatedInfoJson: ', customerDTOWithUpdatedInfoJson

    Given path '/api/customers/111111111 '
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    When request customerDTOWithUpdatedInfoJson
    When method PUT
    Then status 400
#    And print response
    And match header Content-Type contains 'application/problem+json'
    And match $.title == "Application Generic unknown failure"
    And match $.message contains "Customer object 'Unique Id' field must match pattern"
    And match $.detail == "Application could not handle failure."
