package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import main.java.interfaces.TaskManager;
import main.java.interfaces.UserManager;
import main.java.utils.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public class HttpUserServer {

	public static final int PORT = 8080;

	private HttpServer server;
	private Gson gson;

	private TaskManager taskManager;
	private UserManager userManager;

	public HttpUserServer() throws IOException{
		this(Managers.getDefaultUserManager());
	}

	public HttpUserServer(UserManager userManger) throws IOException {
		this.userManager = userManger;
		this.taskManager = userManger.getTaskManager();
		gson = Managers.getGson();
		server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
		server.createContext("/api/v1/users", this::handleUsers);
	}

	public void start() {
		System.out.println("Запущен локальный сервер на порту: " + PORT);
		server.start();
	}

	public void stop() {
		server.stop(0);
		System.out.println("UserServer has been stoped at port: " + PORT);
	}

	public String readText(HttpExchange httpExchange) throws IOException {
		return new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
	}

	public static void main(String[] args) throws IOException {
		HttpUserServer userServer = new HttpUserServer();
		userServer.start();
//		userServer.stop();
	}

	public void sendText(HttpExchange httpExchange, String response) throws IOException {
		byte[] responseByte = response.getBytes();
		Headers headers = httpExchange.getResponseHeaders();
		headers.add("Content-Type", "apllication/json;charset=utf-8");
		httpExchange.sendResponseHeaders(200, responseByte.length);
		OutputStream outputStream = httpExchange.getResponseBody();
		outputStream.write(responseByte);
	}

	private void handleUsers(HttpExchange httpexchange1) {
	}


}
