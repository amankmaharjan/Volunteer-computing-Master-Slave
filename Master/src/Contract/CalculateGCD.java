package Contract;

import java.io.Serializable;

/**
 * file name: CalculateGCD.java
 * @author aman 
 * Class file for calculating GCD
 */
public class CalculateGCD implements Task, Serializable {
    //  fields to store gcd of first and second number
    long gcd;
    long first, second;
    //  constructor
    public CalculateGCD( long first, long second) {
        this.first = first;
        this.second = second;
    }
    
    // from Task interface
    @Override
    public void executeTask() {
      this.gcd=  findGCD(this.first, this.second);
    }
    // From task interface
    @Override
    public Object getResult() {
        return this.gcd;
    }
    // method to find the GCD value of  two number
    public long findGCD(long first, long second) {
        if (first == 0) {
            return second;
        } else {
            while (second != 0) {
                if (first > second) {
                    first = first- second;
                } else {
                    second = second - first;
                }
            }
            return first;
        }
    }
}
