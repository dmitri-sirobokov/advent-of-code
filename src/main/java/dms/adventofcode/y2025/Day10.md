
The following is a sample input from Advent of Code 2025, Day 10, First machine (sample).

https://adventofcode.com/2025/day/10

```text
(3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
```

It represents 5 buttons that are wired to specified counters, 
and 4 counters with expected values 3,5,4,7 correspondingly, that's when machine is activated.

The task is to find minimum number of button clicks to activate the machine.

Problem consists of 2 parts. First one is to find any **feasible** solution. Feasible solution is a solution that 
will result in activation of the machine (the output matches).
Second part is to find an **optimal* solution. Optimal solution is the best one from all feasible solutions,
in this case it is minimal number of button presses.

These 2 parts of the problem we can identify as Linear Algebra and Integer Linear Programming (ILP).

From ILP theory we know, that once we find a feasible solution, we can always find an optimal solution. 
I will go step by step to show how we can find feasible solutions and then optimal solution, and discuss 
the techniques which we can use to find it very quickly.

Let's call buttons as $x_1,x_2,x_3,x_4,x_5,x_6$ variables, i.e. the number of times we pressed these buttons.

And output is $b_1,b_2,b_3,b_4$, are counters.

We can translate this input to a system of linear equations:


$\begin{aligned}
a_{11} x_1 + a_{12} x_2 + a_{13} x_3 + a_{14} x_4 + a_{15} x_5 + a_{16} x_6 &= b_1 \\
a_{21} x_1 + a_{22} x_2 + a_{23} x_3 + a_{24} x_4 + a_{25} x_5 + a_{26} x_6 &= b_2 \\
a_{31} x_1 + a_{32} x_2 + a_{33} x_3 + a_{34} x_4 + a_{35} x_5 + a_{36} x_6 &= b_3 \\
a_{41} x_1 + a_{42} x_2 + a_{43} x_3 + a_{44} x_4 + a_{45} x_5 + a_{46} x_6 &= b_4 \\
\end{aligned}$

In matrix form we can write it as

$A x = b$

Note, $a_{ij} \in \{0,1\}$

or $A \in \{0,1\}^{m \times n}$ in matrix notations, sometimes called as binary matrix.

Using the input from our example above:

$\begin{aligned}
x_5+x_6=3 \\
x_2 + x_6 = 5 \\
x_3 + x_4 + x_5 = 4 \\
x_1 + x_2 + x_4 = 7
\end{aligned}$

In matrix form:

$\begin{bmatrix}
0 &amp; 0 &amp; 0 &amp; 0 &amp; 1 &amp; 1 \\
0 &amp; 1 &amp; 0 &amp; 0 &amp; 0 &amp; 1 \\
0 &amp; 0 &amp; 1 &amp; 1 &amp; 1 &amp; 0 \\
1 &amp; 1 &amp; 0 &amp; 1 &amp; 0 &amp; 0
\end{bmatrix} \times \begin{bmatrix}x_1 \\x_2 \\x_3 \\x_4 \\ x_5 \\ x_6 \end{bmatrix}=\begin{bmatrix} 3 \\ 5 \\ 4 \\ 7 \end{bmatrix}$

Finding values of $x$ variables is equivalent to finding a feasible solution, which may or may not be an optimal solution.
We will discuss finding optimal solution later, after we found feasible solution.

Before we proceed, it is worth noting that matrix $A$ is of size $m\times n$, where $m \le n$ (usually). 
Both $m$ and $n$ can be quite large for some machines. There is probably a lot of redundancy in these equations. 
To remediate large redundancy we can use a technique called SNF decomposition.

SNF decomposition is a very useful technique that reveals many interesting properties of the matrix $A$.
In our case, we will use it to extract independent (free) variables and finds a basic solution. As the result we will rewrite our
original equations in parametric form:

$x = x_0 + N t$

Parametric form will be much more compact in compare with a previous $At=b$, as we can show that number of independent free parameters is usually very small.

We will use elementary matrix transformation to find $x_0$ and $N$.

Using our example, the result of the SNF transformation is:

$x_0 = \begin{bmatrix} 0 \\ 5 \\ -1 \\ 2 \\ 3 \\ 0 \end{bmatrix}, \
N=\begin{bmatrix}
1 &amp; 0 \\
0 &amp; -1 \\
1 &amp; 0 \\
-1 &amp; 1 \\
0 &amp; -1 \\
0 &amp; 1
\end{bmatrix},\ t=\begin{bmatrix} t_1 \\ t_2 \end{bmatrix}$

$x_0$ is called a basis solution (constant), $N$ is called a null-space, or kernel, or just a free vectors space, 
$t$ are our new independent (free) variables.

So our parametric solution would be:

$x=x_0+Nt=\begin{bmatrix} 0 \\ 5 \\ -1 \\ 2 \\ 3 \\ 0 \end{bmatrix} + \
\begin{bmatrix}
1 &amp; 0 \\
0 &amp; -1 \\
1 &amp; 0 \\
-1 &amp; 1 \\
0 &amp; -1 \\
0 &amp; 1
\end{bmatrix} \begin{bmatrix} t_1 \\ t_2 \end{bmatrix}$

As we can see, we have reduced our problem from 6 to 2 independent variables: $t_1,t_2$

I run the statistic for all machines, it turns out that in worst case scenario we have only 3 independent variables:

### Free‑variable distribution across 151 machines

| # of free variables | Machines | % of machines | Notes                    |
|---------------------:|---------:|--------------:|--------------------------|
| 0                    | 78       | 51.66%        | trivial, ~0 compute time |
| 1                    | 36       | 23.84%        | trivial, ~0 compute time |
| 2                    | 31       | 20.53%        | non‑trivial              |
| 3                    | 6        | 3.97%         | maximum observed         |
| **Total**            | **151**  | **100.00%**   |                          |

As I mentioned earlier, we first look for a feasible solution, and next - optimal solution. That is a 2-phase process.

We can show that any combination of $t_1,t_2$ in parametric form will always produce a solution for $At=b$, 
that's why it is called a null-space (another advantage of parametric form).
However, not every solution is feasible, because of the constraints: $x_i \ge 0$. In other terms, number of button clicks can't be negative.

We have to rewrite original constraints $x_i \ge 0$ in terms of $t_i$ (or our new null-space).

We can start from a trivial cases, before we come to a more generic form for new constraints.

Case 1 - no free t-variables. It turns out that more than 50% machines have no free variables and solution is very trivial - just a base solution $x_0$
(todo: include example).

Case 2 - one free variable. The solution is still very trivial. We can use a simple formula to calculate single $t_1$ where all $x_i \ge 0$.

Case 3 - with 2 or more variables the solution may or may not be that trivial.. In some case we may further reduce it, or fallback to 
branch-and-bound + simplex method for solving ILP. Even in that case, the search space should be very small, bacaurse parametric 
form is already close to the feasible solution...Work in progress.

## Minimization (ILP) problem

Once we found parametric form that generates solutions, we can focus on ILP problem, i.e. feasibility and optimality.

Case 1, that we discussed earlier, we have already solution $x_0$, this is the only possible feasible solution and it is also optimal.

Case 2, the solution is still trivial we find the smallest non-negative $x_i$ value from parametric form. That is simple arithmetic.

Case 3, is more generic, and we may use in the worst case scenario branch-and-bound and simplex method to work through the edges of the feasible solutions.

Let's use our example again. We found our parametric solution:

$x=x_0+Nt=\begin{bmatrix} 0 \\ 5 \\ -1 \\ 2 \\ 3 \\ 0 \end{bmatrix} + \
\begin{bmatrix}
1 &amp; 0 \\
0 &amp; -1 \\
1 &amp; 0 \\
-1 &amp; 1 \\
0 &amp; -1 \\
0 &amp; 1
\end{bmatrix} \begin{bmatrix} t_1 \\ t_2 \end{bmatrix}$

It has 2 variables $t_1$ and $t_2$, even so it falls in the non-trivial case, I can still find a trivial solution, as shown below.

To find optimal feasible solution we need:
- constraints
- objective

### Constraints

Original constraints from $Ax=b$ are:

$x \ge 0$

Note, that basic solution is not yet feasible because $x_{03}=-1$, we need to find $t$ to make it feasible.
$t_2$ also does not contribute to feasibility, because $N_{3,2} = 0$.

$t_1$ is the only variable that makes solution feasible and it has to be $t_1 \ge 1$

### Objective function

$z=min \sum x_i$

For our parametric solution, we can write it this way:

$z = min (\sum x_{0i} + \mathbf{1}^\top N t)$

$\mathbf{1}^\top N$ is just a math way to specify sum of the columns in matrix N.

Because $x_0$ is a constant we can just minimize $\mathbf{1}^\top N t$ :

$z = min (\mathbf{1}^\top N t)$

For our example from above, our objective function will be:

$z=min (\begin{bmatrix} 1 && 0 \end{bmatrix} \begin{bmatrix}t_1 \\t_2 \end{bmatrix})=min(t_1)$

Note that $t_2$ does not contribute in our objective, and objective is reduced just to minimizing $t_1$

Taking into account above constraints and objective function,  $min(t_1) = 1 | t_1 \ge 1$

Substituting $t=[1,0]$ into out parametric solution, we get:

$x=x_0+Nt=\begin{bmatrix} 0 \\ 5 \\ -1 \\ 2 \\ 3 \\ 0 \end{bmatrix} + \
\begin{bmatrix}
1 &amp; 0 \\
0 &amp; -1 \\
1 &amp; 0 \\
-1 &amp; 1 \\
0 &amp; -1 \\
0 &amp; 1
\end{bmatrix} \begin{bmatrix} 1 \\ 0 \end{bmatrix}=
\begin{bmatrix}
1 \\
5 \\
0 \\
1 \\
3 \\
0
\end{bmatrix}$

Minimum number of button clicks:

$\sum x_i=10$


So, as I have shown, even with 2 independent variables the solution sometimes can be reduced to a trivial minimization of one variable.



