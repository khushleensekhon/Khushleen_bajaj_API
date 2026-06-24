package com.chitkara.bfhl.server;

import com.chitkara.bfhl.dto.BfhlRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal JSON parser for BfhlRequest — no external libraries.
 * Handles: {"data": ["a", "1", "334"]}
 */
public class JsonParser {

    public static BfhlRequest parseRequest(String json) throws IllegalArgumentException {
        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException("Request body is empty");
        }

        String trimmed = json.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON object");
        }

        // Find "data" array
        int dataIdx = trimmed.indexOf("\"data\"");
        if (dataIdx == -1) {
            throw new IllegalArgumentException("Missing required field: data");
        }

        int arrStart = trimmed.indexOf("[", dataIdx);
        int arrEnd   = trimmed.lastIndexOf("]");
        if (arrStart == -1 || arrEnd == -1 || arrStart > arrEnd) {
            throw new IllegalArgumentException("Field 'data' must be a JSON array");
        }

        String arrContent = trimmed.substring(arrStart + 1, arrEnd).trim();
        List<String> items = new ArrayList<>();

        if (!arrContent.isEmpty()) {
            // Parse quoted strings from the array
            int i = 0;
            while (i < arrContent.length()) {
                if (arrContent.charAt(i) == '"') {
                    int end = findClosingQuote(arrContent, i + 1);
                    if (end == -1) throw new IllegalArgumentException("Unterminated string in array");
                    items.add(unescape(arrContent.substring(i + 1, end)));
                    i = end + 1;
                    // skip comma and whitespace
                    while (i < arrContent.length() && (arrContent.charAt(i) == ',' || arrContent.charAt(i) == ' ')) i++;
                } else {
                    i++;
                }
            }
        }

        BfhlRequest req = new BfhlRequest();
        req.setData(items);
        return req;
    }

    private static int findClosingQuote(String s, int start) {
        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == '\\') { i++; continue; }
            if (s.charAt(i) == '"')  return i;
        }
        return -1;
    }

    private static String unescape(String s) {
        return s.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n",  "\n")
                .replace("\\r",  "\r")
                .replace("\\t",  "\t");
    }
}
