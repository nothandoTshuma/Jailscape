package com.group18.service;

public class MessageOfTheDayService {

    /**
     * Logger used to log usual messages
     */
    private static final Logger LOGGER = Logger.getLogger("MessageOfTheDayService");

    /**
     * URL in which when a GET request is called on, will return a random puzzle
     */
    public static final String PUZZLE_URL = "http://cswebcat.swan.ac.uk/puzzle";

    /**
     * URL in which when a GET request is called on, will return the final message of the day
     */
    public static final String MESSAGE_URL = "http://cswebcat.swan.ac.uk/message";


    /**
     * Used to retrieve the message of the day
     * @return The message of the day
     */
    public static String getMessageOfTheDay() {
        try {
            String puzzle = sendGETRequest(PUZZLE_URL, Optional.empty());
            String solvedPuzzle = solvePuzzle(puzzle);

            String queryParams =
                    String.format("solution=%s", URLEncoder.encode(solvedPuzzle, "UTF-8"));

            return sendGETRequest(MESSAGE_URL, Optional.of(queryParams));

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
     * @param queryParams The optional query params to be used on the endpoint
     * @return The response returned from the request
     * @throws IOException A possible error that could occur during the request
     */
    private static String sendGETRequest(String endpoint, Optional<String> queryParams) throws IOException {
        URL url = queryParams.isPresent() ?
                new URL(endpoint + "?" + queryParams.get()) : new URL(endpoint);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return handleResponse(connection);
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

            LOGGER.fine("Successfully retrieved response of: " + response.toString());
            return response.toString();

        } else {

            LOGGER.log(Level.WARNING, "Received HTTP status code: " + responseCode);
            return "";
        }
    }
}
