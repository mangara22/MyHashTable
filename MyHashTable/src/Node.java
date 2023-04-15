public class Node <T>{
    private T data;
    private Node<T> next;

    public Node () {};
    public Node (T newData, Node<T> nextNode){
        this.data = newData;
        this.next = nextNode;
    }

    public T getData(){
        return this.data;
    }
    public Node<T> getNext(){
        return this.next;
    }
    public void setData(T newData){
        this.data = newData;
    }
    public void setNext(Node<T> nextNode){
        this.next = nextNode;
    }
}
