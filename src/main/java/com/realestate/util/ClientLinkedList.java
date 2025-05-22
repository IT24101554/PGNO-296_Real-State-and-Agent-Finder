package com.realestate.util;

import com.realestate.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientLinkedList {
    private Node head;
    private int size;

    private class Node {
        Client data;
        Node next;

        Node(Client data) {
            this.data = data;
            this.next = null;
        }
    }

    public ClientLinkedList() {
        head = null;
        size = 0;
    }

    public void add(Client client) {
        Node newNode = new Node(client);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public Client getById(Long id) {
        Node current = head;
        while (current != null) {
            if (current.data.getId().equals(id)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    public boolean remove(Long id) {
        if (head == null) return false;

        if (head.data.getId().equals(id)) {
            head = head.next;
            size--;
            return true;
        }

        Node current = head;
        while (current.next != null) {
            if (current.next.data.getId().equals(id)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public boolean update(Client client) {
        Node current = head;
        while (current != null) {
            if (current.data.getId().equals(client.getId())) {
                current.data = client;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public List<Client> toList() {
        List<Client> list = new ArrayList<>();
        Node current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    public int size() {
        return size;
    }
}