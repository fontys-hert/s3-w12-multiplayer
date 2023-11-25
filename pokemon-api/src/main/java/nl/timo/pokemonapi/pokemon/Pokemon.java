package nl.timo.pokemonapi.pokemon;

import jakarta.persistence.*;

import java.util.Optional;

@Entity
public class Pokemon {
    private Integer noPlayerId = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private Integer strength = 0;
    private Integer maxHp = 0;
    private Integer hpCurrent = 0;
    private Boolean isTaken;

    protected Pokemon() {}

    public Pokemon(String name, Integer strength, Integer maxHp) {
        this.name = name;
        this.strength = strength;
        this.maxHp = this.hpCurrent = maxHp;
        this.isTaken = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(Integer maxHp) {
        this.maxHp = maxHp;
    }

    public int getHpCurrent() {
        return hpCurrent;
    }

    public void setHpCurrent(Integer hp) {
        hpCurrent = hp;
    }

    public Boolean getIsTaken() {
        return isTaken;
    }

    public Boolean getIsAvailable() {
        return !isTaken;
    }

    public void setIsTaken(Boolean taken) {
        isTaken = taken;
    }

    public void reset() {
        isTaken = false;
        hpCurrent = maxHp;
    }

    public Integer getNoPlayerId() {
        return noPlayerId;
    }

    public void setNoPlayerId(Integer noPlayerId) {
        this.noPlayerId = noPlayerId;
    }

    public Boolean getTaken() {
        return isTaken;
    }

    public void setTaken(Boolean taken) {
        isTaken = taken;
    }
}
