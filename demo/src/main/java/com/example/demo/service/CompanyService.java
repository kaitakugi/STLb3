package com.example.demo.service;

import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    // Lấy tất cả các công ty
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // Lấy công ty theo ID
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    // Tạo mới công ty
    public Company createCompany(Company company) {
        if (company.getName() == null || company.getName().isEmpty()) {
            throw new IllegalArgumentException("Tên công ty không được để trống");
        }
        if (company.getAddress() == null || company.getAddress().isEmpty()) {
            company.setAddress("Địa chỉ mặc định");
        }
        return companyRepository.save(company);
    }

    // Cập nhật thông tin công ty
    public Company updateCompany(Long id, Company companyDetails) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            company.setName(companyDetails.getName());
            company.setAddress(companyDetails.getAddress());
            return companyRepository.save(company);
        }
        return null;
    }

    // Xóa công ty theo ID
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy công ty với ID: " + id);
        }
        companyRepository.deleteById(id);
    }

    public Company findById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public void createCompany() {
        Company company = new Company();
        company.setName("Example Name");
        company.setAddress("Example Address");
        companyRepository.save(company);
    }
}