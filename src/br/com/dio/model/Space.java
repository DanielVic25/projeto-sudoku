package br.com.dio.model;

public class Space {
    private Integer actual;
    private final int expect;
    private final  boolean fixed;


    public Space(int expect, boolean fixed) {
        this.expect = expect;
        this.fixed = fixed;

        if (fixed) {
            actual = expect;
        }
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        if (fixed) return;
        this.actual = actual;
    }

    public void clearSpace() {
        setActual(null);
    }


    public int getExpect() {
        return expect;
    }

    public boolean isFixed() {
        return fixed;
    }
}
