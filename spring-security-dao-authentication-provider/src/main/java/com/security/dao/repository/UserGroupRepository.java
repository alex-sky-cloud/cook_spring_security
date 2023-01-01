package com.security.dao.repository;

import com.security.dao.model.Group;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends PagingAndSortingRepository<Group, Long> {
    Group findByCode(String code);
}

