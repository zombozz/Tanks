# Tanks

## Overview

This project is a Java-based tank game prototype utilizing the Processing library for graphics and Gradle for dependency management. Players control tanks that aim and fire at each other, with scoring based on hits and a game-over condition when only one tank remains.

## Project Specifications

- **Game Window:** 864x640 pixels
- **Frame Rate:** 30 FPS
- **Dependencies:** Java 8, Gradle, Processing library (`processing.core`, `processing.data`)

## Gameplay

### Levels
- Each level is defined by a text file (28x20 grid) where:
  - `X` denotes terrain height.
  - Letters (A, B, C, ...) denote human players.
  - Numbers (0, 1, 2, ...) denote AI players (optional).
  - `T` denotes trees.
  - Spaces are ignored.
- Terrain is smoothed using a moving average.

### Tanks
- **Controls:**
  - **UP**: Move turret left (+3 radians/sec)
  - **DOWN**: Move turret right (-3 radians/sec)
  - **LEFT**: Move tank left (-60 px/sec)
  - **RIGHT**: Move tank right (+60 px/sec)
  - **W**: Increase turret power (+36 units/sec)
  - **S**: Decrease turret power (-36 units/sec)
  - **SPACEBAR**: Fire projectile (ends turn)
- **Fuel:** Tanks start with 250 fuel units.
- **Health:** Tanks start with 100 health.

### Projectiles
- **Trajectory**: Determined by turret angle and power level.
- **Speed:** 60 px/s to 540 px/s based on power level.
- **Gravity:** 3.6 px/s².
- **Wind:** Affects trajectory horizontally; initial random value between -35 and 35, changing by -5 to 5 after each turn.

### Explosions
- **Radius:** 30 pixels.
- **Damage:** Decreases linearly with distance from impact.
- **Terrain:** Destroyed in a 30-pixel radius around impact.

### Parachutes
- **Usage:** 3 per game, slows descent to 60 px/s if terrain is destroyed below the tank; otherwise, descent is 120 px/s with 1hp damage per pixel.

### Powerups
- **INFO1113 Powerups:**
  - **Repair Kit (r)**: Cost 20, repairs 20 health.
  - **Additional Fuel (f)**: Cost 10, adds 200 fuel units.
- **COMP9003 Powerups:**
  - **Additional Parachute (p)**: Cost 15, adds 1 parachute.
  - **Larger Projectile (x)**: Cost 20, next shot has double radius.

### Scoreboard
- Persistent across levels, tracks scores based on damage inflicted.

### End of Game
- **Level End:** When only one tank remains.
- **Game End:** Displays winner and final scores, with scores shown in descending order with a 0.7s delay.

## Configuration

- **config.json:** Contains game settings including level layout files, background images, foreground colors, and optional tree sprites.

## Getting Started

1. **Clone the repository:**
   ```sh
   git clone https://github.com/zombozz/Tanks.git
   
2. **Navigate to the project directory:**

    ```sh
    cd tank-game
    ```

3. **Build the project using Gradle:**

    ```sh
    ./gradle build
    ```

4. **Run the game:**

    ```sh
    ./gradle run
    ```

## Testing

Unit tests are implemented using JUnit, and code coverage is managed by JaCoCo. To run tests and generate coverage reports:

```sh
./gradle test jacocoTestReport
```
- Javadoc: Generated Javadoc is available in the build/docs/javadoc directory.
