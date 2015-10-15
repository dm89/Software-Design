package assign1.minesweeper

import spock.lang.Specification

class MinesweeperTest extends Specification {
   
  def minesweeper
  def minesweeperToDesignExpose
  def cellsTriedtoExpose 

  def setup() {
    minesweeper = new Minesweeper()
    
    minesweeperToDesignExpose = new Minesweeper()
    cellsTriedtoExpose = [] 
    minesweeperToDesignExpose.metaClass.trytoExpose = { row, column -> 
      cellsTriedtoExpose << [row, column]
    }
  }

  def "canary"() {
    expect:
      true
  }

  def "an unexposed cell can be exposed"() {
      minesweeper.exposeCell(1, 1)
    expect:
      Minesweeper.CellState.EXPOSED == minesweeper.stateOfCell(1, 1)
  }

  def "when we call expose on an exposed cell, it remains exposed"() {
    minesweeper.exposeCell(1, 1)
    minesweeper.exposeCell(1, 1)
    expect:
      Minesweeper.CellState.EXPOSED == minesweeper.stateOfCell(1, 1)   
  }

  def "exposeCell should throw an exception if out of bounds"() {
    when:
      minesweeper.exposeCell(row, column)
    then:
      thrown(IndexOutOfBoundsException)
    where:
      row | column
      -1  | 0
      10  | 0
      0   | -1
      0   | 10

  }

  def "when a cell is exposed, its neighboring cell may be exposed"() {   
    minesweeperToDesignExpose.exposeCell(2, 2)
    expect:
      [[1, 1], [1, 2], [1, 3], [2, 1], [2, 3], [3, 1], [3, 2], [3,3]]  == cellsTriedtoExpose
  }

  def "when an unexposed cell is exposed, it does not try to expose itself again"() {  
    when:
      minesweeperToDesignExpose.exposeCell(3, 3)  
    then:
      !cellsTriedtoExpose.contains([3, 3])  
  }

  def "when a exposed cell is exposed, it doesn't try to expose any cell"(){
    minesweeperToDesignExpose.exposeCell(2,2)
    cellsTriedtoExpose.clear()
    minesweeperToDesignExpose.exposeCell(2,2)
    expect:
      cellsTriedtoExpose.empty  
  }

  def "trytoExpose should not call expose if out of bounds"() {
    def minesweeper = new Minesweeper()
    minesweeper.metaClass.exposeCell = {row, column -> fail("Should not be called") }
    minesweeper.trytoExpose(row, column)
    expect:
      true
    where:
      row | column
      -1  | 0
      10  | 0
      0   | -1
      0   | 10
  }

  def "tryToExpose will expose a valid cell"() {
    def minesweeper = new Minesweeper()
    def called = false
    minesweeper.metaClass.exposeCell = {row, column -> called = true }
    minesweeper.trytoExpose(1, 1)
    expect:
      called
  }

  def "an unexposed cell can be sealed"() {
    minesweeper.sealCell(1, 1)
    expect:
     Minesweeper.CellState.SEALED == minesweeper.stateOfCell(1, 1)   
  }

  def "an exposed cell cannot be sealed"() {
    minesweeper.exposeCell(1, 1)
    minesweeper.sealCell(1, 1)
    expect:
      Minesweeper.CellState.EXPOSED == minesweeper.stateOfCell(1, 1)
  }

  def "a sealed cell cannot be exposed"() {
    minesweeper.sealCell(1, 1)
    minesweeper.exposeCell(1, 1)
    expect:
      Minesweeper.CellState.SEALED == minesweeper.stateOfCell(1, 1)
  }

  def "sealCell should throw an exception if out of bounds"() {
    when:
      minesweeper.sealCell(row, column)
    then:
      thrown(IndexOutOfBoundsException)
    where:
      row | column
      -1  | 0
      10  | 0
      0   | -1
      0   | 10
  }

  def "when expose is called on a sealed cell, it does not call trytoExpose"() {
    minesweeperToDesignExpose.sealCell(1, 1)
    minesweeperToDesignExpose.exposeCell(1, 1)
    expect:
      cellsTriedtoExpose.empty   
  }

  def "a sealed cell can be unsealed"() {
    minesweeper.sealCell(1, 1)
    minesweeper.unsealCell(1, 1)
    expect:
      Minesweeper.CellState.UNEXPOSED == minesweeper.stateOfCell(1, 1)
  }

  def "when we call sealCell on a sealed cell, it remains sealed"() {
    minesweeper.sealCell(1, 1)
    minesweeper.sealCell(1, 1)
    expect:
      Minesweeper.CellState.SEALED == minesweeper.stateOfCell(1, 1)   
  }

  def "unsealCell should throw an exception if out of bounds"() {
    when:
      minesweeper.sealCell(row, column)
    then:
      thrown(IndexOutOfBoundsException)
    where:
      row | column
      -1  | 0
      10  | 0
      0   | -1
      0   | 10
  }

  def "when we call unsealCell on an exposed cell, it remains exposed"() {
    minesweeper.exposeCell(1, 1)
    minesweeper.unsealCell(1, 1)
    expect:
      Minesweeper.CellState.EXPOSED == minesweeper.stateOfCell(1, 1)
  }

  def "when we call unsealCell on an unexposed cell, it remains unexposed"() {
    minesweeper.unsealCell(1, 1)  
    expect:
      Minesweeper.CellState.UNEXPOSED == minesweeper.stateOfCell(1, 1)
  }

  def "when expose is called on a mined cell, it does not call trytoExpose"() {
    minesweeperToDesignExpose.mines[1][1] = true
    minesweeperToDesignExpose.exposeCell(1, 1)
    expect:
      cellsTriedtoExpose.empty
    }
  

  def "countAdjacentMines will return zero, if there is no mine in its neighboring cells"() {
    expect:
      minesweeper.countAdjacentMines(1, 2) == 0
  }


  def "countAdjacentMines will return one if there is one mine in its neighboring cells"() {
    minesweeper.mines[1][1] = true
    expect:
      minesweeper.countAdjacentMines(1, 2) == 1
  }


  def "countAdjacentMines will return eight if there are eight mines in its neighboring cells"() {
    minesweeper.mines[2][2] = true
    minesweeper.mines[3][2] = true
    minesweeper.mines[4][2] = true
    minesweeper.mines[4][3] = true
    minesweeper.mines[4][4] = true
    minesweeper.mines[3][4] = true
    minesweeper.mines[2][4] = true
    minesweeper.mines[2][3] = true
    expect:
      minesweeper.countAdjacentMines(3, 3) == 8
  }

  def "countAdjacentMines for a cell in the corner with all adjacent mines returns three"() {
    minesweeper.mines[0][1] = true
    minesweeper.mines[1][1] = true
    minesweeper.mines[1][0] = true
    expect:
      minesweeper.countAdjacentMines(0, 0) == 3

  }

  def "countAdjacentMines for a cell on the edge with all adjacent mines returns five"() {
    minesweeper.mines[0][0] = true
    minesweeper.mines[0][1] = true
    minesweeper.mines[1][1] = true
    minesweeper.mines[2][0] = true
    minesweeper.mines[2][1] = true
    expect:
      minesweeper.countAdjacentMines(1, 0) == 5
  }

  def "A cell is adjacent, if it is next to one or more mined cells"() {
    minesweeper.mines[1][1] = true
    expect:
      minesweeper.isCellAdjacent(1, 2)
  }

  def "An empty cell has no mine and is not next to a mined cell"() {
    expect:
      minesweeper.isCellEmpty(1, 1)
  }

  def "mined cell is not an empty cell"() {
    minesweeper.mines[1][1] = true
    expect:
      !minesweeper.isCellEmpty(1, 1)
  }

  def "a cell next to a mined cell is not empty"() {
    minesweeper.mines[1][1] = true
    expect:
      !minesweeper.isCellEmpty(1, 2)
  }
 
  def "when expose is called on an adjacent cell, it does not call trytoExpose"() {
    minesweeperToDesignExpose.mines[1][1] = true
    minesweeperToDesignExpose.exposeCell(1, 2)
    expect:
      cellsTriedtoExpose.empty
  }

  def "there are 10 mines in the game"() {
    minesweeper.setRandomMines()
    def count = 0
    (0..minesweeper.SIZE-1).each { row ->
      (0..minesweeper.SIZE-1).each { column ->
        if( minesweeper.isCellMined(row, column) )
          count++
      }
    }
    expect:
      count == 10
  }

  def "setRandomMines places mines at random"() {
    minesweeper.setRandomMines()
    def minesweeperToTestRandomMines = new Minesweeper()
    minesweeperToTestRandomMines.setRandomMines()
    def differingPositionsInMines = 0

    (0..minesweeper.SIZE-1).each { row ->
      (0..minesweeper.SIZE-1).each { column ->
        if( minesweeper.isCellMined(row, column) != minesweeperToTestRandomMines.isCellMined(row, column) )
          differingPositionsInMines++
      }
    }
    expect:
      differingPositionsInMines > 0
  }

  def "user gets a well initialized minesweeper on create"() {
    Minesweeper minesweeper = Minesweeper.create()
    def count = 0
    (0..minesweeper.SIZE-1).each { row ->
      (0..minesweeper.SIZE-1).each { column ->
        if( minesweeper.isCellMined(row, column) )
          count++
      }
    }
    expect:
      count > 0
  }

  def "game in progress at start"() {
    expect:
      minesweeper.isGameInProgress()
  }

  def "game is lost when a mine cell is exposed"() {
    minesweeper.mines[1][1] = true
    minesweeper.exposeCell(1, 1)
    expect:
      minesweeper.isGameLost()
  }

  def "game won when all mines are sealed and all other cells are exposed"() {
    def minesweeper = new Minesweeper()
    for(int j = 0; j < 10; j++) {      
      minesweeper.mines[0][j] = true
      minesweeper.sealCell(0, j)
    }
    minesweeper.exposeCell(5, 5)
    
    expect:
      minesweeper.isGameWon()
  }

  def "game not won when all mines are sealed and all other cells are not exposed"() {
    def minesweeper = new Minesweeper()
    for(int j = 0; j < 10; j++) {      
      minesweeper.mines[0][j] = true
      minesweeper.sealCell(0, j)
    }

    expect:
      !minesweeper.isGameWon()
  }

  def "game not won when all mines are not sealed and all other cells are exposed"() {
    def minesweeper = new Minesweeper()
    for(int j = 0; j < 10; j++) {      
      minesweeper.mines[0][j] = true
    }
    minesweeper.exposeCell(0, 8)
    expect:
      !minesweeper.isGameWon()
  }

  def "game not won when all mines are not sealed and all other cells are not exposed"() {
    def minesweeper = new Minesweeper()
    for(int j = 0; j < 10; j++) {      
      minesweeper.mines[0][j] = true
    }
    expect:
      !minesweeper.isGameWon()
  }

}