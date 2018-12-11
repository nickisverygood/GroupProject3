

import java.io.Serializable;

public class User implements Comparable<User> , Serializable {
    private int id;
	private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String city;
    private BST<User> friends;
    private List<String> interests;

    public User(String name){
        this.firstName = name.split(" ")[0];
        this.lastName = name.split(" ")[1];
        this.userName = "";
        this.password = "";
        this.city = "";
        this.friends = new BST<>();
        this.interests = new List<>();
        this.id = -1;
    }

    public User(String firstName,String lastName,String userName,
    		String password,String city,List<String> interests,int id){
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.userName = userName;
    	this.password = password;
    	this.city = city;	
    	this.friends = new BST<>();
        this.interests = new List<>(interests);
        this.id = id;
    }
/*accossor */
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getUserName() {
        return this.userName;
    }
    public String getPassword() {
        return this.password;
    }
    public String getCity() {
        return this.city;
    }
    public int getID(){return this.id;}
    public BST<User> getFriends() {
    	return friends;
    }
    public List<String> getInterests(){
    	return this.interests;
    }
   
/*Mutator */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setlastName(String lastName) {
        this.lastName = lastName;
    }
    public void setuserName(String userName) {
        this.userName = userName;
    }
    public void setpassword(String password) {
        this.password = password;
    }
    public void setcity(String city) {
        this.city = city;
    }
    public void setFriends(BST<User> friends) {
		this.friends = new BST<User>(friends);
	}
    public void setInterests(List<String> interests) {
		this.interests = new List<String>(interests);
	}

	public void addFriend(User friend){
		if(!isFriend(friend)) {
			friends.insert(friend);
		}
    }
    public void removeFriend(User friend){
    	if(isFriend(friend)) {
    		friends.remove(friend);
    	}
    }

    public boolean isFriend(User friend){
        return friends.search(friend);
    }

 /*Addditional operators */ 
    @Override public String toString() {
//        String result = ":FirstName " + firstName
//                + "\nLastName: " + lastName
//                + "\nUserName: " + userName
//                + "\nPassWord: " + password
//                + "\nCity: " + city
//                +"\nFriendList: \n" + this.friends.preOrderPrint()
//        		+"\nInterestsLiest: : \n";
//        interests.pointIterator();
//        for(int i = 0 ; i < interests.getLength();i++) {
//        	result += "Interests"+i+ ": "+ getInterests()+ "\n";
//        	interests.advanceIterator();
//        }
        return firstName+" "+lastName;
    }
    
    
    @Override public boolean equals(Object o) {
    	if( o == this ) {
    		return true;
    	}else if( !(o instanceof User)) {
    		return false;
    	}else {
    		@SuppressWarnings("unchecked")
			User Cmp = (User)o;
    		if(Cmp.firstName != this.firstName || Cmp.lastName != this.lastName) {
    			return false;
    		}
    		 return true;
    	}
       
    }
    
	@Override
	public int compareTo(User o) {
		if (this.firstName.compareTo(o.firstName) != 0) {
            return this.firstName.compareTo(o.firstName);
        } else if(this.lastName.compareTo(o.lastName) != 0) {
                return this.lastName.compareTo(o.lastName);
        }else {
        }
 		 return 0;
	}
    
	@Override public int hashCode() {
    	String key = firstName + lastName; //define key for this class
    	int sum = 0;
    	for (int i = 0; i < key.length(); i++) {
    	sum += (int) key.charAt(i);
    	}
    	return sum;
    }
}
