package mainPack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;

public class GenerateClass {
	
	String srcPath;
	String projectClonePath;
	
	public ResultObject start(String repoUrl) {
		
		if(!cloneProject(repoUrl)) {
			return new ResultObject(1, "Could not clone project from the URL: "+repoUrl);
		}
		
		findSourceFolder();
		
		return new ResultObject(0, "Ola kala");
	}
	
	private boolean cloneProject(String repoUrl) {
		
		//new directory creation
		String folder_name = repoUrl.replaceAll("[^A-Za-z0-9]","");
		folder_name += "_cloned";

		System.out.println("Creating new folder...");
		File clone_folder = new File(folder_name);
		boolean flag = clone_folder.mkdir();
		
		if(!flag) {
			System.out.println("could not create folder");
			return false;
		}
		
		String cloneDirectoryPath = "./"+folder_name; // Ex.in windows c:\\gitProjects\SpringBootMongoDbCRUD\
		System.out.println("Clone path: "+cloneDirectoryPath);
		this.projectClonePath = cloneDirectoryPath;
		try {
		    System.out.println("Cloning "+repoUrl+" into "+cloneDirectoryPath);
		    Git.cloneRepository()
		        .setURI(repoUrl)
		        .setDirectory(Paths.get(cloneDirectoryPath).toFile())
		        .call();
		    System.out.println("Completed Cloning");
		    return true;
		} catch (GitAPIException e) {
		    System.out.println("Exception occurred while cloning repo");
			deleteDir(clone_folder);
		    e.printStackTrace();
		    return false;
		}

	}

	public boolean findSourceFolder() {
		
		File clone_folder = new File(this.projectClonePath);

		String[] directories = clone_folder.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		
		System.out.println(Arrays.toString(directories));
		
		return true;
	}
	
	public void fetchMethods(String sourceFilePath) {
		MethodFinder mdfndr = new MethodFinder();
		mdfndr.find(sourceFilePath);
	}

	private void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            if (! Files.isSymbolicLink(f.toPath())) {
	                deleteDir(f);
	            }
	        }
	    }
	    file.delete();

		System.out.println("Deleted folder: "+file.getName()+"...");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//---------------------------------------------------------------------------------------------------------------------------
	
//	public void prevMain(String[] args) {
//
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			JsonSchema shcema = mapper.generateJsonSchema(TempClass.class);
//			System.out.println(shcema.toString());
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			System.out.println("Error catch...");
//			e.printStackTrace();
//		}
//
//	}
//	
//	public String getTestMethod() {
//		String ret = "";
//
//		RestTemplate rest = new RestTemplate();
//		ret = rest.getForObject("http://195.251.210.147:3997/GetRepoURL", String.class);
//		//String result = rest.postForObject("http://195.251.210.147:3997", String.class);
//		System.out.println(ret);
//
//		//ObjectMapper mapper = new ObjectMapper();
//		//Object object = mapper.readValue(result, ExampleJson.class);
//
//		return ret;
//	}
	
	
}