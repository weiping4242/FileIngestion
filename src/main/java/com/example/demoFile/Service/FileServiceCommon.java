package com.example.demoFile.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demoFile.DTO.FileDTO;
import com.example.demoFile.DTO.FileOutputDTO;
import com.example.demoFile.Repo.FileRepo;
import com.example.demoFile.Repo.FileRepoImpl;

@Service
public class FileServiceCommon {
//	private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	private static final int THREAD_POOL_SIZE = 1;
	private static String HEADERS = "Country,Bank Name,Unique customer count";
//	private static String FILE_PATH = "C:\\Users\\heng.wei.ping\\eclipse-workspace\\demoFile\\src\\main\\resources\\templates\\";
	private static String FILE_PATH = "";
	@Autowired
	private FileRepo fileRepo;
	
	@Autowired
	private FileRepoImpl fileRepoImpl;
	
	public String processCSVNoDB(String fileName) {
		String fullFile = FILE_PATH.concat(fileName);
		System.out.println("processCSVNoDB");
		ConcurrentHashMap<FileOutputDTO, Integer> countryBankCustomerCount = new ConcurrentHashMap<>();
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		try {
			Set<String> lines = Files.newBufferedReader(Paths.get(fullFile))
                    .lines()
                    .skip(1)
                    .collect(Collectors.toSet());
			
			for (String line : lines) {
                executor.submit(() -> processFileInput(line, countryBankCustomerCount));
            }
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            
            System.out.println("Contents of ConcurrentHashMap:");
            for (FileOutputDTO key : countryBankCustomerCount.keySet()) {
                System.out.println("Key: " + key + ", Value: " + countryBankCustomerCount.get(key));
            }
            
            // Aggregate data and sort
            List<Map.Entry<FileOutputDTO, Integer>> sortedList = countryBankCustomerCount.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toList());
            
//            
            sortedList = sortList(sortedList);

            // Generate output CSV file
            generateOutputFile(sortedList);
			return "Success";
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}
	}
	
	
	
	public String processCSVDB(String fileName) {
		String fullFile = FILE_PATH.concat(fileName);
		System.out.println("processCSVDB");
		List<FileDTO> fileList = new ArrayList<>();
//		try {
//			fileList = parseCSVToObject(fullFile);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		try {
			Set<String> lines = Files.newBufferedReader(Paths.get(fullFile))
                    .lines()
                    .skip(1)
                    .collect(Collectors.toSet());
			
			for (String line : lines) { 
				executor.submit(() -> { FileDTO fileDTO = processFileInput(line); 
				if (fileDTO != null) { 
					synchronized (fileList) {
						fileList.add(fileDTO); 
						} 
					} 
				}); 
			}
			 
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            System.out.println(fileList.size());
//            fileRepo.saveAll(fileList);
            
            List<FileOutputDTO> output = fileRepoImpl.getOutputFileResult();
            System.out.println("Test 123123123:::" + output.size());
            generateOutputFile2(output);
            //Retrieve Data from DB
			return "Success";
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}
	}

	public FileDTO processFileInput(String line) {
		System.err.println("line 108: " + line);
		
        // Parse CSV line and extract country and bank information
        // Assuming the format: country,bank,customerCount
        String[] parts = line.split(",");
        for(String part: parts) {
        	System.err.println("part: " + part);
        }
        if (parts.length == 4) {
        	FileDTO fileDTO = new FileDTO();
        	System.out.println("In line 124 zzz");
        	fileDTO.setCustomerName(parts[0]);
        	fileDTO.setCustomerId(parts[1]);
        	fileDTO.setCountryCode(parts[2]);
        	fileDTO.setBankCode(parts[3]);
        	System.out.println("Line 130" + fileDTO.toString());
        	System.out.println("Line test2");
            return fileDTO;
        } else {
            System.err.println("Invalid CSV line: " + line);
            return null;
        }
    }
	
	public void processFileInput(String line, ConcurrentHashMap<FileOutputDTO, Integer> countryBankCustomerCount) {
		System.err.println("line 108: " + line);
		
        // Parse CSV line and extract country and bank information
        // Assuming the format: country,bank,customerCount
        String[] parts = line.split(",");
        for(String part: parts) {
        	System.err.println("part: " + part);
        }
        if (parts.length == 4) {
        	System.out.println("In line 149");
            FileOutputDTO countryBank = new FileOutputDTO(parts[3],parts[2]);
            int count = 1;

            // Update concurrent map with aggregated customer count
            // Using putIfAbsent to handle concurrent modifications correctly
            countryBankCustomerCount.putIfAbsent(countryBank, 0);
            
            // Increment count atomically using compute method
            countryBankCustomerCount.compute(countryBank, (key, value) -> (value == 0) ? count : value + count);
        } else {
            System.err.println("Invalid CSV line: " + line);
        }
    }
	
	public void generateOutputFile(List<Map.Entry<FileOutputDTO, Integer>> sortedList) {
        File outputFile = new File("output.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Write sorted data to output CSV file
        	writer.write(HEADERS);
        	for (Map.Entry<FileOutputDTO, Integer> entry : sortedList) {
                writer.newLine();
                writer.write(entry.getKey().getCountryCode()+","
                				+ entry.getKey().getBankCode() + "," 
                				+ entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void generateOutputFile2(List<FileOutputDTO> sortedList) {
        File outputFile = new File("output.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Write sorted data to output CSV file
        	writer.write(HEADERS);
        	for (FileOutputDTO fileOutput : sortedList) {
                writer.newLine();
                writer.write(fileOutput.getCountryCode() + ","
                             + fileOutput.getBankCode() + "," 
                             + fileOutput.getCount());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public List<Entry<FileOutputDTO, Integer>> sortList(List<Map.Entry<FileOutputDTO, Integer>> sortedList) {
		sortedList.sort(Comparator.<Map.Entry<FileOutputDTO, Integer>>comparingInt(Map.Entry::getValue).reversed()
//                .thenComparing((entry1, entry2) -> entry1.getKey().getCountryCode().compareTo(entry2.getKey().getCountryCode()))
                .thenComparing((entry1, entry2) -> entry2.getKey().getCountryCode().compareTo(entry1.getKey().getCountryCode()))
                .thenComparing((entry1, entry2) -> entry1.getKey().getBankCode().compareTo(entry2.getKey().getBankCode())));
	return sortedList;
	}
	
	public List<FileDTO> parseCSVToObject(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        
        // Create CSVParser object
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
        
        // Parse CSV to list of FileDTO objects
        List<FileDTO> fileList = new ArrayList<>();
        for (CSVRecord csvRecord : csvParser) {
            FileDTO fileDTO = new FileDTO();
            fileDTO.setCustomerId(csvRecord.get("Customer ID"));
            fileDTO.setCustomerName(csvRecord.get("Customer Name"));
            fileDTO.setCountryCode(csvRecord.get("Country Code"));
            fileDTO.setBankCode(csvRecord.get("Bank Code"));
            fileList.add(fileDTO);
        }
        
        // Close the reader
        reader.close();
        System.out.println(fileList.size());
        return fileList;
    }
}
