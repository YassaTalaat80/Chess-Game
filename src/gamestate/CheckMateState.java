package gamestate;

import board.*;
import common.*;

public class CheckMateState implements GameStateCheck {

    @Override
    public StateInfo getState(PiecesMetadata metadata, PieceColor playerColor) {
        Board board = Board.getBoard();

        StateInfo stateInfo = new StateInfo(0, "");
        Spot kingSpot = metadata.findKingLocation(playerColor);
        boolean isChecked = metadata.isSpotThreatened(playerColor, kingSpot);
        if (!isChecked)
            return stateInfo;

        for (int i=0;i<board.getHeight();i++){
            for(int j=0;j<board.getWidth();j++){
                Spot pieceSpot =new Spot(i,j);
                if(metadata.canPieceRescueKing(pieceSpot,playerColor)){
                    return stateInfo;
                }
            }
        }
        stateInfo.setStateDescription("CHECKMATE\n" + playerColor + " loses");
        stateInfo.setStateCode(2);

        return stateInfo;
    }

    @Override
    public boolean isIllegalForCurrentPlayer() {
        return false;
    }
}
