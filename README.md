# A3 Connect Four
George Shao (g3shao 20849675)

## Setup
* Windows 11
* IntelliJ IDEA 2022.2.3 (Community Edition)
* kotlin.jvm 1.7.10
* Java SDK 17.0.2 (temurin)

## Enhancement 
I added the following enhancement:
- User can set the dimension of the board before the game starts while the following rules need to apply to the dimensions:
    1. User must enter rows, columns, and length to start the game.
    2. Rows need to be smaller than columns (because of defects in starter code) or user will get a warning.
    3. 1 <= rows <= 10, all input should be positive integers, otherwise, user will get a warning.
    4. 1 <= columns <= 12, all input should be positive integers, otherwise, user will get a warning.
    5. Length could be equal to 1, then the player #1 will wins. And length could greater than rows and columns, the game will end with draw.