package catan.game.turn;

import catan.API.response.Code;
import catan.API.response.Messages;
import catan.API.response.UserResponse;
import catan.game.bank.Bank;
import catan.game.enumeration.Development;
import catan.game.enumeration.Resource;
import catan.game.game.Game;
import catan.game.player.Player;
import catan.game.property.Intersection;
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
                response = new UserResponse(HttpStatus.SC_ACCEPTED, "The message\nhas no assigned action.", null);
                return true;
            }
        });

        //region First Two Rounds

        fsm.setAction("buildSettlement", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (game.getAvailableSettlementPositions(game.getCurrentPlayer()).size() == 0) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.NoAvailableSettlementPosition), null);
                    return true;
                }
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
                if (game.getAvailableRoadPositions(game.getCurrentPlayer()).size() == 0) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.NoAvailableRoadPosition), null);
                    return true;
                }
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>() {
                        });
                Player currentPlayer = game.getCurrentPlayer();
                int settlementsNumber = currentPlayer.getSettlementsNumber();
                Intersection lastSettlement = currentPlayer.getSettlements().get(settlementsNumber - 1);
                int start = requestArguments.get("start");
                int end = requestArguments.get("end");
                if (!(lastSettlement.getId() == start || lastSettlement.getId() == end)) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED,
                            "It does not connect\nto your last intersection.", null);
                    return false;
                }
                Code code = game.buildRoad(start, end);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                game.changeTurn();
//                Map<String, Object> result = new HashMap<>();
//                result.put("nextPlayer", game.getCurrentPlayer().getId());
//                response = new UserResponse(HttpStatus.SC_OK, "The road was built successfully.", result);
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
                    game.updateDiscardState();
                    game.setInDiscardState(game.checkInDiscardState());
                    result.putAll(game.getRollSevenResult());
                    fsm.setShareData(result);
                    fsm.ProcessFSM("rollSeven");
                } else {
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
                String answer = requestArguments.get("answer");
                if (answer == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                if (answer.equalsIgnoreCase("no")) {
                    response = new UserResponse(HttpStatus.SC_OK, "Okay.", null);
                    return true;
                }
                String player = requestArguments.get("player");
                Pair<Code, Resource> result = game.stealResource(player);
                if (result.getValue() == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(result.getKey()),
                            null);
                    return false;
                }
                Map<String, Object> responseArguments = new HashMap<>();
                responseArguments.put("resource", result.getValue());
                response = new UserResponse(HttpStatus.SC_OK, "The resource card\nwas stolen successfully.",
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
                response = new UserResponse(HttpStatus.SC_OK, "The trade partners\nwere sent successfully.",
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
                    return code == Code.NoPartner;
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
                    return false;
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
                Pair<Code, Development> result = game.buyDevelopment();
                Code code = result.getKey();
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                Map<String, Object> responseArguments = new HashMap<>();
                responseArguments.put("development", result.getValue());
                response = new UserResponse(HttpStatus.SC_OK, "The development was bought successfully.",
                        responseArguments);
                return true;
            }
        });

        fsm.setAction("buyRoad", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (game.getAvailableRoadPositions(game.getCurrentPlayer()).size() == 0) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.NoAvailableRoadPosition), null);
                    return true;
                }
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
                if (game.getAvailableSettlementPositions(game.getCurrentPlayer()).size() == 0) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.NoAvailableSettlementPosition), null);
                    return true;
                }
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
                if (game.getAvailableCityPositions(game.getCurrentPlayer()).size() == 0) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.NoAvailableCityPosition), null);
                    return true;
                }
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
                if (game.getAvailableRoadPositions(game.getCurrentPlayer()).size() == 0) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.NoAvailableRoadPosition), null);
                    return true;
                }
                Code code = game.useDevelopment(Development.roadBuilding);
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                Bank bank = game.getBank();
                Player currentPlayer = game.getCurrentPlayer();
                if (!bank.hasRoad(currentPlayer)) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.BankNoRoad), null);
                    return false;
                }
                currentPlayer.setRoadsToBuild(Math.min(bank.getRoadsNumber(currentPlayer), 2));
                response = new UserResponse(HttpStatus.SC_OK, "You can use\nRoad Building development card.",
                        null);
                return true;
            }
        });

        fsm.setAction("goNext", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
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
                response = new UserResponse(HttpStatus.SC_OK, "You can use\nYear of Plenty development card.",
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
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "The resource cards\nwere stolen successfully.",
                        responseArguments);
                return true;
            }
        });

        fsm.setAction("buildDevelopmentRoad", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                if (game.getAvailableRoadPositions(game.getCurrentPlayer()).size() == 0) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.NoAvailableRoadPosition), null);
                    return true;
                }
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
                Player currentPlayer = game.getCurrentPlayer();
                currentPlayer.setRoadsToBuild(currentPlayer.getRoadsToBuild() - 1);
                if (currentPlayer.getRoadsToBuild() < 1) {
                    response = new UserResponse(HttpStatus.SC_OK, "The road was built successfully.\nYou do not have roads in bank.", null);
                    fsm.ProcessFSM("goNext");
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "The road was built successfully.", null);
                return true;
            }
        });

        fsm.setAction("takeTwoResources", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Bank bank = game.getBank();
                int resourcesNumber = 0;
                for (Resource resource : Resource.values()) {
                    if (resource != Resource.desert) {
                        resourcesNumber += bank.getResourcesNumber(resource);
                    }
                }
                if (resourcesNumber == 0) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.BankNoResource),
                            null);
                    return true;
                }
                if (resourcesNumber == 1) {
                    for (Resource resource : Resource.values()) {
                        if (resource != Resource.desert) {
                            if (bank.getResourcesNumber(resource) == 1) {
                                bank.removeResource(resource);
                                game.getCurrentPlayer().addResource(resource);
                                response = new UserResponse(HttpStatus.SC_OK, "You have taken the last\nresource card from the bank.",
                                        null);
                                return true;
                            }
                        }
                    }
                }
                if (resourcesNumber == 2) {
                    for (Resource resource : Resource.values()) {
                        if (resource != Resource.desert) {
                            int resourceNumberByType = bank.getResourcesNumber(resource);
                            if (resourceNumberByType > 0) {
                                bank.removeResource(resource, resourceNumberByType);
                                game.getCurrentPlayer().addResource(resource, resourceNumberByType);
                            }
                        }
                    }
                    response = new UserResponse(HttpStatus.SC_OK, "You have taken the last\ntwo resource cards from the bank.",
                            null);
                    return true;
                }
                if (arguments == null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest),
                            null);
                    return false;
                }
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>() {
                        });
                Code code = game.takeTwoResources(requestArguments.get("resource_0"), requestArguments.get("resource_1"));
                if (code != null) {
                    response = new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                    return false;
                }
                response = new UserResponse(HttpStatus.SC_OK, "The resource cards\nwere taken successfully.",
                        null);
                return true;
            }
        });

        //endregion

        //region Turn

        fsm.setAction("endTurn", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Code changeTurnResult = game.changeTurn(1);
                if (changeTurnResult == null) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("nextPlayer", game.getCurrentPlayer().getId());
                    response = new UserResponse(HttpStatus.SC_OK, "The turn was changed successfully.", result);
                    return true;
                } else if (changeTurnResult == Code.FoundWinner) {
                    fsm.ProcessFSM("endGame");
                    response = new UserResponse(HttpStatus.SC_OK, "The game has ended successfully.", null);
                    return false;
                }
                fsm.ProcessFSM("endGame");
                response = new UserResponse(HttpStatus.SC_OK,
                        "The game has ended because\nthere are not enough active players.", null);
                return false;
            }
        });

        //endregion
    }
}
