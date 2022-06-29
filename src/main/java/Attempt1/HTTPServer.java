package Attempt1;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HTTPServer {
    public static ServerSocket server;
    public static Socket socket;
    public static ExecutorService threadPool = Executors.newFixedThreadPool(3);

    
    public static void startServer(int port, String[]directories){
        try {
            server = new ServerSocket(port);
            System.out.println("Server is listening on port: " + port);

            //TASK 4 - CHECK WHETHER PATH EXISTS, IS DIRECTORY, IS READABLE
            for (String directory : directories){
                File dir = new File (directory);
                if (dir.exists())
                    System.out.println(directory + " exists.");
                else{
                    System.out.println(directory + " does not exist");
                    System.out.println("Exiting Program");
                    System.exit(1);
                }

                if (dir.isDirectory())
                    System.out.println(directory + " is a directory.");
                else{
                    System.out.println(directory + " is not a directory.");
                    System.out.println("Exiting Program");
                    System.exit(1);
                }

                if (dir.canRead())
                    System.out.println(directory + " is readable.");
                else {
                    System.out.println(directory + " is not readable.");
                    System.out.println("Exiting Program");
                    System.exit(1);
                }
            }

            while((socket = server.accept()) != null){
                System.out.println("Browser has connected");
                //TASK 5 - MULTITHREADING
                HTTPClientConnection clientHandler = new HTTPClientConnection(socket, directories);
                threadPool.execute(clientHandler);
            }
                threadPool.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
}
