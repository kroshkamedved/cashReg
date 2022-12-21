package com.elearn.fp.service;

import com.elearn.fp.db.entity.User;
import com.elearn.fp.db.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserManagerTest {
    private User user;
    private ResultSet rs;


    @BeforeEach
    void init() throws SQLException {
        user = new User();
        user.setLogin("TestUnit");
        user.setId(1);
        user.setRole(UserRole.CASHIER);
        user.setPassword("hashedPassword");

        rs = mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("TestUnit");
        when(rs.getString("pass_hash")).thenReturn("hashedPassword");
        when(rs.getString("role")).thenReturn(UserRole.CASHIER.name());
    }

    @Test
    void testFindUser() throws SQLException, NamingException {

    }

    @Test
    void testExtractUser() throws SQLException {
        assertEquals(user,UserManager.extractUser(rs));
    }
}