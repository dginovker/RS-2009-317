package org.gielinor.game.node.entity.player.link.prayer;

/**
 * Created by Stan van der Bend on 03/02/2018.
 *
 * project: Gielinor-Server
 * package: org.gielinor.game.content.activity.minigame
 */
public enum  PrayerBook {

    NORMAL(5608),
    CURSES(21356);

    private int bookID;

    PrayerBook(int bookID) {
        this.bookID = bookID;
    }

    public int getBookID() {
        return bookID;
    }
}
