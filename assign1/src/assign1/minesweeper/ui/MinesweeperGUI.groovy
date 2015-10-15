package assign1.minesweeper.ui

import groovy.swing.SwingBuilder
import java.awt.GridLayout
import javax.swing.JFrame
import javax.swing.*
import java.awt.*
import java.awt.event.*

import assign1.minesweeper.*

def minesweeper = Minesweeper.create()
SwingBuilder swing = new SwingBuilder()

def handleClick = { event -> 
  
  if(minesweeper.isGameInProgress()) {

    String which = ((SwingUtilities.isRightMouseButton(event) || event.controlDown)) ? 'Right' : 'Left'
    JButton button = ( JButton ) event.getSource();
    String name = button.getName();
    def (rowString, columnString) = name.tokenize('-')
    Integer row = rowString.toInteger()
    Integer column = columnString.toInteger()   
    
    if( which == 'Left' && minesweeper.stateOfCell(row, column) != Minesweeper.CellState.SEALED ) 
        minesweeper.exposeCell(row, column) 

    if( which == 'Right') {
      if( minesweeper.stateOfCell(row, column) == Minesweeper.CellState.SEALED )
        minesweeper.unsealCell(row, column)
      else
        minesweeper.sealCell(row, column)
    }

    displayCells(minesweeper, swing)

    if( minesweeper.isGameWon() ) {
      JOptionPane.showMessageDialog(null, "Game Won!")
    }

    if( minesweeper.isGameLost() ) {
      disableAllButtonsAndDisplayAllMines(minesweeper, swing)
        JOptionPane.showMessageDialog(null, "Game Lost")
    }
  }
}

frame = swing.frame(title: "Minesweeper", defaultCloseOperation: JFrame.EXIT_ON_CLOSE, pack: true, show: true) {

  panel(layout: new GridLayout(minesweeper.SIZE, minesweeper.SIZE)) {

    (0..< minesweeper.SIZE).each { row ->
      (0..< minesweeper.SIZE).each { column ->
        def buttonID = "$row-$column"
        button = button(id: buttonID, '', mouseReleased: handleClick) 
      }
    }
  }
}
frame.setSize(500, 500)


def displayCells(minesweeper, swing) {
  (0..< minesweeper.SIZE).each { row ->
    (0..< minesweeper.SIZE).each { column ->
      JButton currentButton = swing."$row-$column"
      String displayText = ""
      if( minesweeper.stateOfCell(row, column) == Minesweeper.CellState.EXPOSED ) {
        currentButton.setEnabled(false)

      if( !minesweeper.isCellMined(row, column) && minesweeper.isCellAdjacent(row, column) )
        displayText = minesweeper.countAdjacentMines(row, column).toString()

      if( minesweeper.isCellMined(row, column))
      displayText = "M"
      }

      if( minesweeper.stateOfCell(row, column) == Minesweeper.CellState.SEALED ) {
        displayText = "S"
      }

      currentButton.setText(displayText) 
    }
  }
}

def disableAllButtonsAndDisplayAllMines(minesweeper, swing) {
  (0..< minesweeper.SIZE).each { row ->
    (0..< minesweeper.SIZE).each { column ->
      JButton currentButton = swing."$row-$column"
      currentButton.setEnabled(false) 
      if( minesweeper.isCellMined(row, column))
        currentButton.setText("M")
    }
  }
}
