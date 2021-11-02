package com.example.demo.services.customer.web.constant;

public final class AppResponseMessage {

    private AppResponseMessage() {

    }

    public enum ErrorType {
        APPLICATION_REQUEST_FIELD_INVALID("Application request field has invalid value", "The value of one or more fields from the request is invalid (empty or incorrect data type)."),
        DATABASE_NO_RECORD_FOUND("Database no record(s) found", "No records were present and therefore nothing has been fetched."),
        DATABASE_RECORD_NOT_FOUND("Database record not found", "Record with the provided input not found."),
        DATABASE_RECORD_PERSISTENCE_FAILED("Database record(s) persistence constraint violated", "Database record persistence operation has violated one or more constraint(s)."),
        DATABASE_CONNECTION_CLOSED_ERROR("Database connection is closed", "Application could not get the database connection or connection has been interrupted."),
        DATABASE_TRANSACTION_NOT_CREATED("Database transaction could not created", "Application could not create the database transaction."),
        DATABASE_QUERY_NOT_COMPILED("Database query is not compiled", "Application could not compile a query object."),
        DATABASE_QUERY_OPERATION_TIMEOUT("Database query operation is timed out", "Application database query operation has timed out."),
        DATABASE_RECORD_LOCKING_FAILED("Database locking is failed", "Application could not get the lock on the database."),
        DATABASE_RECORD_RETRIEVAL_FAILED("Database record(s) retrieval is failed", "Application database query record(s) retrieval operation has failed."),
        DATABASE_RECORD_UNKNOWN_ACCESS("Database record(s) unknown access", "Application could not understand the database access operation."),
        WEBSERVICE_NO_RECORD_FOUND("Webservice no record(s) found", "No records were present and therefore nothing has been fetched."),
        WEBSERVICE_RECORD_NOT_FOUND("Webservice record not found", "Record with the provided input not found."),
        WEBSERVICE_RECORD_FIELD_INVALID("Webservice record field vale invalid", "Record with the provided input has invalid field value."),
        WEBSERVICE_SERVER_CONNECTION_ERROR("Webservice server connection error", "Webservice could not connect server."),
        INTERNAL_SERVER_UNKNOWN_ERROR("Application Generic unknown failure", "Application could not handle failure.");

        private final String title;
        private final String detail;

        ErrorType(String title, String detail) {
            this.title = title;
            this.detail = detail;
        }

        public String title() {
            return this.title;
        }

        public String detail() {
            return this.detail;
        }
    }

}
