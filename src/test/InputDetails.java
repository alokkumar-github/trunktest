package test;

public class InputDetails {
	
	private String branchName = null;
	private String svnRepo = null;
	private String gitRepo = null;
	private boolean isBranch = false;
	private String repoName = null;
	private String projectName = null;
	private boolean isTagExist = false;

	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getSvnRepo() {
		return svnRepo;
	}
	public void setSvnRepo(String svnRepo) {
		this.svnRepo = svnRepo;
	}
	public String getGitRepo() {
		return gitRepo;
	}
	public void setGitRepo(String gitRepo) {
		this.gitRepo = gitRepo;
	}
	public boolean isBranch() {
		return isBranch;
	}
	public void setBranch(boolean isBranch) {
		this.isBranch = isBranch;
	}
	public String getRepoName() {
		return repoName;
	}
	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setTagExist(boolean isTagExist) {
		this.isTagExist = isTagExist;
	}
	public boolean isTagExist() {
		return isTagExist;
	}

}
