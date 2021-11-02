package com.example.demo.services.customer.web.controller;

import com.example.demo.services.customer.TestCustomerApplication;
import com.example.demo.services.customer.TestLifecycleLogger;
import com.example.demo.services.customer.exception.CustomerNotFoundException;
import com.example.demo.services.customer.exception.CustomerPersistenceFailureException;
import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.service.TestCustomerServiceImpl;
import com.example.demo.services.customer.stub.CustomerDTOForPutITStub;
import com.example.demo.services.customer.stub.CustomerDTOITStub;
import com.example.demo.services.customer.util.TestAppUtil;
import com.example.demo.services.customer.web.CustomerDTO;
import com.example.demo.services.customer.web.CustomerDTOForPut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerIT extends TestCustomerApplication implements TestLifecycleLogger {

    private static final String REQUEST_MAPPING_URI = "/api";
    private static final String REQUEST_MAPPING_CUSTOMERS_URI = REQUEST_MAPPING_URI + "/customers";
    private static final String REQUEST_MAPPING_CUSTOMERS_BATCH_URI = REQUEST_MAPPING_CUSTOMERS_URI + "/batch";
    private static final String REQUEST_MAPPING_URI_PATH_VAR_ID = REQUEST_MAPPING_CUSTOMERS_URI + "/{customerId}";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestCustomerServiceImpl testService;


    @BeforeEach
    public void beforeEachTest() {
        testService.deleteCustomers();
    }

    @Test
    void testPostCustomer() {
        final CustomerDTO customerDTOToBePosted;

        customerDTOToBePosted = new CustomerDTOITStub().getCustomerDTO();

        try {
            mvc.perform(post(REQUEST_MAPPING_CUSTOMERS_URI)
                            .content(TestAppUtil.asJsonString(customerDTOToBePosted))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isCreated())
                    /*.andExpect(jsonPath("$.id").exists())*/
                    .andExpect(jsonPath("$.uniqueId").exists())
                    .andExpect(jsonPath("$.name").value(customerDTOToBePosted.getName()))
                    .andExpect(jsonPath("$.email").value(customerDTOToBePosted.getEmail()))
                    .andExpect(jsonPath("$.telephone").value(customerDTOToBePosted.getTelephone()))
                    .andExpect(jsonPath("$.createdBy").value(customerDTOToBePosted.getModifiedBy()))
                    .andExpect(jsonPath("$.lastModifiedBy").value(customerDTOToBePosted.getModifiedBy()))
                    .andExpect(jsonPath("$._links").exists())
                    /*.andExpect(jsonPath("$._links.self").exists())*/
                    .andExpect(jsonPath("$._links.self.href").isNotEmpty())
                    /*.andExpect(jsonPath("$._links.customers").exists())*/
                    .andExpect(jsonPath("$._links.customers.href").isNotEmpty());

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testPostCustomerBodyIsNull() {
        try {
            mvc.perform(post(REQUEST_MAPPING_CUSTOMERS_URI)
                            .content(TestAppUtil.asJsonString(null))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertThat(result.getResolvedException()).isNotNull())
                    .andExpect(result -> assertThat(result.getResolvedException() instanceof HttpMessageNotReadableException).isTrue())
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Required request body is missing:"));

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Disabled
    @Test
    void testGetCustomers() {
        final Customer savedCustomer;

        savedCustomer = testService.saveCustomer();

        try {
            mvc.perform(get(REQUEST_MAPPING_CUSTOMERS_URI)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$..customers").exists())
                    /*.andExpect(jsonPath("$..customers").isArray())*/
                    .andExpect(jsonPath("$..customers").isNotEmpty())
                    .andExpect(jsonPath("$..customers[*].id").exists())
                    /*.andExpect(jsonPath("$..customers[*].id").isNotEmpty())*/
                    .andExpect(jsonPath("$..customers[*].id").value(savedCustomer.getId()))
                    .andExpect(jsonPath("$..customers[*].uniqueId").exists())
                    /*.andExpect(jsonPath("$..customers[*].uniqueId").isNotEmpty())*/
                    .andExpect(jsonPath("$..customers[*].uniqueId").value(savedCustomer.getUniqueId()))
                    .andExpect(jsonPath("$..customers[*].name").value(savedCustomer.getName()))
                    .andExpect(jsonPath("$..customers[*].email").value(savedCustomer.getEmail()))
                    .andExpect(jsonPath("$..customers[*].telephone").value(savedCustomer.getTelephone()))
                    .andExpect(jsonPath("$..customers[*].createdBy").value(savedCustomer.getCreatedBy()))
                    .andExpect(jsonPath("$..customers[*].lastModifiedBy").value(savedCustomer.getLastModifiedBy()))
                    .andExpect(jsonPath("$..customers[*]._links").exists())
                    /*.andExpect(jsonPath("$..customers[*]._links").isArray())*/
                    .andExpect(jsonPath("$..customers[*]._links.self.href").isNotEmpty())
                    .andExpect(jsonPath("$..customers[*]._links.customers.href").isNotEmpty())
                    .andExpect(jsonPath("$.._links").exists())
                    /*.andExpect(jsonPath("$.._links").isArray())*/
                    .andExpect(jsonPath("$.._links.self.href").isNotEmpty());

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Disabled
    @Test
    void testGetCustomersUniqueIdIsNotExist() {
        try {
            mvc.perform(get(REQUEST_MAPPING_CUSTOMERS_URI)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$..customers").doesNotExist())
                    .andExpect(jsonPath("$.._links").exists())
                    .andExpect(jsonPath("$.._links.self.href").isNotEmpty());

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testGetCustomer() {
        final Customer savedCustomer;

        savedCustomer = testService.saveCustomer();

        try {
            mvc.perform(get(REQUEST_MAPPING_URI_PATH_VAR_ID, savedCustomer.getUniqueId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    /*.andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.id").value(savedCustomer.getId()))
                    .andExpect(jsonPath("$.uniqueId").exists())
                    .andExpect(jsonPath("$.uniqueId").isNotEmpty())*/
                    .andExpect(jsonPath("$.uniqueId").value(savedCustomer.getUniqueId()))
                    .andExpect(jsonPath("$.name").value(savedCustomer.getName()))
                    .andExpect(jsonPath("$.email").value(savedCustomer.getEmail()))
                    .andExpect(jsonPath("$.telephone").value(savedCustomer.getTelephone()))
                    .andExpect(jsonPath("$.createdBy").value(savedCustomer.getCreatedBy()))
                    .andExpect(jsonPath("$.lastModifiedBy").value(savedCustomer.getLastModifiedBy()))
                    .andExpect(jsonPath("$._links").exists())
                    /*.andExpect(jsonPath("$._links.self").exists())*/
                    .andExpect(jsonPath("$._links.self.href").isNotEmpty())
                    /*.andExpect(jsonPath("$._links.customers").exists())*/
                    .andExpect(jsonPath("$._links.customers.href").isNotEmpty());

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testGetCustomerUniqueIdIsNotExist() {
        try {
            mvc.perform(get(REQUEST_MAPPING_URI_PATH_VAR_ID, "1111111111")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertThat(result.getResolvedException()).isNotNull())
                    .andExpect(result -> assertThat(result.getResolvedException() instanceof CustomerNotFoundException).isTrue())
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Could not find customer with the 'customerId':"));

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testGetCustomerUniqueIdIsNegative() {
        try {
            mvc.perform(get(REQUEST_MAPPING_URI_PATH_VAR_ID, "-100000000")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertThat(result.getResolvedException()).isNotNull())
                    .andExpect(result -> assertThat(result.getResolvedException() instanceof ConstraintViolationException).isTrue())
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Customer object 'Unique Id' field must match pattern"));

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testPutCustomer() {
        final CustomerDTO customerDTOListToBePut;
        final Customer savedCustomer;

        savedCustomer = testService.saveCustomer();

        customerDTOListToBePut = new CustomerDTOITStub().getCustomerDTOWithUpdatedInfo();

        try {
            mvc.perform(put(REQUEST_MAPPING_URI_PATH_VAR_ID, savedCustomer.getUniqueId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(TestAppUtil.asJsonString(customerDTOListToBePut))
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isCreated())
                    /*.andExpect(jsonPath("$.id").exists())*/
                    .andExpect(jsonPath("$.uniqueId").exists())
                    .andExpect(jsonPath("$.name").value(customerDTOListToBePut.getName()))
                    .andExpect(jsonPath("$.email").value(customerDTOListToBePut.getEmail()))
                    .andExpect(jsonPath("$.telephone").value(customerDTOListToBePut.getTelephone()))
                    .andExpect(jsonPath("$.createdBy").value(customerDTOListToBePut.getModifiedBy()))
                    .andExpect(jsonPath("$.lastModifiedBy").value(customerDTOListToBePut.getModifiedBy()))
                    .andExpect(jsonPath("$._links").exists())
                    /*.andExpect(jsonPath("$._links.self").exists())*/
                    .andExpect(jsonPath("$._links.self.href").isNotEmpty())
                    /*.andExpect(jsonPath("$._links.customers").exists())*/
                    .andExpect(jsonPath("$._links.customers.href").isNotEmpty());

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testPutCustomerUniqueIdIsNotExist() {
        final CustomerDTO customerDTOListToBePut;

        customerDTOListToBePut = new CustomerDTOITStub().getCustomerDTOWithUpdatedInfo();

        try {
            mvc.perform(put(REQUEST_MAPPING_URI_PATH_VAR_ID, "1111111111")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(TestAppUtil.asJsonString(customerDTOListToBePut))
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isCreated())
                    /*.andExpect(jsonPath("$.id").exists())*/
                    .andExpect(jsonPath("$.uniqueId").exists())
                    .andExpect(jsonPath("$.name").value(customerDTOListToBePut.getName()))
                    .andExpect(jsonPath("$.email").value(customerDTOListToBePut.getEmail()))
                    .andExpect(jsonPath("$.telephone").value(customerDTOListToBePut.getTelephone()))
                    .andExpect(jsonPath("$.createdBy").value(customerDTOListToBePut.getModifiedBy()))
                    .andExpect(jsonPath("$.lastModifiedBy").value(customerDTOListToBePut.getModifiedBy()))
                    .andExpect(jsonPath("$._links").exists())
                    /*.andExpect(jsonPath("$._links.self").exists())*/
                    .andExpect(jsonPath("$._links.self.href").isNotEmpty())
                    /*.andExpect(jsonPath("$._links.customers").exists())*/
                    .andExpect(jsonPath("$._links.customers.href").isNotEmpty());

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testPutCustomerUniqueIdIdIsNegative() {
        final CustomerDTO customerDTOListToBePut;

        customerDTOListToBePut = new CustomerDTOITStub().getCustomerDTOWithUpdatedInfo();

        try {
            mvc.perform(put(REQUEST_MAPPING_URI_PATH_VAR_ID, "-100000000")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(TestAppUtil.asJsonString(customerDTOListToBePut))
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertThat(result.getResolvedException()).isNotNull())
                    .andExpect(result -> assertThat(result.getResolvedException() instanceof ConstraintViolationException).isTrue())
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Customer object 'Unique Id' field must match pattern"));

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testPutCustomerUniqueIdIdIsNull() {
        final CustomerDTO customerDTOListToBePut;

        customerDTOListToBePut = new CustomerDTOITStub().getCustomerDTOWithUpdatedInfo();

        try {
            mvc.perform(put(REQUEST_MAPPING_URI_PATH_VAR_ID, (Object) null)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(TestAppUtil.asJsonString(customerDTOListToBePut))
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertThat(result.getResolvedException()).isNotNull())
                    .andExpect(result -> assertThat(result.getResolvedException() instanceof HttpRequestMethodNotSupportedException).isTrue())
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Request method 'PUT' not supported"));

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testPutCustomerBodyIsNull() {
        final Customer savedCustomer;

        savedCustomer = testService.saveCustomer();

        try {
            mvc.perform(put(REQUEST_MAPPING_URI_PATH_VAR_ID, savedCustomer.getUniqueId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(TestAppUtil.asJsonString(null))
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertThat(result.getResolvedException()).isNotNull())
                    .andExpect(result -> assertThat(result.getResolvedException() instanceof HttpMessageNotReadableException).isTrue())
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Required request body is missing:"));

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testDeleteCustomer() {
        final Customer savedCustomer;

        savedCustomer = testService.saveCustomer();

        try {
            mvc.perform(delete(REQUEST_MAPPING_URI_PATH_VAR_ID, savedCustomer.getUniqueId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk());

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testDeleteCustomerUniqueIdIdIsNotExist() {
        try {
            mvc.perform(delete(REQUEST_MAPPING_URI_PATH_VAR_ID, "1111111111")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertThat(result.getResolvedException()).isNotNull())
                    .andExpect(result -> assertThat(result.getResolvedException() instanceof CustomerNotFoundException).isTrue())
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Could not find customer with the 'customerId':"));

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testDeleteCustomerUniqueIdIdIsNegative() {
        try {
            mvc.perform(delete(REQUEST_MAPPING_URI_PATH_VAR_ID, "-100000000")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertThat(result.getResolvedException()).isNotNull())
                    .andExpect(result -> assertThat(result.getResolvedException() instanceof ConstraintViolationException).isTrue())
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Customer object 'Unique Id' field must match pattern"));

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testDeleteCustomerUniqueIdIdIsNull() {
        try {
            mvc.perform(delete(REQUEST_MAPPING_URI_PATH_VAR_ID, (Object) null)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertThat(result.getResolvedException()).isNotNull())
                    .andExpect(result -> assertThat(result.getResolvedException() instanceof HttpRequestMethodNotSupportedException).isTrue())
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Request method 'DELETE' not supported"));

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Test
    void testPostCustomers() {
        final List<CustomerDTO> customerDTOListToBePosted;

        customerDTOListToBePosted = new CustomerDTOITStub().getCustomerDTOList();

        try {
            mvc.perform(post(REQUEST_MAPPING_CUSTOMERS_BATCH_URI)
                            .content(TestAppUtil.asJsonString(customerDTOListToBePosted))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isMultiStatus())
                    .andExpect(jsonPath("$..customers").exists())
                    .andExpect(jsonPath("$..customers").isNotEmpty())
                    /*.andExpect(jsonPath("$..customers[*].id").exists())*/
                    .andExpect(jsonPath("$..customers[*].uniqueId").exists())
                    .andExpect(jsonPath("$..customers[*].name").value(customerDTOListToBePosted.get(0).getName()))
                    .andExpect(jsonPath("$..customers[*].email").value(customerDTOListToBePosted.get(0).getEmail()))
                    .andExpect(jsonPath("$..customers[*].telephone").value(customerDTOListToBePosted.get(0).getTelephone()))
                    .andExpect(jsonPath("$..customers[*].createdBy").value(customerDTOListToBePosted.get(0).getModifiedBy()))
                    .andExpect(jsonPath("$..customers[*].lastModifiedBy").value(customerDTOListToBePosted.get(0).getModifiedBy()))
                    .andExpect(jsonPath("$..customers[*]._links").exists())
                    .andExpect(jsonPath("$..customers[*]._links.self.href").isNotEmpty())
                    .andExpect(jsonPath("$..customers[*]._links.customers.href").isNotEmpty())
                    .andExpect(jsonPath("$.._links").exists());

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Disabled
    @Test
    void testPostCustomersMappingIsFailed() {
        final List<CustomerDTO> customerDTOListToBePosted;

        customerDTOListToBePosted = new ArrayList<>();

        try {
            mvc.perform(post(REQUEST_MAPPING_CUSTOMERS_BATCH_URI)
                            .content(TestAppUtil.asJsonString(customerDTOListToBePosted))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertThat(result.getResolvedException()).isNotNull())
                    .andExpect(result -> assertThat(result.getResolvedException() instanceof ConstraintViolationException).isTrue())
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Application could not handle failure."));

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Disabled
    @Test
    void testPostCustomersPersistingIsFailed() {
        final Customer savedCustomer;

        final List<CustomerDTO> customerDTOListToBePosted;

        savedCustomer = testService.saveCustomer();

        customerDTOListToBePosted = new CustomerDTOITStub().getCustomerDTOList();
        /*customerDTOListToBePosted.forEach(customer -> customer.setUniqueId(savedCustomer.getUniqueId()));*/

        try {
            mvc.perform(post(REQUEST_MAPPING_CUSTOMERS_BATCH_URI)
                            .content(TestAppUtil.asJsonString(customerDTOListToBePosted))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(result -> assertThat(result.getResolvedException()).isNotNull())
                    .andExpect(result -> assertThat(result.getResolvedException() instanceof CustomerPersistenceFailureException).isTrue())
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Application has failed to save all the customer(s) to the database"));

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

    @Disabled
    @Test
    void testPutCustomers() {
        final Customer savedCustomer;

        final List<CustomerDTOForPut> customerDTOListToBePut;

        savedCustomer = testService.saveCustomer();

        customerDTOListToBePut = new CustomerDTOForPutITStub().getCustomerDTOWithUpdatedInfoList();
        /*customerDTOListToBePut.forEach(customer -> customer.setUniqueId(savedCustomer.getUniqueId()));*/

        try {
            mvc.perform(put(REQUEST_MAPPING_CUSTOMERS_BATCH_URI)
                            .content(TestAppUtil.asJsonString(customerDTOListToBePut))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isMultiStatus())
                    .andExpect(jsonPath("$..customers").exists())
                    .andExpect(jsonPath("$..customers").isNotEmpty())
                    .andExpect(jsonPath("$..customers[*].id").exists())
                    .andExpect(jsonPath("$..customers[*].uniqueId").exists())
                    .andExpect(jsonPath("$..customers[*].name").value(customerDTOListToBePut.get(0).getCustomer().getName()))
                    .andExpect(jsonPath("$..customers[*].email").value(customerDTOListToBePut.get(0).getCustomer().getEmail()))
                    .andExpect(jsonPath("$..customers[*].telephone").value(customerDTOListToBePut.get(0).getCustomer().getTelephone()))
                    .andExpect(jsonPath("$..customers[*].createdBy").value(customerDTOListToBePut.get(0).getCustomer().getModifiedBy()))
                    .andExpect(jsonPath("$..customers[*].lastModifiedBy").value(customerDTOListToBePut.get(0).getCustomer().getModifiedBy()))
                    .andExpect(jsonPath("$..customers[*]._links").exists())
                    .andExpect(jsonPath("$..customers[*]._links.self.href").isNotEmpty())
                    .andExpect(jsonPath("$..customers[*]._links.customers.href").isNotEmpty())
                    .andExpect(jsonPath("$.._links").exists());

        } catch (Exception ex) {
            fail("Test failed: ", ex);
        }
    }

}
