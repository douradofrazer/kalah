![Kalah Workflow](https://github.com/github/docs/actions/workflows/master-ci.yaml/badge.svg)

Kalah
======================

- Reference : https://en.wikipedia.org/wiki/Kalah

### Gameplay
The game provides a Kalah board and a number of seeds or counters. \
The board has 6 small pits, called houses, on each side; and a big pit, called an end zone or store, at each end. \
The object of the game is to capture more seeds than one's opponent.


- At the beginning of the game, four seeds are placed in each house. This is the traditional method.
- Each player controls the six houses and their seeds on the player's side of the board. The player's score is the number of seeds in the store to their right.
- Players take turns sowing their seeds. On a turn, the player removes all seeds from one of the houses under their control. Moving counter-clockwise, the player drops one seed in each house in turn, including the player's own store but not their opponent's.
- If the last sown seed lands in an empty house owned by the player, and the opposite house contains seeds, both the last seed and the opposite seeds are captured and placed into the player's store.
- If the last sown seed lands in the player's store, the player gets an additional move. There is no limit on the number of moves a player can make in their turn.
- When one player no longer has any seeds in any of their houses, the game ends. The other player moves all remaining seeds to their store, and the player with the most seeds in their store wins.
- It is possible for the game to end in a draw.

### Terminology used in the game:
- **House**: One of the twelve pits in the board.
- **Store**: The large house at the right side of the board, belonging to the playerDto.
- **Seed**: A small counter used to play the game.
- **Move**: A playerDto's move, beginning with the playerDto sowing seeds from one of the houses they own.
- **Sowing:** The act of removing all seeds from a house and distributing them in the pits, beginning with the next pit.
- **Capture**: The act of capturing all seeds in the house directly opposite an opponent's house when the last seed sown lands in that opponent's house, and the opponent's house was not empty before the turn began.


### Pre-Requisites
- Git
- Java 17
- Maven
- Docker

## How to run the game
- Clone the repository
```bash
git clone https://github.com/douradofrazer/kalah.git
```
- Run the following command to run the project
```bash
mvn spring-boot:run
```

- Create a game
```bash
curl --location 'localhost:8080/v1/games/init' \
--header 'Content-Type: application/json' \
--data '{
    "userName" : "outlaw_24"
}'
```

- Join a game
```bash
curl --location --request PUT 'localhost:8080/v1/games/{gameRef}/join' \
--header 'Content-Type: application/json' \
--data '{
    "userName": "billy_the_kid"
}'
```

- Make a move
```bash
curl --location --request PUT 'localhost:8080/v1/games/{gameRef}/move' \
--header 'Content-Type: application/json' \
--data '{
    "userName" : "outlaw_24",
    "houseIndex" : 0
}'
```

- Keep Making Moves Until The Game Ends

- Get Game Status
```bash
curl --location 'localhost:8080/v1/games/{gameRef}/status' \
```

