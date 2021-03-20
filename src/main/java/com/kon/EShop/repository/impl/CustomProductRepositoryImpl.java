package com.kon.EShop.repository.impl;

import com.kon.EShop.repository.CustomProductRepository;
import com.kon.EShop.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomProductRepositoryImpl implements CustomProductRepository {
    @Autowired
    private JdbcTemplate template;
    private List<Map<String, Object>> list;
    private List<String> colList = new ArrayList<>();
    private List<String> colNotNull = new ArrayList<>();
    private String newTable = "new_products";
    private int colNull;

    public void queryWith(List<String> cols, String tableName) {
        getColumnList(tableName);
        getNotNullColumns();
        try {
            String s = FileManager.getPath("csv") + "table.csv";
            template.execute("DROP TABLE IF EXISTS " + newTable);
            template.execute(getNew(cols == null ? colList:cols));
            template.execute("COPY " + newTable + " FROM '" + s + "' DELIMITER ';' CSV");
            template.execute(updateNew(cols == null ? colList:cols, tableName));
            template.execute(insertNew(cols == null ? colList:cols, tableName));
        } catch (Exception e){
            System.out.println("exception in CustomProductRepositoryImpl");
            e.printStackTrace();
        } finally {
            template.execute("DROP TABLE IF EXISTS " + newTable);
        }
    }

    private String insertNew(List<String> colList, String table) {
        StringBuilder sbi = new StringBuilder("INSERT INTO products (");
        StringBuilder sbs = new StringBuilder(" SELECT ");
        for (Map<String, Object> m : list) {
            String name = (String) m.get("column_name");
            if (colList.contains(name)){
                sbi.append(name).append(", ");
                sbs.append(name).append(", ");
                if (colNotNull.contains(name)) colNull++;
            }
        }
        if (colNull != colNotNull.size())
            System.out.println("не все обязательные колонки добавлены");
        sbi.deleteCharAt(sbi.length()-2).deleteCharAt(sbi.length()-1).append(")");
        sbs.deleteCharAt(sbs.length()-2);
        sbi.append(sbs)
                .append("FROM "+ newTable + " newp WHERE NOT exists(")
                .append("SELECT 1 FROM " + table + " p WHERE p.id = newp.id)");
        String s = sbi.toString();
        return s;
    }

    private String updateNew(List<String> columnList, String table) {
        StringBuilder sb = new StringBuilder("UPDATE " + table + " p SET ");
        for (Map<String, Object> m : list) {
            if (columnList.contains(m.get("column_name"))) {
                if (!m.get("column_name").equals("id"))
                    sb.append(m.get("column_name")).append(" = newp.").append(m.get("column_name")).append(", ");
            }
        }
        sb.deleteCharAt(sb.length() - 2).append("FROM ").append(newTable).append(" newp WHERE p.id = newp.id");
        String s = sb.toString();
        return s;
    }

    private String getNew(List<String> columnList) {
        StringBuilder sb = new StringBuilder("CREATE TABLE " + newTable + " (");
        for (Map<String, Object> m : list) {
            if (columnList.contains(m.get("column_name"))) {
                sb.append((String) m.get("column_name")).append(" ");
                sb.append(m.get("data_type"));
                sb.append(", ");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        String s = sb.toString();
        String s1 = s.replaceAll("character varying", "varchar");
        return s1;
    }

    private void getColumnList(String tableName) {
        this.list = template.queryForList(
                "SELECT ordinal_position, column_name, data_type, is_nullable " +
                        "FROM information_schema.columns " +
                        "WHERE information_schema.columns.table_name='" + tableName + "' order by ordinal_position");
    }

    private void getNotNullColumns() {
        this.colList.clear();
        this.colNotNull.clear();
        for (Map<String, Object> m : list) {
            this.colList.add((String) m.get("column_name"));
            if (m.get("is_nullable").equals("NO"))
                this.colNotNull.add((String) m.get("column_name"));
        }
    }
}




