//Generic Node

public class Node<T> {
    private T data;
    Node<T> prev;
    Node<T> next;

    public Node()
    {
        data = null;
        prev = null;
        next = null;
    }

    public Node(T data)
    {
        this.data = data;
        next = null;
        prev = null;
    }

    public Node(T data, Node<T> next, Node<T> prev)
    {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    // Accessors
    public T getData() { return data; }
    public Node<T> getNext() { return next; }
    public Node<T> getPrev() { return prev; }

    // Mutators
    public void setData(T data) { this.data = data; }
    public void setNext(Node<T> next) { this.next = next; }
    public void setPrev(Node<T> prev) { this.prev = prev; }

    @Override
    public boolean equals(Object other) 
    {
        if (other == this) return true;
        if (!(other instanceof Node)) return false;

        Node<T> n = (Node<T>) other;

        return this.getData().equals(n.getData());
    }
}