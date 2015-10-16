# Software-Design

#### assign 1 - Minesweeper Game
Using Test First Development (no code without test first), we implemented the minesweeper game in Groovy.

The object of the game is for a player to find and seal all ten hidden 
mines among hundred cells (10x10 cells). The mines may be in any random
location.

There are three types of cells: mined cell, adjacent cell, empty cell. 

A mined cell has a hidden mine.

An adjacent cell is next to one or more mined cells and knows the 
number of mines next to it. This number is not revealed to the user 
initially. 

Last, an empty cell has no mine and is not next to a mined cell.

At the start of the game all cells are displayed grayed out, the player 
has no initial clue which cells are mined, adjacent, or empty. The user 
can either expose a cell or seal it.

The player would seal a cell that is suspected to have a mine. Only an 
unexposed cell can be sealed. A sealed cell is shown with a seal symbol. 
If a cell is sealed, the player can unseal it and it will turn gray again. 

Only an unexposed cell can be exposed. 

Several actions may take place when an empty cell is exposed. First, when 
an empty cell is exposed, it's emptiness is shown. Immediately, all
unsealed cells next to the exposed empty cell are also exposed. Any sealed 
cell is left unchanged.

When an adjacent cell is exposed (either due to users direct action or due 
to the successive action from exposing of a neighboring cell) it's count 
is shown and no further action happens. 

If a mined cell is exposed (either due to users direct action or due to 
the successive action from exposing a neighboring cell) the mine is shown 
and the game ends.

There are two outcomes of the game. The player wins after sealing all the 
mines and exposing all the other cells. The player loses if a mined cell 
is exposed.

#### assign 2 - Weather application
A console application to obtain weather data for cities using Test Driven Development with JUnit and Mockito in Java.

The openweathermap.org site provides weather data. We can access, for example,
the weather in Houston, by sending a request to

http://api.openweathermap.org/data/2.5/weather?q=Houston,us&units=imperial&appid=bd82977b86bf27fb59a04b61b657fb6f

This program will read from a file a list of cities. It will then get the temperature and the current condition (for example, sunny or cloudy) from the webservice mentioned above. The program will then display, in sorted order of the city name, the current temperature and the weather condition. 
It will also report the hottest city and the coldest city.
