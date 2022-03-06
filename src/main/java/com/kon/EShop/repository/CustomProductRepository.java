package com.kon.EShop.repository;

import java.util.List;

public interface CustomProductRepository {
    void updCSV(List<String> columns, String tableName);
}
