package com.xgame.service.interfaces;

import com.xgame.common.viewmodels.MatchViewModel;

public interface IChessMatchService {
	String createMatch(int whiteId, int blackId);
	MatchViewModel getMatch(int id);
	String getChessBoard(int id);
	String updateMatch(int id, String chessBoard);
}
