package com.rohitsuratekar.NCBSinfo.database;

public class QueryBuilder {

    private String query = "";

    public QueryBuilder() {
    }

    public String build() {
        return query;
    }

    public QueryBuilder select() {
        query = query.trim();
        query += " SELECT ";
        return this;
    }

    public QueryBuilder limit(int value) {
        query = query.trim();
        query += " LIMIT " + String.valueOf(value) + " ";
        return this;
    }

    public QueryBuilder offset(int value) {
        query = query.trim();
        query += " OFFSET " + String.valueOf(value) + " ";
        return this;
    }

    public QueryBuilder where() {
        query = query.trim();
        query += " WHERE ";
        return this;
    }

    public QueryBuilder exists() {
        query = query.trim();
        query += " EXISTS ";
        return this;
    }

    public QueryBuilder openBracket() {
        query = query.trim();
        query += " ( ";
        return this;
    }

    public QueryBuilder closeBracket() {
        query = query.trim();
        query += " ) ";
        return this;
    }

    public QueryBuilder column(String columnName) {
        query = query.trim();
        query += " " + columnName + " ";
        return this;
    }

    public QueryBuilder selectMax(String columnName) {
        query = query.trim();
        query += " SELECT MAX(" + columnName + ") ";
        return this;
    }

    public QueryBuilder max(String columnName) {
        query = query.trim();
        query += " MAX(" + columnName + ") ";
        return this;
    }

    public QueryBuilder whereColumn(String columnName) {
        query = query.trim();
        query += " WHERE " + columnName + " ";
        return this;
    }

    public QueryBuilder isEqualTo(String value) {
        query = query.trim();
        query += " = '" + value + "' ";
        return this;
    }


    public QueryBuilder selectAll() {
        query = query.trim();
        query += " SELECT * ";
        return this;
    }

    public QueryBuilder selectColumn(String columnName) {
        query = query.trim();
        query += " SELECT " + columnName + " ";
        return this;
    }

    public QueryBuilder fromTable(String tableName) {
        query = query.trim();
        query += " FROM " + tableName + " ";
        return this;
    }

    public QueryBuilder whereColumnIsEqual(String columnName, String value) {
        query = query.trim();
        query += " WHERE " + columnName + " ='" + value + "' ";
        return this;
    }

    public QueryBuilder columnIsEqual(String columnName, String value) {
        query = query.trim();
        query += " " + columnName + " ='" + value + "' ";
        return this;
    }

    public QueryBuilder and() {
        query = query.trim();
        query += " AND ";
        return this;
    }

    public QueryBuilder or() {
        query = query.trim();
        query += " OR ";
        return this;
    }

    public QueryBuilder having() {
        query = query.trim();
        query += " HAVING ";
        return this;
    }

    public QueryBuilder groupBy() {
        query = query.trim();
        query += " GROUP BY ";
        return this;
    }

    public QueryBuilder orderBy() {
        query = query.trim();
        query += " ORDER BY ";
        return this;
    }

    public QueryBuilder in() {
        query = query.trim();
        query += " IN ";
        return this;
    }

    public QueryBuilder distinct() {
        query = query.trim();
        query += " DISTINCT ";
        return this;
    }


    QueryBuilder columnList(String... allColumns) {
        query = query.trim();
        query += " ";
        for (int i = 0; i < allColumns.length - 1; i++) {
            query += allColumns[i] + " , ";
        }
        query += allColumns[allColumns.length - 1] + " ";
        return this;
    }

}
