
# Advent of Code 2025 — Day 10: Factory

> Goal: explain the implemented approach in clear, practical terms for readers with basic
> linear algebra knowledge. Covers modeling, the parametric (null-space) form, feasibility,
> minimization, and the Branch-and-Bound + Simplex strategy used for the few non‑trivial cases.

---

## Problem (rephrased)
You are given a machine with **buttons** and **counters**. Each button is wired to a subset of counters.
Pressing a button adds `1` to each counter it is connected to. The machine *activates* when counters
match their target values.

We need:

1. **Part 1 (lights/XOR)** — find the smallest set of buttons to press **at most once** so that the light pattern
   matches the target (XOR behavior). This is a subset search problem over 0/1 presses.
2. **Part 2 (counters/ILP)** — allow buttons to be pressed multiple times; find a **feasible** assignment and then the
   **optimal** one with the minimum total number of presses.

> In code, Part 1 brute‑forces subsets and uses XOR to compute the light state; Part 2 models the counters as an
> integer linear program (ILP) and solves it via parametric form and Branch‑and‑Bound + Simplex for the few hard cases.

Example input (from the description):
```
(3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
```
It represents 6 buttons (columns) wired to 4 counters (rows), and target counter values `{3,5,4,7}`.  
We’ll model this as a matrix equation.

---

## Notation
- Let $A ∈ \{0,1\}^{m\times n}$ be the **wiring matrix** ($m$ counters, $n$ buttons). $A[i,j]=1$ means button $j$ affects counter $i$.
- Let $x ∈ ℤ^n$ be **how many times** each button is pressed.
- Let $b ∈ ℤ^m$ be the **target counters**.
- Counters evolve as a sum: $A x = b$. (We also require $x ≥ 0$ — you can’t press a button a negative number of times.)

With the sample above, the system becomes:

$$\begin{aligned}
x_5 + x_6 & = 3 \\
x_2 + x_6 & = 5 \\
x_3 + x_4 + x_5 & = 4 \\
x_1 + x_2 + x_4 & = 7
\end{aligned}$$

In matrix form:

$$\begin{bmatrix}
0&0&0&0&1&1\\
0&1&0&0&0&1\\
0&0&1&1&1&0\\
1&1&0&1&0&0
\end{bmatrix}
x= \begin{bmatrix} 3\\
5\\
4\\
7 \end{bmatrix}$$

---

## Part 1 (lights): brute‑force subsets with XOR
Part 1 treats lights as bits and each button as toggling certain lights. The implementation enumerates all
non‑empty subsets of buttons (`1..(1<<n)-1`), applies XOR per affected light, and keeps the subset with the fewest
buttons that matches the target pattern. This is fast enough given puzzle constraints.

**Key idea:** compute the light vector for a subset and compare to target; track the minimum subset size.

---

## Part 2 (counters): integer linear programming in parametric form
We want any **feasible** solution to $A x = b, x ≥ 0$, then the **optimal** one that minimizes total presses.
The objective is:

$\min \; \mathbf{1}^\top x = \min \sum_{i=1}^{n} x_i$


### From equations to a compact parametric form
Using integer row/column operations (conceptually, Smith/Hermite normal forms), we express the full solution set as:

$x = x_0 + N t$, where:

- $x₀ ∈ ℤ^n$ is **one particular (basis) solution** of $A x = b$,
- $N ∈ ℤ^{n×k}$ (columns) spans the **integer null‑space** of $A$ ($A N = 0$),
- $t ∈ ℤ^k$ are the **free integer parameters** (often $k ≤ 3$ in this puzzle’s datasets).

This drastically reduces the search space from dimension $n$ to $k$.

> Empirically, across 151 machines in the input, the number of free variables tops out at 3; more than half
> of the machines have $k=0$ (the base solution is already unique). See distribution below.

### Feasibility becomes simple inequalities in $t$
Because $x = x_0 + N t$ and we need $x ≥ 0$, feasibility is just:

$x_0 + N t \;\ge\; 0 \quad(\text{componentwise})$.

That is, a small set of linear inequalities in the few parameters $t$.

### Objective in $t$
Since $\mathbf{1}^\top x = \mathbf{1}^\top x_0 + \mathbf{1}^\top N \; t$, and $x_0$ is constant, minimizing
$\sum x_i$ is equivalent to minimizing the linear form $c^\top t$ where $c = \mathbf{1}^\top N$ (sum of each null‑space column).

### Worked example (the sample)
For the sample, the solver produced:


$$x_0 = \begin{bmatrix} 0 \\
5 \\
-1 \\
2 \\
3 \\
0 \end{bmatrix},\quad
N = \begin{bmatrix} 1&0 \\
0&-1 \\
1&0 \\
-1&1 \\
0&-1 \\
0&1 \end{bmatrix}, \quad
t = \begin{bmatrix} t_1\\
t_2 \end{bmatrix}$$

The only negative entry in $x_0$ is $x_{0,3}=-1$. Inspecting the third row of $N$ shows that only $t_1$ can fix it
($N_{3,1}=1$, $N_{3,2}=0$), so feasibility requires $t_1 ≥ 1$. The objective is $\min (1·t_1 + 0·t_2)$, so the minimum is
$t_1 = 1$ (any $t_2$), and substituting gives:

$x = \begin{bmatrix} 1 \\ 5 \\ 0 \\ 1 \\ 3 \\ 0 \end{bmatrix},\quad \sum x_i = 10$

---

## Fast paths before Branch‑and‑Bound (B&B)
In practice we handle most machines without search:

- **No free variables ($k=0$).** The unique base solution $x_0$ is feasible; it is automatically optimal.
- **One free variable ($k=1$).** Let the single null‑space column be $f$ and write $x(t) = x_0 + t f$.
  You can derive a closed form interval $[lb, ub]$ of integer $t$ that makes all components non‑negative.
  Then choose the best end of the interval based on the reduced cost $c = \sum f_i$:
    - If $c < 0$, pick $t = ub$ (to decrease the objective).
    - If $c ≥ 0$, pick $t = lb$.
      This is constant‑time arithmetic.

These two cases already solve ~75% of machines instantly.

---

## Branch‑and‑Bound + Simplex for the few hard cases ($k ≥ 2$)
When there are 2 or 3 free variables, we solve the small ILP in $t$ with standard **Branch‑and‑Bound (B&B)** guided by
**LP relaxation** solved by **Simplex**.

### What we relax and why Simplex
- The ILP in parameters is: minimize $c^\top t$ subject to $x_0 + N t ≥ 0$ and any bounds on $t$.
- We relax integrality ($t ∈ ℤ^k$ → $t ∈ ℝ^k$) to get a linear program (LP). Simplex quickly finds an optimal LP solution
  and its **objective value is a lower bound** on any integer solution in that branch. If the LP is infeasible, we can prune the branch.
- If the LP solution happens to be integral, we are done for that branch. If not, we **branch** on one fractional component.

### How branching works (high‑level)
1. Start from the root with wide bounds on each $t_j$ (effectively $(-∞, +∞)$), plus feasibility constraints $x_0 + N t ≥ 0$.
2. Solve the LP (Simplex). If infeasible, prune. If integral, map $t$ back to $x$ and update the best solution.
3. Otherwise, pick a fractional $t_i = v$ and split into two children with $t_i ≤ ⌊v⌋$ and $t_i ≥ ⌈v⌉$.
4. Repeat; use the LP objective to prune any node whose lower bound is not better than the current best.

### Notes specific to this implementation
- For sign‑mixed ranges on a variable, the search first splits the domain at 0 to separate positive/negative bounds.
- Internally the LP uses the constraints $−N t ≤ x_0$ (equivalent to $x_0 + N t ≥ 0$) and adds bound constraints per branch.
- The objective $c = 1^T N$ (sum of each column of $N$) is computed once; Simplex minimizes $c^T t$.
- After solving the LP, if a branch used the substitution $t' = −t$ for negative ranges, the solution is converted back.

This B&B is cheap here because $k$ is tiny (≤3), so the tree is shallow and Simplex calls are minimal.

---

## Empirical null‑space sizes (151 machines)

| # of free variables | Machines | % of machines | Notes                   |
|---------------------:|---------:|--------------:|-------------------------|
| 0                    |       78 |        51.66% | trivial, ~0 compute time|
| 1                    |       36 |        23.84% | trivial, ~0 compute time|
| 2                    |       31 |        20.53% | non‑trivial             |
| 3                    |        6 |         3.97% | maximum observed        |
| **Total**            |     **151** |     **100.00%** |                         |

---

## Implementation overview (Java)
- **Input parsing.** Lights pattern (Part 1), button wiring, and counter targets are parsed from each line.
- **Part 1.** Enumerates button subsets, applies XOR per light, checks equality, and keeps the subset with the minimum size.
- **Part 2.** Builds $A$ and $b$, computes $(x_0, N)$, then:
    - If $k=0$: $x_0$ is both feasible and optimal.
    - If $k=1$: compute $[lb, ub]$ from $x_0 + t f ≥ 0$, choose end by reduced cost $c$.
    - Else: run B&B with Simplex to minimize $c^T t$ subject to $x_0 + N t ≥ 0$, map $t$ → $x$, and track the best.

---

## Correctness (intuitive)
- Any solution to $A x = b$ is captured by $x = x_0 + N t$. Enforcing $x ≥ 0$ in that form is sufficient and necessary
  because $A N = 0$ by construction.
- The objective is linear in $t$; LP relaxation provides a valid lower bound. B&B enumerates only the branches needed,
  and whenever it finds an integral LP solution, that is optimal for that branch. With tiny $k$, the global optimum is
  found quickly.

---

## Appendix: one‑variable bounds
For $x(t) = x_0 + t f$ and $x(t) ≥ 0$, each component yields:
- If $f_j > 0: t ≥ ⌈−x_{0,j} / f_j⌉$.
- If $f_j < 0: t ≤ ⌊ x_{0,j} / (−f_j) ⌋$.
- If $f_j = 0: x_{0,j} ≥ 0$ (otherwise infeasible).

Take the tightest lower/upper bound over all components, then choose $t$ at the better end according to the reduced cost $c = \sum f_j$.

---

## Worked sample: final numbers
Using the sample’s $x_0$ and $N$ above, feasibility forces $t_1 ≥ 1$, the objective reduces to $min(t_1)$, and the minimum is
$t_1=1$. The resulting press counts are $x = [1,5,0,1,3,0]$ with total $10$ presses.

