package com.group18.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This service will be responsible of retrieving the message of the day,
 * at any particular time of day.
 *
 * @author danielturato
 */
public class MessageOfTheDayService {

    /**
     * URL in which when a GET request is called on, will return a random puzzle
     */
    public static final String PUZZLE_URL = "http://cswebcat.swan.ac.uk/puzzle";

    /**
     * URL in which when a GET request is called on, will return the final message of the day
     */
    public static final String MESSAGE_URL = "http://cswebcat.swan.ac.uk/message";

    /**
     * Logger used to log messages to the console
     */
    private static final Logger LOGGER = Logger.getLogger("MessageOfTheDayService");


    /**
     * Used to retrieve the message of the day
     * @return The message of the day
     */
    public static String getMessageOfTheDay() {
        try {
            String puzzle = sendGETRequest(PUZZLE_URL);
            String solvedPuzzle = solvePuzzle(puzzle);

            String queryParams =
                    String.format("solution=%s", URLEncoder.encode(solvedPuzzle, "UTF-8"));

            return sendGETRequest(MESSAGE_URL, queryParams);

        } catch (IOException ex) {
            LOGGER.log(Level.WARNING,"Unable to retrieve the message of the day", ex);

            return "";
        }

    }

    /**
     * Solves the puzzle retrieved from the puzzle URI
     * @param puzzle The puzzle to be solved
     * @return The solved puzzle
     */
    private static String solvePuzzle(String puzzle) {
        int shiftValue = 1;
        StringBuilder solvedPuzzle = new StringBuilder();

        for (int i = 0; i < puzzle.length(); i++) {
            char currentLetter = puzzle.charAt(i);

            if (currentLetter == 'A' && shiftValue == -1) {
                solvedPuzzle.append('Z');
            } else if (currentLetter == 'Z' && shiftValue == 1) {
                solvedPuzzle.append('A');
            } else {
                solvedPuzzle.append((char) (currentLetter + shiftValue));
            }

            shiftValue = shiftValue == 1 ? -1 : 1;
        }

        return solvedPuzzle.toString();
    }

    /**
     * This method is used to send all GET requests needed to get the MessageOfTheDay
     * @param endpoint The endpoint used for the request
     * @return The response returned from the request
     * @throws IOException A possible error that could occur during the request
     */
    private static String sendGETRequest(String endpoint) throws IOException {
        URL url = new URL(endpoint);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return handleResponse(connection);
    }

    /**
     * The is method used to send a GET request that includes query parameters
     * @param endpoint The endpoint used for the request
     * @param queryParams The query params added to the endpoint
     * @return The response returned from the request
     * @throws IOException A possible error that could occur during the request
     */
    private static String sendGETRequest(String endpoint, String queryParams) throws IOException {
        endpoint = endpoint + "?" + queryParams;

        return sendGETRequest(endpoint);
    }

    /**
     * Handles the response of a GET request on the MessageOfTheDay service
     * @param connection The connection, containing the required response
     * @return The response of the GET request
     * @throws IOException A possible error that could occur, during the extraction of the response
     */
    private static String handleResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            StringBuilder response = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());

            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }

            LOGGER.log(Level.INFO, "Successfully retrieved response of: " + response.toString());
            return response.toString();

        } else {

            LOGGER.log(Level.WARNING, "Received HTTP status code: " + responseCode);
            return "";
        }
    }
}
