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

        String[] bootcampNames = new String[] {"Devworks Bootcamp","ModernTech Bootcamp","Codemasters Bootcamp","Devcentral Bootcamp"};

        bootcampParameters.setJsonElement("data.name");
        bootcampParameters.setBootcampCount(4);
        bootcampParameters.setComparisonParameter("Bootcamp Name");

        log.info("\n\n=========================== Issuing API GET call to retrieve all bootcamps  ===========================");
        Response response = commonFunctions.getAllBootcampsGETCall(bootcampParameters);
        log.info("\n\n=========================== API GET call completed successfully and HTTP Status Code of 200 validated ===========================");
        String comparisonParameter = bootcampParameters.getComparisonParameter();
        log.info("\n\n++++++++++++++++++++++++ COMPARING " + comparisonParameter.toUpperCase() + " FOR FIRST FOUR bootcampS ++++++++++++++++++++++++");
        for (int counter=0; counter < bootcampParameters.getBootcampCount(); counter++) {
            log.info("\n------------------------------------ COMPARING " + comparisonParameter + " " + (counter+1) + " ------------------------------------");
            String bootcampJSONValueExpected = bootcampNames[counter];
            String bootcampJSONValueResponse = commonFunctions.returnStringValueInResponse(response, "data[" + counter + "]." +
                    commonFunctions.returnSecondComparisonParameter(bootcampParameters.getComparisonParameter()));
            log.info(comparisonParameter + " Expected: " + bootcampJSONValueExpected + "; In response: " + bootcampJSONValueResponse);
            bootcampParameters.setExpectedValue(bootcampJSONValueExpected);
            bootcampParameters.setResponseValue(bootcampJSONValueResponse);
            commonFunctions.compareActualResponse(bootcampParameters);
            log.info("------------------------------------ " + comparisonParameter + " " + (counter+1) + " COMPARISON COMPLETED ------------------------------------");
            if(counter == 0) {
                bootcampParameters.setBootcampId(commonFunctions.returnStringValueInResponse(response, "data[" + counter + "]._id"));
            }
        }
        log.info("+++++++++++++++++++++++++++++ " + comparisonParameter.toUpperCase() + " FOR FIRST FOUR bootcampS COMPLETED +++++++++++++++++++++++++++++");
    }

    public void validateBootcampElement(BootcampParameters bootcampParameters, String bootcampQualifier, String bootcampElement) {

        switch (bootcampElement) {
            case "NameExisting":
                bootcampParameters.setExpectedValue("Devworks Bootcamp");
                bootcampParameters.setJsonElement("data.name");
                break;
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

    public void createNewbootcamp(BootcampParameters bootcampParameters, String bootcampQualifier) {

        JSONParser jsonParser = new JSONParser();
        JSONObject fullObject;

        try (FileReader reader = new FileReader(System.getProperty("user.dir") + "/src/test/resources/staticjsonfiles/bootcamp.json")) {

            log.info("\n\n=========================== Issuing API POST call to create new bootcamp  ============================");
            Object obj = jsonParser.parse(reader);
            fullObject = (JSONObject) obj;

            Response response = commonFunctions.addBootcampPOSTCall(bootcampParameters, fullObject);
            log.info("\n\n=========================== API POST call completed successfully and HTTP Status Code of 201 validated ===========================");

            String bootcampId = commonFunctions.returnStringValueInResponse(response, "data._id");
            log.info("bootcamp created with ID: " + bootcampId);
            bootcampParameters.setBootcampId(bootcampId);
            bootcampParameters.setComparisonParameter("Bootcamp Name");
            bootcampParameters.setJsonElement("data.name");
            commonFunctions.validateBootcampname(bootcampParameters, response, bootcampQualifier + " BOOTCAMP IN POST CALL API RESPONSE");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void updatebootcamp(BootcampParameters bootcampParameters) {

        JSONParser jsonParser = new JSONParser();
        JSONObject fullObject;

        try (FileReader reader = new FileReader(System.getProperty("user.dir") + "/src/test/resources/staticjsonfiles/bootcampupdate.json")) {

            log.info("\n\n=========================== Issuing API PUT call to update bootcamp  ============================");
            Object obj = jsonParser.parse(reader);
            fullObject = (JSONObject) obj;

            RestAssured.baseURI = bootcampParameters.getBaseURI();
            Response response = commonFunctions.updateBootcampPUTCall(bootcampParameters, fullObject);
            log.info("\n\n=========================== API PUT call completed successfully and HTTP Status Code validated ===========================");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        log.info("\n+++++++++++++++++++++++++++++++ Comparing bootcamp parameter post update +++++++++++++++++++++++++++++++ ");

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

}
