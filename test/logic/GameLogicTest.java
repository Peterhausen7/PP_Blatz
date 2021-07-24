package logic;

import org.junit.Assert;
import org.junit.Test;

public class GameLogicTest {
    private GUIConnector gui = new FakeGUI();
    private Player[] players = { new Player(new Position(0, 0)), new Player(new Position(0, 0)),
            new Player(new Position(0, 0)), new Player(new Position(0, 0)) };
    private GameLogic game = new GameLogic(gui,"L100,I101,T000,I102,L200\n" +
                                                "I003,I104,I005,I106,I007\n" +
                                                "T300,I108,T000,I109,T100\n" +
                                                "I010,I111,I012,I113,I014\n" +
                                                "L000,I115,T200,I116,L300", players);

    @Test
    public void nextPlayersTurn() {
        Player currentPlayer = game.getCurrentPlayer();
        int numOfPlayers = game.getNumOfPlayers();

        game.makeTurn();
        //currentPlayer operates starting from 0,
        //so numOfPlayers has to be checked with -1
        //to find the last player
        if (currentPlayer.getPlayerNum() < numOfPlayers - 1) {
            Assert.assertTrue(game.getCurrentPlayer().getPlayerNum() == currentPlayer.getPlayerNum() + 1);
        } else {
            Assert.assertTrue(game.getCurrentPlayer().getPlayerNum() == 0);
        }

        //Do this a few times to cycle between players
        currentPlayer = game.getCurrentPlayer();
        game.makeTurn();
        if (currentPlayer.getPlayerNum() < numOfPlayers - 1) {
            Assert.assertTrue(game.getCurrentPlayer().getPlayerNum() == currentPlayer.getPlayerNum() + 1);
        } else {
            Assert.assertTrue(game.getCurrentPlayer().getPlayerNum() == 0);
        }

        currentPlayer = game.getCurrentPlayer();
        game.makeTurn();
        if (currentPlayer.getPlayerNum() < numOfPlayers - 1) {
            Assert.assertTrue(game.getCurrentPlayer().getPlayerNum() == currentPlayer.getPlayerNum() + 1);
        } else {
            Assert.assertTrue(game.getCurrentPlayer().getPlayerNum() == 0);
        }

        currentPlayer = game.getCurrentPlayer();
        game.makeTurn();
        if (currentPlayer.getPlayerNum() < numOfPlayers - 1) {
            Assert.assertTrue(game.getCurrentPlayer().getPlayerNum() == currentPlayer.getPlayerNum() + 1);
        } else {
            Assert.assertTrue(game.getCurrentPlayer().getPlayerNum() == 0);
        }

        currentPlayer = game.getCurrentPlayer();
        game.makeTurn();
        if (currentPlayer.getPlayerNum() < numOfPlayers - 1) {
            Assert.assertTrue(game.getCurrentPlayer().getPlayerNum() == currentPlayer.getPlayerNum() + 1);
        } else {
            Assert.assertTrue(game.getCurrentPlayer().getPlayerNum() == 0);
        }
    }

    @Test
    public void isTreasureAtX() {
        Corridor corr = game.getCorridorAtX(0, 0);
        Assert.assertEquals( 0, corr.getTreasure());

        corr = game.getCorridorAtX(1, 0);
        Assert.assertEquals(1, corr.getTreasure());

        corr = game.getCorridorAtX(2, 0);
        Assert.assertEquals(0, corr.getTreasure());

        corr = game.getCorridorAtX(3, 0);
        Assert.assertEquals(2, corr.getTreasure());

        corr = game.getCorridorAtX(4, 0);
        Assert.assertEquals(0, corr.getTreasure());



        corr = game.getCorridorAtX(0, 1);
        Assert.assertEquals(3, corr.getTreasure());

        corr = game.getCorridorAtX(1, 1);
        Assert.assertEquals(4, corr.getTreasure());

        corr = game.getCorridorAtX(2, 1);
        Assert.assertEquals(5, corr.getTreasure());

        corr = game.getCorridorAtX(3, 1);
        Assert.assertEquals(6, corr.getTreasure());

        corr = game.getCorridorAtX(4, 1);
        Assert.assertEquals(7, corr.getTreasure());



        corr = game.getCorridorAtX(0, 2);
        Assert.assertEquals(0, corr.getTreasure());

        corr = game.getCorridorAtX(1, 2);
        Assert.assertEquals(8, corr.getTreasure());

        corr = game.getCorridorAtX(2, 2);
        Assert.assertEquals(0, corr.getTreasure());

        corr = game.getCorridorAtX(3, 2);
        Assert.assertEquals(9, corr.getTreasure());

        corr = game.getCorridorAtX(4, 2);
        Assert.assertEquals(0, corr.getTreasure());


        corr = game.getCorridorAtX(0, 3);
        Assert.assertEquals(10, corr.getTreasure());

        corr = game.getCorridorAtX(1, 3);
        Assert.assertEquals(11, corr.getTreasure());

        corr = game.getCorridorAtX(2, 3);
        Assert.assertEquals(12, corr.getTreasure());

        corr = game.getCorridorAtX(3, 3);
        Assert.assertEquals(13, corr.getTreasure());

        corr = game.getCorridorAtX(4, 3);
        Assert.assertEquals(14, corr.getTreasure());


        corr = game.getCorridorAtX(0, 4);
        Assert.assertEquals(0, corr.getTreasure());

        corr = game.getCorridorAtX(1, 4);
        Assert.assertEquals(15, corr.getTreasure());

        corr = game.getCorridorAtX(2, 4);
        Assert.assertEquals(0, corr.getTreasure());

        corr = game.getCorridorAtX(3, 4);
        Assert.assertEquals(16, corr.getTreasure());

        corr = game.getCorridorAtX(4, 4);
        Assert.assertEquals(0, corr.getTreasure());
    }

    @Test
    public void pathExists() {
        Position from = new Position(0, 0);
        Position to = new Position(2, 2);
       // Assert.assertNotNull(game.findPath(from, to));
        from = new Position(2, 1);
        to = new Position(2, 4);
       // Assert.assertNotNull(game.findPath(from, to));
        //@TODO




    }

    @Test
    public void pathDoesNotExist() {
        Position from = new Position(1, 1);
        Position to = new Position(4, 2);
     //   Assert.assertNull(game.findPath(from, to));
        from = new Position(4, 2);
        to = new Position(1, 1);
      //  Assert.assertNull(game.findPath(from, to));
        //@TODO
    }

    @Test public void pathBigField() {

        game = new GameLogic(gui, "L100,I100,T000,L200,T000,L100,L200\n" +
                "I000,L100,I000,T100,I000,T100,L300\n" +
                "T300,L100,T300,I000,T000,L300,T100\n" +
                "T200,I000,I000,T100,I000,I000,L100\n" +
                "T300,L100,T200,L000,T100,L100,T100\n" +
                "I000,I000,L100,L200,L100,I000,L100\n" +
                "L000,T100,T200,T100,T200,I100,L300", players);

        //exists
        Position from = new Position(0, 0);
        Position to = new Position(6, 1);
       // Assert.assertNotNull(game.findPath(from, to));
        from = new Position(6, 0);
        to = new Position(0, 4);
      //  Assert.assertNotNull(game.findPath(from, to));

//@TODO
        //doesnt exist
        from = new Position(0, 0);
        to = new Position(4, 5);
     //   Assert.assertNull(game.findPath(from, to));
        from = new Position(4, 4);
        to = new Position(6, 6);
      //  Assert.assertNull(game.findPath(from, to));
    }

//    @Test public void pathBigField2() {
//
//        game = new GameLogic(gui, "L100,I100,T000,L200,T000,L100,L200\n" +
//                "I000,L100,I000,T100,I000,T100,L300\n" +
//                "T300,L100,T300,I000,T000,L300,T100\n" +
//                "T200,I000,I000,T100,I000,I000,L100\n" +
//                "T300,L100,T200,L000,T100,L100,T100\n" +
//                "I000,I000,L100,L200,L100,I000,L100\n" +
//                "L000,T100,T200,T100,T200,I100,L300", players);
//
//        //exists
//        Position from = new Position(0, 0);
//        Position to = new Position(6, 1);
//       // Assert.assertTrue(game.pathExist2(from, to));
//        Position[] test = game.pathExist(from, to);
//        Assert.assertNotNull(game.pathExist(from, to));
//       // System.out.println(test);
//        for (Position pos:test) {
//            System.out.println(pos);
//        }
//
//        from = new Position(6, 0);
//        to = new Position(0, 4);
//       // Assert.assertTrue(game.pathExist2(from, to));
//        Assert.assertNotNull(game.pathExist(from, to));
//
//
//        //doesnt exist
//        from = new Position(0, 0);
//        to = new Position(4, 5);
//       // Assert.assertFalse(game.pathExist2(from, to));
//        Assert.assertNull(game.pathExist(from, to));
//        from = new Position(4, 4);
//        to = new Position(6, 6);
//       // Assert.assertFalse(game.pathExist2(from, to));
//        Assert.assertNull(game.pathExist(from, to));
//    }




    @Test
    public void correctRowToTarget() {
        game = new GameLogic(gui,"L100,I101,L200\n" +
                                                 "I003,I004,I005\n" +
                                                 "L000,I108,L300",
                                                                 players);

      //  game.setFreeCorridor(new Corridor(CorridorType.I)); @TODO post Field class shit
       // game.pushCorridor(2, 1);
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

       // game.setFreeCorridor(new Corridor(CorridorType.L)); @TODO post Field class shit
      //  game.pushCorridor(1, 2);
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
}