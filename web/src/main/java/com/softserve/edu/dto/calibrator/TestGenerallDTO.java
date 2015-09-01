package com.softserve.edu.dto.calibrator;

import com.softserve.edu.dto.CalibrationTestDTO;
import com.softserve.edu.dto.CalibrationTestDataDTO;

import java.util.List;

public class TestGenerallDTO {
    private List<CalibrationTestDataDTO> smallForm;
    private CalibrationTestDTO testForm;

    public TestGenerallDTO() {

    }

    public List<CalibrationTestDataDTO> getSmallForm() {
        return smallForm;
    }

    public void setSmallForm(List<CalibrationTestDataDTO> smallForm) {
        this.smallForm = smallForm;
    }

    public CalibrationTestDTO getTestForm() {
        return testForm;
    }

    public void setTestForm(CalibrationTestDTO testForm) {
        this.testForm = testForm;
    }
}
