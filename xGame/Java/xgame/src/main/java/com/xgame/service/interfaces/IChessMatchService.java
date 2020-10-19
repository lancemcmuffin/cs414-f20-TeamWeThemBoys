package com.xgame.service.interfaces;

import java.util.List;

import com.xgame.common.viewmodels.MatchInviteViewModel;
import com.xgame.common.viewmodels.MatchViewModel;

public interface IChessMatchService {
	MatchViewModel createMatch(int whiteId, int blackId);
	public List<MatchInviteViewModel> getInvites(int userId);
	public boolean acceptInvite(int matchId);
	MatchViewModel getMatch(int id);
	String getChessBoard(int id);
	String updateMatch(int id, String chessBoard);
}
