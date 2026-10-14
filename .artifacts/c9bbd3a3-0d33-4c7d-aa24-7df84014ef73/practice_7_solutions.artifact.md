# Practice 7: Jetpack Compose Phases — Complete Solutions & Guide

This document contains answers to all theoretical questions (Q1, Q2, Q3, Q8, Q12) and walkthroughs/explanations for all hands-on exercises (Q4–Q7, Q9–Q11, Q13–Q14) implemented in the `FramePhases` project (`uz.ttpu.myapplication`).

---

## Part 1 — The frame (Questions 1–3)

### Question 1 — The three phases of a frame
- **(a) Order and outputs:**
  1. **Composition:** Executes composable functions to produce a tree-like UI structure (UI tree of nodes).
  2. **Layout:** Measures and places every node in the UI tree, outputting 2D coordinates and sizes.
  3. **Drawing:** Renders the nodes onto the canvas (screen), outputting pixels.
- **(b) Two steps of Layout phase:**
  1. **Measurement step:** Parent measures children (passing constraints).
  2. **Placement step:** Parent places children (assigning x, y positions).
- **(c) LazyColumn exception:**
  `LazyColumn` (and `LazyRow`, `BoxWithConstraints`) depend on parent layout constraints/size during composition because the number and content of visible items rendered in composition depend on how much space the parent layout provides. For example, a scrollable list calculates how many items fit on screen based on the measured viewport height.
- **(d) Skipping mechanism:**
  Compose skips unnecessary work using **Smart Recomposition** and **Restart Scopes**. If state hasn't changed or inputs to a composable/modifier haven't changed, Compose skips re-running that function or phase.

---

### Question 2 — Walk the layout algorithm
- **(a) Measure and place order:**
  - `Row` measures children (Image and Column).
  - Image measures itself (48×48).
  - Column measures its children (Text A 100×20, Text B 80×20).
  - Column measures itself (width = max(100, 80) = 100, height = 20 + 20 = 40).
  - Row measures itself (width = 48 + 100 = 148, height = max(48, 40) = 48).
  - Row places children (Image at x=0, y=0; Column at x=48, y=4).
  - Column places its children (Text A at x=0, y=0; Text B at x=0, y=20 relative to Column).
- **(b) Final sizes and positions:**
  - Column size: `100 × 40`
  - Row size: `148 × 48`
  - Positions relative to Row:
    - Image: `(0, 0)`
    - Column: `(48, 4)`
    - Text A (inside Column): relative to Row is `(48 + 0, 4 + 0) = (48, 4)`
    - Text B (inside Column): relative to Row is `(48 + 0, 4 + 20) = (48, 24)`
- **(c) Visits & traversal time:**
  Each node is visited **once** during measurement and **once** during placement (single-pass tree traversal). If every node were measured twice at each level, traversal time would grow exponentially ($O(2^n)$ instead of $O(n)$), destroying 60fps performance.
- **(d) Phase & contents:**
  Produced by the **Layout phase**. Each node contains measurement constraints, width, height, and alignment/placement blocks.

---

### Question 3 — State reads and restart scopes
- **(a) Snippet 1 vs Snippet 2:**
  Both are equivalent. Property delegation (`var padding by ...`) desugars into `.value` reads under the hood. The read happens when the code containing the read executes (during composition/modifier construction).
- **(b) Text A vs Text B offset read:**
  - For `Text("A")`, `offsetX` is read inside the lambda `{ IntOffset(...) }`, which runs during the **layout phase (placement step)**.
  - For `Text("B")`, `offsetX` is read directly in the composition body (above the modifier). Thus, changing `offsetX` triggers **composition** for Text B, whereas for Text A it only triggers **layout (placement)**, skipping composition.
- **(c) Restart scope:**
  A restart scope is a block of code (such as a composable function body) that Compose can re-execute when state read inside it changes. For a read made directly in the body of a composable function, the restart scope is the **composable function itself**.

---

## Part 2 — Seeing the phases in Android Studio (Questions 4–8)

### Question 4 (`LectureTree.kt`)
- Logs composition, layout/measure, layout/place, and draw.
- Rotation triggers full recomposition and layout/draw because configuration changes recreate the activity tree.

### Question 5 (`PaddingDemo.kt`)
- `Modifier.padding(padding)` reads `padding` during modifier construction (Composition phase). Toggling padding triggers **Composition -> Layout -> Drawing**.

### Question 6 (`OffsetDemo.kt`)
- `Modifier.offset { ... }` reads `offsetX` inside the lambda during layout placement. Toggling Move skips composition and triggers **Layout (measure/place) and Draw** only. Moving the read above the text makes it read during Composition, causing full recomposition.

### Question 7 (`ColorDemo.kt`)
- `Canvas { drawRect(color) }` reads `color` inside `draw` lambda. Changing color triggers **Drawing phase only** (`color demo: draw`), skipping composition and layout! Replacing with `Modifier.background(color)` reads color during composition, triggering full recomposition.

---

### Question 8 — Which phase re-runs?
| Read Type | Composition | Layout | Drawing |
|---|---|---|---|
| (a) `Modifier.padding(p)` | Runs | Runs | Runs |
| (b) `Modifier.offset { ... }` | Skipped | Runs (placement) | Runs |
| (c) `Modifier.offset(p)` | Runs | Runs | Runs |
| (d) `Canvas { drawRect(c) }` | Skipped | Skipped | Runs |
| (e) `Layout { ... s ... }` | Skipped | Runs (measure) | Runs |
| (f) `Modifier.drawWithContent { ... c ... }` | Skipped | Skipped | Runs |

* **Animation choice:** (b), (d), or (f) are ideal for 60fps animations because they bypass the expensive Composition phase entirely.

---

## Part 3 — Optimizing state reads (Questions 9–11)

### Question 9 (`ParallaxDemo.kt`)
- **ParallaxSlow:** Reads `listState.firstVisibleItemScrollOffset` during modifier construction (Composition). Scrolling triggers recomposition on every scroll event.
- **ParallaxFast:** Reads inside lambda `.offset { }`, bypassing composition and layout (only runs placement/draw).

### Question 10 (`ScrollToTopDemo.kt`)
- **ScrollToTopNaive:** Reads `listState.firstVisibleItemIndex` directly in composition. Recomposes the entire list on every scroll index change.
- **ScrollToTopDerived (`derivedStateOf`):** Filters state changes so recomposition only triggers when `showButton` boolean actually changes (at index 0 -> 1 boundary), reducing recompositions by over 95%.

### Question 11 (`SkipDemo.kt`)
- Passing `count` directly reads it in `Parent`, making `Parent` the restart scope. Passing `{ count }` as a lambda defers the read to `Child`, so `Parent` skips recomposition entirely when `count` changes!

---

## Part 4 — Cyclic phase dependency (Questions 14)

### Question 13 & 14 (`StackBelow.kt`)
- Custom layout implementation (`StackBelow`) measures children with loose constraints, calculates total height with spacing, and places them sequentially without feedback loops, achieving optimal single-pass rendering.
