package com.xgame.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.xgame.common.enums.MatchStatus;
import com.xgame.common.viewmodels.MatchInviteViewModel;
import com.xgame.common.viewmodels.MatchViewModel;
import com.xgame.data.IChessMatchRepository;
import com.xgame.data.IMessageRepository;
import com.xgame.data.IUserRepository;
import com.xgame.data.entities.ChessMatch;
import com.xgame.data.entities.Message;
import com.xgame.service.interfaces.IChessMatchService;

@Service
public class ChessMatchService implements IChessMatchService {

	@Autowired
	private IChessMatchRepository matchRepo;
	@Autowired
	private IUserRepository userRepo;
	@Autowired
	private IMessageRepository messageRepo;
	
	@Override
	public MatchViewModel createMatch(int whiteId, int blackId) {
		var whitePlayer = userRepo.findById(whiteId);
		var blackPlayer = userRepo.findById(blackId);
		var match = new ChessMatch("This is a chessboard");
		
		if(whitePlayer.isPresent() && blackPlayer.isPresent() && 
				!whitePlayer.get().getIsDeleted() && 
				!blackPlayer.get().getIsDeleted()) {
			
			match.setWhitePlayer(whitePlayer.get());
			match.setBlackPlayer(blackPlayer.get());
			match.setMatchStatus(MatchStatus.PENDING);
			match.setTurnCount(0);
			
			var newMatch = matchRepo.save(match);
			return new MatchViewModel(newMatch);
		}
		
		return null;
	}
	
	@Override
	public List<MatchInviteViewModel> getInvites(int userId) {
		var matches = matchRepo.findByBlackPlayerIdAndMatchStatus(userId, MatchStatus.PENDING);
		
		return matches
				.stream()
				.map(m -> new MatchInviteViewModel(m.getId(), m.getWhitePlayer().getNickname(), m.getCreationTimestamp()))
				.collect(Collectors.toList());
	}
	
	@Override
	public boolean acceptInvite(int matchId) {
		var match = matchRepo.findById(matchId);
		
		if(match.isPresent()) {
			//TODO: initialize chess board
			
			var m = match.get();
			m.setTurnCount(1);
			m.setMatchStatus(MatchStatus.INPROGRESS);
			m.setStartTimestamp(new Timestamp(System.currentTimeMillis()));
			
			var updatedMatch = matchRepo.saveAndFlush(m);
			var whitePlayer = updatedMatch.getWhitePlayer();
			var blackPlayer = updatedMatch.getBlackPlayer();
			var message = new Message(whitePlayer, blackPlayer.getNickname() + " has accepted your invitation to a match!");
			
			messageRepo.save(message);
			
			return true;
		}
		
		return false;
	}
	

	@Override
	public MatchViewModel getMatch(int id) {
		var matchState = matchRepo.findById(id);
		matchState.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No match with that id exists."));
		return new MatchViewModel(matchState.get());
	}

	@Override
	public String getChessBoard(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateMatch(int id, String chessBoard) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
