package movehandlers;

import board.Move;
import board.Board;
import board.Spot;
import common.*;
import pieces.*;

public class RegularMoveHandler extends MoveHandler {
    @Override
    public boolean handleMove(PiecesMetadata metadata, Move move) {
        Board board = Board.getBoard();
        Piece movingPiece = board.getPiece(move.getStartSpot());

        if (metadata.isMoveValid(move)
                && !movingPiece.isAllyPiece(board.getPiece(move.getEndSpot()))) {

            if (movingPiece instanceof Pawn && Math.abs(move.getStartSpot().getX() - move.getEndSpot().getX()) == 2) {
                int middleRow = (move.getStartSpot().getX() + move.getEndSpot().getX()) / 2;
                metadata.setEnPassantTarget(new Spot(middleRow, move.getStartSpot().getY()));
            } else {
                metadata.setEnPassantTarget(null);
            }

            board.movePiece(move);
            return true;
        }

        if (nextMoveHandler != null)
            return nextMoveHandler.handleMove(metadata, move);
        else
            return false;
    }
}
