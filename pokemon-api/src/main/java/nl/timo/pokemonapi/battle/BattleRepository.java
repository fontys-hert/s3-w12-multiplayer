package nl.timo.pokemonapi.battle;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BattleRepository extends CrudRepository<Battle, Long> {

    Optional<Battle> findByPokemonLeft(Long id);
}
