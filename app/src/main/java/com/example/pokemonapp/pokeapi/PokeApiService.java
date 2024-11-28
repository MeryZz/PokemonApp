package com.example.pokemonapp.pokeapi;

import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.models.PokemonList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeApiService {
    String BASE_URL = "https://pokeapi.co/api/v2/";

    //para un pokemon por id
    @GET("pokemon/{id}")
    Call<Pokemon> getPokemonById(@Path("id") String id);

    //para una lista de pokemons
    @GET("pokemon")
    Call<PokemonList> getPokemonList(@Query("limit") int limit, @Query("offset") int offset);
}
