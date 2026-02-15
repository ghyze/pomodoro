package nl.ghyze.pomodoro.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Generic JSON serialization utility using Gson.
 * Provides methods for reading and writing JSON files with error handling.
 */
public class JsonSerializer {

    /**
     * Creates a Gson instance configured with pretty printing.
     *
     * @return configured Gson instance
     */
    public static Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Reads and deserializes a JSON file to an object of the specified type.
     *
     * @param file the file to read
     * @param type the class type to deserialize to
     * @param <T> the type parameter
     * @return the deserialized object
     * @throws IOException if reading fails or JSON is invalid
     */
    public static <T> T readJson(Path file, Class<T> type) throws IOException {
        if (!Files.exists(file)) {
            throw new IOException("File does not exist: " + file);
        }

        try (Reader reader = Files.newBufferedReader(file)) {
            Gson gson = createGson();
            T result = gson.fromJson(reader, type);
            if (result == null) {
                throw new IOException("Failed to deserialize JSON from: " + file);
            }
            return result;
        } catch (JsonSyntaxException e) {
            throw new IOException("Invalid JSON in file: " + file + " - " + e.getMessage(), e);
        }
    }

    /**
     * Serializes an object to JSON and writes it to a file.
     *
     * @param file the file to write to
     * @param object the object to serialize
     * @throws IOException if writing fails
     */
    public static void writeJson(Path file, Object object) throws IOException {
        if (object == null) {
            throw new IOException("Cannot serialize null object");
        }

        try (Writer writer = Files.newBufferedWriter(file)) {
            Gson gson = createGson();
            gson.toJson(object, writer);
        } catch (Exception e) {
            throw new IOException("Failed to write JSON to file: " + file + " - " + e.getMessage(), e);
        }
    }
}
