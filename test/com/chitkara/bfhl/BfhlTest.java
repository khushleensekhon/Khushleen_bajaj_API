package com.chitkara.bfhl;

import com.chitkara.bfhl.dto.BfhlRequest;
import com.chitkara.bfhl.dto.BfhlResponse;
import com.chitkara.bfhl.server.JsonParser;
import com.chitkara.bfhl.server.JsonSerializer;
import com.chitkara.bfhl.service.BfhlServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 * Self-contained test runner — no JUnit/external jars needed.
 * Exit code 0 = all pass, 1 = any failure.
 */
public class BfhlTest {

    private static int passed = 0;
    private static int failed = 0;

    private static final BfhlServiceImpl SERVICE =
            new BfhlServiceImpl("john doe", "17091999", "john@xyz.com", "ABCD123");

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════");
        System.out.println("  BFHL Test Suite");
        System.out.println("═══════════════════════════════════════════");

        // ── Service logic tests ──────────────────────────────────────────────
        testExampleA();
        testExampleB();
        testExampleC();
        testEmptyData();
        testOnlyNumbers();
        testOnlySpecialChars();
        testUserIdFormat();
        testNumbersReturnedAsStrings();

        // ── JSON layer tests ─────────────────────────────────────────────────
        testJsonParserBasic();
        testJsonParserEmpty();
        testJsonParserMissingDataField();
        testJsonSerializerRoundTrip();

        // ── Summary ──────────────────────────────────────────────────────────
        System.out.println("═══════════════════════════════════════════");
        System.out.printf("  Results: %d passed, %d failed%n", passed, failed);
        System.out.println("═══════════════════════════════════════════");
        if (failed > 0) System.exit(1);
    }

    // ── Test helpers ─────────────────────────────────────────────────────────

    private static BfhlResponse run(String... items) {
        BfhlRequest req = new BfhlRequest();
        req.setData(Arrays.asList(items));
        return SERVICE.processData(req);
    }

    private static void assertEquals(String testName, Object expected, Object actual) {
        if (expected == null ? actual == null : expected.equals(actual)) {
            System.out.println("  ✓ " + testName);
            passed++;
        } else {
            System.out.println("  ✗ " + testName);
            System.out.println("      expected: " + expected);
            System.out.println("      actual  : " + actual);
            failed++;
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        assertEquals(testName, true, condition);
    }

    // ── Service tests ─────────────────────────────────────────────────────────

    static void testExampleA() {
        System.out.println("\n[Example A]  data=[a, 1, 334, 4, R, $]");
        BfhlResponse r = run("a", "1", "334", "4", "R", "$");
        assertEquals("is_success",         true,                       r.isSuccess());
        assertEquals("user_id",            "john_doe_17091999",        r.getUserId());
        assertEquals("email",              "john@xyz.com",             r.getEmail());
        assertEquals("roll_number",        "ABCD123",                  r.getRollNumber());
        assertEquals("odd_numbers",        List.of("1"),               r.getOddNumbers());
        assertEquals("even_numbers",       List.of("334", "4"),        r.getEvenNumbers());
        assertEquals("alphabets",          List.of("A", "R"),          r.getAlphabets());
        assertEquals("special_characters", List.of("$"),               r.getSpecialCharacters());
        assertEquals("sum",                "339",                      r.getSum());
        assertEquals("concat_string",      "Ra",                       r.getConcatString());
    }

    static void testExampleB() {
        System.out.println("\n[Example B]  data=[2, a, y, 4, &, -, *, 5, 92, b]");
        BfhlResponse r = run("2", "a", "y", "4", "&", "-", "*", "5", "92", "b");
        assertEquals("odd_numbers",        List.of("5"),               r.getOddNumbers());
        assertEquals("even_numbers",       List.of("2", "4", "92"),    r.getEvenNumbers());
        assertEquals("alphabets",          List.of("A", "Y", "B"),     r.getAlphabets());
        assertEquals("sum",                "103",                      r.getSum());
        assertEquals("concat_string",      "ByA",                      r.getConcatString());
        assertTrue("special_chars contains &", r.getSpecialCharacters().contains("&"));
        assertTrue("special_chars contains -", r.getSpecialCharacters().contains("-"));
        assertTrue("special_chars contains *", r.getSpecialCharacters().contains("*"));
    }

    static void testExampleC() {
        System.out.println("\n[Example C]  data=[A, ABCD, DOE]");
        BfhlResponse r = run("A", "ABCD", "DOE");
        assertEquals("odd_numbers",        List.of(),                        r.getOddNumbers());
        assertEquals("even_numbers",       List.of(),                        r.getEvenNumbers());
        assertEquals("alphabets",          List.of("A", "ABCD", "DOE"),      r.getAlphabets());
        assertEquals("special_characters", List.of(),                        r.getSpecialCharacters());
        assertEquals("sum",                "0",                              r.getSum());
        assertEquals("concat_string",      "EoDdCbAa",                       r.getConcatString());
    }

    static void testEmptyData() {
        System.out.println("\n[Edge] Empty data array");
        BfhlResponse r = run();
        assertEquals("sum",            "0", r.getSum());
        assertEquals("concat_string",  "",  r.getConcatString());
        assertTrue("odd_numbers empty",        r.getOddNumbers().isEmpty());
        assertTrue("even_numbers empty",       r.getEvenNumbers().isEmpty());
        assertTrue("alphabets empty",          r.getAlphabets().isEmpty());
        assertTrue("special_characters empty", r.getSpecialCharacters().isEmpty());
    }

    static void testOnlyNumbers() {
        System.out.println("\n[Edge] Only numbers");
        BfhlResponse r = run("2", "3", "10", "7");
        assertEquals("odd_numbers",  List.of("3", "7"),    r.getOddNumbers());
        assertEquals("even_numbers", List.of("2", "10"),   r.getEvenNumbers());
        assertEquals("sum",          "22",                 r.getSum());
        assertTrue("concat_string empty", r.getConcatString().isEmpty());
    }

    static void testOnlySpecialChars() {
        System.out.println("\n[Edge] Only special characters");
        BfhlResponse r = run("@", "#", "!");
        assertEquals("sum", "0", r.getSum());
        assertTrue("specials size 3", r.getSpecialCharacters().size() == 3);
        assertTrue("concat_string empty", r.getConcatString().isEmpty());
    }

    static void testUserIdFormat() {
        System.out.println("\n[Edge] user_id format");
        BfhlResponse r = run("1");
        assertEquals("user_id", "john_doe_17091999", r.getUserId());
        assertTrue("lowercase", r.getUserId().equals(r.getUserId().toLowerCase()));
    }

    static void testNumbersReturnedAsStrings() {
        System.out.println("\n[Edge] Numbers returned as strings");
        BfhlResponse r = run("1", "2");
        assertEquals("sum is string '3'", "3", r.getSum());
        assertEquals("odd as string",  List.of("1"), r.getOddNumbers());
        assertEquals("even as string", List.of("2"), r.getEvenNumbers());
    }

    // ── JSON layer tests ──────────────────────────────────────────────────────

    static void testJsonParserBasic() {
        System.out.println("\n[JSON] Parser — basic input");
        try {
            BfhlRequest req = JsonParser.parseRequest("{\"data\":[\"a\",\"1\",\"334\"]}");
            assertEquals("parsed size", 3, req.getData().size());
            assertEquals("first item",  "a",   req.getData().get(0));
            assertEquals("second item", "1",   req.getData().get(1));
            assertEquals("third item",  "334", req.getData().get(2));
        } catch (Exception e) {
            assertEquals("JSON parse basic", "no exception", e.getMessage());
        }
    }

    static void testJsonParserEmpty() {
        System.out.println("\n[JSON] Parser — empty array");
        try {
            BfhlRequest req = JsonParser.parseRequest("{\"data\":[]}");
            assertTrue("empty list", req.getData().isEmpty());
        } catch (Exception e) {
            assertEquals("JSON parse empty", "no exception", e.getMessage());
        }
    }

    static void testJsonParserMissingDataField() {
        System.out.println("\n[JSON] Parser — missing data field throws");
        try {
            JsonParser.parseRequest("{\"foo\":[\"a\"]}");
            assertEquals("should have thrown", "exception", "none thrown");
        } catch (IllegalArgumentException e) {
            assertTrue("correct exception", e.getMessage().contains("data"));
        }
    }

    static void testJsonSerializerRoundTrip() {
        System.out.println("\n[JSON] Serializer — output contains required keys");
        BfhlResponse r = run("a", "1", "$");
        String json = JsonSerializer.toJson(r);
        assertTrue("has is_success",         json.contains("\"is_success\""));
        assertTrue("has user_id",            json.contains("\"user_id\""));
        assertTrue("has email",              json.contains("\"email\""));
        assertTrue("has roll_number",        json.contains("\"roll_number\""));
        assertTrue("has odd_numbers",        json.contains("\"odd_numbers\""));
        assertTrue("has even_numbers",       json.contains("\"even_numbers\""));
        assertTrue("has alphabets",          json.contains("\"alphabets\""));
        assertTrue("has special_characters", json.contains("\"special_characters\""));
        assertTrue("has sum",                json.contains("\"sum\""));
        assertTrue("has concat_string",      json.contains("\"concat_string\""));
    }
}
