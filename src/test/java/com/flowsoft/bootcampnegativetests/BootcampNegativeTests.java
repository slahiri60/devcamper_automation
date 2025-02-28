package com.flowsoft.bootcampnegativetests;

import com.flowsoft.bootcampcrud.BootcampCRUDFunctions;
import com.flowsoft.commonartifacts.BootcampParameters;
import com.flowsoft.commonartifacts.CommonFunctions;
import lombok.extern.java.Log;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Log
public class BootcampNegativeTests {

    static BootcampParameters bootcampParameters = new BootcampParameters();
    static BootcampCRUDFunctions bootcampCRUDFunctions = new BootcampCRUDFunctions();
    static CommonFunctions commonFunctions = new CommonFunctions();

    String originalBootcampId = "628ffe7137adb88bffff7f8f";

    @BeforeClass
    @Parameters({"baseURI"})
    public void init(String baseURI) {
        bootcampParameters.setBaseURI(baseURI);
    }

    @Test(priority=1,enabled=false)
    public void negativeInvalidBootcampIdFormat() {
        log.info("\n\n====================================================================================");
        log.info("*********************************************** TEST001 - Negative Test to validate error for invalid Bootcamp ID format ***********************************************");
        bootcampParameters.setBootcampId(commonFunctions.generateInvalidbootcampIdFormat(originalBootcampId));
        bootcampCRUDFunctions.deletebootcamp(bootcampParameters, 404, "Failure");
        bootcampParameters.setBootcampId(originalBootcampId);
    }

    @Test(priority=2,enabled=false)
    public void negativeNonexistentBootcampId() {
        log.info("\n\n====================================================================================");
        log.info("*********************************************** TEST002 - Negative Test to validate error for non-existent Bootcamp ID ***********************************************");
        bootcampParameters.setBootcampId(commonFunctions.generateNonexistentbootcampId( bootcampParameters.getBootcampId()));
        bootcampCRUDFunctions.deletebootcamp(bootcampParameters, 404, "Failure");
        bootcampParameters.setBootcampId(originalBootcampId);
    }
}
