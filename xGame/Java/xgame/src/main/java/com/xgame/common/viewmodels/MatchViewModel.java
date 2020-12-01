package com.xgame.common.viewmodels;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.xgame.common.enums.MatchStatus;
import com.xgame.data.entities.ChessMatch;

public class MatchViewModel {
	private int id;
	private int whitePlayerId;
	private int blackPlayerId;
	private String whitePlayerNickname;
	private String blackPlayerNickname;
	private int winningPlayerId;
	private String winningPlayerNickname;
	private int turnCount;
	private MatchStatus status;
	@JsonRawValue
	private String chessBoard;
	
	public MatchViewModel(ChessMatch match) {
		var whitePlayer = match.getWhitePlayer();
		var blackPlayer = match.getBlackPlayer();
		var winningPlayer = match.getWinningPlayer();
		
		if(whitePlayer != null && blackPlayer != null) {
			this.id = match.getId();
			this.whitePlayerId = whitePlayer.getId();
			this.blackPlayerId = blackPlayer.getId();
			this.whitePlayerNickname = whitePlayer.getNickname();
			this.blackPlayerNickname = blackPlayer.getNickname();
			this.turnCount = match.getTurnCount();
			this.chessBoard = match.getChessBoard();
			this.status = match.getMatchStatus();
		}
		
		if(winningPlayer != null) {
			this.winningPlayerId = winningPlayer.getId();
			this.winningPlayerNickname = winningPlayer.getNickname();
		}
	}
	
	//getters and setters
	public int getId() {
		return id;
	}
	public int getWhitePlayerId() {
		return whitePlayerId;
	}
	public int getBlackPlayerId() {
		return blackPlayerId;
	}
	public String getWhitePlayerNickname() {
		return whitePlayerNickname;
	}
	public String getBlackPlayerNickname() {
		return blackPlayerNickname;
	}
	public int getTurnCount() {
		return turnCount;
	}
	public String getChessBoard() {
		return chessBoard;
	}
	public void setChessBoard(String chessBoard) {
		this.chessBoard = chessBoard;
	}
	public MatchStatus getStatus() {
		return status;
	}
	public void setStatus(MatchStatus status) {
		this.status = status;
	}

	public int getWinningPlayerId() {
		return winningPlayerId;
	}

	public void setWinningPlayerId(int winningPlayerId) {
		this.winningPlayerId = winningPlayerId;
	}

	public String getWinningPlayerNickname() {
		return winningPlayerNickname;
	}

	public void setWinningPlayerNickname(String winningPlayerNickname) {
		this.winningPlayerNickname = winningPlayerNickname;
	}
}
