package nl.timo.pokemonapi.integration;

import nl.timo.pokemonapi.pokemon.Pokemon;
import nl.timo.pokemonapi.pokemon.PokemonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PokemonControllerTest {
    @Autowired
    private MockMvc web;

    @Test
    public void getsAllPokemonCorrectly() throws Exception {
        web.perform(get("/pokemon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].name").value("Pikachu"))
                .andExpect(jsonPath("$.[1].name").value("Bulbasaur"))
        ;
    }
}
