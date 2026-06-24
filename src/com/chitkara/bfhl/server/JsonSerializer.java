package com.chitkara.bfhl.server;

import com.chitkara.bfhl.dto.BfhlResponse;

import java.util.List;

/**
 * Minimal JSON serializer — no external libraries required.
 */
public class JsonSerializer {

    public static String toJson(BfhlResponse r) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        appendBool(sb,   "is_success",         r.isSuccess());         sb.append(",");
        appendStr(sb,    "user_id",             r.getUserId());         sb.append(",");
        appendStr(sb,    "email",               r.getEmail());          sb.append(",");
        appendStr(sb,    "roll_number",         r.getRollNumber());     sb.append(",");
        appendList(sb,   "odd_numbers",         r.getOddNumbers());     sb.append(",");
        appendList(sb,   "even_numbers",        r.getEvenNumbers());    sb.append(",");
        appendList(sb,   "alphabets",           r.getAlphabets());      sb.append(",");
        appendList(sb,   "special_characters",  r.getSpecialCharacters()); sb.append(",");
        appendStr(sb,    "sum",                 r.getSum());            sb.append(",");
        appendStr(sb,    "concat_string",       r.getConcatString());
        sb.append("}");
        return sb.toString();
    }

    public static String errorJson(String message) {
        return "{\"is_success\":false,\"message\":" + quoteStr(message) + "}";
    }

    // ── internals ────────────────────────────────────────────────────────────

    private static void appendBool(StringBuilder sb, String key, boolean val) {
        sb.append(quoteStr(key)).append(":").append(val);
    }

    private static void appendStr(StringBuilder sb, String key, String val) {
        sb.append(quoteStr(key)).append(":").append(quoteStr(val));
    }

    private static void appendList(StringBuilder sb, String key, List<String> list) {
        sb.append(quoteStr(key)).append(":[");
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(quoteStr(list.get(i)));
            }
        }
        sb.append("]");
    }

    private static String quoteStr(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t") + "\"";
    }
}
