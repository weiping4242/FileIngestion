package com.example.demoFile.Repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demoFile.DTO.FileOutputDTO;

@Service
public class FileRepoImpl {

	@Value("${spring.datasource.url}")
    private String URL;
	
	@Value("${spring.datasource.username}")
    private String USERNAME;

	@Value("${spring.datasource.password}")
    private String PASSWORD;
	
	public List<FileOutputDTO> getOutputFileResult() {
		List<FileOutputDTO> resultList = new ArrayList<>();
		
		 String sql = "SELECT \r\n"
		 		+ "    country_code AS Country,\r\n"
		 		+ "    bank_code AS Bank_Name,\r\n"
		 		+ "    COUNT(DISTINCT customer_id) AS Unique_customer_count\r\n"
		 		+ "FROM \r\n"
		 		+ "    fileingestion.filedto\r\n"
		 		+ "GROUP BY \r\n"
		 		+ "    country_code, bank_code\r\n"
		 		+ "ORDER BY \r\n"
		 		+ "	Unique_customer_count desc, Country desc;";
		 Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			// Process the result set
			while (rs.next()) {
				String country = rs.getString("Country");
				String bankName = rs.getString("Bank_Name");
				int uniqueCustomerCount = rs.getInt("Unique_customer_count");
				
				// Create a new FileOutputDTO object and add it to the list
				FileOutputDTO fileOutputDTO = new FileOutputDTO(country, bankName, uniqueCustomerCount);
				resultList.add(fileOutputDTO);
			}
			return resultList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         return null;
	}
}
