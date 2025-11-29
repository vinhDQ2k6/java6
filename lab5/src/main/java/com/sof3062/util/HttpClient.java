package com.sof3062.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

public class HttpClient {

  public static HttpURLConnection openConnection(String url, String method)
    throws IOException {
    try {
      var connection = (HttpURLConnection) URI.create(url)
        .toURL()
        .openConnection();

      connection.setRequestProperty(
        "Content-Type",
        "application/json; charset=utf-8"
      );
      connection.setRequestMethod(method);
      return connection;
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  public static byte[] readData(HttpURLConnection connection)
    throws IOException {
    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
      throw new IOException(
        "Error: " +
        connection.getResponseCode() +
        " - " +
        connection.getResponseMessage()
      );
    }
    try (var inputStream = connection.getInputStream()) {
      return inputStream.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    } finally {
      connection.disconnect();
    }
  }

  public static byte[] postData(HttpURLConnection connection, byte[] data)
    throws IOException {
    connection.setDoOutput(true);
    try (var outputStream = connection.getOutputStream()) {
      outputStream.write(data);
      outputStream.flush();
      return readData(connection);
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    } finally {
      connection.disconnect();
    }
  }
}
