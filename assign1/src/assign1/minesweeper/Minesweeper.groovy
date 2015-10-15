package assign1.minesweeper

class Minesweeper {

  enum CellState { UNEXPOSED, EXPOSED, SEALED } 

  private final def SIZE = 10

  def state = new CellState[SIZE][SIZE]
  def mines = new boolean[SIZE][SIZE]
  
  def Minesweeper() {
    (0..9).each { i ->
      (0..9).each { j ->
        state[i][j] = CellState.UNEXPOSED
        mines[i][j] = false
      }
    }
  }

  def setRandomMines() {
    def rand = new Random()
    def numberOfRandomMines = 0
    while( numberOfRandomMines < SIZE ) {
      def randomRow = rand.nextInt(SIZE)
      def randomColumn = rand.nextInt(SIZE)
      if( !isCellMined(randomRow, randomColumn) ) {
        mines[randomRow][randomColumn] = true
        numberOfRandomMines++
      }
    }
  }

  static def create() {
    Minesweeper minesweeper = new Minesweeper()
    minesweeper.setRandomMines()
    minesweeper
  }

  def verifyBounds( row, column ) {
   if(!isInRange(row, column)) 
     throw new IndexOutOfBoundsException("Index out of Bounds")
  }

  def isInRange(row, column){
    row >= 0 && row < SIZE && column >= 0 && column < SIZE
  }
  
  def exposeCell( row, column ) {

    verifyBounds( row, column )

    if( state[row][column] == CellState.UNEXPOSED ) {
      state[row][column] = CellState.EXPOSED    
      
      if( isCellEmpty( row, column ) ) {
        (-1..1 ).each { rowOffset ->
          (-1..1 ).each { columnOffset ->
            if( rowOffset != 0 || columnOffset != 0 )
              trytoExpose(row + rowOffset, column + columnOffset)            
          }
        }
      }
    }
  }

  def stateOfCell(row, column) {
    state[row][column]
  }

  def trytoExpose(row, column) {
    if( isInRange(row, column) ) 
      exposeCell(row, column) 
  }

  def sealCell(row, column) {
    verifyBounds(row, column)
    if(state[row][column] == CellState.UNEXPOSED ) 
      state[row][column] = CellState.SEALED
  }

  def unsealCell(row, column) {
    verifyBounds(row, column)
    if(state[row][column] == CellState.SEALED )
      state[row][column] = CellState.UNEXPOSED
  } 
  
  def isCellMined(row, column) {
    mines[row][column]
  }
  
  def countAdjacentMines(row, column) {
    def count = 0
    (-1..1 ).each { rowOffset ->
      (-1..1 ).each { columnOffset ->
        if( rowOffset != 0 || columnOffset != 0 )
          if( isInRange(row + rowOffset, column + columnOffset) && isCellMined(row + rowOffset, column + columnOffset) )
             count++            
     }
    }
    count
  }

  def isCellAdjacent(row, column) {
    countAdjacentMines(row, column) > 0
  }

  def isCellEmpty(row, column) {
    !isCellMined(row, column) && !isCellAdjacent(row, column)
  }

  def isGameWon() {
    def sealedMineCount = 0
    def unminedExposedCount = 0

    (0..SIZE-1).each { row ->
      (0..SIZE-1).each { column ->
        if( isCellMined(row, column) && state[row][column] == CellState.SEALED)
          sealedMineCount++
        else if( !isCellMined(row, column) && state[row][column] == CellState.EXPOSED)
          unminedExposedCount++
      }
    }

    sealedMineCount == SIZE && unminedExposedCount == (SIZE*SIZE - SIZE)
  }

  def isGameLost() {
    def gameLost = false
    (0..SIZE-1).each { row ->
      (0..SIZE-1).each { column ->
        if( isCellMined(row, column) && state[row][column] == CellState.EXPOSED)
          gameLost = true
      }
    }
    gameLost
  }

  def isGameInProgress() {
    !isGameLost() && !isGameWon()
  }

}

