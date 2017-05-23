package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class SvnToGitMigration {

	private static String authors = null;

	public static void main(String args[]) throws IOException,
			InterruptedException {
		if (args.length < 2) {
			System.out.println("Please provide valid input file");
			return;
		}
		String filePath = args[0];
		authors = args[1];
		authors = "--authors-file " + authors + " " +"--username rkonda003c ";
		CsvProcessor csvProcessor = new CsvProcessor();
		List<InputDetails> inputDetailsList = csvProcessor
				.parseCsvFile(filePath);

		if (inputDetailsList != null && inputDetailsList.size() > 0) {
			for (InputDetails inputDetails : inputDetailsList) {
				System.out.println("inputDetails" + inputDetails.isBranch()
						+ "-" + inputDetails.getBranchName() + "-"
						+ inputDetails.getGitRepo() + "-"
						+ inputDetails.getSvnRepo() + "-"
						+ inputDetails.getProjectName());

				if (inputDetails.isBranch()
						&& (!inputDetails.isTagExist() && !inputDetails
								.getBranchName().equalsIgnoreCase("master"))) {
					String cmd = createBranchMigrationScript(inputDetails);
					executeCommand(cmd);
					String projectName = "repo//"
							+ inputDetails.getProjectName() + "_"
							+ inputDetails.getBranchName();
					File file = new File(projectName);
					if (file.exists()) {
						//FileUtils.deleteDirectory(file);
					}

				} else if (inputDetails.isTagExist()
						&& (!inputDetails.isBranch() && !inputDetails
								.getBranchName().equalsIgnoreCase("master"))) {
					String cmd = createTagMigrationScript(inputDetails);
					executeCommand(cmd);
					String projectName = "repo//"
							+ inputDetails.getProjectName() + "_"
							+ inputDetails.getBranchName();
					File file = new File(projectName);
					if (file.exists()) {
						//FileUtils.deleteDirectory(file);
					}
				} else if (inputDetails.getBranchName().equalsIgnoreCase(
						"master")) {
					String cmd = createMasterMigrationScript(inputDetails);
					executeCommand(cmd);
					String projectName = "repo//"
							+ inputDetails.getProjectName();
					File file = new File(projectName);
					if (file.exists()) {
						//FileUtils.deleteDirectory(file);
					}
				}

			}
		}

	}

	private static String createMasterMigrationScript(InputDetails inputDetails)
			throws IOException {
		String directoryName = "repo//" + inputDetails.getProjectName();
		StringBuffer sb = new StringBuffer();
		sb.append("git remote remove origin").append(" \n");
		sb.append("git svn clone ").append(authors)
				.append(inputDetails.getSvnRepo()).append(" ")
				.append(directoryName).append(" \n");
		sb.append("cd ").append(directoryName).append(" \n");
		/*sb.append("git svn show-ignore ").append(StringEscapeUtils.escapeJava(">")).append(" .gitignore").append(" \n");*/
		sb.append("git add .").append(" \n");
		//sb.append("git status").append(" \n");
		sb.append("git remote add origin ").append(inputDetails.getGitRepo())
				.append(" \n");
		sb.append("git remote -v").append(" \n");
		//sb.append("git push -u origin ").append(inputDetails.getGitRepo()).append(" master").append(" \n");
		sb.append("git config credential.helper store").append(" \n");
		sb.append("git push origin master").append(" \n");
		sb.append("git config credential.helper 'cache --timeout=500000'").append(" \n");
		sb.append("git config --list").append(" \n");
		String fileName = "D:\\svnToGitMigration\\"+inputDetails.getProjectName() + ".bat";
		writeToFile(fileName, sb.toString());
		sb = null;
		return fileName + " >> " + "D:\\svnToGitMigration\\"+inputDetails.getProjectName()
				+ "_migration.log";
	}

	private static String createBranchMigrationScript(InputDetails inputDetails)
			throws IOException {
		StringBuffer sb = new StringBuffer();
		String projectName = inputDetails.getProjectName() + "_"
				+ inputDetails.getBranchName();
		String directoryName = "repo//" + projectName;
		sb.append("git svn clone ").append(authors).append(inputDetails.getSvnRepo()).append(" ")
				.append(directoryName).append(" \n");
		sb.append("cd ").append(directoryName).append(" \n");
		//sb.append("git svn show-ignore ").append(StringEscapeUtils.escapeJava(">")).append(" .gitignore").append(" \n");
		sb.append("git add .").append(" \n");
		sb.append("git status ").append(" \n");
		sb.append("git remote add origin ").append(inputDetails.getGitRepo())
				.append(" \n");
		sb.append("git remote -v").append(" \n");
		sb.append("git checkout -b ").append(inputDetails.getBranchName())
				.append(" \n");
		//sb.append("git push ").append(inputDetails.getGitRepo()).append(" ").append(inputDetails.getBranchName()).append(" \n");
		sb.append("git push origin ").append(inputDetails.getBranchName()).append(" \n");
		String fileName = "D:\\svnToGitMigration\\"+projectName + ".bat";
		writeToFile(fileName, sb.toString());
		sb = null;
		return fileName + " >> " + "D:\\svnToGitMigration\\"+projectName + "_migration.log";

	}

	private static String createTagMigrationScript(InputDetails inputDetails)
			throws IOException {
		StringBuffer sb = new StringBuffer();
		String projectName = inputDetails.getProjectName() + "_"
				+ inputDetails.getBranchName();
		String directoryName = "repo//" + projectName;
		sb.append("git svn clone ").append(authors)
				.append(inputDetails.getSvnRepo()).append(" ")
				.append(directoryName).append(" \n");
		sb.append(" cd ").append(directoryName).append(" \n");
	/*	sb.append("git svn show-ignore ").append(StringEscapeUtils.escapeJava(">")).append(" .gitignore").append(" \n");*/
		sb.append("git add .").append(" \n");
		sb.append("git status ").append(" \n");
		sb.append("git remote add origin ").append(inputDetails.getGitRepo())
				.append(" \n");
		sb.append("git tag ").append(inputDetails.getBranchName()).append(" -m  \"Adding Tag ")
				.append(inputDetails.getBranchName()).append(" \"")
				.append(" \n");
		sb.append("git checkout tags/").append(inputDetails.getBranchName())
				.append(" \n");
		sb.append("git push origin ").append(" ").append(inputDetails.getBranchName()).append(" \n");
		String fileName = "D:\\svnToGitMigration\\"+projectName + ".bat";
		writeToFile(fileName, sb.toString());
		sb = null;
		return fileName + " >> " + "D:\\svnToGitMigration\\"+ projectName + "_migration.log";
	}

	private static void executeCommand(String command) throws IOException,
			InterruptedException {
		//String[] commandArray = { "/bin/bash", "-c", "ssh " + command };
        //Process process = Runtime.getRuntime().exec(commandArray);

		Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
		Thread.sleep(30000);
	}

	public static void writeToFile(String fileName, String content)
			throws IOException {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
			file = new File(fileName);
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file, true);
		FileChannel fileChannel = fileOutputStream.getChannel();
		ByteBuffer byteBuffer = ByteBuffer.wrap(content.getBytes());
		fileChannel.write(byteBuffer);
		fileOutputStream.close();
		fileChannel.close();
	}

}
