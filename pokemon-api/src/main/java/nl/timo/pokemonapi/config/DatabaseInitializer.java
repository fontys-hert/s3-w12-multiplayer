package nl.timo.pokemonapi.config;

import nl.timo.pokemonapi.pokemon.Pokemon;
import nl.timo.pokemonapi.pokemon.PokemonRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
    private final PokemonRepository _repo;

    public DatabaseInitializer(PokemonRepository repo) {
        _repo = repo;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void SeedData() {
        Pokemon pikachu = new Pokemon("Pikachu", 10, 20);
        Pokemon bulbasaur = new Pokemon("Bulbasaur", 12, 30);

        _repo.save(pikachu);
        _repo.save(bulbasaur);
    }
}
