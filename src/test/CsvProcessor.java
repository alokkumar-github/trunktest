package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvProcessor {

	public List <InputDetails>  parseCsvFile(String filePath){
	        String line = "";
	        String cvsSplitBy = ",";
	        List <InputDetails> inputDetailsList = new ArrayList<InputDetails>();
	       
	        try  {
	        	 BufferedReader br = new BufferedReader(new FileReader(filePath));
	        	int i=0;
	            while ((line = br.readLine()) != null) {
	            	 if (i>0){
		                String[] input = line.split(cvsSplitBy);
		                InputDetails inputDetails = getInputDetails(input);
		                inputDetailsList.add(inputDetails);
	                }
	                i++;
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return inputDetailsList;

	    }

	public InputDetails getInputDetails(String[] csvDetails){
		InputDetails inputDetails = new InputDetails();
		inputDetails.setSvnRepo(csvDetails[0]);
		inputDetails.setGitRepo(csvDetails[1]);
		inputDetails.setBranch(Boolean.parseBoolean(csvDetails[2]));
		inputDetails.setBranchName(csvDetails[3]);
		inputDetails.setProjectName(csvDetails[4]);
		if (csvDetails.length >5){
			inputDetails.setTagExist(Boolean.parseBoolean(csvDetails[5]));
		}

		return inputDetails;
	}

	}


