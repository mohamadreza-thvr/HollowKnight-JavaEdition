# Hollow Knight - 2D Metroidvania Clone 🪲

![LibGDX](https://img.shields.io/badge/LibGDX-1.12.1-red.svg?logo=libgdx)
![Java](https://img.shields.io/badge/Java-17%2B-blue.svg?logo=java)
![Architecture](https://img.shields.io/badge/Architecture-MVC-brightgreen)

A simplified, 2D side-scrolling Metroidvania game inspired by **Hollow Knight**, developed as the final project for the **Advanced Programming Course** at **Sharif University of Technology**.

This project is built using the [libGDX](https://libgdx.com/) framework and strictly follows the **Model-View-Controller (MVC)** design pattern. It features complex enemy AI, platforming mechanics, a charm-based inventory system, and a multi-phase boss fight.

---

## ✨ Features

### 🎮 Core Gameplay & Platforming
* **Dynamic Physics:** Smooth interactions, gravity, and precise collision detection.
* **Movement Mechanics:** Walking, Variable-height Jumping, Double Jump, Dashing (with cooldowns), Wall Sliding (Mantis Claw), and Down-striking (Pogo Jumping) on enemies/spikes.
* **Health & Soul System:** Mask-based health system with invincible frames upon taking damage, and a "Soul" vessel that fills by striking enemies.
* **Healing (Focus):** Players can consume collected Soul to regenerate health masks by holding the Focus button.

### ⚔️ Combat & AI
* **Melee & Spells:** Primary nail attacks (multi-directional) and magic spells (*Vengeful Spirit* projectile & *Howling Wraiths* area-of-effect).
* **Basic & Advanced Enemies:** * Ground and Flying enemies with vision-based AI, pathfinding, and unique attack patterns (e.g., *Husk Hornhead*, *Crystal Guardian*).
    * Knockback mechanics for both the player and enemies.
* **Epic Boss Fight (False Knight):**
    * Dynamic, distance-based decision-making AI with randomized anti-spam mechanics.
    * 5 unique boss moves (Mace Slam, Charge Run, Offensive/Defensive Leaps, Shockwave Slam).
    * 2-Phase combat system with a "Stun State" and scaling speed.
    * Enclosed arena with dynamic camera shakes on heavy impacts.

### 🧿 Charms & Inventory System
* Fully functional UI menu to equip/unequip charms using a limited "Notch" system.
* **Implemented Charms:** *Soul Catcher*, *Dashmaster*, *Unbreakable Strength*, *Quick Slash*, *Quick Focus*, *Heavy Blow*, *Sharp Shadow*, and *Void Heart*.

### 🗺️ Environments & World
* **Themed Areas:** Hand-crafted environments (e.g., *Forgotten Crossroads*, *Greenpath*) with unique tilesets, background assets, dynamic particles, and environmental hazards (spikes/crystals).
* **Secrets:** Breakable walls that reveal hidden rooms and rewards.
* **NPC Interaction:** Zote the Mighty! Includes proximity-based dialog boxes and specific voice SFX.

### 🛠️ Menus, Settings & Persistence
* **Comprehensive UI:** Main Menu, Pause Menu, Dynamic Settings (Audio, Controls, Brightness), and an Achievement tracking system.
* **Save/Load System:** Game states (health, soul, map position, charms, achievements) are serialized and persisted using JSON/Database.
* **Cheat Codes:** Noclip/Spectator mode, God Mode, Refill Soul, Emergency Heal, and Teleport to Boss.

---

## ⌨️ Controls (Default)

| Action | Key |
| :--- | :--- |
| **Move Left / Right** | `Left / Right Arrow Keys` |
| **Look Up / Down** | `Up / Down Arrow Keys` |
| **Jump / Double Jump** | `Z` |
| **Attack (Nail)** | `X` |
| **Dash** | `C` |
| **Focus (Heal)** | `A` (Hold) |
| **Inventory / Charms** | `I` |
| **Interact (Zote / Menus)** | `E` or `Enter` |
| **Pause Game** | `ESC` |

*(Controls can be remapped in the in-game Settings Menu).*

---

## 🚀 Build & Run (Gradle)

This project uses [Gradle](https://gradle.org/) to manage dependencies. The Gradle wrapper is included, so you can run Gradle tasks using `gradlew.bat` (Windows) or `./gradlew` (Linux/Mac).

### Project Structure
- `core`: Main module with the application logic (MVC architecture) shared by all platforms.
- `lwjgl3`: Primary desktop platform launcher using LWJGL3.

### Useful Commands
- **Run the Game:** ```bash
  ./gradlew lwjgl3:run
