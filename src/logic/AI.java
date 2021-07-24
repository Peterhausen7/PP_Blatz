package logic;

import java.util.*;

/**
 * Class representing a turn for a player, containing the position to push, the position to move to and
 * the rotation to be used for the push operation.
 */
class AIMove {
    /** Position to push */
    private final Position pushPos;
    /** Position to move to after the push */
    private final Position playerMove;
    /** Rotation to be used for the push */
    private final Rotation freeCorridorRot;

    /**
     * Constructor creating an AI Move.
     * @param pushPos - pos to push
     * @param playerMove - pos to move to
     * @param freeCorridorRot - rot to be used
     */
    AIMove(Position pushPos, Position playerMove, Rotation freeCorridorRot) {
        this.pushPos = pushPos;
        this.playerMove = playerMove;
        this.freeCorridorRot = freeCorridorRot;
    }

    /**
     * Gets the position to push
     * @return - the position to push
     */
    Position getPushPos() {
        return pushPos;
    }

    /**
     * Gets the position to move to
     * @return - the position to move to
     */
    Position getPlayerMove() {
        return playerMove;
    }

    /**
     * Gets the rotation to be used
     * @return - rotation to be used
     */
    Rotation getFreeCorridorRot() {
        return freeCorridorRot;
    }
}


/**
 * Class for generic AI
 */
public class AI {

    /** The player this AI belongs to */
    private final Player player;

    /**
     * Constructor creating the AI.
     * @param player - the player the AI belongs to
     */
    AI(Player player) {
        this.player = player;
    }

    /**
     * Gets the enum type of this AI
     * @return - the enum type of this AI
     */
    PlayerType getType() {
        return PlayerType.AI;
    }


    /**
     * Finds a move that affects either the player or the treasure. Used if the AI can't find a move that brings it
     * closer to its target. If the player and the target are not movable, a random push operation will be used.
     * Priority: push the player > push the target > push anything.
     * Rotation used is always random.
     * @param targetPos - Position of the players target
     * @param field - the logical field consisting of corridors
     * @param pushablePositions - list of the pushable Position's
     * @return a new AIMove with the priority above.
     */
    private AIMove findMoveMovingPlayerOrTarget(Position targetPos, Field field, List<Position> pushablePositions) {
        Random rng = new Random();
        Player fakePlayer = new Player(player.getPos());
        //Is player pushable?
        if (fakePlayer.getPos().isPushableRowCol(field.getFieldSize())) {
            for (Position pushPos : pushablePositions) {
                //find a pushMove affecting the player
                if (fakePlayer.getPos().overlapsRowCol(pushPos, field.getFieldSize())) {
                    //push the player with the first found pushMove to get its new location
                    field.pushFromPos(pushPos, fakePlayer);
                    //Return the AIMove, the player does not move after the push operation.
                    return new AIMove(pushPos, fakePlayer.getPos(),
                            Rotation.values()[rng.nextInt(Rotation.values().length)]);
                }
            }
        }
        //Is target pushable?
        if (targetPos.isPushableRowCol(field.getFieldSize())) {
            for (Position pushPos : pushablePositions) {
                //find a pushMove affecting the target
                if (targetPos.overlapsRowCol(pushPos, field.getFieldSize())) {
                    //Return the AIMove, the player does not move after the push operation
                    return new AIMove(pushPos, player.getPos(),
                            Rotation.values()[rng.nextInt(Rotation.values().length)]);
                }
            }
        }
        //If neither the player nor the target were pushable, return a random pushPos, don't move the player
        return new AIMove(pushablePositions.get(rng.nextInt(pushablePositions.size())), player.getPos(),
                Rotation.values()[rng.nextInt(Rotation.values().length)]);
    }

    /**
     * Helper method for finding a move. If the current targetPos is not reachable by any pushMove,
     * this method will be recursively called with it's neighbours as the new targetPos.
     *
     * If there is no way to create a reachable position that is closer or equivalent
     * to the originalTarget then the players current position, the method will stop it's recursion and return null.
     * Otherwise the first closest position to the originalTarget will be returned.
     * @param targetPos - the desired position to reach
     * @param field - the logical field consisting of corridors.
     * @param originalTarget - the original desired position to reach (the players target)
     * @param checked - Set containing the already checked positions
     * @return new AIMove that brings the player closer to its originalTarget
     */
    private AIMove findPushMoveHelper(Position targetPos, Field field, Position originalTarget,
                                        Set<Position> checked) {
        Field tempField;
        Player fakePlayer;
        Position tempTarget;
        int fieldSize = field.getFieldSize();
        //adding the current position to the set, so it does not get checked again
        checked.add(targetPos);

        Position blockedPos = field.getBlockedPos();
        List<Position> pushablePositions = Position.getAllPushablePos(fieldSize);
        //No need to check the blocked position, so it is removed
        pushablePositions.remove(blockedPos);

        for (Position pushPos : pushablePositions) {
            //working with a temporary field, so that the push operation does not have to be reversed
            tempField = new Field(field);
            //creating a fake player with the current player's position to be used for push operations
            fakePlayer = new Player(player.getPos());
            Rotation pushRot = tempField.getFreeCorridor().getRotation();
            tempField.pushFromPos(pushPos, fakePlayer);
            /*
            If the goal is still the originalTarget(treasure or starting corner) then recalculate its position,
            in case the push operation affected it
            */
            if (targetPos == null || targetPos.equals(originalTarget)) {
                tempTarget = tempField.calcCurrentTarget(player);
            } else {
                tempTarget = targetPos;
            }
            //If there is a path to targetPos after the push move, the found move will be returned
            if (tempField.findPath(fakePlayer.getPos(), tempTarget) != null) {
                return new AIMove(pushPos, tempTarget, pushRot);
            }
            //Repeat for every rotation
            for (int i = 0; i < Rotation.values().length - 1; i++) {
                tempField = new Field(field);
                tempField.rotateFreeCorridorLeft();
                fakePlayer = new Player(player.getPos());
                pushRot = tempField.getFreeCorridor().getRotation();
                tempField.pushFromPos(pushPos, fakePlayer);
                if (targetPos == null || targetPos.equals(originalTarget)) {
                    tempTarget = tempField.calcCurrentTarget(player);
                } else {
                    tempTarget = targetPos;
                }
                //If there is a path to targetPos after the push move, the found move will be returned
                if (tempField.findPath(fakePlayer.getPos(), tempTarget) != null) {
                    return new AIMove(pushPos, tempTarget, pushRot);
                }
            }
        }
        /*
        if the original target is null (the target is the free corridor), any move will get the player closer,
        so the (non existent) neighbours won't be checked
        */
        if (originalTarget != null && targetPos != null) {
            //if the target pos is unreachable, its connected neighbours will be checked instead
            int distanceBetweenTargets = Position.distanceBetween(targetPos, originalTarget);
            int distanceToTarget = Position.distanceBetween(player.getPos(), originalTarget);
            //don't continue, if there is no better reachable position
            if (distanceBetweenTargets < distanceToTarget - 1) {
                AIMove result;
                //The best result found so far (determined by distance to original target after moving)
                AIMove bestSoFar = null;
                Map<Direction, Position> neighbours = targetPos.getNeighbours(fieldSize);
                for (Direction dir : Direction.values()) {
                    Position neighbour = neighbours.get(dir);
                    //Connected neighbour and not checked yet
                    if (neighbour != null && !checked.contains(neighbour)) {
                        //recursive call with the neighbour as the new targetPos
                        result = findPushMoveHelper(neighbour, field, originalTarget, checked);
                        if (result != null) {
                            if (bestSoFar == null) {
                                bestSoFar = result;
                            } else {
                                int distanceResult = Position.distanceBetween(result.getPlayerMove(),
                                        originalTarget);
                                int distanceBestSoFar = Position.distanceBetween(bestSoFar.getPlayerMove(),
                                        originalTarget);
                                if (distanceResult < distanceBestSoFar) {
                                    bestSoFar = result;
                                }
                            }
                        }
                    }
                }
                if (bestSoFar != null) {
                    return bestSoFar;
                }
            }
        }
        /*
         Original target is not the freeCorridor and the method is in the first call ->
         targetPos and originalTarget are the same
        */
        if (originalTarget != null && originalTarget.equals(targetPos)) {
            return findMoveMovingPlayerOrTarget(originalTarget, new Field(field), pushablePositions);
        }
        //Should never be reached, if it is somehow reached, the GameLogic will just give control to the GUI/Human
        return null;
    }

    /**
     * Finds a move for the player (push and movement)
     * @param field - the logical field consisting of corridors
     * @return new AIMove that contains the information for a turn
     */
    AIMove findPushMove(Field field) {
        Position targetPos = field.calcCurrentTarget(player);

        return findPushMoveHelper(targetPos, field, targetPos, new HashSet<>());
    }
}
