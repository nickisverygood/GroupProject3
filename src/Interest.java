

import java.io.Serializable;

public class Interest implements Comparable<Interest>, Serializable {
    private String interest;
    private BST<User> commonInterestUsers;

    public Interest(String interest){
        this.interest = interest;
        commonInterestUsers = new BST<>();
    }

    public void insert(User newuser){
        commonInterestUsers.insert(newuser);
    }

    public String getInterest(){
        return interest;
    }
    public BST<User> getUsers(){
        return commonInterestUsers;
    }

    @Override
    public int compareTo(Interest o) {
        return o.interest.compareTo(this.interest);
    }

    @Override
    public boolean equals(Object o) {
        if( o == this ) {
            return true;
        }else if( !(o instanceof Interest)) {
            return false;
        }else {
            @SuppressWarnings("unchecked")
            Interest Cmp = (Interest)o;
            return Cmp.interest.compareTo(this.interest) ==0;
        }
    }

    @Override public int hashCode() {
        String key =interest; //define key for this class
        int sum = 0;
        for (int i = 0; i < key.length(); i++) {
            sum += (int) key.charAt(i);
        }
        return sum;
    }
}
