

import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        Scanner scanner;
        scanner = new Scanner(System.in);
        String[] data = scanner.nextLine().split("\t");
        for (String s:data){
            System.out.print(s+"\n");
        }

    }
}
