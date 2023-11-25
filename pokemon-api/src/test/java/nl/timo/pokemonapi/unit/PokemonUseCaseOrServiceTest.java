package nl.timo.pokemonapi.unit;

import nl.timo.pokemonapi.pokemon.Pokemon;
import nl.timo.pokemonapi.pokemon.PokemonController;
import nl.timo.pokemonapi.pokemon.PokemonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PokemonUseCaseOrServiceTest {
    @Test
    public void getsAllPokemon() {
        PokemonRepository repo = mock(PokemonRepository.class);

        when(repo.findAll()).thenReturn(List.of(
                new Pokemon("Marc", 10, 10),
                new Pokemon("Tren", 5, 20),
                new Pokemon("Sumaya", 10, 30),
                new Pokemon("Andy", 10, 50)
        ));

        PokemonController controller = new PokemonController(repo);

        ResponseEntity<?> response = controller.getAll();

        List<Pokemon> pokemon = (List<Pokemon>)response.getBody();

        assert response.getStatusCode().value() == 200;
        assert pokemon.size() == 4;
        assert pokemon.get(0).getName() == "Marc";
    }
}
