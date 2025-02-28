package com.flowsoft.commonartifacts;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.java.Log;
import org.json.simple.JSONObject;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

@Log
public class CommonFunctions {

    static BootcampParameters bootcampParameters = new BootcampParameters();

    public Response getAllBootcampsGETCall(BootcampParameters bootcampParameters) {

        RestAssured.baseURI = bootcampParameters.getBaseURI();
        Response response = given()
                .expect().statusCode(200)
                .when()
                .get("/");
        return response;
    }

    public Response addBootcampPOSTCall(BootcampParameters bootcampParameters, JSONObject fullObject) {

        RestAssured.baseURI = bootcampParameters.getBaseURI();
        Response response =
                given()
                        .contentType("application/json")
                        .body(fullObject)
                        .expect().statusCode(201)
                        .when()
                        .post("/");
        return response;
    }

    public Response getSingleBootcampGETCall(BootcampParameters bootcampParameters) {

        RestAssured.baseURI = bootcampParameters.getBaseURI();
        Response response = given()
                .pathParam("bootcampId", bootcampParameters.getBootcampId())
                .expect().statusCode(200)
                .when()
                .get("/{bootcampId}");
        return response;
    }

    public Response updateBootcampPUTCall(BootcampParameters bootcampParameters, JSONObject fullObject) {

        RestAssured.baseURI = bootcampParameters.getBaseURI();
        Response response =
                given()
                        .contentType("application/json")
                        .body(fullObject)
                        .pathParam("bootcampId", bootcampParameters.getBootcampId())
                        .expect().statusCode(200)
                        .when()
                        .put("/{bootcampId}");
        return response;
    }

    public Response deleteBootcampDELETECall(BootcampParameters bootcampParameters, int statusCode) {

        RestAssured.baseURI = bootcampParameters.getBaseURI();
        Response response =
                given()
                        .pathParam("bootcampId", bootcampParameters.getBootcampId())
                        .expect().statusCode(statusCode)
                        .when()
                        .delete("/{bootcampId}");
        return response;
    }

    public int computeNumberOfItemsInResponse(Response response, String parameter) {

        return response.jsonPath().getInt(parameter);
    }

    public String returnStringValueInResponse(Response response, String parameter) {

        return response.jsonPath().getString(parameter);
    }

    public void compareActualResponse(BootcampParameters bootcampParameters) {

        String comparisonParameter = bootcampParameters.getComparisonParameter();
        Assert.assertEquals(bootcampParameters.getResponseValue(), bootcampParameters.getExpectedValue(), comparisonParameter + " value does not match");
        log.info(comparisonParameter +" value validated to match");
    }

    public String returnSecondComparisonParameter(String comparisonParameter) {

        String[] comparisonParameterArray = comparisonParameter.split(" ", 2);
        return comparisonParameterArray[1].toLowerCase();
    }

    public String generateInvalidbootcampIdFormat(String originalbootcampId) {

        return (originalbootcampId + "lkjsdijf");
    }

    public String generateNonexistentbootcampId(String bootcampId) {

        char newchar = bootcampId.charAt(bootcampId.length()-1);
        String newbootcampId = bootcampId.substring(0, bootcampId.length() - 1) + String.valueOf(newchar+=1);

        return newbootcampId;
    }

    public void validateBootcampname(BootcampParameters bootcampParamters, Response response, String bootcampQualifier) {

        String comparisonParameter = bootcampParamters.getComparisonParameter();
        log.info("\n\n+++++++++++++++++++++++++++++++++ COMPARING " + comparisonParameter.toUpperCase() + " FOR " + bootcampQualifier + " +++++++++++++++++++++++++++++++++");
        String bootcampJSONValueInResponse = returnStringValueInResponse(response, bootcampParamters.getJsonElement());
        log.info(comparisonParameter + " Expected: " + bootcampParamters.getExpectedValue() + "; In response: " + bootcampJSONValueInResponse);
        bootcampParamters.setResponseValue(bootcampJSONValueInResponse);
        compareActualResponse(bootcampParamters);
        log.info("+++++++++++++++++++++++++++++++++++++ VALIDATION COMPLETED FOR " + comparisonParameter.toUpperCase() + " FOR " + bootcampQualifier +  " +++++++++++++++++++++++++++++++++");
    }
}
