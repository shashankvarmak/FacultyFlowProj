package com.example.FacultyFlow.service;

import com.example.FacultyFlow.model.Faculty;
import com.example.FacultyFlow.repository.FacultyRepository;
import com.example.FacultyFlow.repository.FacultyScheduleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyScheduleRepository scheduleRepository;

    public FacultyService(FacultyRepository facultyRepository,FacultyScheduleRepository scheduleRepository) {
        this.facultyRepository = facultyRepository;
        this.scheduleRepository=scheduleRepository;
    }

    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }
    public boolean isFacultyAvailable(Long facultyId, String day, String period) {
        System.out.println("Inside checkFacultyAvailability method");
        Integer available = scheduleRepository.isFacultyAvailable(facultyId, day, period);
        System.out.println("Faculty ID: " + facultyId + ", Day: " + day + ", Period: " + period + ", Available: " + available);
        return available != null && available == 1;  // Convert Integer to Boolean
    }



}
