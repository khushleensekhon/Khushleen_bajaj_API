package com.chitkara.bfhl.service;

import com.chitkara.bfhl.dto.BfhlRequest;
import com.chitkara.bfhl.dto.BfhlResponse;

import java.util.ArrayList;
import java.util.List;

public class BfhlServiceImpl implements BfhlService {

    private final String fullName;
    private final String dob;
    private final String email;
    private final String rollNumber;

    public BfhlServiceImpl(String fullName, String dob, String email, String rollNumber) {
        this.fullName   = fullName;
        this.dob        = dob;
        this.email      = email;
        this.rollNumber = rollNumber;
    }

    @Override
    public BfhlResponse processData(BfhlRequest request) {
        List<String> data = request.getData();

        List<String> oddNumbers   = new ArrayList<>();
        List<String> evenNumbers  = new ArrayList<>();
        List<String> alphabets    = new ArrayList<>();
        List<String> specialChars = new ArrayList<>();
        long sum = 0;
        StringBuilder allAlphaChars = new StringBuilder();

        for (String item : data) {
            if (isNumeric(item)) {
                long val = Long.parseLong(item);
                sum += val;
                if (val % 2 == 0) evenNumbers.add(item);
                else              oddNumbers.add(item);

            } else if (isAlphabetic(item)) {
                alphabets.add(item.toUpperCase());
                allAlphaChars.append(item);

            } else if (item.length() == 1) {
                specialChars.add(item);

            } else {
                // mixed token
                StringBuilder letters = new StringBuilder();
                for (char c : item.toCharArray()) {
                    if (Character.isLetter(c))      letters.append(c);
                    else if (!Character.isDigit(c)) specialChars.add(String.valueOf(c));
                }
                if (!letters.isEmpty()) {
                    alphabets.add(letters.toString().toUpperCase());
                    allAlphaChars.append(letters);
                }
            }
        }

        return BfhlResponse.builder()
                .isSuccess(true)
                .userId(buildUserId())
                .email(email)
                .rollNumber(rollNumber)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialChars)
                .sum(String.valueOf(sum))
                .concatString(buildConcatString(allAlphaChars.toString()))
                .build();
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private String buildUserId() {
        return fullName.toLowerCase().replace(" ", "_") + "_" + dob;
    }

    private boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        try { Long.parseLong(s); return true; }
        catch (NumberFormatException e) { return false; }
    }

    private boolean isAlphabetic(String s) {
        if (s == null || s.isEmpty()) return false;
        for (char c : s.toCharArray()) if (!Character.isLetter(c)) return false;
        return true;
    }

    /**
     * Reverse all collected alpha chars, then alternate caps (UPPER at even index).
     * "ayb" → "bya" → "ByA"
     * "AABCDDOE" → "EODDCBAA" → "EoDdCbAa"
     */
    private String buildConcatString(String allChars) {
        if (allChars.isEmpty()) return "";
        String reversed = new StringBuilder(allChars).reverse().toString();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            result.append(i % 2 == 0 ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }
        return result.toString();
    }
}
