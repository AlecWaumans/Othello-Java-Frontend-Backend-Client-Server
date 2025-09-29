package server;

import othello.model.State;
import othello.model.Position;
import common.GameInfo;
import common.PositionDTO;

import java.util.List;
import java.util.stream.Collectors;
import othello.model.GameState;

/**
 * Utility class responsible for converting a {@link State} object from the Othello model
 * into a {@link GameInfo} object that can be sent to clients.
 * 
 * This is part of the mapping layer, which isolates the internal model representation
 * from the format exposed to clients.
 * 
 * Author: Alec Waumans (2025)
 */
public class StateMapper {

    /**
     * Converts a {@link State} object into a {@link GameInfo} DTO for network transmission.
     *
     * @param state The current game state from the server game model.
     * @return A {@link GameInfo} object containing the mapped data.
     */
    public static GameInfo toGameInfo(State state) {

        // Determine if the game is finished
        boolean isEndGame = state.getGameState() != GameState.RUNNING;

        // Build and return the GameInfo DTO
        return new GameInfo(
            toDTOList(state.getBlackTokens(), isEndGame),  // Black tokens
            toDTOList(state.getWhiteTokens(), isEndGame),  // White tokens
            toDTOList(state.getPossibleMove(), isEndGame), // Possible moves
            state.getScore(),                              // Current score
            state.getNextColor().name(),                   // "BLACK" or "WHITE"
            
            // Map the GameState enum from the model to the GameInfo.GameStatus enum for clients
            switch (state.getGameState()) {
                case RUNNING -> GameInfo.GameStatus.RUNNING;
                case ENDGAME -> GameInfo.GameStatus.ENDGAME;
                case SURRENDER -> GameInfo.GameStatus.SURRENDER;
            }
        );
    }

    /**
     * Converts a list of {@link Position} objects into a list of {@link PositionDTO} objects.
     *
     * @param positions The list of positions from the model.
     * @param endGame   True if the game has ended; affects the DTO's state.
     * @return A list of {@link PositionDTO} objects for client consumption.
     */
    private static List<PositionDTO> toDTOList(List<Position> positions, boolean endGame) {
        return positions.stream()
                .map(pos -> new PositionDTO(pos.getX(), pos.getY(), endGame))
                .collect(Collectors.toList());
    }
}
