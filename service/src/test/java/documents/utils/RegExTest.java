package documents.utils;

import com.softserve.edu.documents.utils.FormattingTokens;
import com.softserve.edu.documents.utils.RegEx;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tests for regular expressions used in the application.
 */
public class RegExTest {
    @Test
    public void findAllColumns() {
        String testData = "№ $VERIFICATION_CERTIFICATE_NUMBER  some text" +
                "$EFF_DATE";
        List<String> expectedResult = Arrays.asList("$VERIFICATION_CERTIFICATE_NUMBER",
                "$EFF_DATE");

        String regEx = RegEx.FIND_ALL_COLUMNS.toString();

        Matcher matcher = Pattern.compile(regEx).matcher(testData);
        List<String> allMatches = new ArrayList<>();

        while (matcher.find()) {
            allMatches.add(matcher.group());
        }

        Assert.assertEquals(expectedResult, allMatches);
    }

    @Test
    public void findAllFormattingTokens() {
        String testData = FormattingTokens.COLUMN.toString() +
                FormattingTokens.RIGHT_SIDE +
                FormattingTokens.COLUMN +
                FormattingTokens.RIGHT_SIDE +
                FormattingTokens.COLUMN;

        String regEx = RegEx.FIND_ALL_FORMATTING_TOKENS.toString();

        Matcher matcher = Pattern.compile(regEx).matcher(testData);
        List<String> allMatches = new ArrayList<>();

        while (matcher.find()) {
            allMatches.add(matcher.group());
        }

        Assert.assertTrue(5 == allMatches.size());
    }
}
