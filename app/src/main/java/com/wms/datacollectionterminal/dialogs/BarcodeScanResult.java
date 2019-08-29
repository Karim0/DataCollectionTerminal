package com.wms.datacollectionterminal.dialogs;

public interface BarcodeScanResult {
    void result(String s);
    void error(Exception e);
}
