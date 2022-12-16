package com.example.service;


import com.example.model.Company;

import java.util.List;

public interface CompanyService {

    Company getCompanyById(Long id);

    Company getCompanyByName(String name);

    List<Company> getCompanyList();

    void createCompany(Company company);

    Company updateCompany(Company company);

    void deleteCompanyById(Long id);

    void delete(Company company);
}
