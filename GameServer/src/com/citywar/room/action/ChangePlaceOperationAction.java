package com.citywar.room.action;

import com.citywar.room.BaseRoom;

public class ChangePlaceOperationAction extends AbstractRoomAction
{
    private BaseRoom room;
    private int place;
    private boolean isOpen;

    public ChangePlaceOperationAction(BaseRoom room, int index, boolean isOpen)
    {
        this.room = room;
        place = index;
        this.isOpen = isOpen;

    }

    @Override
    public void execute()
    {
        if (room.getPlayerCount() > 0)
            room.updatePosUnsafe(place, isOpen);
    }

}
