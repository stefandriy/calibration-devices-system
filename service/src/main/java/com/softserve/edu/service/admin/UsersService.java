package com.softserve.edu.service.admin;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Check whereas user with {@code username} exist in database
     *
     * @param username must not be non {@literal null}
     * @return {@literal true} if user with {@code username} doesn't exist in
     * database, else {@literal false}
     */
    public boolean existsWithUsername(String username) {
        return userRepository.findOne(username) == null;
    }

    /**
     * get all users who are added in system
     * @param pageNumber
     * @param itemsPerPage
     * @param search
     * @return page of users with search
     */
//    @Transactional
//    public Page<User> getUsersBySearchAndPagination(int pageNumber,
//                                                    int itemsPerPage, String search) {
//        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
//        return search == null ? userRepository.findAll(pageRequest)
//                : userRepository.findByRoleLikeIgnoreCase("%" + search + "%",
//                pageRequest);
//    }



}
