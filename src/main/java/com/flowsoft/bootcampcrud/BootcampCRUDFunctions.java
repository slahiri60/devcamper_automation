package com.flowsoft.bootcampcrud;

import com.flowsoft.commonartifacts.CommonFunctions;
import com.flowsoft.commonartifacts.BootcampParameters;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.java.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static io.restassured.RestAssured.given;

@Log
public class BootcampCRUDFunctions {

    static BootcampParameters bootcampParameters = new BootcampParameters();
    static CommonFunctions commonFunctions = new CommonFunctions();

    public void getAllBootcamps(BootcampParameters bootcampParameters) {

        log.info("\n\n=========================== Issuing API GET call to retrieve all bootcamps  ===========================");
        Response response = commonFunctions.getAllBootcampsGETCall(bootcampParameters);
        log.info("\n\n=========================== API GET call completed successfully and HTTP Status Code of 200 validated ===========================");
    }

    public void createNewbootcamp(BootcampParameters bootcampParameters, String bootcampQualifier) {

        log.info("\n\n=========================== Issuing API POST call to create new bootcamp  ============================");

        Response response = commonFunctions.addBootcampPOSTCall(bootcampParameters, returnJSONBody("bootcamp"));
        log.info("\n\n=========================== API POST call completed successfully and HTTP Status Code of 201 validated ===========================");

        String bootcampId = commonFunctions.returnStringValueInResponse(response, "data._id");
        log.info("bootcamp created with ID: " + bootcampId);
        bootcampParameters.setBootcampId(bootcampId);
        bootcampParameters.setComparisonParameter("Bootcamp Name");
        bootcampParameters.setJsonElement("data.name");
        commonFunctions.validateBootcampname(bootcampParameters, response, bootcampQualifier + " BOOTCAMP IN POST CALL API RESPONSE");
    }

    public void getSinglebootcamp(BootcampParameters bootcampParameters) {

        log.info("\n\n=========================== Issuing API GET call to retrieve new bootcamp  ============================");

        Response response = commonFunctions.getSingleBootcampGETCall(bootcampParameters);
        log.info("\n\n=========================== API GET call to retrieve new bootcamp completed successfully and HTTP Status Code of 200 validated ===========================");

        log.info("\n+++++++++++++++++++++++++++++++ Comparing bootcamp parameter post update +++++++++++++++++++++++++++++++ ");

    }

    public void updatebootcamp(BootcampParameters bootcampParameters) {

        log.info("\n\n=========================== Issuing API PUT call to update bootcamp  ============================");

        Response response = commonFunctions.updateBootcampPUTCall(bootcampParameters, returnJSONBody("bootcampupdate"));
        log.info("\n\n=========================== API PUT call completed successfully and HTTP Status Code validated ===========================");

        log.info("\n+++++++++++++++++++++++++++++++ Comparing bootcamp parameter post update +++++++++++++++++++++++++++++++ ");
        bootcampParameters.setComparisonParameter("Bootcamp Housing Status");
    }

    public void deletebootcamp(BootcampParameters bootcampParameters, int statusCode, String result) {

        log.info("\n\n=========================== Issuing API DELETE call to delete bootcamp  ============================");
        Response response = commonFunctions.deleteBootcampDELETECall(bootcampParameters, statusCode);
        log.info("\n\n=========================== API DELETE call completed successfully and HTTP Status Code of " + statusCode + " validated ===========================");

        String comparisonParameter = "bootcamp Deletion " + result + "; Success Field Status";
        bootcampParameters.setJsonElement("success");
        bootcampParameters.setComparisonParameter(comparisonParameter);
        if (result.equals("Success")) {
            bootcampParameters.setExpectedValue("true");
        } else {
            bootcampParameters.setExpectedValue("false");
        }
        log.info("\n\n+++++++++++++++++++++++++++++++++ COMPARING " + comparisonParameter.toUpperCase() + " FOR SINGLE bootcamp +++++++++++++++++++++++++++++++++");
        String bootcampJSONValueInResponse = commonFunctions.returnStringValueInResponse(response, "success");
        log.info("API deletion failure success field status -  Expected: " + bootcampParameters.getExpectedValue() + "; In response: " + bootcampJSONValueInResponse);
        bootcampParameters.setResponseValue(bootcampJSONValueInResponse);
        commonFunctions.compareActualResponse(bootcampParameters);
        log.info("+++++++++++++++++++++++++++++++++++++ " + comparisonParameter.toUpperCase() + " FOR SINGLE bootcamp COMPLETED +++++++++++++++++++++++++++++++++++++");

        if (!result.equals("Success")) {
            comparisonParameter = "bootcamp Deletion Failure Error Field Status";
            bootcampParameters.setJsonElement("error");
            bootcampParameters.setComparisonParameter(comparisonParameter);
            bootcampParameters.setExpectedValue("Bootcamp not found with id of " + bootcampParameters.getBootcampId());
            log.info("\n\n+++++++++++++++++++++++++++++++++ COMPARING " + comparisonParameter.toUpperCase() + " FOR SINGLE bootcamp +++++++++++++++++++++++++++++++++");
            bootcampJSONValueInResponse = commonFunctions.returnStringValueInResponse(response, "error");
            log.info("API deletion failure error field status -  Expected: " + bootcampParameters.getExpectedValue() + "; In response: " + bootcampJSONValueInResponse);
            bootcampParameters.setResponseValue(bootcampJSONValueInResponse);
            commonFunctions.compareActualResponse(bootcampParameters);
            log.info("+++++++++++++++++++++++++++++++++++++ " + comparisonParameter.toUpperCase() + " FOR SINGLE bootcamp COMPLETED +++++++++++++++++++++++++++++++++++++");
        }
    }

    public void validateBootcampElement(BootcampParameters bootcampParameters, String bootcampQualifier, String bootcampElement) {

        switch (bootcampElement) {
            case "NameNew":
                bootcampParameters.setExpectedValue("Test Bootcamp");
                bootcampParameters.setJsonElement("data.name");
                break;
            case "HousingOriginal":
                bootcampParameters.setExpectedValue("false");
                bootcampParameters.setJsonElement("data.housing");
                break;
            case "HousingUpdated":
                bootcampParameters.setExpectedValue("true");
                bootcampParameters.setJsonElement("data.housing");
                break;
            default:
                break;
        }

        log.info("\n\n=========================== Issuing API GET call to retrieve single bootcamp  ============================");
        Response response = commonFunctions.getSingleBootcampGETCall(bootcampParameters);
        log.info("\n\n=========================== API GET call completed successfully and HTTP Status Code of 200 validated ===========================");
        commonFunctions.validateBootcampname(bootcampParameters, response, bootcampQualifier + " BOOTCAMP IN GET CALL API RESPONSE");
    }

    public JSONObject returnJSONBody(String filename) {

        JSONParser jsonParser = new JSONParser();
        JSONObject fullObject = new JSONObject();

        try (FileReader reader = new FileReader(System.getProperty("user.dir") + "/src/test/resources/staticjsonfiles/" + filename + ".json")) {
            Object obj = jsonParser.parse(reader);
            fullObject = (JSONObject) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return fullObject;

    }

}
