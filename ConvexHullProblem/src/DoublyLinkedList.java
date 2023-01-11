//Generic DoublyLinkedList

public class DoublyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;

    public DoublyLinkedList()
    {
        head = null;
        tail = null;
    }

    // Accessors
    public Node<T> getHead() { return head; }
    public Node<T> getTail() { return tail; }
    public T getHeadData() { return (head == null) ? null : head.getData(); }
    public T getTailData() { return (tail == null) ? null : tail.getData(); }

    // Modify
    public void addFirst(T data)
    {
        Node<T> temp = new Node<T>(data, head, null);

        if (head != null)
        {
            head.setPrev(temp);
        }
        
        head = temp;
        if (tail == null)
        {
            tail = head;
        }
    }

    public void addLast(T data)
    {
        Node<T> temp = new Node<>(data, null, tail);

        if (tail != null)
        {
            tail.setNext(temp);
        }

        tail = temp;
        if (head == null)
        {
            head = tail;
        }
    }

    public T removeFirst()
    {
        T temp = null;

        if (isEmpty()) 
        {
            return null;
        }
        else if (head == tail)
        {
            temp = getHeadData();
            head = tail = null;
        }
        else
        {
            temp = getHeadData();
            head = head.getNext();
            head.setPrev(null);
        }
        
        return temp;
    }
    
    public T removeLast()
    {
        T temp = null;

        if (isEmpty()) 
        {
            return null;
        }
        else if (head == tail)
        {
            temp = getHeadData();
            head = tail = null;
        }
        else
        {
            temp = getTailData();
            tail = tail.getPrev();
            tail.setNext(null);
        }

        return temp;
    }
    
    // Assumes instance of n is the same as in list
    public boolean remove(Node<T> n)
    {
        if (isEmpty()) return false;

        Node<T> next = n.getNext();
        Node<T> prev = n.getPrev();

        if (prev == null) {
            head = next;
        }
        else
        {
            prev.setNext(next);
        }

        if (next == null)
        {
            tail = prev;
        }
        else
        {
            next.setPrev(prev);
        }

        n.setNext(null);
        n.setPrev(null);

        return true;
    }

    // Attaches the head/tail if possible (add/remove will break, which we can fix, but we don't need it)
    // [If curious, why not try to make a fully functional doubly circular linked list as an exercise :) ]
    //
    // NOTE: You don't need to use this in your work, it's required in the provided code
    public boolean makeCircular()
    {
        if (isEmpty() || head == tail) return false;
        tail.setNext(head);
        head.setPrev(tail);

        return true;
    }

    public boolean isEmpty() { return head == null; }
}