package nl.timo.pokemonapi.battle;

import nl.timo.pokemonapi.pokemon.Pokemon;

import java.util.List;

public class BattleMessage {
    private List<Pokemon> pokemons;
    private String message;

    public BattleMessage(String message, List<Pokemon> pokemons) {
        this.pokemons = pokemons;
        this.message = message;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public String getMessage() {
        return message;
    }
}
