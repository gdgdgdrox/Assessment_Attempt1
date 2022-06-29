package Attempt1;

import java.util.Arrays;

public class Main {
    public static int port = 3000;
    public static String[] directories = {"./static"};

    public static void main(String[] args) {
        //TASK 2 - COMMAND LINE ARGUMENTS
        if (args.length > 0){
            for (int i = 0; i < args.length; i++){
                if (args[i].equals("--port"))
                    port = Integer.parseInt(args[i+1]);
                if (args[i].equals("--docRoot")){
                    directories = args[i+1].split(":");
                }
            }
        }
        System.out.println("Port: " + port);
        System.out.println("Directories: " + Arrays.toString(directories));

        //START SERVER
        HTTPServer.startServer(port, directories);
    }
}
