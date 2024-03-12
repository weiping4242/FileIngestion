package com.example.demoFile.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demoFile.DTO.FileDTO;
import com.example.demoFile.DTO.FileUploadDTO;
import com.example.demoFile.Service.FileService;

@RestController
public class MyController {
	
	@Autowired
	private FileService fileService;
	
	@GetMapping(value="/")
	public String getPage() {
		return "Welcome";
	}
	
	@GetMapping(value="/getFiles")
	public List<FileDTO> getAllFile() {
		return fileService.getAllFile();
	}
	
	@PostMapping(value="/clearData")
	public String clearData() {
		return fileService.clearData();
	}
	
	@PostMapping(value="/IngestFileNoDB")
	public String ingestFileNoDB(@RequestBody FileUploadDTO file) {
		return fileService.ingestFileNoDB(file.getFileName());
	}
	
	@PostMapping(value="/IngestFileDB")
	public String ingestFileDB(@RequestBody FileUploadDTO file) {
		return fileService.ingestFileDB(file.getFileName());
	}
}