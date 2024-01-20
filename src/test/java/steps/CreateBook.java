package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateBook {
    private int BookingID;
    public void setBaseUri(String baseUri)
    {
        RestAssured.baseURI = baseUri;
    }

    @Given("User is authorized with credentials")
    public void auth() {
        String tokenEndpoint = "https://restful-booker.herokuapp.com/auth";

        // Prepare the request parameters
        String requestBody = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        // Send the request to the token endpoint
        Response response = RestAssured.given()
                .baseUri(tokenEndpoint)
                .header("Content-Type", ContentType.JSON)
                .body(requestBody)
                .post();

        // Extract and print the access token from the response
        String accessToken = response.jsonPath().getString("token");
        System.out.println("token: " + accessToken);
    }

    @When("user sends the request to Create New book")
    public void createBooking() {
        // Request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("firstname", "Jim");
        requestBody.put("lastname", "Brown");
        requestBody.put("totalprice", 111);
        requestBody.put("depositpaid", true);

        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2018-01-01");
        bookingDates.put("checkout", "2019-01-01");

        requestBody.put("bookingdates", bookingDates);
        requestBody.put("additionalneeds", "Breakfast");

        // Perform the request
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("https://restful-booker.herokuapp.com/booking");

        // Validate the response
        Assert.assertEquals(200, response.getStatusCode());

        // Extract bookingid from the response
        BookingID = response.jsonPath().getInt("bookingid");
        System.out.println("Booking ID: " + BookingID);

        // You can now use the bookingId as needed in your test
        // For example, you might want to assert that the bookingId is greater than 0.
        assert BookingID > 0 : "Booking ID should be greater than 0";

    }

    @Then("Get Booking ID details")
        public void GetBookingdetails ()
    {
        Response retrieveResponse = given()
                .when()
                .get("https://restful-booker.herokuapp.com/booking/" + BookingID);
        Assert.assertEquals(200, retrieveResponse.getStatusCode());
        String responseBody = retrieveResponse.getBody().asString();
        System.out.println("Retrieve Response Body: " + responseBody);
    }
    }

