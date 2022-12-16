package com.example.service;

import com.example.model.Company;
import com.example.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('COMPANY_READ') and hasAuthority('DEPARTMENT_READ')")
    public Company getCompanyById(Long id) {

        return companyRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found by id : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('COMPANY_READ') and hasAuthority('DEPARTMENT_READ')")
    public Company getCompanyByName(String name) {

        return companyRepository
                .findByName(name)
                .orElseThrow(() -> new RuntimeException("Company not found by name: " + name));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('COMPANY_READ')")
    public List<Company> getCompanyList() {
        return (List<Company>) companyRepository.findAll();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('COMPANY_CREATE')")
    public void createCompany(Company company) {

        companyRepository.save(company);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('COMPANY_UPDATE')")
    public Company updateCompany(Company company) {

        return companyRepository.save(company);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('COMPANY_DELETE')")
    public void deleteCompanyById(Long id) {

        companyRepository.deleteById(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('COMPANY_DELETE')")
    public void delete(Company company) {
        companyRepository.delete(company);
    }
}
