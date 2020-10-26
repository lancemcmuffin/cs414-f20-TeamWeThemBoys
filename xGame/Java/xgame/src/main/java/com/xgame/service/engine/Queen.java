package com.xgame.service.engine;

import java.util.ArrayList;

public class Queen extends ChessPiece{

	public Queen(ChessBoard board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return this.color == Color.BLACK ? "\u265B" : "\u2655";
	}

	@Override
	public ArrayList<String> legalMoves() throws IllegalPositionException {
		ArrayList<String> moves = new ArrayList<String>();
		ChessBoard tempBoard = this.board;
		ChessPiece[] pieces = {
		       new Rook(tempBoard, this.color),
		       new Bishop(tempBoard, this.color),
		    };
	    for (ChessPiece piece : pieces) {
	        try {
				piece.setPosition(toPosition(this.column, this.row));
				moves.addAll(piece.legalMoves());
			} catch (IllegalPositionException e) {
				throw(e);
			}  
	    }
	    
	     return moves;
	}

}
