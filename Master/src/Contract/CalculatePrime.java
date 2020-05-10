package Contract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * file name: CalculatePrime.java
 * @author aman 
 * Class file for calculating Prime of number
 */

public class CalculatePrime implements Task, Serializable {

    // Fields for number and calculating the prime numbers
    private final int number;
    private final List<Integer> result = new ArrayList<Integer>();

    // Constructor
    public CalculatePrime(int number) {
        this.number = number;
    }
    // Method that checks prime
    public boolean isPrime(int n) {
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
    
    // from Task interface
    @Override
    public List<Integer> getResult() {
        return result;
    }
    
    // from Task interface
    @Override
    public void executeTask() {
        for (int i = 2; i < number; i++) {
            if (isPrime(i) == true) {
                result.add(i);
            }
        }
    }

}
