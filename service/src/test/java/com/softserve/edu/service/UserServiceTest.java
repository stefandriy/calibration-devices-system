package com.softserve.edu.service;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Volodya NT on 12.08.2015.
 * Modified by Nazariy Melnychuk on 17.08.2015
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Mock
    private User user;

    @Test
    public void testChangePassword() throws Exception {
        String username = "Petro";
        String hash_of_password = "$2a$10$59Mv7tEUrVH8iBeDsm9y7.zUcJoPHnnyOvMnC4zKRV8.wlnugQ2G2";
        String old_pasword = "pass";
        String new_pasword = "ddd";

        when(user.getPassword()).thenReturn(hash_of_password);
        when(user.getUsername()).thenReturn(username);
        when(userRepository.findOne(username)).thenReturn(user);


        boolean isChanged =  userService.changePassword(user.getUsername(), old_pasword, new_pasword);
        assertEquals(true, isChanged);
        verify(user, times(1)).setPassword(anyString());
    }

    @Test
    public void testChangeFieldIfWrongType() throws Exception{
        String username = "Petro";
        String newFieldValue = "some";
        String type = "abba";

        when(userRepository.findOne(anyString())).thenReturn(user);
        boolean isChanged = userService.changeField(username, newFieldValue, type);
        assertFalse(isChanged);
        verify(userRepository, never()).save(any(User.class));
    }
}