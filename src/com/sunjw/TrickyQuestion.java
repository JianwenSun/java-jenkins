package com.sunjw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 4/29/2017.
 */
public class TrickyQuestion {

}

class MinValue {
    public static void main(String[] args) {
        System.out.println(Math.min(Double.MIN_VALUE, 0.0d));
    }
}

class SystemExitOnTryCatch {
    public static void main(String[] args) {
        try {
            System.out.println("Start trying..");
            throw new IllegalStateException();
        } catch (Exception e) {
            System.out.println("Exception on try..");
            System.exit(0);
        } finally {
            System.out.println("Finally..");
        }
    }
}

class CanOverWritePrivateStaticMethodOfAClass {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SuperA superA = new SuperA();
        A a = new A();

        Class<?> _superAClass = SuperA.class;
        Method _superAMethod = _superAClass.getDeclaredMethod("show1");
        _superAMethod.setAccessible(true);
        _superAMethod.invoke(null);

        Class<?> _aClass = A.class;
        Method _aMethod = _aClass.getDeclaredMethod("show1");
        _aMethod.setAccessible(true);
        _aMethod.invoke(null);

        superA.show();
        superA.show1();

        a.show();
        ;
        a.show1();
    }

    static class A {
        public static void show() {
            System.out.println("Class A Show method");
        }

        private static void show1() {
            System.out.println("Class A Show1 method");
        }
    }

    static class SuperA extends A {
        public static void show() {
            System.out.println("Super Class A Show method");
        }

        private static void show1() {
            System.out.println("Super Class A Show1 method");
        }
    }
}

class WillExpressionThrowException {
    public static void main(String[] args) {
        double result = 1.0 / 0;
        System.out.println(result);

        result = Integer.MAX_VALUE - Integer.MIN_VALUE;
        System.out.println(result);

        double x = Double.NaN;
        if (x == Double.NaN) {
            System.out.println("x equal to Double.Nan");
        } else {
            System.out.println("x not equal to Double.Nan");
        }
    }
}

class WhatWillPrint {
    public static void main(String[] args) throws Exception {
        char[] chars = new char[]{'\u0097'};
        String str = new String(chars);
        byte[] bytes = str.getBytes();
        System.out.println(Arrays.toString(bytes));
    }
}

class OverloadingOverridingTest {
    public static void main(String[] args) {
        Loan cheapLoan = Loan.createLoan("HSBC");
        Loan veryCheapLoan = Loan.createLoan("Citibank", 8.5);
        Loan personalLoan = new PersonalLoan();
        personalLoan.test();
        personalLoan.toString();
    }

    static class Loan {
        private double interestRate;
        private String customer;
        private String lender;

        public void test() {
            throw new NullPointerException();
        }

        public static Loan createLoan(String lender) {
            Loan loan = new Loan();
            loan.lender = lender;
            return loan;
        }

        public static Loan createLoan(String lender, double interestRate) {
            Loan loan = new Loan();
            loan.lender = lender;
            loan.interestRate = interestRate;
            return loan;
        }

        @Override
        public String toString() {
            return "This is Loan by Citibank";
        }
    }

    static class PersonalLoan extends Loan {
        @Override
        public String toString() {
            return "This is Personal Loan by Citibank";
        }

        @Override
        public void test() {
            System.out.println("Super test");
            //super.Test();
        }
    }
}

class AreEqual {
    public static void main(String[] args) {
        if (3 * 0.1 == 0.3) {
            System.out.println("0.1 * 3 == 0.3");
        } else {
            System.out.println("0.1 * 3 != 0.3");
        }

        int a1 = 1000, b1 = 1000;

        System.out.println(a1 == b1);

        Integer i1 = 1000; //it does print `true` with i1 = 1000, but not i1 = 1, and one of the answers explained why.
        Integer i2 = 1000;
        System.out.println(i1 != i2);

        // Prints "true".
        int i11 = 1;
        Integer i12 = new Integer(i11);
        System.out.println(i11 == i12);

// Prints "false".
        int i21 = 0;
        Integer i22 = new Integer(i21);
        i21 += 1;
        System.out.println(i21 == i22);

        int i31 = 0;
        i31 += 1;
        Integer i32 = new Integer(i31);
        System.out.println(i31 == i32);

        double a = 0.1 * 3;
        double b = 0.3;
        double c = 0.3;

        if (c == b) {
            System.out.println("0.1 * 3 == 0.3");
        } else {
            System.out.println("0.1 * 3 != 0.3");
        }

    }

    class SuperThread extends Thread {

    }
}

