package common;

import board.*;
import pieces.*;

public class PiecesMetadata {
    private Spot enPassantTarget = null;

    private Board board = Board.getBoard();

    public void setEnPassantTarget(Spot spot) { this.enPassantTarget = spot; }
    public Spot getEnPassantTarget() { return enPassantTarget; }
    // does the piece have any available moves
    public boolean canPieceMove(Spot pieceSpot, PieceColor color) {
        if (board.isSpotEmpty(pieceSpot))
            return false;
        for (int i = 0; i < board.getHeight(); i++)
            for (int j = 0; j < board.getWidth(); j++) {
                Spot endSpot = new Spot(i, j);
                if (board.getPiece(pieceSpot) instanceof King) {
                    if (isMoveValid(new Move(pieceSpot, endSpot)) && !isSpotThreatened(color, endSpot))
                        return true;
                } else if (isMoveValid(new Move(pieceSpot, endSpot)))
                    return true;
            }
        return false;
    }
    public boolean canPieceRescueKing(Spot pieceSpot, PieceColor color) {
        Piece piece = board.getPiece(pieceSpot);
        if (piece == null) return false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Spot endSpot = new Spot(i, j);
                Move move = new Move(pieceSpot, endSpot);

                if (isMoveValid(move)) {
                    Piece tempCaptured = board.getPiece(endSpot);
                    board.setPiece(piece, endSpot);
                    board.resetTile(pieceSpot);
                    Spot kingSpot = findKingLocation(color);
                    boolean stillThreatened = isSpotThreatened(color, kingSpot);

                    board.setPiece(piece, pieceSpot);
                    board.setPiece(tempCaptured, endSpot);

                    if (!stillThreatened) return true;
                }
            }
        }
        return false;
    }
    public boolean isSpotThreatened(PieceColor defendingColor, Spot defendingSpot) {
        for (int i = 0; i < board.getHeight(); i++)
            for (int j = 0; j < board.getWidth(); j++) {
                Spot enemySpot = new Spot(i, j);
                Piece enemyPiece = board.getPiece(enemySpot);
                if (!board.isSpotEmpty(enemySpot)
                        && enemyPiece.getColor().equals(getOppositeColor(defendingColor))
                        && isMoveValid(new Move(enemySpot, defendingSpot))) {
                    return true;
                }
            }
        return false;
    }
    public boolean isMoveValid(Move move) {
        Board board = Board.getBoard();
        Piece piece = board.getPiece(move.getStartSpot());
        if (board.isSpotEmpty(move.getStartSpot())) {
            return false;
        }
        return piece.isMoveValid(move);
    }

    public Spot findKingLocation(PieceColor color) {
        for (int i = 0; i < board.getHeight(); i++)
            for (int j = 0; j < board.getWidth(); j++) {
                Piece temp = board.getPiece(new Spot(i, j));
                if (temp instanceof King && temp.getColor().equals(color))
                    return new Spot(i, j);
            }
        return null;
    }

    private PieceColor getOppositeColor(PieceColor color) {
        return color.equals(PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
    }
}
