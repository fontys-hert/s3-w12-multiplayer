package nl.timo.pokemonapi.pokemon;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PokemonRepository extends CrudRepository<Pokemon, Long> {
    Optional<Pokemon> findByName(String name); //select where name = ?
}

//public class PokemonRepository {
//    private List<Pokemon> pokemonList;
//
//    public PokemonRepository() {
//        pokemonList = new ArrayList<>();
//        pokemonList.add(new Pokemon("Tihomir", 22, 100));
//        pokemonList.add(new Pokemon("Marc", 2, 10));
//        pokemonList.add(new Pokemon("Mendes", 10, 50));
//    }
//
//    public List<Pokemon> findAll() {
//        return pokemonList;
//    }
//
//    public void save(Pokemon pokemon) {
//        pokemonList.add(pokemon);
//    }
//}
