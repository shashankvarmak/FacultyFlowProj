package com.example.FacultyFlow.controller;

import com.example.FacultyFlow.model.Faculty;
import com.example.FacultyFlow.model.FacultySchedule;
import com.example.FacultyFlow.repository.FacultyRepository;
import com.example.FacultyFlow.service.FacultyScheduleService;
import com.example.FacultyFlow.service.FacultyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyRepository facultyRepo;
    private final FacultyScheduleService facultyScheduleService;
    private FacultyService facultyService;

    public FacultyController(FacultyRepository facultyRepo ,FacultyScheduleService facultyScheduleService,FacultyService facultyService) {
        this.facultyRepo = facultyRepo;
        this.facultyScheduleService=facultyScheduleService;
        this.facultyService=facultyService;
    }

    @GetMapping("/dashboard")
    public String facultyDashboard(HttpSession session, Model model) {
        // Get logged-in faculty email from session
        String email = (String) session.getAttribute("loggedInUserEmail");

        if (email == null) {
            return "redirect:/login"; // Redirect if no user is logged in
        }

        // Fetch faculty details from database
        Faculty faculty = facultyRepo.findByEmail(email);

        if (faculty == null) {
            return "redirect:/login"; // Extra check to avoid errors
        }

        // Fetch faculty schedule from the database
        List<FacultySchedule> schedule = facultyScheduleService.getScheduleByFaculty(faculty.getId());

        // Pass faculty and schedule details to Thymeleaf
        model.addAttribute("faculty", faculty);
        model.addAttribute("schedule", schedule);

        return "faculty/dashboard";
    }

    @GetMapping("/list")
    public List<Faculty> getAllFaculty() {
        return facultyRepo.findAll();
    }
    // Method to load the faculty availability page (Thymeleaf)
    @GetMapping("/availability")
    public String showAvailabilityPage(Model model) {
        List<Faculty> faculties = facultyService.getAllFaculty();
        model.addAttribute("faculties", faculties);
        return "faculty/check-availability";  // Render Thymeleaf page
    }

    // AJAX endpoint for checking faculty availability
    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, Boolean>> checkAvailability(
            @RequestParam Long facultyId,
            @RequestParam String day,
            @RequestParam String period) {

        boolean available = facultyService.isFacultyAvailable(facultyId, day, period);

        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);

        return ResponseEntity.ok(response);  // Return JSON response
    }




}
