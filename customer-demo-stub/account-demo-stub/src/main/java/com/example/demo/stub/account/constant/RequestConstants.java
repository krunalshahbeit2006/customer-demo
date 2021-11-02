package com.example.demo.stub.account.constant;

public final class RequestConstants {

    public static final String UNIQUE_ID_PATTERN = "^[A-Za-z0-9]{1,10}$";


    private RequestConstants() {

    }

    public enum RequestPriority {

        PRIORITY_MAPPING(1),
        PRIORITY_MAPPING_PROXY(2);

        private final int priority;

        RequestPriority(final int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

    }

    public enum RequestHeaderParam {
        SCENARIO("x-scenario");

        private final String header;

        RequestHeaderParam(String header) {
            this.header = header;
        }

        public String getHeader() {
            return this.header;
        }
    }

    public enum RequestQueryParam {
        CUSTOMER_ID("customerId");

        private final String param;

        RequestQueryParam(String param) {
            this.param = param;
        }

        public String getParam() {
            return this.param;
        }
    }
}
