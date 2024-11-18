package com.sparta.doguin.domain.report;

public enum ReportType {

    REPORT_NOT_CONFIRMED("Not Confirmed"),
    REPORT_ACCEPT("Accept"),
    REPORT_REJECT("Reject");

    private String type;

    ReportType(String type) {
        this.type = type;
    }
}
