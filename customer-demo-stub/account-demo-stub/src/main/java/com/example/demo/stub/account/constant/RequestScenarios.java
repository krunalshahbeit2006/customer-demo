package com.example.demo.stub.account.constant;

public final class RequestScenarios {

    public enum SuccessScenario {
        NO_RECORD("OK-NO_RECORD", "2xx/01/00/output.json"),
        SINGLE_RECORD("OK-SINGLE_RECORD", "2xx/01/01/output.json"),
        RECORD_IBAN_INVALID("OK-RECORD_IBAN_INVALID", "2xx/01/02/output.json");

        private final String scenario;
        private final String filePath;

        SuccessScenario(final String scenario, final String filePath) {
            this.scenario = scenario;
            this.filePath = filePath;
        }

        public String getScenario() {
            return this.scenario;
        }

        public String getFilePath() {
            return this.filePath;
        }
    }

    public enum ClientErrorScenario {
        INPUT("BAD_REQUEST-INPUT", "The request is invalid.");

        private final String scenario;
        private final String detail;

        ClientErrorScenario(final String scenario, final String detail) {
            this.scenario = scenario;
            this.detail = detail;
        }

        public String getScenario() {
            return this.scenario;
        }

        public String getDetail() {
            return this.detail;
        }
    }
}
