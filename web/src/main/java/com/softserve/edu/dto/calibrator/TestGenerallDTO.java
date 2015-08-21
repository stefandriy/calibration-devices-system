package com.softserve.edu.dto.calibrator;

import com.softserve.edu.dto.CalibrationTestDTO;
import com.softserve.edu.dto.CalibrationTestDataDTO;

import java.util.List;

/**
 * Created by MAX on 21.08.2015.
 */
public class TestGenerallDTO {
    private List<CalibrationTestDataDTO> smallForm;
    private CalibrationTestDTO TestFormData;

    public TestGenerallDTO() {

    }

    public List<CalibrationTestDataDTO> getSmallForm() {
        return smallForm;
    }

    public void setSmallForm(List<CalibrationTestDataDTO> smallForm) {
        this.smallForm = smallForm;
    }

    public CalibrationTestDTO getTestFormData() {
        return TestFormData;
    }

    public void setTestFormData(CalibrationTestDTO testFormData) {
        TestFormData = testFormData;
    }
}
