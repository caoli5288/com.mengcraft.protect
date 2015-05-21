package com.mengcraft.util;

public class ArrayBuilder<T> {

    private static final int SIZE_DEFAULT = 8;
    private Object[] array;
    private int cursor;

    public ArrayBuilder() {
        array = new Object[] {};
    }

    public void append(T value) {
        if (cursor == array.length) {
            growArray();
        }
        array[cursor++] = value;
    }

    public int length() {
        return cursor;
    }

    public Object[] build() {
        Object[] output = new Object[cursor];
        while (cursor != 0) {
            output[--cursor] = array[cursor];
        }
        return output;
    }

    public T[] build(T[] input) {
        if (input.length < cursor) {
            throw new ArrayIndexOutOfBoundsException();
        }
        Object[] temp = input;
        while (cursor != 0) {
            temp[--cursor] = array[cursor];
        }
        return input;
    }

    private void growArray() {
        int size = cursor << 1;
        if (size == 0) {
            size = SIZE_DEFAULT;
        }
        Object[] bigger = new Object[size];
        for (int i = 0; i != array.length;) {
            bigger[i] = array[i++];
        }
        this.array = bigger;
    }
}
