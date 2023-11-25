package nl.timo.pokemonapi.pokemon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PokemonController {
    private final PokemonRepository _repo;

    public PokemonController(PokemonRepository repo) {
        _repo = repo;
    }

    @GetMapping("/pokemon")
    public ResponseEntity<Iterable<Pokemon>> getAll() {
        return ResponseEntity.ok(_repo.findAll());
    }

    @PostMapping("/pokemon")
    public ResponseEntity<?> create(@RequestBody Pokemon pokemon) {
        _repo.save(pokemon);

        return ResponseEntity.created(URI.create("/pokemon/derp")).build();
    }

    @GetMapping("/pokemon/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        Optional<Pokemon> found = _repo.findByName(name);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(found.get());
    }
}
