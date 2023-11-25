package nl.timo.pokemonapi.battle;

import nl.timo.pokemonapi.pokemon.Pokemon;

public class Battle {
    private Pokemon pokemonToUse;
    private Pokemon pokemonOpponent;

    public Battle(Pokemon pokemonToUse, Pokemon pokemonOpponent) {
        this.pokemonToUse = pokemonToUse;
        this.pokemonOpponent = pokemonOpponent;
    }

    public Pokemon getPokemonToUse() {
        return pokemonToUse;
    }

    public Pokemon getPokemonOpponent() {
        return pokemonOpponent;
    }

    public void setPokemonOpponent(Pokemon pokemon) {
        pokemonOpponent = pokemon;
    }
}
