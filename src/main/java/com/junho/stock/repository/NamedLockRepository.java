package com.junho.stock.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NamedLockRepository {

    private final JdbcTemplate jdbcTemplate;

    public NamedLockRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void getLock(String key) {
        jdbcTemplate.execute("SELECT GET_LOCK('" + key + "', 10)");
    }

    public void releaseLock(String key) {
        jdbcTemplate.execute("SELECT RELEASE_LOCK('" + key + "')");
    }

}
