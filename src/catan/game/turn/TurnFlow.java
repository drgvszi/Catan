package catan.game.turn;

import catan.API.response.Code;
import catan.API.response.Messages;
import catan.API.response.UserResponse;
import catan.game.enumeration.Development;
import catan.game.enumeration.Resource;
import catan.game.game.Game;
import catan.util.Helper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankzz.dynamicfsm.action.FSMAction;
import com.github.ankzz.dynamicfsm.fsm.FSM;
import javafx.util.Pair;
import org.apache.http.HttpStatus;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TurnFlow {
    public final Game game;
    public FSM fsm;
    public UserResponse response;

    public TurnFlow(Game game) throws IOException, SAXException, ParserConfigurationException {
        this.game = game;
        fsm = new FSM("stateConfig.xml", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                response = new UserResponse(HttpStatus.SC_ACCEPTED, "The message has no assigned action.", null);
                return true;
            }
        });

        //region First Two Rounds

        fsm.setAction("buildSettlement", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>() {
                        });
                Code code = game.buildSettlement(requestArguments.get("intersection"));
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                Map<String, Object> resources = new ObjectMapper().convertValue(game.getSecondSettlementResources(),
                        new TypeReference<HashMap<String, Object>>() {
                        });
                response = new UserResponse(HttpStatus.SC_OK, "The settlement was built successfully.",
                        resources);
                return true;
            }
        });

        fsm.setAction("buildRoad", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>() {
                        });
                Code code = game.buildRoad(requestArguments.get("start"), requestArguments.get("end"));
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                game.changeTurn();
                response = new UserResponse(HttpStatus.SC_OK, "The road was built successfully.", null);
                return true;
            }
        });

        //endregion

        //region Dice

        fsm.setAction("rollDice", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Pair<Integer, Integer> dice = game.rollDice();
                int firstDice = dice.getKey();
                int secondDice = dice.getValue();
                Map<String, Object> result = new HashMap<>();
                result.put("dice_1", firstDice);
                result.put("dice_2", secondDice);
                int diceSum = firstDice + secondDice;
                if (diceSum == 7) {
                    game.setInDiscardState(true);
                    result.putAll(game.getRollSevenResult());
                    fsm.setShareData(result);
                    fsm.ProcessFSM("rollSeven");
                } else {
                    game.setInDiscardState(false);
                    result.putAll(game.getRollNotSevenResult(diceSum));
                    fsm.setShareData(result);
                    fsm.ProcessFSM("rollNotSeven");
                }
                return false;
            }
        });

        fsm.setAction("rollSeven", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, Object> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Object>>() {
                        });
                response = new UserResponse(HttpStatus.SC_OK, Messages.getMessage(Code.DiceSeven), requestArguments);
                return true;
            }
        });

        fsm.setAction("rollNotSeven", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, Object> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Object>>() {
                        });
                response = new UserResponse(HttpStatus.SC_OK, Messages.getMessage(Code.DiceNotSeven), requestArguments);
                return true;
            }
        });

        //endregion

        //region Robber

        fsm.setAction("moveRobber", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>() {
                        });
                int tile = requestArguments.get("tile");
                Code code = game.moveRobber(tile);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                Map<String, Object> players = game.getPlayersToStealResourceFrom(tile);
                response = new UserResponse(HttpStatus.SC_OK, "The robber was moved successfully.", players);
                return true;
            }
        });

        fsm.setAction("stealResource", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>() {
                        });
                String player = requestArguments.get("player");
                Resource resource = game.stealResource(player);
                if (resource == null) {
                    response = new UserResponse(HttpStatus.SC_OK, "The player does not have resource cards.",
                            null);
                }
                Map<String, Object> responseArguments = new HashMap<>();
                responseArguments.put("resource", resource);
                response = new UserResponse(HttpStatus.SC_OK, "The resource card was stolen successfully.",
                        responseArguments);
                return true;
            }
        });

        //endregion

        //region Trade

        fsm.setAction("playerTrade", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, Integer> resources = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>() {
                        });
                Map<Resource, Integer> offer = new HashMap<>();
                Map<Resource, Integer> request = new HashMap<>();
                for (String resourceString : resources.keySet()) {
                    if (resourceString.endsWith("_o")) {
                        Resource resource = Helper.getResourceOfferFromString(resourceString);
                        if (resource != null) {
                            offer.put(resource, resources.get(resourceString));
                        }
                    } else if (resourceString.endsWith("_r")) {
                        Resource resource = Helper.getResourceRequestFromString(resourceString);
                        if (resource != null) {
                            request.put(resource, resources.get(resourceString));
                        }
                    }
                }
                Code code = game.playerTrade(offer, request);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "The trade has started successfully.", null);
                return true;
            }
        });

        fsm.setAction("sendPartners", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                response = new UserResponse(HttpStatus.SC_OK, "The trade partners were sent successfully.",
                        game.sendPartners());
                return true;
            }
        });

        fsm.setAction("selectPartner", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>() {
                        });
                Code code = game.selectPartner(requestArguments.get("player"));
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "The trade was made successfully.", null);
                return true;
            }
        });

        fsm.setAction("noPlayerTrade", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>() {
                        });
                int port = Integer.parseInt(requestArguments.get("port"));
                String offer = requestArguments.get("offer");
                String request = requestArguments.get("request");
                Code code = game.noPlayerTrade(port, offer, request);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                }
                response = new UserResponse(HttpStatus.SC_OK, "The trade was made successfully.", null);
                return true;
            }
        });

        //endregion

        //region Buy

        fsm.setAction("buyDevelopment", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Code code = game.buyDevelopment();
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "The development was bought successfully.", null);
                return true;
            }
        });

        fsm.setAction("buyRoad", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>() {
                        });
                int start = requestArguments.get("start");
                int end = requestArguments.get("end");
                Code code = game.buyRoad(start, end);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "The road was bought successfully.", null);
                return true;
            }
        });

        fsm.setAction("buySettlement", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>() {
                        });
                int intersection = requestArguments.get("intersection");
                Code code = game.buySettlement(intersection);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "The settlement was bought successfully.", null);
                return true;

            }
        });

        fsm.setAction("buyCity", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>() {
                        });
                int intersection = requestArguments.get("intersection");
                Code code = game.buyCity(intersection);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "The city was bought successfully.", null);
                return true;
            }
        });

        //endregion

        //region Development

        fsm.setAction("useDevelopment", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>() {
                        });
                String action = game.useDevelopment(requestArguments.get("development"));
                if (action == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                } else {
                    fsm.ProcessFSM(action);
                }
                return false;
            }
        });

        fsm.setAction("useKnight", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Code code = game.useDevelopment(Development.knight);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "You can use Knight development card.",
                        null);
                return true;
            }
        });

        fsm.setAction("useMonopoly", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Code code = game.useDevelopment(Development.monopoly);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "You can use Monopoly development card.",
                        null);
                return true;
            }
        });

        fsm.setAction("useRoadBuilding", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Code code = game.useDevelopment(Development.roadBuilding);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "You can use Road Building development card.",
                        null);
                return true;
            }
        });

        fsm.setAction("useYearOfPlenty", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Code code = game.useDevelopment(Development.yearOfPlenty);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "You can use Year of Plenty development card.",
                        null);
                return true;
            }
        });

        fsm.setAction("takeResourceFromAll", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>() {
                        });
                Pair<Code, Map<String, Object>> result = game.takeResourceFromAll(requestArguments.get("resource"));
                Code code = result.getKey();
                Map<String, Object> responseArguments = result.getValue();
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                } else {
                    response = new UserResponse(HttpStatus.SC_OK, "The resource cards were stolen successfully.",
                            responseArguments);
                }
                return true;
            }
        });

        fsm.setAction("buildDevelopmentRoad", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>() {
                        });
                Code code = game.buildRoad(requestArguments.get("start"), requestArguments.get("end"));
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "The road was built successfully.", null);
                return true;
            }
        });

        fsm.setAction("takeTwoResources", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>() {
                        });
                Code code = game.takeTwoResources(requestArguments.get("resource_1"), requestArguments.get("resource_2"));
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "The resource cards were taken successfully.",
                        null);
                return true;
            }
        });

        //endregion

        //region Turn

        fsm.setAction("endTurn", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (game.changeTurn(1)) {
                    response = new UserResponse(HttpStatus.SC_OK, "The turn was changed successfully.", null);
                } else {
                    fsm.ProcessFSM("endGame");
                    response = new UserResponse(HttpStatus.SC_OK, "The game has ended successfully.", null);
                }
                return true;
            }
        });

        //endregion
    }
}
