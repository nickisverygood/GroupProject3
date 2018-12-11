

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //Initialize
        Scanner sc = new Scanner(System.in);
        NetworkData network;
        try {
            //network = readData("userData.dat");
            network = readDataTxt("userData.txt");
            network.checkNetworkSize();
        }catch (FileNotFoundException ex){
            network =new NetworkData(20,20);
            network.addUsers();
        }
        
        //Program Starts
        System.out.print("Welcome to Network!:\n\n");
        network.login();
        boolean exit = false;
        while (exit == false) {

            switch (menu()) {
                case "View All Friends":
                    network.viewAllFriends();
                    break;
                case "View a Friend's Profile":
                    network.viewAFriend();
                    break;
                case "Remove a Friend":
                    network.removeAFriend();
                    break;
                case "Search by Name":
                    network.searchByName();
                    break;
                case "Search by Interest":
                    network.searchByInterest();
                    break;
                case "Get Friend Recommendations":
                    network.getFriendRecommendations();
                    break;
                case "Quit":
                    network.logout();
                    saveData("userData.dat",network);
                    saveDataTxt("userData.txt",network);
                    exit = true;
                    break;
                default:
                    System.out.print("\nInvalid Input! Please try again.\n");

            }
        }

    }

    public static String menu() {
        Scanner scanner;
        scanner = new Scanner(System.in);
        // menu
        System.out.print("Please select from the following options:\n" +
                "(V)- View My Friends\n" +
                "(S)- Search for a New Friend\n" +
                "(G)- Get Friend Recommendations\n" +
                "(Q)- Quit\n");
        String selection = scanner.nextLine().toUpperCase();

        switch (selection) {
            case "V":
                System.out.print("Please select from the following options:\n" +
                        "(1)- View All Friends\n" +
                        "(2)- View a Friend's Profile\n" +
                        "(3)- Remove a Friend\n");
                selection = scanner.nextLine();
                switch (selection) {
                    case "1":
                        return "View All Friends";
                    case "2":
                        return "View a Friend's Profile";
                    case "3":
                        return "Remove a Friend";
                    default:
                        return "error";
                }
            case "S":
                System.out.print("Please select from the following options:\n" +
                        "(1)- Search by Name\n" +
                        "(2)- Search by Interest\n");
                selection = scanner.nextLine();
                switch (selection) {
                    case "1":
                        return "Search by Name";
                    case "2":
                        return "Search by Interest";
                    default:
                        return "error";
                }
            case "G":
                return "Get Friend Recommendations";
            case "Q":
                return "Quit";
            default:
                return "error";
        }
    }

    public static void saveData(String filename, NetworkData network){
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(network);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    
    public static void saveDataTxt(String filename, NetworkData network){
    	String text ="";
    	ArrayList<User> userArray = network.bstUsers.getArray();
    	for(User thisuser:userArray) {
    		String[] userData = new String[13];
    		for(String data:userData) {
    			data = "";
    		}
    		
    		userData[0]=thisuser.getFirstName()+" "+thisuser.getLastName();
    		userData[1]=thisuser.getUserName();
    		userData[2]=thisuser.getPassword();
    		userData[3]=thisuser.getCity();
    		
    		thisuser.getInterests().pointIterator();
    		ArrayList<String> listOfInterests = new ArrayList<>();
            while(!thisuser.getInterests().offEnd()){
            	listOfInterests.add(thisuser.getInterests().getIterator());
            	thisuser.getInterests().advanceIterator();
            }	
    		userData[4]=listOfInterests.get(0);
    		userData[5]=listOfInterests.get(1);
    		
    		String thisuserstring = "";
    		for(int i=0;i<6;i++) {
    			thisuserstring = thisuserstring+userData[i]+"\t";
    		}
    		
    		ArrayList<User> thisusersfriends = thisuser.getFriends().getArray();
    		for(User friends:thisusersfriends) {
    			thisuserstring = thisuserstring+friends.getFirstName()+" "+friends.getLastName()+"\t";
    		}
    		
    		text = text+thisuserstring+"\r\n";
    	}
    	
    	
    	try (PrintWriter out = new PrintWriter(filename)) {
    	    out.println(text);
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

    public static NetworkData readData(String filename) throws FileNotFoundException{
        NetworkData e;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (NetworkData) in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException fex){
            throw fex;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("User class not found");
            c.printStackTrace();
            return null;
        }
        return e;
    }
    
    public static NetworkData readDataTxt(String filename) throws FileNotFoundException{
        NetworkData e=new NetworkData(20,20);;
        String text = ""; try { text = new String(Files.readAllBytes(Paths.get(filename))); } catch (IOException e1) { e1.printStackTrace(); }
        e.addUsersString(text);
        return e;
    }
}
