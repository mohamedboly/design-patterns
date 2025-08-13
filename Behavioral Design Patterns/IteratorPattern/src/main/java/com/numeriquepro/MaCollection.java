package com.numeriquepro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class MaCollection implements Iterable<String> {
    private final List<String> elements = new ArrayList<>();

    public void add(String e) {
        elements.add(e);
    }

    @Override
    public Iterator<String> iterator() {
        return new ForwardIterator();
    }

    public Iterator<String> reverseIterator() {
        return new ReverseIterator();
    }

    private class ForwardIterator implements Iterator<String> {
        private int index = 0;
        public boolean hasNext() { return index < elements.size(); }
        public String next() {
            if (!hasNext()) throw new NoSuchElementException();
            return elements.get(index++);
        }
    }

    private class ReverseIterator implements Iterator<String> {
        private int index = elements.size() - 1;
        public boolean hasNext() { return index >= 0; }
        public String next() {
            if (!hasNext()) throw new NoSuchElementException();
            return elements.get(index--);
        }
    }
}