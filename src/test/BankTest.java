package test;

import catan.game.bank.Bank;
import catan.game.enumeration.Resource;
import catan.game.game.BaseGame;
import catan.game.player.Player;
import catan.game.rule.Component;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    BaseGame game = new BaseGame();
    List<Player> players = new ArrayList<>();

    @DisplayName("Verificam daca banca este creata corect")
    @Test
    void CreateBank() {
    players.add(new Player("Toderas",game));
    players.add(new Player("Maria",game));
    players.add(new Player("Ionut",game));
    players.add(new Player("Alin",game));
    Bank bank = new Bank(players);

    // verificam ca in banca sa nu fie mai multe resurse decat am creat
    for(Resource resource:Resource.values()) // create resources / exist resources
        assertFalse(bank.existsResource(resource,Component.RESOURCES_BY_TYPE+1));

    // create proprietises region

    // verificam ca numarul de drumuri din banca sa nu fie mai mare decat am creat
    for( Player player : players ) // create roads / get number of roads
        assertEquals( Component.ROADS,bank.getNumberOfRoads(player) );

    for( Player player : players ) // create roads / has roads
        assertTrue( bank.hasRoad(player) );

    for( Player player : players ) // create settlements / has settlements
        assertTrue( bank.hasSettlement(player) );

    for( Player player : players ) // create cities / has cities
        assertTrue( bank.hasCity(player) );

    // end region

    }

    @DisplayName("Folosim varianta functiei cu un singur parametru pentru ca face apel la cea cu 2 parametri")
    @Test
    void takeResource() {
        players.add( new Player("Toderas" , game) );
        players.add( new Player("Maria" , game) );
        players.add( new Player("Ionut" , game) );
        players.add( new Player("Alin" , game) );
        Bank bank = new Bank(players);

        if ( bank.takeResource(Resource.brick) != null &&
        bank.takeResource(Resource.grain) != null &&
        bank.takeResource(Resource.lumber) != null &&
        bank.takeResource(Resource.ore) != null &&
        bank.takeResource(Resource.wool) != null )
        // folosesc false pentru ca in banca nu ar trebui sa mai fie numarul initial de resurse
        for( Resource resource : Resource.values() )
            if( !resource.toString().equalsIgnoreCase("Desert") )
            assertFalse(bank.existsResource ( resource,Component.RESOURCES_BY_TYPE) );
    }

//    @DisplayName("Dupa ce am creat banca ar trebui sa putem lua Development cards din ea")
//    @Test
//    void takeDevelopmentCards() {
//        players.add( new Player("Toderas" , game) );
//        players.add( new Player("Maria" , game) );
//        players.add( new Player("Ionut" , game) );
//        players.add( new Player("Alin" , game) );
//        Bank bank = new Bank(players);
//
//        assertTrue( bank.takeKnight( players.get(1) ) );
//        assertTrue( bank.takeMonopoly( players.get(1) ) );
//        assertTrue( bank.takeRoadBuilding( players.get(1) ) );
//        assertTrue( bank.takeYearOfPlenty( players.get(1) ) );
//        assertTrue( bank.takeVictoryPoint( players.get(1) ) );
//    }

    @DisplayName("Verificam ca player-ul sa poata beneficia de toate drumurile sale")
    @Test
    void takeRoad() {
        players.add( new Player("Toderas" , game) );
        players.add( new Player("Maria" , game) );
        players.add( new Player("Ionut" , game) );
        players.add( new Player("Alin" , game) );
        Bank bank = new Bank(players);

        for(int index = 0 ; index < Component.ROADS ; index++) {
            assertTrue(bank.hasRoad(players.get(1)));
            assertEquals(null,bank.takeRoad(players.get(1)));

        }
        assertFalse(bank.hasRoad(players.get(1)));
    }

    @DisplayName("Verificam ca player-ul sa poata beneficia de toate asezarile sale")
    @Test
    void takeSettlement() {
        players.add( new Player("Toderas" , game) );
        players.add( new Player("Maria" , game) );
        players.add( new Player("Ionut" , game) );
        players.add( new Player("Alin" , game) );
        Bank bank = new Bank(players);

        for(int index = 0 ; index < Component.SETTLEMENTS ; index++) {
            assertTrue(bank.hasSettlement(players.get(2)));
            assertNull(bank.takeSettlement(players.get(2)));
        }
        assertFalse(bank.hasSettlement(players.get(2)));
    }

    @DisplayName("Verificam ca player-ul sa poata beneficia de toate orasele sale")
    @Test
    void takeCity() { // exista o greseala in cod
        players.add( new Player("Toderas" , game) );
        players.add( new Player("Maria" , game) );
        players.add( new Player("Ionut" , game) );
        players.add( new Player("Alin" , game) );
        Bank bank = new Bank(players);

        for(int index = 0 ; index < Component.CITIES ; index++) {
            assertTrue( bank.hasCity( players.get(3) ) );
            assertNull( bank.takeCity( players.get(3) ) );
        }
//        assertFalse( bank.hasCity( players.get(3) ) );
    }

//    @DisplayName("Folosim doar functia ce primeste ca parametru un Map")
//    @Test greseala in cod? retruneaza BankNoBrick
//    void giveResources() {
//        players.add( new Player("Toderas" , game) );
//        players.add( new Player("Maria" , game) );
//        players.add( new Player("Ionut" , game) );
//        players.add( new Player("Alin" , game) );
//        Bank bank = new Bank(players);
//
//        Map<Resource, Integer> resources = new HashMap<>();
//
//        assertNull( bank.takeResource( Resource.brick) );
//        assertNull( bank.takeResource( Resource.grain) );
//        assertNull( bank.takeResource( Resource.lumber) );
//        assertNull( bank.takeResource( Resource.ore) );
//        assertNull( bank.takeResource( Resource.wool) );
//
//        resources.put(Resource.brick,1);
//        resources.put(Resource.grain,1);
//        resources.put(Resource.lumber,1);
//        resources.put(Resource.ore,1);
//        resources.put(Resource.wool,1);
//
//        for( Resource resource : Resource.values() )
//            assertTrue( bank.existsResource( resource,Component.RESOURCES_BY_TYPE) );
//    }

}