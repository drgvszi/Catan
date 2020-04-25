package catan.game;

import catan.API.Response;
import catan.API.request.Status;
import catan.game.gameType.Game;
import com.github.ankzz.dynamicfsm.action.FSMAction;
import com.github.ankzz.dynamicfsm.fsm.FSM;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class TurnFlow {
    public FSM fsm;
    public final Game game;
    public Response response;


    TurnFlow(Game game) throws IOException, SAXException, ParserConfigurationException {
        this.game = game;
        fsm = new FSM("stateConfig.xml", new FSMAction() {
            @Override
            public boolean action(String curState, String message, String nextState, Object args) {
                response = new Response(Status.SUCCESS,"The message has no assigned function!","");
                return true;
            }
        });
        fsm.setAction("rollDice", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                Random random = new Random();
                if(random.nextInt(7)+random.nextInt(7)==7)
                    fsm.ProcessFSM("rollNotASeven");//fsm.ProcessFSM("rollASeven");}
                else
                    fsm.ProcessFSM("rollNotASeven");
                return false;
            }
        });
        fsm.setAction("rollASeven", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(Status.SUCCESS,"Rolled a seven!","");
                return true;
            }
        });
        fsm.setAction("rollNotASeven", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(Status.SUCCESS,"Rolled not a seven!","");
                return true;
            }
        });
        fsm.setAction("giveResources", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(Status.SUCCESS,"Give resources!","");
                return true;
            }
        });
        fsm.setAction("moveRobber", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(Status.SUCCESS,"Move robbert!","");
                return true;
            }
        });
        fsm.setAction("giveSelectedResource", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(Status.SUCCESS,"Give selected resource!","");
                return true;
            }
        });
        fsm.setAction("tradeBetweenPlayers", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response=new Response(Status.SUCCESS,"Trade between player","");
                return true;
            }
        });
        fsm.setAction("buyRoad", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(Status.SUCCESS,"Buy road successfully!","");
                return true;
            }
        });
        fsm.setAction("buyHouse", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(Status.SUCCESS,"Buy house successfully!","");
                if(!game.buySettlement(Integer.parseInt(((HashMap<String,String>) o).get("spot")))) {
                    response = new Response(Status.ERROR, "Buying the house is not possible!","");
                    return false;
                }
                return true;
            }
        });
        fsm.setAction("buyCity", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(Status.SUCCESS,"Buy City successfully!","");
                return true;
            }
        });
        fsm.setAction("playDevCard", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(Status.SUCCESS,"Dev Card played successfully!","");
                return true;
            }
        });
        fsm.setAction("endTurn", new FSMAction() {
            @Override
            public boolean action(String curState, String message, String nextState, Object args) {
                response = new Response(Status.SUCCESS,"Turn changed successfully!","");
                return game.changeTurn();
            }
        });
        System.out.println(fsm.getCurrentState());
    }
}
