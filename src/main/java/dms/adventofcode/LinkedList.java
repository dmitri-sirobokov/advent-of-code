package dms.adventofcode;

import java.util.Iterator;

/**
 * Native Java {@link java.util.LinkedList} does not support O(1) operations to insert
 * or delete nodes, that normally LinkedList supposed to do. In native Java LinkedList you would
 * need to iterate objects first that is O(n) operation. Very unexpected for modern language like Java
 * after a few decades. That's the reason of this custom implementation that supports O(1) operation.
 */
public class LinkedList<E> implements Iterable<LinkedList.Node<E>> {

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Node<E>> iterator() {
        return new Iterator<>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Node<E> next() {
                Node<E> result = current;
                current = current.next;
                return result;
            }
        };
    }

    public static final class Node<E> {
        private final E element;
        private Node<E> next;
        private Node<E> prev;
        Node(E element) {
            this.element = element;
        }

        public E getElement() {
            return element;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrev() {
            return prev;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public Node<E> addFirst(E value) {
        Node<E> newNode = new Node<>(value);
        if (head == null) {
            head = tail = newNode;
        } else {
            head.prev = newNode;
            newNode.next = head;
            head = newNode;
        }
        size++;
        return newNode;
    }

    public Node<E> addLast(E value) {
        Node<E> newNode = new Node<>(value);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return newNode;
    }


    public Node<E> addBefore(Node<E> target, E value) {
        if (target == null) {
            return addFirst(value);
        }

        Node<E> newNode = new Node<>(value);
        newNode.next = target;
        newNode.prev = target.prev;

        if (target.prev != null) {
            target.prev.next = newNode;
        } else {
            head = newNode;
        }

        target.prev = newNode;
        size++;
        return newNode;
    }

    public Node<E> addAfter(Node<E> target, E value) {
        if (target == null || target == tail) {
            return addLast(value);
        }

        Node<E> newNode = new Node<>(value);
        newNode.next = target.next;
        newNode.prev = target;

        if (target.next != null) {
            target.next.prev = newNode;
        } else {
            tail = newNode;
        }

        target.next = newNode;
        size++;
        return newNode;
    }


    public void remove(Node<E> node) {
        if (node == null) return;

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        node.prev = null;
        node.next = null;
        size--;
    }

    public int size() {
        return this.size;
    }

}
