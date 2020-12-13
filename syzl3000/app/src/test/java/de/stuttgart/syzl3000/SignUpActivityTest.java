package de.stuttgart.syzl3000;

import org.junit.Before;
import org.junit.Test;

import de.stuttgart.syzl3000.authentication.AuthService;


import static org.junit.Assert.*;

public class SignUpActivityTest {

    private AuthService authService;

        @Before
        public void initObjects() {
            authService = new AuthService();
        }

        @Test
        public void emailValidator_CorrectEmailFormat_ReturnsTrue() {
            assertTrue(authService.isEmailValid("name@email.com"));
        }

        @Test
        public void emailValidator_WrongEmailFormat_ReturnsFalse() {
            assertFalse(authService.isEmailValid("user^domain.co.in"));
        }



}