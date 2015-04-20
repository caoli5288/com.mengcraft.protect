package com.mengcraft.util;

import java.util.Iterator;

public class ArrayIttor<E> implements Iterator<E> {

	private final E[] array;
	private int cursor;

	@Override
	public boolean hasNext() {
		return array.length > cursor;
	}

	@Override
	public E next() {
		return hasNext() ? array[cursor++] : null;
	}

	public int remain() {
		return array.length - cursor;
	}

	public ArrayIttor(E... array) {
		this.array = array;
	}

}
