package com.example.demo.controller;

import com.example.demo.dto.ReportRequestDto;
import com.example.demo.service.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/report-users")
    public void updateUserStatus(@RequestBody ReportRequestDto reportRequestDto) {
        adminService.updateUserStatus(reportRequestDto.getUserIds(), reportRequestDto.getStatus());
    }
}
