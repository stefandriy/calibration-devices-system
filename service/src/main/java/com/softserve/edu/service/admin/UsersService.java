package com.softserve.edu.service.admin;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.entity.util.Roles;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserRoleRepository userRoleRepository;
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
     @Transactional
     public Page<User> getUsersBySearchAndPagination(int pageNumber,
                                                    int itemsPerPage, String search) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return search == null ? userRepository.findAll(pageRequest)
                : userRepository.findByRoleLikeIgnoreCase("%" + search + "%",
                pageRequest);
    }


    @Transactional
    public String getRoleByUserName(String username){return userRepository.getRoleByUserName(username);
    }

    @Transactional
    public List<UserRole> getRoleByUserNam(String username){
        return userRepository.getRoleByUserNam(username);
    }

    @Transactional
    public List<String> getRoles(String username){
        return userRoleRepository.getRoles(username);
    }

    @Transactional
    public String getOrganization(String username,User user){
       String nameOrganization="";
      List<String> roles = getRoles(username);
        if (roles.contains(Roles.SYS_ADMIN.name())) {
        } else {
           nameOrganization=user.getOrganization().getName();
        }
        return nameOrganization;
    }
}
