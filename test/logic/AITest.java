package logic;

import org.junit.Assert;
import org.junit.Test;

public class AITest {
    private GUIConnector gui = new FakeGUI();
    private Player[] players = { new AI(), new AI(), new AI(), new AI() };
    private GameLogic game = new GameLogic(gui,"L100,I101,L200\n" +
                                                    "I003,I004,I005\n" +
                                                    "L000,I108,L300",
                                                    players);


    @Test
    public void correctRowToTarget() {
        Player player = game.getPlayerX(0);
        player.addTreasure(Treasure.T04);
        game.setFreeCorridor(new Corridor(CorridorType.I));
        player.makePushMove(game);
        Corridor[][] expectedField = new Corridor[3][3];
        expectedField[0][0] = new Corridor(CorridorType.L, Rotation.RIGHT, null);
        expectedField[1][0] = new Corridor(CorridorType.I, Rotation.RIGHT, Treasure.T01);
        expectedField[2][0] = new Corridor(CorridorType.L, Rotation.UPSIDE_DOWN, null);

        //pushed row
        expectedField[0][1] = new Corridor(CorridorType.I, Rotation.NEUTRAL, Treasure.T04);
        expectedField[1][1] = new Corridor(CorridorType.I, Rotation.NEUTRAL, Treasure.T05);
        //pushed in card
        expectedField[2][1] = new Corridor(CorridorType.I, Rotation.NEUTRAL, null);

        expectedField[0][2] = new Corridor(CorridorType.L, Rotation.NEUTRAL, null);
        expectedField[1][2] = new Corridor(CorridorType.I, Rotation.RIGHT, Treasure.T08);
        expectedField[2][2] = new Corridor(CorridorType.L, Rotation.LEFT, null);

        Assert.assertArrayEquals(expectedField, game.getField());
    }

    @Test
    public void correctColToTarget() {
        game = new GameLogic(gui, "L100,I101,L200\n" +
                                       "I003,L204,I005\n" +
                                       "L000,I108,L300", players);
        Player player = game.getPlayerX(0);
        player.addTreasure(Treasure.T04);
        game.setFreeCorridor(new Corridor(CorridorType.L));
        player.makePushMove(game);
        Corridor[][] expectedField = new Corridor[3][3];
        expectedField[0][0] = new Corridor(CorridorType.L, Rotation.RIGHT, null);
        //pushed col
        expectedField[1][0] = new Corridor(CorridorType.L, Rotation.UPSIDE_DOWN, Treasure.T04);
        expectedField[2][0] = new Corridor(CorridorType.L, Rotation.UPSIDE_DOWN, null);

        expectedField[0][1] = new Corridor(CorridorType.I, Rotation.NEUTRAL, Treasure.T03);
        //pushed col
        expectedField[1][1] = new Corridor(CorridorType.I, Rotation.RIGHT, Treasure.T08);
        expectedField[2][1] = new Corridor(CorridorType.I, Rotation.NEUTRAL, Treasure.T05);

        expectedField[0][2] = new Corridor(CorridorType.L, Rotation.NEUTRAL, null);
        //pushed in card
        expectedField[1][2] = new Corridor(CorridorType.L, Rotation.NEUTRAL, null);
        expectedField[2][2] = new Corridor(CorridorType.L, Rotation.LEFT, null);

        Assert.assertArrayEquals(expectedField, game.getField());
    }

    @Test
    public void moveWhileKeepingPath() {
        game = new GameLogic(gui, "L100,I101,L200\n" +
                "I003,L204,I005\n" +
                "L000,I108,L300", players);
        Player player = game.getPlayerX(0);
        player.addTreasure(Treasure.T08);
        game.setFreeCorridor(new Corridor(CorridorType.I));
        //should push middle col from the left to keep path,
        //if wrong rotation or row push is used, the path is cut off
        player.makePushMove(game);
        Position from = new Position(0, 0);
        Position to = new Position(1, 2);
        Assert.assertNotNull(game.findPath(from, to));
    }
}