package com.example.tcc;



public class List<T> {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public List() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public List(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Capacidade inicial não pode ser negativa: " + initialCapacity);
        }
        elements = new Object[initialCapacity];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public T get(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Índice: " + position + ", Tamanho: " + size);
        }
        return (T) elements[position];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(T element) {
        ensureCapacity();
        elements[size++] = element;
    }

    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Índice: " + index + ", Tamanho: " + size);
        }
        ensureCapacity();

        // Mover elementos para a direita
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    public boolean remove(T element) {
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice: " + index + ", Tamanho: " + size);
        }

        @SuppressWarnings("unchecked")
        T removedElement = (T) elements[index];

        // Mover elementos para a esquerda
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null; // Limpar referência

        return removedElement;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    public void addAll(List<T> other) {
        for (int i = 0; i < other.size(); i++) {
            add(other.get(i));
        }
    }

    // Método para adicionar todos os elementos de uma java.util.List
    public void addAll(java.util.List<T> other) {
        for (T element : other) {
            add(element);
        }
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            int newCapacity = elements.length == 0 ? DEFAULT_CAPACITY : elements.length * 2;
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}