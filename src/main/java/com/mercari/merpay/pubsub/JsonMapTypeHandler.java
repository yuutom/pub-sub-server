package com.mercari.merpay.pubsub;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class JsonMapTypeHandler extends BaseTypeHandler<Map<String, Object>> {
    private static final Gson gson = new Gson();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, gson.toJson(parameter));
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        if (json != null && json.startsWith("\"") && json.endsWith("\"")) {
            json = json.substring(1, json.length() - 1);  // Remove the enclosing quotes
            json = json.replace("\\\"", "\"");  // Unescape the internal quotes
        }
        return gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        if (json != null && json.startsWith("\"") && json.endsWith("\"")) {
            json = json.substring(1, json.length() - 1);  // Remove the enclosing quotes
            json = json.replace("\\\"", "\"");  // Unescape the internal quotes
        }
        return gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
    }

    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        if (json != null && json.startsWith("\"") && json.endsWith("\"")) {
            json = json.substring(1, json.length() - 1);  // Remove the enclosing quotes
            json = json.replace("\\\"", "\"");  // Unescape the internal quotes
        }
        return gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
    }
}

