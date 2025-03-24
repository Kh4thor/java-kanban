package main.java.utils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalTime> {
	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public void write(JsonWriter writer, LocalTime value) throws IOException {
		writer.value(value.format(timeFormatter));
	}

	@Override
	public LocalTime read(JsonReader reader) throws IOException {
		String timeString = reader.nextString();
		return LocalTime.parse(timeString, timeFormatter);
	}
}
