package com.elearn.fp.service;

import com.elearn.fp.db.entity.User;
import com.elearn.fp.db.entity.UserRole;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserManagerTest {

    @Test
    void getInstance() {
    }

    @Test
    void findUser() {
    }

    @Test
    void testFindUser() {
    }

    @Test
    void testExtractUser() throws SQLException {

        User user = new User();
        user.setLogin("TestUnit");
        user.setId(1);
        user.setRole(UserRole.CASHIER);
        user.setPassword("hashedPassword");

        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("TestUnit");
        when(rs.getString("pass_hash")).thenReturn("hashedPassword");
        when(rs.getString("role")).thenReturn(UserRole.CASHIER.name());

        assertEquals(user,UserManager.extractUser(rs));
    }
}