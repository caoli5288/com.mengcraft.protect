package com.mengcraft.protect.util;

import java.util.Iterator;

public class ArrayActor<E> implements Iterator<E> {

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

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public E get(int index) {
		if (index < 00 || index >= array.length) {
			throw new IndexOutOfBoundsException();
		}
		return array[index];
	}

	public int remain() {
		return array.length - cursor;
	}

	public int cursor() {
		return cursor;
	}

	public ArrayActor(E... array) {
		this.array = array;
	}

}
