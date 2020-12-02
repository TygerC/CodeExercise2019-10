# Code exercise 

Done for an interview done in October 2019.

## The objective

The objective is to fit 4 tetris pieces into 4x4 grid. The pieces you can find from the file (palikkatiedosto.txt). The pieces should be first fitted into your application, where the application fits the pieces into the grid and gives an answer about the spots of the pieces. Pieces can’t be turned, they has to be fit into the grid in given posture.

The file includes four rows, where every row defines one piece. The row defines the identification of the piece, and the grids of what the piece is composed. Row has the form of <id>:<x1>,<y1>;<x2>,<y2>;<x3>,<y3>;<x4>,<y4>.
Example row would be A:0,0;1,0;2,0;2,1. This piece consists of four squares where three of them are horizontally next to each other, and one on top of them.

- The output format is the same as input
- The program should print the first matching solution

