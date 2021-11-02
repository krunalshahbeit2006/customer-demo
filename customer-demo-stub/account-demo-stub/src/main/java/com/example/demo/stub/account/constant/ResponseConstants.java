package com.example.demo.stub.account.constant;

import org.springframework.http.HttpHeaders;

public final class ResponseConstants {

    private ResponseConstants() {

    }

    public enum ResponseHeaderParam {
        CONTENT_TYPE(HttpHeaders.CONTENT_TYPE);

        private final String key;

        ResponseHeaderParam(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }
}
