package com.example.demoFile.Repo;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demoFile.DTO.FileDTO;
import com.example.demoFile.DTO.FileOutputDTO;

public interface FileRepo extends JpaRepository<FileDTO, Long> {
	
}
