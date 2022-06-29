package Attempt1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HTTPClientConnection implements Runnable{
    public Socket socket;
    public String[] directories;
    
    public HTTPClientConnection(Socket socket, String[] directories){
        this.socket = socket;
        this.directories = directories;
    }
   
    public void run(){
        try {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader (isr);
            OutputStream os = socket.getOutputStream();

            String request = br.readLine();
            System.out.println("Browser's request: " + request);
            String[] requestTerms = request.split(" ");
            String method = requestTerms[0];
            System.out.println("Method: " + method);
            String resource = requestTerms[1];
            System.out.println("Requesting for resource: " + resource);

            //ACTION 1 - NOT A GET METHOD
            if (!method.equals("GET")){
                System.out.println("Inside Action 1 - Not A Get Method");
                os.write(("HTTP/1.1 405 Method Not Allowed \r\n").getBytes());
                os.write(("\r\n").getBytes());
                os.write((method + " not supported\r\n").getBytes());  
                os.flush();

                //Close connection and exit thread
                os.close();
                br.close();
                socket.close();
            }
            else{
                //ACTION 2 - RESOURCE DOES NOT EXIST
                if (resource.equals("/"))
                    resource = "/index.html";
                if((constructPathToResource(directories, resource)).isEmpty()){
                        System.out.println("Inside Action 2 - Resource Does Not Exist");
                        os.write(("HTTP/1.1 404 Not Found\r\n").getBytes());
                        os.write(("\r\n").getBytes());
                        os.write((resource + " not found\r\n").getBytes());  
                        os.flush();

                        //Close connection and exit thread
                        os.close();
                        br.close();
                        socket.close();
                    }
                    else {
                        //ACTION 3 - RESOURCE EXIST AND IS NOT PNG
                        if (!resource.endsWith("png")){
                            System.out.println("Inside Action 3 - Resource Exist and is not a PNG");
                            FileInputStream fis = new FileInputStream(constructPathToResource(directories, resource));
                            os.write(("HTTP/1.1 200 OK\r\n").getBytes());
                            os.write(("\r\n").getBytes());
                            os.write(fis.readAllBytes());      
                            os.flush();
                            
                            //Close connection and exit thread
                            os.close();
                            br.close();
                            socket.close();

                        }
                        else {
                            //ACTION 4 - RESOURCE EXIST AND IS A PNG
                            System.out.println("Inside Action 4 - Resource Exist and is a png");
                            FileInputStream fis = new FileInputStream(constructPathToResource(directories, resource));
                            os.write(("HTTP/1.1 200 OK\r\n").getBytes());
                            os.write(("Content-Type: image/png\r\n").getBytes());
                            os.write(("\r\n").getBytes());
                            os.write(fis.readAllBytes());
                            os.flush();

                            //Close connection and exit thread
                            os.close();
                            br.close();
                            socket.close();
                        }
                        }
                }
        }
        
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    /*This method has 2 purposes. 
    First purpose: determine whether a resource exists (if it returns an empty string, the resource does not exist)
    Second purpose: if resource exists, a path is constructed */
    public String constructPathToResource(String[] directories, String resource){
        String pathToResource = "";
        for (String directory : directories){
            File dir = new File (directory);
            String[] dirFiles = dir.list();
            for (String file : dirFiles){
                if (file.equals((resource).substring(1))){
                    pathToResource = directory + resource;          
                    break;
                }
            }
        }
        if (pathToResource.isEmpty())
            System.out.println(pathToResource);
        return pathToResource;
    }
}


//METHOD TO CHECK WHETHER RESOURCE EXISTS IN THE DIRECTORY
/*public boolean resourceExists(String[]directories, String resource){
    boolean found = false;
        for (String directory : directories){
            File dir = new File (directory);
            String[] dirFiles = dir.list();
            for (String file : dirFiles){
                if (resource.contains(file)){
                    found=true;
                    break;
                }
            }
        }
    return found;
}
*/
        
    



