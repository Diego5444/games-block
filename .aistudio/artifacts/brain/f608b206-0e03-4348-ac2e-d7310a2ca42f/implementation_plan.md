# Block Blast: Gems & Wood Puzzle

Un videojuego de rompecabezas estilo *Block Blast* para Android en Jetpack Compose, con estética clásica de gemas brillantes y madera tallada. Incorpora mecánicas completas de colocación táctil de bloques poliominó, detección y destrucción simultánea de filas y columnas, multiplicador de combos consecutivos, animaciones de explosión de gemas, y dos modos de juego: **Modo Clásico Infinito** y **Modo Aventura con Niveles Progresivos**.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> Decisiones de diseño y producto confirmadas en la fase de clarificación con el usuario:

- **Modos de Juego Confirmados**: Se integrarán **ambos modos**:
  - *Modo Clásico Infinito*: Juego sin fin con selector de 3 piezas por turno, dificultad progresiva y tabla de récords personales.
  - *Modo Aventura*: Mapa de niveles progresivos (1 a 15+) con objetivos específicos (alcanzar puntuación, limpiar gemas congeladas/obstáculos, o completar en número limitado de movimientos), calificación de 1 a 3 estrellas y guardado de progreso.
- **Tablero Dinámico**: Tamaño estándar de 8x8 en Modo Clásico, y dimensiones/configuraciones adaptables (7x7, 8x8 con obstáculos fijos o gemas meta pre-colocadas) en Modo Aventura.
- **Identidad Visual**: Temática premium de **Gemas Brillantes y Madera Pulida** (*Classic Wood & Jewel Puzzle*): marcos de madera oscura pulida con biseles, celdas de cuadrícula con textura sutil de tablero artesanal, bloques de gemas facetadas (Rubí, Zafiro, Esmeralda, Amatista, Topacio, Ámbar) con destellos luminosos, efectos de partículas al hacer "Blast" y banners animados de "Combo x2!", "Super Blast!", etc.

---

## 1. Overview & Core Concept

- **Qué hace el juego**: El jugador arrastra piezas de bloques poliominó de distintas formas desde su bandeja inferior hacia una cuadrícula. Al completar líneas horizontales o verticales completas, los bloques explotan en una ráfaga de gemas, liberando espacio y otorgando puntos.
- **Mecánica de Combos**:
  - Si un movimiento destruye 2 o más líneas a la vez (Multi-Line Clear), se otorga un multiplicador inmediato de puntos (x2, x3, x4).
  - Si el jugador destruye líneas en movimientos consecutivos (Combo Streak), se activa el contador de racha con efectos de fuego/brillo y multiplicadores crecientes.
- **Público Objetivo**: Jugadores de puzzles casuales que buscan una experiencia táctil satisfactoria, ágil y visualmente atractiva con retroalimentación háptica y visual inmediata.

---

## 2. User Experience & Visual Design

### Flujos Principales de Usuario
1. **Pantalla Principal (Home)**:
   - Título estilizado en madera y gemas incrustadas.
   - Puntuación máxima histórica y estrellas totales obtenidas.
   - Botones principales: *Jugar Clásico*, *Modo Aventura (Niveles)*, *Ajustes* (efectos de sonido/vibración háptica).
2. **Pantalla de Juego (Gameplay)**:
   - **Barra Superior**: Botón de pausa, puntaje actual, récord, indicador de nivel/objetivo (en modo Aventura), y contador de Combo Streak activo.
   - **Tablero Central**: Cuadrícula de madera con relieve y guías suaves. Arrastre táctil suave con previsualización translúcida donde encajará la pieza.
   - **Bandeja Inferior de Piezas**: 3 piezas generadas aleatoriamente (o según el nivel). Cada pieza se puede arrastrar con el dedo con elevación visual (`offset`) para no tapar la vista del dedo.
   - **Efectos de Blast y Combos**: Al completar líneas, las gemas estallan con partículas destellantes, vibración háptica suave y banners flotantes ("Combo x2", "Super Combo!", "Incredible!").
3. **Modo Aventura (Selector de Niveles)**:
   - Camino sinuoso de niveles sobre pergamino/madera, mostrando niveles desbloqueados, estrellas (0-3) y puntuación de cada uno.
4. **Modales de Fin de Partida / Victoria**:
   - Diálogo de *Game Over* (cuando no caben más piezas en Clásico) con nuevo récord y opción de reiniciar.
   - Diálogo de *Nivel Completado* con estrellas, puntos obtenidos y botón para avanzar al siguiente nivel.

### Identidad Visual y Paleta de Colores
- **Fondo General**: Caoba oscura y madera nórdica cálida con viñeta suave (`#1A0F0A` a `#2C1810`).
- **Tablero**: Cuadrícula en madera noble incrustada (`#3E2723` marco, `#2B1B17` celdas vacías con bordes sutiles `#5D4037`).
- **Gemas**:
  - *Rubí*: Rojo carmesí brillante con reflejo cristalino (`#E53935` / `#FF8A80`).
  - *Zafiro*: Azul real profundo con brillo celeste (`#1E88E5` / `#80D8FF`).
  - *Esmeralda*: Verde joya vibrante (`#43A047` / `#B9F6CA`).
  - *Topacio / Oro*: Dorado radiante (`#FB8C00` / `#FFE57F`).
  - *Amatista*: Púrpura místico (`#8E24AA` / `#EA80FC`).
  - *Diamante Cian*: Turquesa cristalino (`#00ACC1` / `#84FFFF`).
- **Tipografía**: Jerarquía clara con números grandes legibles para puntajes y combos.

---

## 3. Key Product Decisions & Trade-Offs

- **Mecánica de Arrastre (Drag & Drop en Canvas/Compose)**:
  - *Decisión*: Implementar el arrastre táctil con un desplazamiento vertical (`touchOffsetY`) para que la pieza arrastrada flote justo por encima del dedo del usuario, garantizando que el dedo no tape la casilla donde se va a colocar.
  - *Razón*: Estándar esencial de usabilidad móvil en Block Blast y juegos de puzzle táctiles.
- **Detección de Fin de Juego (Game Over Check)**:
  - *Decisión*: Tras cada colocación, el motor verifica automáticamente si alguna de las piezas restantes en la bandeja puede colocarse en alguna posición válida del tablero. Si ninguna cabe, se activa el Game Over.
- **Persistencia Local con DataStore / SharedPreferences**:
  - *Decisión*: Guardar localmente récord del modo clásico, estado de niveles desbloqueados (estrellas, score) y configuración de sonido/vibración.
  - *Razón*: Carga instantánea, funciona 100% offline sin necesidad de registros engorrosos ni latencia de red.

---

## 4. Technical Architecture & Data Strategy

### Diagrama del Sistema

```
┌──────────────────────────────────────────────────────────────┐
│                        MainActivity                          │
└──────────────────────────────┬───────────────────────────────┘
                               │
                               ▼
┌──────────────────────────────────────────────────────────────┐
│                    BlockBlastViewModel                       │
│  - gameState: StateFlow<GameState>                           │
│  - adventureProgress: StateFlow<AdventureProgress>           │
│  - comboStreak, score, currentPieces, selectedPieceIndex     │
└──────────────┬────────────────────────────────┬──────────────┘
               │                                │
               ▼                                ▼
┌──────────────────────────────┐ ┌─────────────────────────────┐
│       Game Engine Logic      │ │   GamePersistenceRepository │
│  - BoardManager (8x8/NxN)    │ │  - High scores              │
│  - PolyominoShapeFactory     │ │  - Level stars & unlocks    │
│  - LineClearDetector         │ │  - Sound / Haptic settings  │
│  - ComboCalculator           │ └─────────────────────────────┘
│  - PlacementValidator        │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────────────────────────────────────┐
│                          UI Layer                            │
│  ┌───────────────────────┐        ┌───────────────────────┐  │
│  │     HomeScreen        │        │   LevelSelectScreen   │  │
│  └───────────────────────┘        └───────────────────────┘  │
│  ┌────────────────────────────────────────────────────────┐  │
│  │                   GamePlayScreen                       │  │
│  │  - TopScoreHud & ComboBanner                           │  │
│  │  - WoodenGameBoard (Interactive Grid Canvas/Compose)   │  │
│  │  - GemPieceTray (3 Pieces with Drag & Drop Gestures)   │  │
│  │  - ParticleOverlay (Blast animations & sparkles)       │  │
│  │  - GameOverDialog / LevelCompleteDialog                │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

### Modelos de Datos
- `Shape`: Representación de matriz booleana (ej. 1x1, 2x2, 3x3, L-shape, T-shape, I-shape 1x4, etc.) junto a su tipo de gema / color.
- `BoardCell`: Estado de celda vacía o bloqueada con gema (tipo de gema, animación de destrucción activa, etc.).
- `GameMode`: `CLASSIC` vs `ADVENTURE(levelId)`.
- `LevelDefinition`: Requisitos de nivel (dimensiones del tablero, obstáculos fijos, objetivo de puntos, límite de turnos o gemas específicas a destruir).
- `ComboEvent`: Datos de combo para activar animación visual de partículas y multiplicadores.
