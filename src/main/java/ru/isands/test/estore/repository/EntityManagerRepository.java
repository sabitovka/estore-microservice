package ru.isands.test.estore.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EntityManagerRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EntityManagerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void truncateTable(String[] tableNames) {
        StringBuilder sql = new StringBuilder();
        for (String tableName : tableNames) {
            sql.append("DELETE FROM ").append(tableName).append(";");
        }
        jdbcTemplate.execute(sql.toString());
    }
}
