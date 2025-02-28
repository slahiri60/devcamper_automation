package com.flowsoft.bootcampcrudtests;

import com.flowsoft.commonartifacts.CommonFunctions;
import com.flowsoft.commonartifacts.BootcampParameters;
import com.flowsoft.bootcampcrud.BootcampCRUDFunctions;
import lombok.extern.java.Log;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Log
public class BootcampCRUDTests {

    static BootcampParameters bootcampParameters = new BootcampParameters();
    static BootcampCRUDFunctions bootcampCRUDFunctions = new BootcampCRUDFunctions();
    static CommonFunctions commonFunctions = new CommonFunctions();

    @BeforeClass
    @Parameters({"baseURI"})
    public void init(String baseURI) {
        bootcampParameters.setBaseURI(baseURI);
    }

    @Test(priority=1)
    public void retrieveAllBootcamps() {
        log.info("\n\n====================================================================================");
        log.info("*********************************************** TEST001 - Test to retrieve all Bootcamps ***********************************************");
        bootcampCRUDFunctions.getAllBootcamps(bootcampParameters);
    }

    @Test(priority=2)
    public void createNewBootcamp() {
        log.info("\n\n====================================================================================");
        log.info("*********************************************** TEST002 - Test to create new Bootcamp ***********************************************");
        bootcampParameters.setExpectedValue("Test Bootcamp");
        bootcampCRUDFunctions.createNewbootcamp(bootcampParameters, "NEW");
        bootcampCRUDFunctions.validateBootcampElement(bootcampParameters, "NEW", "NameNew");
    }

    @Test(priority=3, dependsOnMethods = {"createNewBootcamp"})
    public void retrieveSingleBootcamp() {
        log.info("\n\n====================================================================================");
        log.info("*********************************************** TEST003 - Test to retrieve single Bootcamp ***********************************************");
        bootcampParameters.setComparisonParameter("Bootcamp Name");
        bootcampCRUDFunctions.validateBootcampElement(bootcampParameters, "NEW", "NameNew");
    }

    @Test(priority=4, dependsOnMethods = {"createNewBootcamp"})
    public void updateNewBootcamp() {
        log.info("\n\n====================================================================================");
        log.info("*********************************************** TEST004 - Test to update new Bootcamp ***********************************************");
        bootcampParameters.setComparisonParameter("Bootcamp Housing Element");
        bootcampCRUDFunctions.validateBootcampElement(bootcampParameters, "EXISTING", "HousingOriginal");
        bootcampCRUDFunctions.updatebootcamp(bootcampParameters);
        bootcampCRUDFunctions.validateBootcampElement(bootcampParameters, "EXISTING", "HousingUpdated");
    }

    @Test(priority=5, dependsOnMethods = {"createNewBootcamp"})
    public void deleteNewBootcamp() {
        log.info("\n\n====================================================================================");
        log.info("*********************************************** TEST005 - Test to delete new Bootcamp ***********************************************");
        bootcampCRUDFunctions.deletebootcamp(bootcampParameters, 200, "Success");
        bootcampCRUDFunctions.deletebootcamp(bootcampParameters, 404, "Failure");
    }
}
