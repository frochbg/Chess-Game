package com.chess.game.pieces;

import com.chess.game.Alliance;
import com.chess.game.board.Board;
import com.chess.game.board.BoardUtils;
import com.chess.game.player.Move;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Bishop extends Piece {
    private static final int[] ROW_OFFSETS = {-1, -1, 1, 1};
    private static final int[] COLUMN_OFFSETS = {-1, 1, -1, 1};

    public Bishop(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        final Pair<Integer, Integer> pieceRowAndColumnCoordinates = BoardUtils.getPieceRowAndColumnCoordinates(this.getPiecePosition());
        for (int i = 0; i < 4; i++) {
            int pieceRowCandidate = pieceRowAndColumnCoordinates.getKey() + ROW_OFFSETS[i];
            int pieceColumnCandidate = pieceRowAndColumnCoordinates.getValue() + COLUMN_OFFSETS[i];
            while (BoardUtils.isValidTile(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate))) {
                final int pieceCoordinateCandidate = BoardUtils.getPieceCoordinate(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate));
                if (!board.getTile(pieceCoordinateCandidate).isTileOccupied()) {
                    legalMoves.add(new Move.NormalMove(pieceCoordinateCandidate, this));
                } else {
                    final Piece attackingPiece = board.getTile(pieceCoordinateCandidate).getPiece();
                    if (this.getPieceAlliance() != attackingPiece.getPieceAlliance()) {
                        legalMoves.add(new Move.AttackingMove(pieceCoordinateCandidate, this, attackingPiece));
                    }
                    break;
                }
                pieceRowCandidate += ROW_OFFSETS[i];
                pieceColumnCandidate += COLUMN_OFFSETS[i];
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.BISHOP;
    }

    @Override
    public int getPieceValue() {
        return 350;
    }
}
