package com.example.shopapp.configurations;

public class ExcelConfig {
    private String sheetName = "Product List";
    private String titleName = "List of Products";
    private String[] headers = {"ID", "Name", "Price", "Description", "Category"};

    public String getSheetName() {
        return sheetName;
    }

    public String getTitleName() {
        return titleName;
    }

    public String[] getHeaders() {
        return headers;
    }
}
