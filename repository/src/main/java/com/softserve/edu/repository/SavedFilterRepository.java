package com.softserve.edu.repository;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.SavedFilter;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Pavlo on 11.12.2015.
 */
@Repository
public interface SavedFilterRepository extends PagingAndSortingRepository<SavedFilter,Long>,JpaSpecificationExecutor {
    List<SavedFilter> findByUserAndLocationUrl(User user, String locationUrl);
    SavedFilter findByUserAndLocationUrlAndName(User user,String locationUrl,String name);
}