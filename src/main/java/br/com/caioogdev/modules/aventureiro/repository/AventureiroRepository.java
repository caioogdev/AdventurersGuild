package br.com.caioogdev.modulos.aventureiro.repository;

import br.com.caioogdev.modules.aventureiro.models.Aventureiro;
import br.com.caioogdev.modules.companheiro.models.Companheiro;
import br.com.caioogdev.shared.enums.Classe;
import br.com.caioogdev.shared.enums.Especie;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AventureiroRepository {

    private final List<Aventureiro> banco = new ArrayList<>();
    private final AtomicLong sequencia = new AtomicLong(1);

    public AventureiroRepository() {
        inicializar();
    }

    private void inicializar() {
        String[] nomes = {
                "Aldric", "Brenna", "Caius", "Dara", "Ewan", "Fiona", "Gareth", "Hilda", "Iago", "Jora",
                "Kael", "Lyra", "Mordecai", "Nessa", "Osric", "Petra", "Quinlan", "Rhea", "Soren", "Thea",
                "Ulric", "Vega", "Wren", "Xander", "Yara", "Zephyr", "Arden", "Briar", "Caspian", "Delara",
                "Edric", "Freya", "Gideon", "Hara", "Ivan", "Jade", "Kelan", "Lira", "Mira", "Nolan",
                "Oryn", "Priya", "Quinn", "Rory", "Sable", "Tomas", "Uma", "Vance", "Willow", "Xeran",
                "Yoel", "Zara", "Aiden", "Bron", "Cyra", "Drake", "Elara", "Finn", "Gale", "Heron",
                "Iris", "Jareth", "Kira", "Lorn", "Mael", "Nira", "Orin", "Piper", "Rael", "Sera",
                "Torin", "Ursa", "Vara", "Wulf", "Xyla", "Ymir", "Zion", "Aldo", "Bela", "Cora",
                "Dion", "Ember", "Fable", "Gray", "Holt", "Idris", "Jest", "Koda", "Luna", "Myra",
                "Nell", "Otto", "Pax", "Remy", "Skye", "Tara", "Ura", "Vale", "Ward", "Xena"
        };

        String[] nomesCompanheiros = {
                "Rex", "Luna", "Spark", "Shadow", "Blaze", "Frost", "Storm", "Ash", "Ember", "Zara",
                "Titan", "Nova", "Echo", "Rune", "Bolt", "Mystic", "Sage", "Cinder", "Gale", "Stone"
        };

        Classe[] classes = Classe.values();
        Especie[] especies = Especie.values();

        for (int i = 0; i < 100; i++) {
            Companheiro companheiro = null;

            if (i % 3 == 0) {
                companheiro = Companheiro.builder()
                        .nome(nomesCompanheiros[i % nomesCompanheiros.length])
                        .especie(especies[i % especies.length])
                        .lealdade((i * 7 + 30) % 101)
                        .build();
            }

            Aventureiro aventureiro = Aventureiro.builder()
                    .id(sequencia.getAndIncrement())
                    .nome(nomes[i])
                    .classe(classes[i % classes.length])
                    .nivel((i % 20) + 1)
                    .ativo(i % 5 != 0)
                    .companheiro(companheiro)
                    .build();

            banco.add(aventureiro);
        }
    }

    public Aventureiro salvar(Aventureiro aventureiro) {
        if (aventureiro.getId() == null) {
            aventureiro.setId(sequencia.getAndIncrement());
            banco.add(aventureiro);
        } else {
            for (int i = 0; i < banco.size(); i++) {
                if (banco.get(i).getId().equals(aventureiro.getId())) {
                    banco.set(i, aventureiro);
                    return aventureiro;
                }
            }
            banco.add(aventureiro);
        }
        return aventureiro;
    }

    public Optional<Aventureiro> buscarPorId(Long id) {
        return banco.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public List<Aventureiro> buscarTodos() {
        return new ArrayList<>(banco);
    }
}
