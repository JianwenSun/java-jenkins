package com.sunjw;

/**
 * Created by Administrator on 4/30/2017.
 */
public class EnumTest implements Runnable {
    static {

    }

    @Override
    public void run() {

    }

    public enum Currency {
        PENNY(1) {
            @Override
            public void color() {

            }
        }, NICKLE(5) {
            @Override
            public void color() {

            }
        }, DIME(10) {
            @Override
            public void color() {

            }
        }, QUARTER(25) {
            @Override
            public void color() {

            }
        };

        private int value;

        Currency(int value) {
            this.value = value;
        }

        public abstract void color();
    }
}
