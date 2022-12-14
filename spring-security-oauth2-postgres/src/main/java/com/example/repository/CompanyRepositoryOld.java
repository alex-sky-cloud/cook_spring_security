package com.example.repository;


import com.example.model.Company;

import java.util.List;

public interface CompanyRepositoryOld {

    Company find(Long id);

    Company find(String name);

    List<Company> findAll();

    void create(Company company);

    Company update(Company company);

    void delete(Long id);

    void delete(Company company);
}
