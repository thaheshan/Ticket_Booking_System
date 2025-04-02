package com.thaheshan.Booking.Logging;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class ThreadLog {
    private final String vipcustomerlog;
    private final String vendorLog;
    private final String customerLog;

    public ThreadLog(String vendorLog, String customerLog, String vipcustomerlog) {
        this.vendorLog = vendorLog;
        this.customerLog = customerLog;
        this.vipcustomerlog = vipcustomerlog;
    }
}
