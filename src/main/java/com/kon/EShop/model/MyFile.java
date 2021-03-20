package com.kon.EShop.model;

import java.io.File;
import java.util.List;

public class MyFile {
    private String tableName;
    private List<String> columnNames;

    public MyFile() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

}
