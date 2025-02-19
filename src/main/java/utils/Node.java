package main.java.utils;

import main.java.model.Task;

public class Node<T extends Task> {

	public T data;
	public Node<T> next;
	public Node<T> prev;

	/*
	 * конструктор узла двусвязного списка
	 */
	public Node(Node<T> prev, T task, Node<T> next) {
		this.data = task;
		this.prev = prev;
		this.next = next;
	}

	/*
	 * проверка на наличие следующей ноды
	 */
	public boolean hasNext() {
		if (next != null) {
			return true;
		}
		return false;
	}

	/*
	 * проверка на наличие предыдущей ноды
	 */
	public boolean hasPrev() {
		if (prev != null) {
			return true;
		}
		return false;
	}
}
