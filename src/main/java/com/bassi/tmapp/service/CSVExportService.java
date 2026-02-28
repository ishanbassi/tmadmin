package com.bassi.tmapp.service;

import com.opencsv.CSVWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CSVExportService<T> {

    public static final int OPTIMAL_BULK_SIZE = 100;
    private final Logger log = LoggerFactory.getLogger(CSVExportService.class);
    protected CSVWriter csvWriter;
    private boolean hasHeader;
    private ByteArrayOutputStream outputStream;
    private int rowId = 0;
    protected DateTimeFormatter dateFormatter;
    protected DateTimeFormatter dateTimeFormatter;
    protected DecimalFormat decimalFormat;
    protected String sheetName;

    protected CSVExportService(String sheetName) {
        this(false, sheetName);
    }

    protected CSVExportService(boolean hasHeader, String sheetName) {
        this.sheetName = sheetName.isBlank() ? "Sheet 1" : sheetName;
        this.dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        this.decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        this.hasHeader = hasHeader;
        init();
    }

    protected void init() {
        this.outputStream = new ByteArrayOutputStream();
        csvWriter = new CSVWriter(new OutputStreamWriter(outputStream));
        rowId = this.hasHeader ? 2 : 1;
        try {
            writeHeader();
        } catch (Exception ex) {
            log.error("Exception creating file headers: ", ex);
        }
    }

    public ByteArrayOutputStream export() {
        try {
            return outputStream;
        } catch (Exception e) {
            log.error("Exception writing to outputStream: ", e);
            return null;
        } finally {
            try {
                csvWriter.close();
            } catch (IOException e1) {
                log.error("Exception closing csvWriter", e1);
            }
        }
    }

    public void writeRecordToCurrentSheets(List<T> transactions) {
        log.debug("Creating records in current sheets for {} rows", transactions.size());
        for (T element : transactions) {
            writeRow(element, rowId);
            rowId++;
        }
    }

    public abstract void writeHeader();

    public abstract void writeRow(T element, int rowNr);

    protected String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return dateFormatter.format(date);
    }

    protected String formatDateTime(ZonedDateTime date) {
        if (date == null) {
            return "";
        }
        return dateTimeFormatter.format(date);
    }

    protected String formatAmount(BigDecimal amount) {
        if (amount == null) {
            return "";
        }
        return decimalFormat.format(amount);
    }

    protected String getAccountId(String accountId) {
        if (accountId == null) {
            return "";
        }
        return accountId;
    }

    protected String formatInteger(Integer val) {
        if (val == null) {
            return "";
        }
        return Integer.toString(val);
    }

    protected String formatLong(Long val) {
        if (val == null) {
            return "";
        }
        return Long.toString(val);
    }
}
