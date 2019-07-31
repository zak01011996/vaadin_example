package com.example.demo.service;

import com.example.demo.model.Lecturer;
import com.example.demo.repository.LecturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    private LecturerRepository lecturerRepository;

    public DataService(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    public Lecturer saveLecturer(Lecturer data) {
        return lecturerRepository.saveAndFlush(data);
    }

    public List<Lecturer> findAllLecturers() {
        return lecturerRepository.findAll();
    }
}
