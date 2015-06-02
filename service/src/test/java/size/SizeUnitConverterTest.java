package size;

import com.softserve.edu.documents.size.SizeUnit;
import com.softserve.edu.documents.size.SizeUnitConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

/**
 * Tests for size units.
 */
@RunWith(value = Parameterized.class)
public class SizeUnitConverterTest {
    static final Double DELTA = 0.01;

    private SizeUnit sourceSizeUnit;
    private double sourceValue;
    private SizeUnit targetSizeUnit;
    private double expectedValue;

    public SizeUnitConverterTest(SizeUnit sourceSizeUnit, double sourceValue,
                                 SizeUnit targetSizeUnit, double expectedValue) {
        this.sourceSizeUnit = sourceSizeUnit;
        this.sourceValue = sourceValue;
        this.targetSizeUnit = targetSizeUnit;
        this.expectedValue = expectedValue;
    }

    @Parameterized.Parameters(name="testData")
    public static Iterable<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                {SizeUnit.CM, 10, SizeUnit.PT, 283.46},
                {SizeUnit.CM, 21, SizeUnit.PT, 595.27},
                {SizeUnit.CM, 21.9, SizeUnit.PT, 620.78},
                {SizeUnit.CM, 0, SizeUnit.PT, 0},
                {SizeUnit.CM, -10, SizeUnit.PT, -283.46},

                {SizeUnit.CM, 10, SizeUnit.TWIP, 5669.29},
                {SizeUnit.CM, 21, SizeUnit.TWIP, 11905.51},
                {SizeUnit.CM, 21.9, SizeUnit.TWIP, 12415.74},
                {SizeUnit.CM, 0, SizeUnit.TWIP, 0},
                {SizeUnit.CM, -10, SizeUnit.TWIP, -5669.29},

                {SizeUnit.PT, 200, SizeUnit.TWIP, 4000},
                {SizeUnit.PT, 11.1, SizeUnit.TWIP, 222},
                {SizeUnit.PT, -0.5, SizeUnit.TWIP, -10},
        });
    }

    @Test
    public void processTestData() {
        double actualValue = SizeUnitConverter.convert(sourceValue,
                sourceSizeUnit, targetSizeUnit);
        Assert.assertEquals(expectedValue, actualValue, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSourceSizeUnitTest() {
        SizeUnitConverter.convert(10, null, SizeUnit.CM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTargetSizeUnitTest() {
        SizeUnitConverter.convert(10, SizeUnit.CM, null);
    }
}
