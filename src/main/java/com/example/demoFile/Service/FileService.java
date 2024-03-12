package com.example.demoFile.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demoFile.DTO.FileDTO;
import com.example.demoFile.Repo.FileRepo;

@Service
public class FileService {
	
	
	@Autowired
	public FileServiceCommon fileServiceCmn;
	
	@Autowired
	private FileRepo fileRepo;
	
	public List<FileDTO> getAllFile() {
		return fileRepo.findAll();
	}
	
	public String clearData() {
		fileRepo.deleteAll();
		return "Cleared all";
	}
	
	public String ingestFileNoDB(String fileName) {
		return fileServiceCmn.processCSVNoDB(fileName);
	}
	
	public String ingestFileDB(String fileName) {
		return fileServiceCmn.processCSVDB(fileName);
	}

}
