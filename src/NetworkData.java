

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NetworkData implements Serializable {
    int expectedUsers;
    int expectedInterests;
    User me;
    BST<User> bstUsers;
    Hash<User> hashName;
    Hash<Interest> hashInterests;
    Graph network;


    //Constructer
    public NetworkData(int expectedUsers, int expectedInterests){
        this.expectedUsers = expectedUsers;
        this.expectedInterests = expectedInterests;
        bstUsers = new BST<User>();
        hashName = new Hash<>((int)Math.ceil((double)expectedUsers*3/2));
        hashInterests = new Hash<>((int)Math.ceil((double)expectedUsers*3/2));
        network = new Graph(expectedUsers);
    }

    //System Operations
    private void addUser(String firstName,String lastName,String userName,
                         String password,String city,List<String> interests){
        User newuser = new User( firstName, lastName, userName,
                password, city,interests,bstUsers.getSize()+1);

        bstUsers.insert(newuser);
        hashName.insert(newuser);

        interests.pointIterator();
        while (!interests.offEnd()){
            Interest dummyInterest = new Interest(interests.getIterator());
            if(hashInterests.search(dummyInterest)==-1){
                hashInterests.insert(dummyInterest);
            }
            hashInterests.get(dummyInterest).insert(newuser);

            interests.advanceIterator();
        }

    }


    private void addFriend(User me,User friend){
        me.addFriend(friend);
        friend.addFriend(me);
        network.addUndirectedEdge(me.getID(),friend.getID());
    }

    private void removeFriend(User me, User friend){
        me.removeFriend(friend);
        friend.removeFriend(me);
        network.removeUndirectedEdge(me.getID(),friend.getID());

        
    }

    public void checkNetworkSize(){
        if((double)(hashName.getNumElements()/expectedUsers)>2/3){
            expectedUsers = (int) Math.ceil((double) bstUsers.getSize()*3/2);
            hashName = new Hash<>((int) Math.ceil((double) expectedUsers*3/2));
            for(User user:bstUsers.getArray()){
                hashName.insert(user);
            }
            network.resize(expectedUsers);
        }

        if((double)(hashInterests.getNumElements()/expectedInterests)>2/3){
            expectedInterests = (int) Math.ceil((double)hashInterests.getNumElements()*3/2);
            Hash<Interest> newhashInterests = new Hash<>((int) Math.ceil((double)hashInterests.getNumElements()*3/2));
            for(Interest interest:hashInterests.getArray()){
                newhashInterests.insert(interest);
            }
            hashInterests=newhashInterests;

        }
    }


    private User searchByNameHelper(String name){
    	User result = hashName.get(new User(name));
        return result;
    }

    private BST<User> searchByInterestHelper(String interest){
        BST<User> result = hashInterests.get(new Interest(interest)).getUsers();
        if (result.isEmpty()) throw new NoSuchElementException();
        return result;
    }

    //User Operations
    public void viewAllFriends(){
        System.out.println("<Friends of "+me.getFirstName()+">");
        me.getFriends().inOrderPrint();
    }

    public void viewAFriend(){
        Scanner scanner;
        scanner = new Scanner(System.in);
        System.out.println("Type a name to view: ");
        String name = scanner.nextLine();

        User friend;
        try {
            friend= hashName.get(new User(name));
        }catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("You are not a friend of "+name);
            return;
        }

        if(me.isFriend(friend)){
            System.out.println("<Profile of "+name+">");
            System.out.println("First Name: "+friend.getFirstName());
            System.out.println("Last Name: "+friend.getLastName());
            System.out.println("City: "+friend.getCity());
            System.out.println("Interests: ");
            friend.getInterests().printNumberedList();
            System.out.println("Friends: ");
            friend.getFriends().inOrderPrint();
        }else {
            System.out.println("You are not a friend of "+name);
        }

    }

    public void removeAFriend(){
        Scanner scanner;
        scanner = new Scanner(System.in);
        System.out.println("Type a name to unfriend: ");
        try {
        	removeFriend(me,hashName.get(new User(scanner.nextLine())));
        }catch(Exception ex) {
        	System.out.println("Invalid Friend!");
        }
        
    }

    public void searchByName(){
        Scanner scanner;
        scanner = new Scanner(System.in);
        System.out.print("Enter Name: ");
        String username = scanner.nextLine();
        try{
            User result = searchByNameHelper(username);
            if(me.isFriend(result)){
                System.out.println(result+ " is already your friend!");
            }else {
                System.out.println(result+ " is not your friend. Want to add? y/n");
                if(scanner.nextLine().toLowerCase().equals("y")){
                	addFriend(me,result);
                }
            }
        }catch (Exception ex){
            System.out.println("No result Found");
        }

    }

    public void searchByInterest(){
        Scanner scanner;
        scanner = new Scanner(System.in);
        System.out.print("Enter Interest: ");
        String interest = scanner.nextLine();
        try {
            BST<User> result = searchByInterestHelper(interest);
            System.out.println("These are the results:");
            ArrayList<User> resultArray = result.getArray();
            for(int i=0;i<resultArray.size();i++){
                System.out.println(resultArray.get(i)+(resultArray.get(i).equals(me)?" <you>":"") +(me.isFriend(resultArray.get(i)) ?(" <already your friend>"):"") );
            }
            System.out.println("Type the name to add: ");
            addFriend(me, hashName.get(new User(scanner.nextLine())));
        }catch (NoSuchElementException ex){
            System.out.println("No result Found");
        }
    }

    public void getFriendRecommendations(){
        network.BFS(me.getID());
        int maxIndex = 0;
        ArrayList<User> allusers = bstUsers.getArray();
        ArrayList<User> rankUser = new ArrayList<>();
        ArrayList<Integer> rankBySeperation= new ArrayList<>();
        ArrayList<Integer> rankByInterest= new ArrayList<>();
        ArrayList<Integer> rankOverall= new ArrayList<>();
        for(int i=0;i<allusers.size();i++){
            System.out.println(allusers.get(i));//+" dist:"+network.getDistance(allusers.get(i).getID()));
            if(!me.isFriend(allusers.get(i)) && !me.equals(allusers.get(i))){
                rankUser.add(allusers.get(i));
                rankBySeperation.add(network.getDistance(allusers.get(i).getID()));
                rankByInterest.add(diffInterests(me,allusers.get(i)));
            }
        }

        for(int i=0;i<rankBySeperation.size();i++){
            int index = rankBySeperation.get(i)+rankByInterest.get(i);
            if(index>maxIndex) maxIndex = index;
            rankOverall.add(index);
        }

        System.out.println("<People You May Know>");
        for(int i=0;i<maxIndex;i++){
            for(int j=0;j<rankOverall.size();j++ ){
                if(rankOverall.get(j)==i){
                    System.out.println(rankUser.get(j));
                }
            }
        }

    }

    private int diffInterests(User user1,User user2){
        int totalInterests = user1.getInterests().getLength() + user2.getInterests().getLength();
        List<String> totalInterestSet = new List<>();
        user1.getInterests().pointIterator();
        user2.getInterests().pointIterator();
        while(!user1.getInterests().offEnd()){
            if(totalInterestSet.linearSearch(user1.getInterests().getIterator())==-1){
                totalInterestSet.addLast(user1.getInterests().getIterator());
            }
            user1.getInterests().advanceIterator();
        }
        while(!user2.getInterests().offEnd()){
            if(totalInterestSet.linearSearch(user2.getInterests().getIterator())==-1){
                totalInterestSet.addLast(user2.getInterests().getIterator());
            }
            user2.getInterests().advanceIterator();
        }
        return totalInterests-totalInterestSet.getLength();
    }
    
    public void createAccount() {
    	Scanner scanner;
        scanner = new Scanner(System.in);
        String[] data = new String[6];
        System.out.print("Enter Your Name:\n");
        data[0] = scanner.nextLine();
        System.out.print("Enter User Account:\n");
        data[1] = scanner.nextLine();
        System.out.print("Enter Password:\n");
        data[2] = scanner.nextLine();
        System.out.print("Enter City:\n");
        data[3] = scanner.nextLine();
        System.out.print("Enter Interest 1:\n");
        data[4] = scanner.nextLine();
        System.out.print("Enter Interest 2:\n");
        data[5] = scanner.nextLine();
        
        List<String> interest = new List<>();
        interest.addLast(data[4]);
        interest.addLast(data[5]);
        addUser(data[0].split(" ")[0],data[0].split(" ")[1],data[1],data[2],data[3],interest);

        loginHelper(data[1],data[2]);
        
    }

    public void addUsers(){
        Scanner scanner;
        scanner = new Scanner(System.in);
        // menu
        System.out.print("Enter Data:\n");
        String tmp = "";
        for (int i=0;i<11;i++){
            tmp+=scanner.nextLine()+"\n";
        }
        String[] alldata = tmp.split("\n");
        ArrayList<ArrayList<String>> friendslist = new ArrayList<>();
        for(String eachuser:alldata){
        	if(eachuser.replace("\n","").replace("\t","").replace("\r","").replace(" ","")=="")break;
            String[] data = eachuser.split("\t");
            List<String> interest = new List<>();
            interest.addLast(data[4]);
            interest.addLast(data[5]);
            addUser(data[0].split(" ")[0],data[0].split(" ")[1],data[1],data[2],data[3],interest);

            ArrayList<String> me = new ArrayList<>();
            me.add(data[0]);
            for (int i=6;i<data.length;i++){
                if(!data[i].equals("") && !data[i].equals(" ")) me.add(data[i]);
            }
            friendslist.add(me);
        }

        for(ArrayList<String> people:friendslist){
            for (int j=1;j<people.size();j++){
                User dummyme = new User(people.get(0));
                User dummyfriend = new User(people.get(j));
                addFriend(hashName.get(dummyme), hashName.get(dummyfriend));
            }
        }
    }
    
    public void addUsersString(String tmp){
        String[] alldata = tmp.split("\r\n");
        ArrayList<ArrayList<String>> friendslist = new ArrayList<>();
        for(String eachuser:alldata){
        	if(eachuser.replace("\n","").replace("\t","").replace("\r","").replace(" ","")=="")break;
            String[] data = eachuser.split("\t");
            for(int i=0;i<data.length;i++) {
            	data[i] = data[i].replace("\t", "").replace("\r", "");
            }
            
            List<String> interest = new List<>();
            interest.addLast(data[4]);
            interest.addLast(data[5]);
            addUser(data[0].split(" ")[0],data[0].split(" ")[1],data[1],data[2],data[3],interest);

            ArrayList<String> me = new ArrayList<>();
            me.add(data[0]);
            for (int i=6;i<data.length;i++){
                if(!data[i].equals("") && !data[i].equals(" ")) me.add(data[i]);
            }
            friendslist.add(me);
        }

        for(ArrayList<String> people:friendslist){
            for (int j=1;j<people.size();j++){
                User dummyme = new User(people.get(0));
                User dummyfriend = new User(people.get(j));
                addFriend(hashName.get(dummyme), hashName.get(dummyfriend));
            }
        }
    }

    public void removeAUser(){

    }

    public boolean loginHelper(String userName, String password) {
        ArrayList<User> userarray = bstUsers.getArray();
        for (User eachuser : userarray){
            if(eachuser.getUserName().equals(userName)){
                if(eachuser.getPassword().equals(password)){
                    me = eachuser;
                    return true;
                }
            }
        }
        return false;
    }

    public void login(){
        Scanner scanner;
        scanner = new Scanner(System.in);
        
        System.out.print("Create Account(C) or Login(L)?  ");
        String selection = scanner.nextLine().toUpperCase();
        switch(selection) {
        	case "C":
        		this.createAccount();
        		break;
        	case "L":
                while(true){
                    System.out.print("Enter Username:");
                    String username = scanner.nextLine();
                    System.out.print("Enter Password:");
                    String password = scanner.nextLine();

                    if(loginHelper(username,password)){
                        break;
                    }else {
                        System.out.print("Wrong Username or Password! Please try again\n\n");
                    }
                }
        	default:
        	
        
        }
        	
        


    }


    public void logout() {
        this.me=null;
    }
}
