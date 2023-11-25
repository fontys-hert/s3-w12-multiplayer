import Pokemon from "./components/Pokemon";
import BulbasaurImage from "./assets/images/bulbasaur.png";
import PikachuImage from "./assets/images/pikachu.png";
import "./assets/css/main.css";
import { useCallback, useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

const pokemonData = {
  Bulbasaur: {
    image: BulbasaurImage,
  },
  Pikachu: {
    image: PikachuImage,
  },
};

function App() {
  const [stompClient, setStompClient] = useState();
  const [stompData, setStompData] = useState(null);
  const [pokemonNamesAvailable, setPokemonNamesAvailable] = useState([]);
  const [myPokemon, setMyPokemon] = useState(null);
  const [opponentPokemon, setOpponentPokemon] = useState(null);
  const [log, setLog] = useState("Select a pokemon to begin!");

  const setupStompClient = () => {
    // stomp client over websockets
    const stompClient = new Client({
      brokerURL: "ws://localhost:8080/ws",
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    stompClient.onConnect = () => {
      // subscribe to the backend public topic
      stompClient.subscribe("/topic/battle", (data) => {
        console.log(data);
        setStompData(data);
      });
    };

    // initiate client
    stompClient.activate();

    // maintain the client for sending and receiving
    return stompClient;
  };

  function doLog(message) {
    console.log(message);
    setLog(message);
  }

  const joinBattle = async (name) => {
    try {
      const response = await fetch("http://localhost:8080/battle/join/" + name);
      const body = await response.json();

      setMyPokemon({
        ...pokemonData[body.pokemonToUse.name],
        ...body.pokemonToUse,
      });

      setOpponentPokemon({
        ...pokemonData[body.pokemonOpponent.name],
        ...body.pokemonOpponent,
      });

      if (!body.pokemonOpponent.isTaken) {
        doLog("Pokemon selected! Waiting for opponent...");
      } else {
        doLog("Ready for battle!");
      }
    } catch (error) {
      doLog("Something went wrong selecting the pokemon. Just reset?");
    }
  };

  const getAvailablePokemon = useCallback(async () => {
    const response = await fetch("http://localhost:8080/battle/available");
    const body = await response.json();
    setPokemonNamesAvailable(body);
  }, []);

  const updatePokemon = useCallback(async () => {
    if (myPokemon == null) return;
    const response = await fetch("http://localhost:8080/battle/status");
    const body = await response.json();

    const myPokemonToUpdate = body.find((p) => p.name === myPokemon?.name);
    setMyPokemon({
      ...myPokemon,
      ...myPokemonToUpdate,
    });

    const opponentToUpdate = body.find((p) => p.name === opponentPokemon?.name);
    if (opponentPokemon == null) return;
    setOpponentPokemon({
      ...opponentPokemon,
      ...opponentToUpdate,
    });
  }, [myPokemon, opponentPokemon]);

  const handleIncomingMessage = useCallback(
    async (data) => {
      if (data == null) return;
      const body = JSON.parse(data.body);
      console.log("getting incoming message: " + body.message);

      switch (body.message) {
        case "player_joined":
          console.log("my", myPokemon);
          if (myPokemon == null) {
            doLog("A player has joined the battle!");
            await getAvailablePokemon();
          } else {
            doLog("Ready for battle!");
            await updatePokemon();
          }
          break;
        case "attack_result":
          await updatePokemon();
          break;
        case "reset":
          setLog("Battle has been reset and is over! Refresh the page...");
          break;
        default:
          setLog("Unknown message: " + body.message);
          break;
      }
    },
    [getAvailablePokemon, myPokemon, updatePokemon]
  );

  useEffect(() => {
    if (stompClient != null) return;
    setStompClient(setupStompClient());
  }, [stompClient]);

  useEffect(() => {
    if (myPokemon == null) {
      console.log('fetching available pokemon');
      getAvailablePokemon();
    }
  }, [getAvailablePokemon, myPokemon]);

  useEffect(() => {
    if (stompData == null) return;
    handleIncomingMessage(stompData);
    setStompData(null);
  }, [handleIncomingMessage, stompData]);

  async function resetGame() {
    await fetch("http://localhost:8080/battle/reset");
  }

  async function onJoinClick(name) {
    doLog(`Selected ${name}!`);
    await joinBattle(name);
  }

  async function attack() {
      const response = await fetch('http://localhost:8080/battle/attack', {
        method: 'POST',
        body: JSON.stringify({
          pokemonToUse: myPokemon,
          pokemonOpponent: opponentPokemon
        }),
        headers: {
          'Content-Type': 'application/json'
        }
      });
      const damage = await response.json();
      doLog(`You attacked ${opponentPokemon.name} for ${damage}hp!`);

      await updatePokemon();
  }

  return (
    <>
      <main className="battlefield">
        <section className="battlefield__section battlefield__section--align-right">
          <Pokemon stats={opponentPokemon}></Pokemon>
        </section>
        <section className="battlefield__section">
          <Pokemon stats={myPokemon} />
          <div className="interaction">
            <div className="log interaction__logs">{log}</div>
            <div className="interaction__moves">
              <button className="attack" disabled={myPokemon == null && opponentPokemon == null} onClick={attack}>fight</button>
              <button onClick={resetGame}>reset</button>
              <button
                disabled={
                  myPokemon != null ||
                  !pokemonNamesAvailable.some((p) => p === "Pikachu")
                }
                onClick={() => onJoinClick("Pikachu")}
              >
                pika
              </button>
              <button
                disabled={
                  myPokemon != null ||
                  !pokemonNamesAvailable.some((p) => p === "Bulbasaur")
                }
                onClick={() => onJoinClick("Bulbasaur")}
              >
                bulba
              </button>
            </div>
          </div>
        </section>
      </main>
    </>
  );
}

export default App;
