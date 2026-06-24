package com.chitkara.bfhl.dto;

import java.util.List;

public class BfhlResponse {
    private boolean isSuccess;
    private String userId;
    private String email;
    private String rollNumber;
    private List<String> oddNumbers;
    private List<String> evenNumbers;
    private List<String> alphabets;
    private List<String> specialCharacters;
    private String sum;
    private String concatString;

    // Builder pattern
    private BfhlResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final BfhlResponse r = new BfhlResponse();
        public Builder isSuccess(boolean v)                    { r.isSuccess = v; return this; }
        public Builder userId(String v)                        { r.userId = v; return this; }
        public Builder email(String v)                         { r.email = v; return this; }
        public Builder rollNumber(String v)                    { r.rollNumber = v; return this; }
        public Builder oddNumbers(List<String> v)              { r.oddNumbers = v; return this; }
        public Builder evenNumbers(List<String> v)             { r.evenNumbers = v; return this; }
        public Builder alphabets(List<String> v)               { r.alphabets = v; return this; }
        public Builder specialCharacters(List<String> v)       { r.specialCharacters = v; return this; }
        public Builder sum(String v)                           { r.sum = v; return this; }
        public Builder concatString(String v)                  { r.concatString = v; return this; }
        public BfhlResponse build()                            { return r; }
    }

    public boolean isSuccess()                  { return isSuccess; }
    public String getUserId()                   { return userId; }
    public String getEmail()                    { return email; }
    public String getRollNumber()               { return rollNumber; }
    public List<String> getOddNumbers()         { return oddNumbers; }
    public List<String> getEvenNumbers()        { return evenNumbers; }
    public List<String> getAlphabets()          { return alphabets; }
    public List<String> getSpecialCharacters()  { return specialCharacters; }
    public String getSum()                      { return sum; }
    public String getConcatString()             { return concatString; }
}
