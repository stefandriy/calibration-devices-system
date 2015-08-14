package com.softserve.edu.service;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

/**
 * Created by Volodya NT on 12.08.2015.
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
        String old_pasword = "$2a$10$59Mv7tEUrVH8iBeDsm9y7.zUcJoPHnnyOvMnC4zKRV8.wlnugQ2G2";
        String new_pasword = "pass";

        when(user.getPassword()).thenReturn(old_pasword);
        when(user.getUsername()).thenReturn(username);
        when(userRepository.findOne(username)).thenReturn(user);


        boolean isChanged =  userService.changePassword(user.getUsername(), new_pasword, new_pasword);
        Assert.assertEquals(true, isChanged);
    }
}