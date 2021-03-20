package com.kon.EShop.repository;

import java.util.List;

public interface CustomProductRepository {
    void queryWith(List<String> columns, String tableName);
}
