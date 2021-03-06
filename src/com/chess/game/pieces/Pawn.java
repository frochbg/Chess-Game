package com.chess.game.pieces;

import com.chess.game.Alliance;
import com.chess.game.board.Board;
import com.chess.game.board.BoardUtils;
import com.chess.game.player.Move;
import com.chess.game.player.Move.AttackingMove;
import com.chess.game.player.Move.NormalMove;
import com.chess.game.player.Move.PawnEnPassantAttackMove;
import com.chess.game.player.Move.PawnJump;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Pawn extends Piece {
    private static final int[] ROW_OFFSETS = {1, 2, 1, 1};
    private static final int[] COLUMN_OFFSETS = {0, 0, 1, -1};
    private final boolean isFirstMove;

    public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(piecePosition, pieceAlliance);
        this.isFirstMove = isFirstMove;
    }

    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
        this.isFirstMove = false;
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        final Pair<Integer, Integer> pieceRowAndColumnCoordinates = BoardUtils.getPieceRowAndColumnCoordinates(this.getPiecePosition());
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                final int pieceRowCandidate = pieceRowAndColumnCoordinates.getKey() + (ROW_OFFSETS[i] * this.getPieceAlliance().getDirection());
                final int pieceColumnCandidate = pieceRowAndColumnCoordinates.getValue() + COLUMN_OFFSETS[i];
                if (BoardUtils.isValidTile(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate))) {
                    final int pieceCoordinateCandidate = BoardUtils.getPieceCoordinate(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate));
                    if (!board.getTile(pieceCoordinateCandidate).isTileOccupied()) {
                        legalMoves.add(new NormalMove(pieceCoordinateCandidate, this));
                    }
                }
            } else if (i == 1 && this.isFirstMove) {
                final int pieceRowCandidate = pieceRowAndColumnCoordinates.getKey() + (ROW_OFFSETS[i] * this.getPieceAlliance().getDirection());
                final int pieceColumnCandidate = pieceRowAndColumnCoordinates.getValue() + COLUMN_OFFSETS[i];
                final int pieceRowToCheck = pieceRowAndColumnCoordinates.getKey() + (ROW_OFFSETS[0] * this.getPieceAlliance().getDirection());
                final int pieceColumnToCheck = pieceRowAndColumnCoordinates.getValue() + COLUMN_OFFSETS[0];
                if (BoardUtils.isValidTile(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate))) {
                    final int pieceCoordinateCandidate = BoardUtils.getPieceCoordinate(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate));
                    final int pieceCoordinateToCheck = BoardUtils.getPieceCoordinate(new Pair<Integer, Integer>(pieceRowToCheck, pieceColumnToCheck));
                    if (!board.getTile(pieceCoordinateCandidate).isTileOccupied() && !board.getTile(pieceCoordinateToCheck).isTileOccupied()) {
                        legalMoves.add(new PawnJump(pieceCoordinateCandidate, this));
                    }
                }
            } else if (i > 1) {
                final int pieceRowCandidate = pieceRowAndColumnCoordinates.getKey() + (ROW_OFFSETS[i] * this.getPieceAlliance().getDirection());
                final int pieceColumnCandidate = pieceRowAndColumnCoordinates.getValue() + (COLUMN_OFFSETS[i] * this.getPieceAlliance().getDirection());
                if (BoardUtils.isValidTile(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate))) {
                    final int pieceCoordinateCandidate = BoardUtils.getPieceCoordinate(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate));
                    if (board.getTile(pieceCoordinateCandidate).isTileOccupied()) {
                        Piece attackingPiece = board.getTile(pieceCoordinateCandidate).getPiece();
                        legalMoves.add(new AttackingMove(pieceCoordinateCandidate, this, attackingPiece));
                    } else if (board.getEnPassantPawn() != null) {
                        final Pawn pawnOnCandidate = board.getEnPassantPawn();
                        final int rowOnCandidate = pieceRowCandidate + (ROW_OFFSETS[0] * this.getPieceAlliance().getDirection() * -1);
                        final int colOnCandidate = pieceColumnCandidate + COLUMN_OFFSETS[0];
                        final int newPieceId = BoardUtils.getPieceCoordinate(new Pair<>(rowOnCandidate, colOnCandidate));
                        if (pawnOnCandidate.getPiecePosition() == newPieceId) {
                            legalMoves.add(new PawnEnPassantAttackMove(pieceCoordinateCandidate, this, pawnOnCandidate));
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.PAWN;
    }

    @Override
    public int getPieceValue() {
        return 100;
    }
}
