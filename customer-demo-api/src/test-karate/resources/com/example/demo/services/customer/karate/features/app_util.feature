@ignore @report=false
Feature: App util

  Background: Set app utility variables for other feature files

  Scenario: Set utility variables
    * def objectAsJson =
      """
        function(objectAsArgument) {
          var TestAppUtil = Java.type("com.example.demo.services.customer.util.TestAppUtil");

          return TestAppUtil.asJsonString(objectAsArgument);
        }
      """
#    * print 'objectAsJson: ', objectAsJson(getCustomerDTO())

    * def getCustomerDTO =
      """
        function() {
          var CustomerDTOITStub = Java.type("com.example.demo.services.customer.stub.CustomerDTOITStub");
          var stub = new CustomerDTOITStub();

          return stub.getCustomerDTO();
        }
      """
#    * print 'customerDTO: ', getCustomerDTO()

    * def getCustomerDTOWithUpdatedInfo =
      """
        function() {
          var CustomerDTOITStub = Java.type("com.example.demo.services.customer.stub.CustomerDTOITStub");
          var stub = new CustomerDTOITStub();
          return stub.getCustomerDTOWithUpdatedInfo();
        }
      """
#    * print 'customerDTOWithUpdatedInfo: ', getCustomerDTOWithUpdatedInfo()
