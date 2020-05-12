package test;

import catan.API.response.Code;
import catan.game.bank.Bank;
import catan.game.enumeration.Development;
import catan.game.enumeration.Port;
import catan.game.enumeration.Resource;
import catan.game.game.BaseGame;
import catan.game.game.Game;
import catan.game.player.Player;
import catan.game.rule.Component;
import catan.game.rule.Cost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {

    @DisplayName("Check valid settlement placing")
    @Test
    public void validSettlement() {
        Game game=new BaseGame();
        game.addPlayer("a",new Player("a",game));
        game.addNextPlayer("a");
        game.startGame();
        assertNull(game.buildSettlement(15));
        assertSame(game.buildSettlement(15), Code.IntersectionAlreadyOccupied);
        assertSame(game.buildSettlement(99), Code.InvalidSettlementPosition);
        assertSame(game.buildSettlement(16), Code.DistanceRuleViolated);
    }
    @DisplayName("Check valid road placing")
    @Test
    public void validRoad() {
        Game game=new BaseGame();
        game.addPlayer("a",new Player("a",game));
        game.addNextPlayer("a");
        game.startGame();
        game.buildSettlement(15);
        assertNull(game.buildRoad(15,16));
        assertSame(game.buildRoad(15,16),Code.RoadAlreadyExistent);
        assertSame(game.buildRoad(17,10), Code.InvalidRoadPosition);
        assertNull(game.buildRoad(16,17));
        assertSame(game.buildRoad(5,6),Code.InvalidRoadPosition);
    }
    @DisplayName("Check valid settlement buy")
    @Test
    public void settlementBuy() {
        Game game=new BaseGame();
        game.addPlayer("a",new Player("a",game));
        game.addNextPlayer("a");
        game.startGame();

        game.buildSettlement(17);
        game.buildRoad(16,17);
        game.buildRoad(15,16);

        game.getPlayer("a").addResource(Resource.lumber,1);
        game.getPlayer("a").addResource(Resource.grain,1);
        game.getPlayer("a").addResource(Resource.brick,1);
        game.getPlayer("a").addResource(Resource.wool,1);

        assertNull(game.buySettlement(15));

        game.getPlayer("a").addResource(Resource.lumber,1);
        game.getPlayer("a").addResource(Resource.grain,1);
        game.getPlayer("a").addResource(Resource.brick,1);
        game.getPlayer("a").addResource(Resource.wool,1);

        assertSame(game.buySettlement(18),Code.NotConnectsToRoad);


    }
    @DisplayName("Check valid road buy")
    @Test
    public void roadBuy() {
        Game game=new BaseGame();
        game.addPlayer("a",new Player("a",game));
        game.addNextPlayer("a");
        game.startGame();

        game.buildSettlement(15);

        game.getPlayer("a").addResource(Resource.lumber,1);
        game.getPlayer("a").addResource(Resource.brick,1);

        assertNull(game.buyRoad(15,16));
        assertSame(game.buyRoad(15,16),Code.PlayerNotEnoughLumber);
        game.getPlayer("a").addResource(Resource.lumber,1);
        assertSame(game.buyRoad(15,16),Code.PlayerNotEnoughBrick);

    }
    @DisplayName("Check valid city buy")
    @Test
    public void cityBuy() {
        Game game=new BaseGame();
        game.addPlayer("a",new Player("a",game));
        game.addNextPlayer("a");
        game.startGame();

        game.buildSettlement(15);
        game.buildRoad(15,16);

        game.getPlayer("a").addResource(Resource.grain,2);
        game.getPlayer("a").addResource(Resource.ore,3);

        assertNull(game.buyCity(15));
        assertSame(game.buyCity(15),Code.InvalidRequest);
        assertNotNull(game.buyCity(17));
        game.buildSettlement(17);
        assertSame(game.buyCity(17),Code.PlayerNotEnoughGrain);
        game.getPlayer("a").addResource(Resource.grain,3);
        assertSame(game.buyCity(17),Code.PlayerNotEnoughOre);
        game.getPlayer("a").addResource(Resource.ore,3);
        assertSame(game.buyCity(17),Code.NotConnectsToRoad);
        game.buildRoad(16,17);
        assertNull(game.buyCity(17));
    }

    @DisplayName("Check valid dev buy")
    @Test
    public void devBuy() {
        Game game=new BaseGame();
        game.addPlayer("a",new Player("a",game));
        game.addNextPlayer("a");
        game.startGame();


        game.getPlayer("a").addResource(Resource.grain,1);
        game.getPlayer("a").addResource(Resource.ore,1);
        game.getPlayer("a").addResource(Resource.wool,1);

        assertNull(game.buyDevelopment());
        assertNotNull(game.buyDevelopment());

    }

    @DisplayName("Check wantToTrade")
    @Test
    public void wantToTrade(){
        Game game=new BaseGame();
        game.addPlayer("a",new Player("a",game));
        game.addPlayer("b",new Player("b",game));
        game.addNextPlayer("a");
        game.addNextPlayer("b");
        game.startGame();

        game.getCurrentPlayer().addResource(Resource.lumber,1);
        HashMap <Resource,Integer> offer=new HashMap<>();
        offer.put(Resource.lumber,1);
        HashMap <Resource,Integer> request=new HashMap<>();
        request.put(Resource.grain,1);
        game.playerTrade( offer,request);

        assertSame(game.wantToTrade("b"),Code.PlayerNotEnoughGrain);

        game.getPlayer("b").addResource(Resource.grain,1);
        assertNull(game.wantToTrade("b"));

        game.getPlayer("b").addResource(Resource.grain,1);
        assertSame(game.wantToTrade("b"),Code.AlreadyInTrade);

    }

    @DisplayName("Check select partner")
    @Test
    public void selectPartner(){
        Game game=new BaseGame();
        game.addPlayer("a",new Player("a",game));
        game.addPlayer("b",new Player("b",game));
        game.addNextPlayer("a");
        game.addNextPlayer("b");
        game.startGame();


        game.getCurrentPlayer().addResource(Resource.lumber,1);
        game.getPlayer("b").addResource(Resource.grain,1);

        HashMap <Resource,Integer> offer=new HashMap<>();
        offer.put(Resource.lumber,1);
        HashMap <Resource,Integer> request=new HashMap<>();
        request.put(Resource.grain,1);

        game.playerTrade( offer,request);
        game.wantToTrade("b");

        assertSame(game.selectPartner("c"),Code.NotInTrade);
        assertNull(game.selectPartner("b"));
    }

    @DisplayName("Check port trade")
    @Test
    public void portTrade(){
        Game game=new BaseGame();
        game.addPlayer("a",new Player("a",game));
        game.addPlayer("b",new Player("b",game));
        game.addNextPlayer("a");
        game.addNextPlayer("b");
        game.startGame();


        assertSame(game.portTrade(19,"lumber","wool"),Code.InvalidRequest);
        int id=game.getBoard().getPorts().indexOf(Port.ThreeForOne);
        game.getCurrentPlayer().addResource(Resource.lumber,3);

        assertNull(game.portTrade(id,"lumber","grain"));
        assertSame(game.portTrade(id,"lumber","grain"),Code.PlayerNotEnoughLumber);

        id=game.getBoard().getPorts().indexOf(Port.Lumber);
        game.getCurrentPlayer().addResource(Resource.lumber,2);
        assertNull(game.portTrade(id,"lumber","grain"));



    }

    @DisplayName("Check bank trade")
    @Test
    public void bankTrade(){
        Game game=new BaseGame();
        game.addPlayer("a",new Player("a",game));
        game.addPlayer("b",new Player("b",game));
        game.addNextPlayer("a");
        game.addNextPlayer("b");
        game.startGame();


        game.getCurrentPlayer().addResource(Resource.lumber,3);

        assertSame(game.bankTrade("lumber","ceva"),Code.InvalidRequest);
        assertSame(game.bankTrade("altceva","grain"),Code.InvalidRequest);

        assertSame(game.bankTrade("lumber","grain"),Code.PlayerNotEnoughLumber);
        game.getCurrentPlayer().addResource(Resource.lumber,1);
        assertNull(game.bankTrade("lumber","grain"));



    }


    @DisplayName("Check move robber")
    @Test
    public void moveRobber(){
        Game game=new BaseGame();
        game.addPlayer("a",new Player("a",game));
        game.addPlayer("b",new Player("b",game));
        game.addNextPlayer("a");
        game.addNextPlayer("b");
        game.startGame();

        int id=game.getBoard().getRobberPosition().getId();
        assertSame(game.moveRobber(id),Code.SameTile);
        assertNull(game.moveRobber(id+1));
        assertSame(game.getBoard().getRobberPosition().getId(),id+1);



    }

}
