import config.TestConfig;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.Header;
import org.testng.annotations.Test;


import java.util.List;
import java.util.Map;

import static constants.Constants.Actions.SWAPI_GET_PEOPLE;
import static constants.Constants.Path.SWAPI_PATH;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;


public class FirstTest extends TestConfig {

    @Test
    public void myFirstTest() {
        given().log().uri().
        when().get(SWAPI_GET_PEOPLE + "1").
        then().log().body().statusCode(200);
    }

    @Test
    public void getSomeFieldInResponseWithIndexAssertion() {
        given().spec(requestSpecificationForSwapiTests).log().uri().
                when().get(SWAPI_PATH).
                then().body("people", equalTo("http://swapi.dev/api/people/")).log().body();
    }

    @Test
    public void getSomeFieldInResponseAssertion() {
        given().spec(requestSpecificationForSwapiTests).log().uri().
        when().get(SWAPI_PATH + SWAPI_GET_PEOPLE).
        then()
        .body("count", equalTo(82))
        .body("results.name[0]", equalTo("Luke Skywalker"))
        .log().body();
    }

    @Test
    public void getAllDataFromRequest() {
        Response response =
            given().spec(requestSpecificationForSwapiTests).log().uri().
            when().get(SWAPI_PATH).
            then().extract().response();

        String jsonResponseAsString = response.asString();
        System.out.println(jsonResponseAsString);
    }

    @Test
    public void getCookieFromResponse() {
        Response response =
            given().spec(requestSpecificationForSwapiTests).log().uri().
            when().get(SWAPI_PATH).
            then().extract().response();
        Map<String, String> allCookie = response.getCookies();
        System.out.println("allCookie--> " + allCookie);

        //String someCookie = response.getCookie("_cfduid");
        //System.out.println("someCookie--> " + someCookie);
    }

    @Test
    public void getHeadersFromResponse() {
        Response response =
            given().spec(requestSpecificationForSwapiTests).log().uri().
            when().get(SWAPI_PATH).
            then().extract().response();

        Headers headers = response.getHeaders();
        System.out.println("headers--> " + headers);

        String contentType = response.getContentType();
        System.out.println("contentType--> " + contentType);
    }

    @Test
    public void validateXmlSchema() {
            given().log().uri().
            when().get("https://maps.googleapis.com/maps/api/place/findplacefromtext/xml?key=AIzaSyCF810taj_IOMnHopkp07T5MrXE2XkQhF8&input=New York&inputtype=textquery&fields=business_status,formatted_address,geometry,icon,name,photos,place_id,plus_code,types&language=ru").
            then().body(matchesXsdInClasspath("xmlSchema.xsd")).log().body();
    }

    @Test
    public void validateJsonSchema() {
        given().log().uri().
                when().get("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?key=AIzaSyCF810taj_IOMnHopkp07T5MrXE2XkQhF8&input=New York&inputtype=textquery&fields=business_status,formatted_address,geometry,icon,name,photos,place_id,plus_code,types&language=ru").
                then().body(matchesJsonSchemaInClasspath("jsonSchema.json")).log().body();
    }

    @Test
    public void getMaoOfElementsWithSomekey() {
        Response response =
            given().spec(requestSpecificationForSwapiTests).log().uri().
            when().get(SWAPI_PATH + SWAPI_GET_PEOPLE);
        Map<String, ?> someObject = response
                .path("results.find {it.name = 'Luke Skywalker'}");
        System.out.println("someObject--> " + someObject);
    }

    @Test
    public void getSingleElementsWithSomekey() {
        Response response =
                given().spec(requestSpecificationForSwapiTests).log().uri().
                        when().get(SWAPI_PATH + SWAPI_GET_PEOPLE);
        String url = response
                .path("results.find {it.name = 'Luke Skywalker'}.url");
        System.out.println("url--> " + url);

    }

    @Test
    public void getAllElementsWithSomekey() {
        Response response =
                given().spec(requestSpecificationForSwapiTests).log().uri().
                        when().get(SWAPI_PATH + SWAPI_GET_PEOPLE);
        System.out.println("response--> " + response.asString());
        List films = response
                .path("results.findAll {it.films}.name");
        System.out.println("films--> " + films);


    }
}
