/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.its.is.addi.halal.azmi;

/**
 * @author USER
 */
public class Tes {

    void readCard(int i) throws Exception {
        System.out.println("read card");
    }

    void checkCard(int i) throws RuntimeException {
        System.out.println("check card");
    }

    public static void main(String[] args) {
        int nums1[] = new int[3];
        int nums2[] = {1, 2, 3, 4, 5};
        nums1 = nums2;
        for (int x : nums1) {
            System.out.print(x + ": ");
        }
    }

}
