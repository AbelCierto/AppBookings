package step;

import com.booking.appbookings.entities.Customer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class CustomerStepDefinitions {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int randomServerPort;
    private String endpointPath;
    private ResponseEntity<String> responseEntity;

    @Given("The Endpoint {string} is available")
    public void theEndpointIsAvailable(String endpointPath) {
        this.endpointPath = String.format(endpointPath, randomServerPort);
    }

    @When("A Customer Request is sent with values {string}, {string}, {string}, {string}, {string}, {string}")
    public void aCustomerRequestIsSentWithValues(String firstName, String lastName, String dni, String address, String phone, String email) {
        Customer customer = new Customer(0L, firstName, lastName, dni, address, phone, email);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Customer> request = new HttpEntity<>(customer, headers);
        responseEntity = testRestTemplate.postForEntity(endpointPath, request, String.class);

    }

    @Then("A customer with status {int} is received")
    public void aCustomerWithStatusIsReceived(int expectedStatusCode) {
        int actualStatusCode = responseEntity.getStatusCodeValue();
        assertThat(actualStatusCode).isEqualTo(expectedStatusCode);
    }

    @When("A Customer Delete is sent with id {string}")
    public void aCustomerDeleteIsSentWithId(String id_customer) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id_customer);
        testRestTemplate.delete(endpointPath+"/{id}", params);
        responseEntity = new ResponseEntity<>(HttpStatus.OK);
    }

    @When("A Customer Selected is sent with id {string}")
    public void aCustomerSelectedIsSentWithId(String id_customer) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id_customer);
        Customer customer = testRestTemplate.getForObject(endpointPath+"/{id}", Customer.class, params);
        responseEntity = new ResponseEntity<>(customer.toString(), HttpStatus.OK);
        System.out.println(customer.toString());

    }

    @When("A Customer who are registered in DB")
    public void aCustomerWhoAreRegisteredInDB() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        responseEntity = testRestTemplate.exchange(endpointPath, HttpMethod.GET, entity, String.class);
        System.out.println(responseEntity);
    }

    @Then("List of Customer with status {int} is received")
    public void listOfCustomerWithStatusIsReceived(int expectedStatusCode) {
        int actualStatusCode = responseEntity.getStatusCodeValue();
        assertThat(actualStatusCode).isEqualTo(expectedStatusCode);
    }

    @When("A Customer Updated is sent with id {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void aCustomerUpdatedIsSentWithId(String id_customer, String firstname, String lastname, String dni, String address, String phone, String email) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id_customer);
        Customer customerUpdated = new Customer(0L, firstname, lastname, dni, address, phone, email);
        testRestTemplate.put(endpointPath+"/{id}", customerUpdated, params);
        responseEntity = new ResponseEntity<>(customerUpdated.toString(), HttpStatus.OK);
    }
}
