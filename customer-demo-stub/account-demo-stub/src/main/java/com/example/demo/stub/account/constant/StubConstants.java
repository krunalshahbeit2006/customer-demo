package com.example.demo.stub.account.constant;

public final class StubConstants {

    private StubConstants() {

    }

    public enum StubMappingSource {
        JAVA("java"),
        JSON("json");

        private final String source;

        StubMappingSource(String source) {
            this.source = source;
        }

        public String getSource() {
            return this.source;
        }
    }
}
