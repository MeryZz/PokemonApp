package com.example.pokemonapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.models.PokemonList;
import com.example.pokemonapp.pokeapi.PokeApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView; //para mostrar la lista
    private PokemonAdapter adapter;   //para el adapter del recyclerView
    private ArrayList<Pokemon> pokemonList = new ArrayList<>(); //lista de pokemons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //para configurar el recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PokemonAdapter(pokemonList);
        recyclerView.setAdapter(adapter);

        //para configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PokeApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PokeApiService pokeService = retrofit.create(PokeApiService.class);

        //para mostrar un Pokémon aleatorio
        pokemonById(pokeService);

        //para mostrar una lista de Pokémon con offset 0 y cargarla en el recyclerView
        loadPokemonList(pokeService, 0);
    }

    //para mostrar un pokemon aleatorio
    public void pokemonById(PokeApiService pokeService) {
        Call<Pokemon> pokeCall = pokeService.getPokemonById(Integer.toString((int) (Math.random() * 807 + 1)));

        pokeCall.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.body() != null) {
                    Pokemon foundPoke = response.body();
                    Log.d("POKEMON NAME", foundPoke.getName());
                    Log.d("POKEMON HEIGHT", foundPoke.getHeight());
                    Log.d("POKEMON WEIGHT", foundPoke.getWeight());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //para cargar una lista de pokemons y actualizar el recyclerView
    private void loadPokemonList(PokeApiService pokeService, int offset) {
        pokeService.getPokemonList(20, offset).enqueue(new Callback<PokemonList>() {
            @Override
            public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {
                if (response.body() != null) {
                    pokemonList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged(); //refrescar la lista en el recyclerView
                }
            }

            @Override
            public void onFailure(Call<PokemonList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ERROR", t.getMessage());
            }
        });
    }

}
