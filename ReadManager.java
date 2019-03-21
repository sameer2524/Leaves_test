import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadManager{

	public ObjectInputStream objectInputStream;

	public ReadManager(String filePath){
		try{
			File file = new File(filePath);
			if (!file.exists()) {
	            file.createNewFile();
	        }
	        this.objectInputStream = new ObjectInputStream(new FileInputStream(file));	
		} catch(Exception e){
			System.out.println("Created a file to store application data.");
		}
	}

	public Object readObject() throws IOException, ClassNotFoundException{
		return objectInputStream.readObject();
	}

}

