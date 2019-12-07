package com.group18.model.entity;

import com.group18.model.Graph;
import com.group18.model.Node;
import com.group18.model.RouteNode;
import com.group18.exception.ShortestPathNotFoundException;
import com.group18.model.Direction;
import com.group18.model.Level;
import com.group18.model.cell.*;

import java.awt.Point;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.group18.model.Direction.*;

/**
 * This enemy moves towards the User by finding the shortest viable path.
 *
 * @author danielturato nothandotshuma riyagupta
 */
public class SmartTargetingEnemy extends Enemy {

    /**
     * Get the user's next direction they wish to move in.
     * This involves calculating the shortest viable path to the User
     * If a path is not found, the enemy then must choose a random but valid direction
     * @param user The current user playing on the level
     * @param level The level this enemy is associated with
     * @return The direction they wish to move in next
     */
    @Override
    public Direction getNextDirection(User user, Level level) {
        Node start = this.getCurrentCell();
        Node target = user.getCurrentCell();
        Graph graph = level.getGraph();

        try {
            List<Node> shortestPath = findShortestPath(graph, start, target);

            if (shortestPath.size() > 1) {
                return calculateDirection(shortestPath.get(1).getPosition());
            }

            return IDLE;

        } catch (ShortestPathNotFoundException ex) {

            return calculateRandomDirection(level);

        }

    }

    /**
     * Calculates the shortest path from a start node to a target node
     * @param start The start node
     * @param target The target node
     * @return A list containing the nodes on the shortest path.
     */
    private List<Node> findShortestPath(Graph graph, Node start, Node target) throws ShortestPathNotFoundException {
        //By using a priority queue, we will always have the node which has the shortest estimated
        //time at the head.
        Queue<RouteNode> unvisitedNodes = new PriorityQueue<>();
        Map<Node, RouteNode> allNodes = new HashMap<>();

        // Initialise starting node and add it to the queue of unvisited nodes
        RouteNode startRouteNode = new RouteNode(start, null, 0, computeDistance(start, target));
        unvisitedNodes.add(startRouteNode);
        allNodes.put(start, startRouteNode);

        while (!unvisitedNodes.isEmpty()) {
            RouteNode currentNode = unvisitedNodes.poll();

            // If the node we are visiting is our target
            if (currentNode.getCurrent().equals(target)) {
                return getShortestPath(allNodes, currentNode);
            }

            // Iterate over each connecting node, calculating it's total estimated distance
            // then adding it to the Priority Queue.
            graph.getConnections(currentNode.getCurrent()).forEach(connection -> {

                RouteNode nextNode = allNodes.getOrDefault(connection, new RouteNode(connection));
                allNodes.put(connection, nextNode);

                double newDistanceFromStart =
                        currentNode.getDistanceFromStart() + computeDistance(currentNode.getCurrent(), connection);

                // If the node is a valid path and has a new found shorter distance
                if (newDistanceFromStart < nextNode.getDistanceFromStart() && isValidNode(connection)) {
                    nextNode.setParent(currentNode.getCurrent());
                    nextNode.setDistanceFromStart(newDistanceFromStart);
                    nextNode.setEstimatedDistanceToTarget(newDistanceFromStart + computeDistance(connection, target));

                    unvisitedNodes.add(nextNode);
                }
            });

        }

        // If the queue is empty and no path has been returned, we must not be able to find one.
        throw new ShortestPathNotFoundException("No shortest path found.");

    }

    /**
     * Calculates the direction this enemy should move in based on the
     * their position and another
     * @param nextPoint The position the enemy wants to move to
     * @return The direction this enemy needs to move in, to reach the point required
     */
    private Direction calculateDirection(Point nextPoint) {
        Point currentPosition = this.getCurrentCell().getPosition();
        int currentPosX = (int) currentPosition.getX();
        int currentPosY = (int) currentPosition.getY();
        int nextPosX = (int) nextPoint.getX();
        int nextPosY = (int) nextPoint.getY();

        if (currentPosX < nextPosX) {
            return RIGHT;
        } else if (currentPosX > nextPosX) {
            return LEFT;
        } else if (currentPosY > nextPosY) {
            return UP;
        } else if (currentPosY < nextPosY) {
            return DOWN;
        }

        return IDLE;
    }

    /**
     * Calculates a random valid valid direction for this enemy to move in
     * @param level The level this enemy is associated with
     * @return The valid direction they should move in
     */
    private Direction calculateRandomDirection(Level level) {
        List<Cell> adjacentCells = level.getAdjacentCells(this.getCurrentCell());

        List<Cell> validCells =
                adjacentCells.stream()
                        .filter(cell -> !(cell instanceof Wall || cell instanceof Goal ||
                                cell instanceof Element || cell instanceof Door || cell instanceof Teleporter))
                        .collect(Collectors.toList());

        if (validCells.size() < 1) {
            return IDLE;
        }

        List<Direction> validDirections =
                validCells.stream()
                        .map(Cell::getPosition)
                        .map(this::calculateDirection)
                        .collect(Collectors.toList());


        Random random = new Random();

        return validDirections.get(random.nextInt(validDirections.size()));
    }

    /**
     * Compute the Manhattan Distance between two nodes in a graph
     * @param start The start node
     * @param target The target node
     * @return The distance between the start and target node
     */
    private double computeDistance(Node start, Node target) {

        return Math.abs(start.getPosition().getX() - target.getPosition().getX()) +
                Math.abs(start.getPosition().getY() - target.getPosition().getY());
    }

    /**
     * Compute the steps in the shortest path by starting at the end of the path, then
     * backtracking looking at each nodes parent until we reach the start of the path.
     * @param allNodes All the nodes that were looked at during the computation
     * @param end The end node of the shortest path
     * @return A list of steps on how to go from the start of a path to it's end
     */
    private List<Node> getShortestPath(Map<Node, RouteNode> allNodes, RouteNode end) {
        List<Node> route = new ArrayList<>();
        RouteNode currentNode = end;

        do {
            route.add(0, currentNode.getCurrent());
            currentNode = allNodes.get(currentNode.getParent());

        } while (currentNode != null);

        return route;
    }

    /**
     * Check if this a node is valid for this Enemy to move on to
     * @param node The node the enemy wants to check is valid
     * @return Boolean suggesting if the node is valid
     */
    private boolean isValidNode(Node node) {
        boolean hasItem = false;
        if (node instanceof Ground) {
            hasItem = ((Ground) node).hasItem();
        }

        // An enemy can not move on to a Wall, Goal, Element or Door cell.
        return !(node instanceof Wall || node instanceof Goal ||
                node instanceof Element || node instanceof Door || hasItem);
    }
}
