package com.example.demo.services.customer.util;

import lombok.extern.slf4j.Slf4j;
import com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;

@Slf4j
public final class AppLoggerUtil {

    private static final String LOG_PATTERN_ORIGINAL_ERROR = "Exception = {} | Message = {} | Cause = {}";
    private static final String LOG_PATTERN_ERROR = "Type = {} | Message = {} | Details = {}";


    private AppLoggerUtil() {

    }

    public static void logWarning(final Exception ex, final ErrorType warnStatus) {
        logException(ex);
        logWarnStatus(warnStatus);
    }

    public static void logError(final Exception ex, final ErrorType errorType) {
        logException(ex);
        logErrorStatus(errorType);
    }

    private static void logException(final Exception ex) {
        log.error(LOG_PATTERN_ORIGINAL_ERROR, AppUtil.errorType(ex), ex.getMessage(), ex.getCause());
    }

    private static void logWarnStatus(final ErrorType errorType) {
        log.warn(LOG_PATTERN_ERROR,
                errorType.name(),
                errorType.title(),
                errorType.detail());
    }

    private static void logErrorStatus(final ErrorType errorType) {
        log.error(LOG_PATTERN_ERROR,
                errorType.name(),
                errorType.title(),
                errorType.detail());
    }

}
