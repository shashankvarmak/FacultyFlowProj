package com.example.FacultyFlow.repository;
import com.example.FacultyFlow.model.FacultySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyScheduleRepository extends JpaRepository<FacultySchedule, Long> {
    List<FacultySchedule> findByFacultyId(Long facultyId);
    List<FacultySchedule> findByDay(String day);
    @Query(value = "SELECT CASE " +
            "WHEN ?3 = 'period1' AND period1 = 'Free' THEN true " +
            "WHEN ?3 = 'period2' AND period2 = 'Free' THEN true " +
            "WHEN ?3 = 'period3' AND period3 = 'Free' THEN true " +
            "WHEN ?3 = 'period4' AND period4 = 'Free' THEN true " +
            "WHEN ?3 = 'period5' AND period5 = 'Free' THEN true " +
            "WHEN ?3 = 'period6' AND period6 = 'Free' THEN true " +
            "WHEN ?3 = 'period7' AND period7 = 'Free' THEN true " +
            "ELSE false END " +
            "FROM faculty_schedule " +
            "WHERE faculty_id = ?1 AND day = ?2", nativeQuery = true)
    Integer isFacultyAvailable(Long facultyId, String day, String period);



}
