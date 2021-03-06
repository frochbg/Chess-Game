package com.chess.game.player;

import com.chess.game.board.Board;

public final class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}
