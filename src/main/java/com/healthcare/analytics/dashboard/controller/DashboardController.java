package com.healthcare.analytics.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/patients")
    public String patientsPage() {
        return "patients";
    }
    @GetMapping("/cohorts")
    public String cohortsPage() {
        return "cohorts";
    }
    @GetMapping("/analytics")
    public String analyticsPage() {
        return "analytics";
    }
}

