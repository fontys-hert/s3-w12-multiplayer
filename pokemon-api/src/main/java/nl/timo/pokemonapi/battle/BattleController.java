package nl.timo.pokemonapi.battle;

import nl.timo.pokemonapi.pokemon.Pokemon;
import nl.timo.pokemonapi.pokemon.PokemonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class BattleController {
    private final PokemonRepository _repo;
    private final SimpMessagingTemplate _messagingTemplate;

    public BattleController(PokemonRepository repo, SimpMessagingTemplate messagingTemplate) {
        _repo = repo;
        _messagingTemplate = messagingTemplate;
    }

    @GetMapping("/battle/available")
    public ResponseEntity<List<String>> getAvailablePokemonNames() {
        Iterable<Pokemon> pokemonList = _repo.findAll();

        List<String> pokemonNamesAvailable = new ArrayList<>();

        for (var pokemon : pokemonList) {
            if (pokemon.getIsAvailable()) {
                pokemonNamesAvailable.add(pokemon.getName());
            }
        }

        return ResponseEntity.ok(pokemonNamesAvailable);
    }

    @GetMapping("/battle/status")
    public ResponseEntity<Iterable<Pokemon>> getStatus() {
        Iterable<Pokemon> pokemonList = _repo.findAll();
        return ResponseEntity.ok(pokemonList);
    }

    @GetMapping("/battle/join/{pokemonName}")
    public ResponseEntity<?> joinBattle(@PathVariable String pokemonName) {
        Iterable<Pokemon> pokemonList = _repo.findAll();

        int playerId = 1;
        Pokemon pokemonOpponent = null;
        Pokemon pokemonToUse = null;

        for (var pokemon : pokemonList) {
            if (pokemon.getName().equals(pokemonName) && pokemon.getIsAvailable()) {
                pokemonToUse = pokemon;
            } else {
                pokemonOpponent = pokemon;
            }
        }

        if (pokemonToUse == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Pokemon is not available");
        }

        pokemonToUse.setIsTaken(true);
        pokemonToUse = _repo.save(pokemonToUse);

        Battle battle = new Battle(pokemonToUse, pokemonOpponent);

        _messagingTemplate.convertAndSend("/topic/battle", new BattleMessage(
                "player_joined",
                List.of(battle.getPokemonToUse(), battle.getPokemonOpponent())
        ));

        return ResponseEntity.ok(battle);
    }

    @PostMapping("/battle/attack")
    public ResponseEntity<Integer> attack(@RequestBody Battle battle) {
        Random random = new Random();
        Pokemon attacker = battle.getPokemonToUse();
        Pokemon defender = battle.getPokemonOpponent();

        Integer damage = defender.getStrength() / 5 + random.nextInt(4);
        Integer newHp = defender.getHpCurrent() - damage;
        defender.setHpCurrent(newHp < 0 ? 0 : newHp);


        _repo.save(defender);

        _messagingTemplate.convertAndSend("/topic/battle", new BattleMessage(
                "attack_result",
                List.of(attacker, defender)
        ));

        return ResponseEntity.ok(damage);
    }

    @GetMapping("/battle/reset")
    public ResponseEntity<?> resetBattle() {
        Iterable<Pokemon> pokemonList = _repo.findAll();

        for (var pokemon : pokemonList) {
            pokemon.reset();
        }

        _repo.saveAll(pokemonList);

        List<Pokemon> result = new ArrayList<>();
        pokemonList.forEach(result::add);
        _messagingTemplate.convertAndSend("/topic/battle", new BattleMessage(
                "reset",
                result
        ));

        return ResponseEntity.noContent().build();
    }

}
